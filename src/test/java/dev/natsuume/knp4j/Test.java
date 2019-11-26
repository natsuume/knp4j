package dev.natsuume.knp4j;

import dev.natsuume.knp4j.wrapper.KnpWrapperBuilder;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
  public static void main(String[] args) {
    var knpWrapper = new KnpWrapperBuilder()
        .setKnpStartNum(1)
        .setJumanStartNum(1)
        .setKnpMaxNum(10)
        .setJumanMaxNum(10)
        .start();
    List<String> strings =
        IntStream.range(0, 10).mapToObj(String::valueOf).collect(Collectors.toList());
    try (Scanner sc = new Scanner(System.in)) {
      strings.parallelStream().forEach(str -> {
        try{
          System.out.println("success: " +knpWrapper.analyze(str).getSurfaceForm());
        }catch (IOException | InterruptedException e){
          System.err.println("failed: " + str);
//          e.printStackTrace();
        }
      });
      System.out.println("finished.");
      knpWrapper.close();
      System.out.println("closed.");
//      while (true) {
//        String line;
//        if ((line = sc.nextLine()) != null) {
//          line = "こんにちは\n" + "今日もいい天気ですね";
//          System.out.println("input : " + line);
//          var result = knpWrapper.analyze(line);
//          System.out.println(result);
//                    System.out.println("score: " + result.getScore());
//
//           result.getRootNode().getDependencySurfaceForm().stream().forEach(System.out::println);
//        }
//      }
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    System.exit(0);
  }
}
