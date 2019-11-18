package dev.natsuume.knp4j.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ProcessStreamThread implements Runnable {
  private final InputStream inputStream;
  private final Consumer<List<String>> updateStreamLines;

  public ProcessStreamThread(InputStream inputStream, Consumer<List<String>> updateStreamLines) {
    this.inputStream = inputStream;
    this.updateStreamLines = updateStreamLines;
  }

  @Override
  public void run() {
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      while (true) {
        List<String> streamLines = readLines(bufferedReader);

        if (streamLines.isEmpty()) {
          continue;
        }

        updateStreamLines.accept(streamLines);
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
      streamLines.add(line);
    }
    return streamLines;
  }
}
