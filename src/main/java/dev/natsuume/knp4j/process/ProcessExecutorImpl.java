package dev.natsuume.knp4j.process;

import dev.natsuume.knp4j.process.io.ProcessStreamReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ProcessExecutorImpl<InputT, OutputT> implements ProcessExecutor<InputT, OutputT> {
  private static final String NEW_LINE = "\n";
  private final Process process;
  private final Function<InputT, String> inputConverter;
  private final Function<List<String>, OutputT> outputConverter;
  private final ProcessStreamReader inputStreamReader;

  public ProcessExecutorImpl(List<String> commands, Function<List<String>, OutputT> outputConverter)
      throws IOException {
    this(commands, Objects::toString, outputConverter);
  }

  /**
   * 指定されたコマンドでサブプロセスを実行し、指定の入出力関数を用いてラップする.
   *
   * @param commands 実行するコマンド
   * @param inputConverter 入力関数
   * @param outputConverter 出力関数
   * @throws IOException プロセスの起動に失敗した
   */
  public ProcessExecutorImpl(
      List<String> commands,
      Function<InputT, String> inputConverter,
      Function<List<String>, OutputT> outputConverter)
      throws IOException {
    this(
        new ProcessBuilder(commands).redirectError(Redirect.INHERIT).start(),
        inputConverter,
        outputConverter);
  }

  /**
   * 指定の入出力関数を用いてプロセスをラップする.
   *
   * @param process プロセス
   * @param inputConverter 入力関数
   * @param outputConverter 出力関数
   */
  public ProcessExecutorImpl(
      Process process,
      Function<InputT, String> inputConverter,
      Function<List<String>, OutputT> outputConverter) {
    this.process = process;
    this.inputConverter = inputConverter;
    this.outputConverter = outputConverter;
    this.inputStreamReader = new ProcessStreamReader(process.getInputStream());
  }

  @Override
  public OutputT exec(InputT input) throws IOException, InterruptedException {
    String inputText = inputConverter.apply(input);
    writeToProcessStream(inputText);
    var lines = inputStreamReader.getStreamLines();
    return outputConverter.apply(lines);
  }

  private void writeToProcessStream(String text) throws IOException {
    BufferedWriter bufferedWriter =
        new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    bufferedWriter.write(text);
    bufferedWriter.write(NEW_LINE); // BufferedWriter::newLineは改行コードが環境で変わるのでNG
    bufferedWriter.flush();
  }

  @Override
  public void close() throws IOException, InterruptedException {
    inputStreamReader.shutdown();
    process.getErrorStream().close();
    process.getInputStream().close();
    process.getOutputStream().close();
    process.destroy();
    process.waitFor();
  }

  @Override
  public boolean isAlive() {
    return process.isAlive();
  }
}
