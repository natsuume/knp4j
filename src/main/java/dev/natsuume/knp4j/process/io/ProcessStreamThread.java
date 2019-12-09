package dev.natsuume.knp4j.process.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.function.Consumer;

public class ProcessStreamThread implements Runnable {
  private final InputStream inputStream;
  private final Consumer<List<String>> outputConsumer;

  public ProcessStreamThread(InputStream inputStream, Consumer<List<String>> outputConsumer) {
    this.inputStream = inputStream;
    this.outputConsumer = outputConsumer;
  }

  @Override
  public void run() {
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      while (true) {
        List<String> streamLines = readLines(bufferedReader);

        if (streamLines.isEmpty()) {
          continue;
        }

        outputConsumer.accept(streamLines);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<String> readLines(BufferedReader bufferedReader) throws IOException {
    List<String> streamLines = new ArrayList<>();
    String line;
    while (bufferedReader.ready()) {
      line = bufferedReader.readLine();
      if (line == null) {
        continue;
      }
      streamLines.add(line);
    }
    return streamLines;
  }
}
