package dev.natsuume.knp4j.process;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ProcessExecutor<InputT, OutputT> {
  private final Process process;
  private final Function<InputT, String> inputConverter;
  private final Function<List<String>, OutputT> outputConverter;
  private final ProcessStreamReader inputStreamReader;

  public ProcessExecutor(List<String> commands, Function<List<String>, OutputT> outputConverter)
      throws IOException {
    this(commands, Objects::toString, outputConverter);
  }

  /**
   * 指定されたコマンドでサブプロセスを実行し、指定の入出力関数を用いてラップする.
   * @param commands 実行するコマンド
   * @param inputConverter 入力関数
   * @param outputConverter 出力関数
   * @throws IOException プロセスの起動に失敗した
   */
  public ProcessExecutor(
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
   * @param process プロセス
   * @param inputConverter 入力関数
   * @param outputConverter 出力関数
   */
  public ProcessExecutor(
      Process process,
      Function<InputT, String> inputConverter,
      Function<List<String>, OutputT> outputConverter) {
    this.process = process;
    this.inputConverter = inputConverter;
    this.outputConverter = outputConverter;
    this.inputStreamReader = new ProcessStreamReader(process.getInputStream());
  }

  /**
   * プロセスに入力を与え、その結果を返す.
   * @param input 入力
   * @return 出力
   */
  public OutputT exec(InputT input) {
    List<String> readLines = new ArrayList<>();
    try {
      String inputText = inputConverter.apply(input);
      BufferedWriter bufferedWriter =
          new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      bufferedWriter.write(inputText);
      bufferedWriter.write("\n"); // BufferedWriter::newLineは改行コードが環境で変わるのでNG
      bufferedWriter.flush();

      readLines = inputStreamReader.getStreamLines();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return outputConverter.apply(readLines);
  }
}
