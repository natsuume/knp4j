package dev.natsuume.knp4j;

import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.data.element.KnpResultParser;
import dev.natsuume.knp4j.wrapper.KnpWrapperBuilder;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
  public static void main(String[] args) {

    var wrapper = new KnpWrapperBuilder()
        .start();
    var test =
        "人間です\n人狼です\n狂人です\n村人です+人間で*す\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+人間です\n人狼です\n狂人です\n村人です+";
    wrapper.analyze(test).stream().map(KnpResult::getRawResultData).forEach(System.out::println);
 }
}
