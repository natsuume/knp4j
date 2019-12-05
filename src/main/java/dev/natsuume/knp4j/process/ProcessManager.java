package dev.natsuume.knp4j.process;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
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
  private final Set<ProcessExecutor<InputT, OutputT>> allProcesses;
  private boolean isAlive = true;

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
   * @param maxProcessNum 同時に実行する最大プロセス数(n > 0)
   * @param processSupplier 実行するプロセスのSupplier
   */
  public ProcessManager(
      int maxProcessNum, Supplier<ProcessExecutor<InputT, OutputT>> processSupplier) {
    this(maxProcessNum, DEFAULT_FIRST_PROCESS_NUM, processSupplier);
  }

  /**
   * constructor.
   *
   * @param maxProcessNum 同時に実行する最大プロセス数(n > 0)
   * @param firstProcessNum インスタンス生成時に実行するプロセス数(0 < n < maxProcessNum)
   * @param processSupplier 実行するプロセスのSupplier
   */
  public ProcessManager(
      int maxProcessNum,
      int firstProcessNum,
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
    allProcesses = Collections.synchronizedSet(new HashSet<>(maxProcessNum));

    var processes =
        IntStream.range(0, firstProcessNum)
            .mapToObj(i -> processSupplier.get())
            .collect(Collectors.toList());
    processExecutors.addAll(processes);
    this.allProcesses.addAll(processExecutors);
  }

  /**
   * 入力をプロセスに与えた結果を取得する.
   *
   * @param input 入力
   * @return プロセスの処理結果
   */
  public OutputT exec(InputT input) throws InterruptedException, IOException{
    ProcessExecutor<InputT, OutputT> processExecutor = getProcessExecutor();
    OutputT result;

    try{
      result = processExecutor.exec(input);
    }catch (IOException | InterruptedException e) {
      try{
        processExecutor.close();
        synchronized (allProcesses){
          allProcesses.remove(processExecutor);
          addNewProcess();
        }
      }catch (IOException e2) {
        isAlive = false;
        e.addSuppressed(e2);
      }
      throw e;
    }

    if(processExecutor.isAlive()){
      processExecutors.put(processExecutor);
    }
    return result;
  }

  private ProcessExecutor<InputT, OutputT> getProcessExecutor() throws InterruptedException {
    var processExecutor = processExecutors.poll();
    if (processExecutor == null) {
      if (allProcesses.size() < maxProcessNum) {
        addNewProcess();
      }
      processExecutor = processExecutors.take();
    }

    return processExecutor;
  }

  private void addNewProcess() throws InterruptedException{
    synchronized (allProcesses) {
      if(allProcesses.size() >= maxProcessNum){
        return;
      }

      var process = processSupplier.get();
      processExecutors.put(process);
      allProcesses.add(process);
    }
  }

  /**
   * 利用可能かどうかを返す.
   * @return 利用可能かどうか
   */
  public boolean isAlive() {
    return isAlive;
  }

  /**
   * このProcessManagerが管理しているProcessをcloseする.
   * @throws IOException
   * @throws InterruptedException
   */
  public void close() throws IOException, InterruptedException{
    synchronized (allProcesses) {
      for(ProcessExecutor<InputT, OutputT> executor: allProcesses) {
        executor.close();
      }
    }
  }
}
