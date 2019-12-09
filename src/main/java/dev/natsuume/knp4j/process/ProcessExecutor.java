package dev.natsuume.knp4j.process;

import io.vavr.control.Try;
import java.io.IOException;

public interface ProcessExecutor<InputT, OutputT> {

  /**
   * プロセスに入力を与え、その結果を返す.
   *
   * @param input 入力
   * @return 出力
   * @throws IOException プロセスのIO処理に失敗した
   * @throws InterruptedException Threadの割り込みが発生した
   */
  OutputT exec(InputT input) throws IOException, InterruptedException;

  /**
   * プロセスをcloseする.
   *
   * @throws IOException プロセスのIO処理に失敗した
   * @throws InterruptedException Threadの割り込みが発生した
   */
  void close() throws IOException, InterruptedException;

  /**
   * プロセスが現在も生きているかを返す.
   *
   * @return プロセスが生きているか
   */
  boolean isAlive();
}
