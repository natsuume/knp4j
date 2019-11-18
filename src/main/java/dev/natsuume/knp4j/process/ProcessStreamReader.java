package dev.natsuume.knp4j.process;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ProcessStreamReader {
  private final BlockingDeque<List<String>> processStreamLines = new LinkedBlockingDeque<>();
  ExecutorService executorService = Executors.newSingleThreadExecutor();

  public ProcessStreamReader(InputStream inputStream) {
    var processStreamThread = new ProcessStreamThread(inputStream, this::updateStreamLines);
    executorService.execute(processStreamThread);
  }

  private void updateStreamLines(List<String> streamLines) {
    try{
      processStreamLines.put(streamLines);
    }catch (InterruptedException e){
      e.printStackTrace();
    }
  }

  public List<String> getStreamLines() throws InterruptedException{
    return processStreamLines.take();
  }

  public boolean isEmpty() {
    return processStreamLines.isEmpty();
  }
}
