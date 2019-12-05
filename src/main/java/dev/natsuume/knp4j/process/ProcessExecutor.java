package dev.natsuume.knp4j.process;

import java.io.IOException;

public interface ProcessExecutor<InputT, OutputT> {

  /**
   * プロセスに入力を与え、その結果を返す.
   * @param input 入力
   * @return 出力
   * @throws IOException
   * @throws InterruptedException
   */
  OutputT exec(InputT input) throws IOException, InterruptedException;

  /**
   * プロセスをcloseする.
   * @throws IOException
   * @throws InterruptedException
   */
  void close() throws IOException, InterruptedException;

  /**
   * プロセスが現在も生きているかを返す.
   * @return プロセスが生きているか
   */
  boolean isAlive();
}
