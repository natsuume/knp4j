package dev.natsuume.knp4j.wrapper;

import dev.natsuume.knp4j.data.JumanResult;
import dev.natsuume.knp4j.parser.ResultParser;
import dev.natsuume.knp4j.process.ProcessExecutor;
import dev.natsuume.knp4j.process.ProcessManager;
import dev.natsuume.knp4j.process.builder.ProcessExecutorBuilder;
import io.vavr.control.Try;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnpWrapper<OutputT> {
  private static final String NEW_LINE_REGEX = "(\r\n|\n|\r)";
  private static final String INVALID_INPUT_REGEX = ".*[\\+\\*].*";
  private final ProcessInitInfo jumanInitInfo;
  private final ProcessInitInfo knpInitInfo;
  private final List<String> jumanCommand;
  private final List<String> knpCommnad;
  private final int retryNum;
  private ProcessManager<String, JumanResult> jumanManager;
  private ProcessManager<JumanResult, OutputT> knpManager;
  private ResultParser<OutputT> resultParser;

  /**
   * KnpWrapperのインスタンスを生成する.
   *
   * @param jumanInitInfo JUMAN設定
   * @param knpInitInfo KNP設定
   * @param retryNum 解析時、エラー等で失敗した際に再試行する回数
   * @param resultParser 解析結果の出力parser
   */
  public KnpWrapper(
      ProcessInitInfo jumanInitInfo,
      ProcessInitInfo knpInitInfo,
      int retryNum,
      ResultParser<OutputT> resultParser) {
    this.jumanInitInfo = jumanInitInfo;
    this.knpInitInfo = knpInitInfo;
    this.jumanCommand = jumanInitInfo.getCommand();
    this.knpCommnad = knpInitInfo.getCommand();
    this.retryNum = retryNum;
    this.resultParser = resultParser;

    this.jumanManager = startJumanManager();
    this.knpManager = startKnpManager(resultParser::parse);
  }

  private ProcessManager<String, JumanResult> startJumanManager() {
    return new ProcessManager<>(
        jumanInitInfo.getMaxNum(),
        jumanInitInfo.getStartNum(),
        () -> instantiateProcessExecutor(jumanCommand, Function.identity(), JumanResult::new));
  }

  private ProcessManager<JumanResult, OutputT> startKnpManager(
      Function<List<String>, OutputT> outputFunction) {
    return new ProcessManager<>(
        knpInitInfo.getMaxNum(),
        knpInitInfo.getStartNum(),
        () -> instantiateProcessExecutor(knpCommnad, JumanResult::toKnpInput, outputFunction));
  }

  private <InputT, OutputT> Try<ProcessExecutor<InputT, OutputT>> instantiateProcessExecutor(
      List<String> command,
      Function<InputT, String> inputConverter,
      Function<List<String>, OutputT> outputConverter) {
    return Try.success(
            new ProcessExecutorBuilder<InputT, OutputT>()
                .setCommand(command)
                .setInputConverter(inputConverter)
                .setOutputConverter(outputConverter))
        .mapTry(builder -> builder.start());
  }

  /**
   * 入力文字列を解析した結果を返す. <br>
   * 解析に失敗した場合はKnpResult.INVALID_RESULTを返す.<br>
   * 改行が含まれる場合それぞれ独立して解析した結果を返す.
   *
   * @param input 入力文字列
   * @return 解析結果のリスト(順序は保証されない)
   */
  public List<OutputT> analyze(String input) {
    var invalidResult = resultParser.getInvalidResult();
    return Arrays.stream(input.split(NEW_LINE_REGEX))
        .map(text -> text.matches(INVALID_INPUT_REGEX) ? invalidResult : analyzeText(input))
        .collect(Collectors.toList());
  }

  private <InputT, OutputT> Try<OutputT> analyze(
      ProcessManager<InputT, OutputT> processManager, InputT input, int tryCount) {
    Try<OutputT> result = Try.success(input).mapTry(processManager::exec);


    if (result.isFailure() && tryCount < retryNum) {
      result = analyze(processManager, input, tryCount + 1);
    }

    return result;
  }

  private OutputT analyzeText(String input) {
    return analyze(jumanManager, input, 0)
        .flatMapTry(result -> analyze(knpManager, result, 0))
        .getOrElseGet(exception -> {
          exception.printStackTrace();
          return resultParser.getInvalidResult();
        });
  }

  /**
   * このインスタンスが管理しているJUMAN, KNPをcloseする.
   *
   * @throws InterruptedException Threadの割り込みが発生した
   * @throws IOException プロセスのIO処理に失敗した
   */
  public void close() throws InterruptedException, IOException {
    jumanManager.close();
    knpManager.close();
  }
}
