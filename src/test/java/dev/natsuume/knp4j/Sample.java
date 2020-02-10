package dev.natsuume.knp4j;

import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.parser.KnpResultParser;
import dev.natsuume.knp4j.wrapper.KnpWrapper;
import dev.natsuume.knp4j.wrapper.KnpWrapperBuilder;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Sample {
  public static void main(String[] args) {
    testAnalyzing();
//    testPerformance();
    System.exit(0);
  }

  private static void testPerformance() {
    long time = System.currentTimeMillis();

    KnpWrapperBuilder<KnpResult> knpWrapperBuilder = new KnpWrapperBuilder<>();
    int jumanMaxNum = 1;
    int knpMaxNum = 5;
    KnpWrapper<KnpResult> wrapper =
        knpWrapperBuilder
            .setJumanMaxNum(jumanMaxNum)
            .setKnpMaxNum(knpMaxNum)
            .setResultParser(new KnpResultParser())
            .start();
    var sampleText = "ノリでアドベントカレンダーに登録したけど、"
        + "間に合う気配がないので今日は%d時間作業をしてからでないと寝ることができない。";
    var texts =
        IntStream.range(0, 100)
            .mapToObj(i -> String.format(sampleText, i))
            .collect(Collectors.toList());
    var results =
        texts
            .parallelStream()
            .map(wrapper::analyze)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    System.out.println("time: " + (System.currentTimeMillis() - time));
  }

  private static void testAnalyzing() {
    List<String> tests = List.of(
    );
    KnpWrapperBuilder<KnpResult> knpWrapperBuilder = new KnpWrapperBuilder<>();
    int jumanMaxNum = 1;
    int knpMaxNum = 5;
    KnpWrapper<KnpResult> wrapper =
        knpWrapperBuilder
            .setJumanMaxNum(jumanMaxNum)
            .setKnpMaxNum(knpMaxNum)
            .setResultParser(new KnpResultParser())
            .start();

    tests.stream()
        .map(wrapper::analyze)
        .flatMap(List::stream)
        .map(KnpResult::getRawResultData)
        .forEach(System.out::println);
  }
}
