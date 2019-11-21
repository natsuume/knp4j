package dev.natsuume.knp4j;

import dev.natsuume.knp4j.process.ProcessExecutor;
import dev.natsuume.knp4j.process.ProcessExecutorBuilder;
import dev.natsuume.knp4j.process.ProcessManager;
import dev.natsuume.knp4j.wrapper.KnpWrapper;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Test {
  public static void main(String[] args) {
    var knpWrapper = new KnpWrapper();
    try (Scanner sc = new Scanner(System.in)) {
      while (true) {
        String line;
        if ((line = sc.nextLine()) != null) {
          System.out.println("input : " + line);
          var result = knpWrapper.analyze(line);
          System.out.println("score: " + result.getScore());
          result.getRootNode().getDependencySurfaceForm().stream().forEach(System.out::println);
        }
      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }
}
