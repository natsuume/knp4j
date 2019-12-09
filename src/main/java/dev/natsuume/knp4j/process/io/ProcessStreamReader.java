package dev.natsuume.knp4j.process.io;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ProcessStreamReader {
  private final BlockingDeque<List<String>> processStreamLines = new LinkedBlockingDeque<>();
  private ExecutorService executorService = Executors.newSingleThreadExecutor();

  public ProcessStreamReader(InputStream inputStream) {
    var processStreamThread = new ProcessStreamThread(inputStream, this::putStreamLines);
    executorService.execute(processStreamThread);
  }

  public List<String> getStreamLines() throws InterruptedException {
    return processStreamLines.take();
  }

  private void putStreamLines(List<String> streamLines) {
    try {
      processStreamLines.put(streamLines);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public boolean isEmpty() {
    return processStreamLines.isEmpty();
  }

  public void shutdown() {
    executorService.shutdown();
  }
}
