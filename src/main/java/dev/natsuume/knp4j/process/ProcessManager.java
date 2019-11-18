package dev.natsuume.knp4j.process;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcessManager<InputT, OutputT> {

  private static final int DEFAULT_MAX_PROCESS_NUM = 1;
  private static final int DEFAULT_FIRST_PROCESS_NUM = 1;
  private final int maxProcessNum;
  private final Supplier<ProcessExecutor<InputT, OutputT>> processSupplier;
  private final ExecutorService executorService = Executors.newCachedThreadPool();

  private final BlockingDeque<ProcessExecutor<InputT, OutputT>> processExecutors;

  /**
   * constructor.
   *
   * @param processSupplier 実行するプロセスのSupplier
   */
  public ProcessManager(Supplier<ProcessExecutor<InputT, OutputT>> processSupplier) {
    this(DEFAULT_MAX_PROCESS_NUM, DEFAULT_FIRST_PROCESS_NUM, processSupplier);
  }

  /**
   * constructor.
   *
   * @param maxProcessNum   同時に実行する最大プロセス数(n > 0)
   * @param processSupplier 実行するプロセスのSupplier
   */
  public ProcessManager(int maxProcessNum,
      Supplier<ProcessExecutor<InputT, OutputT>> processSupplier) {
    this(maxProcessNum, DEFAULT_FIRST_PROCESS_NUM, processSupplier);
  }

  /**
   * constructor.
   *
   * @param maxProcessNum   同時に実行する最大プロセス数(n > 0)
   * @param firstProcessNum インスタンス生成時に実行するプロセス数(0 < n < maxProcessNum)
   * @param processSupplier 実行するプロセスのSupplier
   */
  public ProcessManager(int maxProcessNum, int firstProcessNum,
      Supplier<ProcessExecutor<InputT, OutputT>> processSupplier) {
    this.processSupplier = processSupplier;
    this.maxProcessNum = maxProcessNum;

    if (maxProcessNum < DEFAULT_MAX_PROCESS_NUM || firstProcessNum < DEFAULT_FIRST_PROCESS_NUM) {
      throw new IllegalArgumentException("maxProcessNum and firstProcessNum must be over 0. ");
    }

    if (firstProcessNum > maxProcessNum) {
      firstProcessNum = maxProcessNum;
    }

    this.processExecutors = new LinkedBlockingDeque<>(maxProcessNum);

    var processes = IntStream
        .range(0, firstProcessNum)
        .mapToObj(i -> processSupplier.get())
        .collect(Collectors.toList());
    processExecutors.addAll(processes);
  }

  /**
   * 入力をプロセスに与えた結果を取得する.
   *
   * @param input 入力
   * @return プロセスの処理結果
   * @throws InterruptedException 待機中に割り込みが発生した場合
   */
  public OutputT exec(InputT input) throws InterruptedException, IOException {
    var processExecutor = processExecutors.take();
    var result = processExecutor.exec(input);
    processExecutors.put(processExecutor);
    return result;
  }
}
