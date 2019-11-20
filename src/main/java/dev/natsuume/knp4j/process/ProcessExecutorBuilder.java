package dev.natsuume.knp4j.process;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ProcessExecutorBuilder<InputT, OutputT> {
  private Function<InputT, String> inputConverter = Objects::toString;
  private Function<List<String>, OutputT> outputConverter;
  private ProcessBuilder processBuilder;

  public ProcessExecutorBuilder() {
    this.processBuilder = new ProcessBuilder();
  }

  /**
   * 実行するコマンドを設定する.
   *
   * @param commands 実行するコマンド
   * @return このインスタンス
   */
  public ProcessExecutorBuilder<InputT, OutputT> setCommand(List<String> commands) {
    processBuilder.command(commands);
    return this;
  }

  /**
   * サブプロセスに対する入力を任意の型からString型に変換する関数を設定する. デフォルトはObject::toString.
   *
   * @param inputConverter 入力関数
   * @return このインスタンス
   */
  public ProcessExecutorBuilder<InputT, OutputT> setInputConverter(
      Function<InputT, String> inputConverter) {
    this.inputConverter = inputConverter;
    return this;
  }

  /**
   * サブプロセスの出力として得られたStringのListを任意の出力型に変換する関数を設定する.
   *
   * @param outputConverter 出力関数
   * @return このインスタンス
   */
  public ProcessExecutorBuilder<InputT, OutputT> setOutputConverter(
      Function<List<String>, OutputT> outputConverter) {
    this.outputConverter = outputConverter;
    return this;
  }

  /**
   * このbuilderを使って新規サブプロセスを起動する.
   *
   * @return サブプロセスをラップしたProcessExecutorインスタンス
   * @throws IOException プロセスの起動に失敗した
   */
  public ProcessExecutor<InputT, OutputT> start() {
    try {
      var process = processBuilder.start();
      return new ProcessExecutor<>(process, inputConverter, outputConverter);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.exit(1);
    return null;
  }
}
