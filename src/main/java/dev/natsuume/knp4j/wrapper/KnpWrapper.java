package dev.natsuume.knp4j.wrapper;

import dev.natsuume.knp4j.data.JumanResult;
import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.data.element.KnpResultParser;
import dev.natsuume.knp4j.process.ProcessExecutorImpl;
import dev.natsuume.knp4j.process.ProcessManager;
import dev.natsuume.knp4j.process.builder.ProcessExecutorBuilder;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class KnpWrapper {
  private static final String NEW_LINE_REGEX = ".*(\r\n|\n|\r).*";
  private static final String INVALID_INPUT_REGEX = ".*[+*].*";
  private final ProcessInitInfo jumanInitInfo;
  private final ProcessInitInfo knpInitInfo;
  private final List<String> jumanCommand;
  private final List<String> knpCommnad;
  private final int retryNum;
  private ProcessManager<String, JumanResult> jumanManager;
  private ProcessManager<JumanResult, KnpResult> knpManager;

  /**
   * KnpWrapperのインスタンスを生成する.
   * @param jumanInitInfo JUMAN設定
   * @param knpInitInfo KNP設定
   * @param retryNum 解析時、エラー等で失敗した際に再試行する回数
   */
  public KnpWrapper(ProcessInitInfo jumanInitInfo, ProcessInitInfo knpInitInfo, int retryNum) {
    this.jumanInitInfo = jumanInitInfo;
    this.knpInitInfo = knpInitInfo;
    this.jumanCommand = jumanInitInfo.getCommand();
    this.knpCommnad = knpInitInfo.getCommand();
    this.retryNum = retryNum;

    this.jumanManager = startJumanManager();
    this.knpManager = startKnpManager();
  }

  private ProcessManager<String, JumanResult> startJumanManager() {
    return new ProcessManager<>(
        jumanInitInfo.getMaxNum(), jumanInitInfo.getStartNum(), this::getJumanExecutor);
  }

  private ProcessManager<JumanResult, KnpResult> startKnpManager() {
    return new ProcessManager<>(
        knpInitInfo.getMaxNum(), knpInitInfo.getStartNum(), this::getKnpExecutor);
  }

  private ProcessExecutorImpl<String, JumanResult> getJumanExecutor() {
    return new ProcessExecutorBuilder<String, JumanResult>()
        .setCommand(jumanCommand)
        .setOutputConverter(JumanResult::new)
        .start();
  }

  private ProcessExecutorImpl<JumanResult, KnpResult> getKnpExecutor() {
    return new ProcessExecutorBuilder<JumanResult, KnpResult>()
        .setCommand(knpCommnad)
        .setInputConverter(JumanResult::toKnpInput)
        .setOutputConverter(list -> new KnpResultParser(list).build())
        .start();
  }

  private <T, R> R retryAnalyze(Function<T, R> func, T input) {
    for (int i = 0; i < retryNum; i++) {
      try {
        return func.apply(input);
      } catch (Exception e2) {
        continue;
      }
    }
    return null;
  }

  private JumanResult analyzeJuman(String input) throws InterruptedException, IOException {
    try {
      return jumanManager.exec(input);
    } catch (InterruptedException | IOException e) {
      for (int i = 0; i < retryNum; i++) {
        try {
          return jumanManager.exec(input);
        } catch (InterruptedException | IOException e2) {
          continue;
        }
      }
      throw e;
    }
  }

  private KnpResult analyzeKnp(JumanResult input) throws InterruptedException, IOException {
    try {
      return knpManager.exec(input);
    } catch (InterruptedException | IOException e) {
      for (int i = 0; i < retryNum; i++) {
        try {
          return knpManager.exec(input);
        } catch (InterruptedException | IOException e2) {
          continue;
        }
      }
      throw e;
    }
  }

  /**
   * 入力文字列を解析した結果を返す.
   * @param input 入力文字列
   * @return 解析結果
   * @throws InterruptedException Threadの割り込みが発生した
   * @throws IOException プロセスのIO処理に失敗した
   */
  public KnpResult analyze(String input) throws InterruptedException, IOException {
    if (input.matches(INVALID_INPUT_REGEX) || input.matches(NEW_LINE_REGEX)) {
      return KnpResult.INVALID_RESULT.get(0);
    }
    JumanResult jumanResult = analyzeJuman(input);
    KnpResult knpResult = analyzeKnp(jumanResult);
    return knpResult;
  }

  /**
   * このインスタンスが管理しているJUMAN, KNPをcloseする.
   * @throws InterruptedException Threadの割り込みが発生した
   * @throws IOException プロセスのIO処理に失敗した
   */
  public void close() throws InterruptedException, IOException {
    jumanManager.close();
    knpManager.close();
  }
}
