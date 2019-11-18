package dev.natsuume.knp4j;

import dev.natsuume.knp4j.process.ProcessExecutor;
import dev.natsuume.knp4j.process.ProcessManager;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Test {
  public static void main(String[] args) {
    ProcessManager<String, String> manager = new ProcessManager<>(Test::supply);
    try (Scanner sc = new Scanner(System.in)) {
      while (true) {
        String line;
        if ((line = sc.nextLine()) != null) {
          String result = manager.exec(line);
          System.out.println("result :" + result);
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static ProcessExecutor<String, String> supply() {
    try {
      return new ProcessExecutor<>(List.of("jshell"), list -> String.join("\n", list));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
