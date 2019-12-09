package dev.natsuume.knp4j;

import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.parser.KnpResultParser;
import dev.natsuume.knp4j.parser.ResultParser;
import dev.natsuume.knp4j.wrapper.KnpWrapper;
import dev.natsuume.knp4j.wrapper.KnpWrapperBuilder;
import java.util.List;

public class Sample {
  public static void main(String[] args) {
    // KNPWrapperを作成するためのBuilder
    ResultParser<KnpResult> knpResultParser = new KnpResultParser();
    KnpWrapperBuilder<KnpResult> knpWrapperBuilder = new KnpWrapperBuilder<>();
    KnpWrapper<KnpResult> wrapper =
        knpWrapperBuilder
            .setJumanCommand(List.of("bash", "-c", "jumanpp")) // Jumanの実行コマンド
            .setKnpCommand(
                List.of(
                    "bash",
                    "-c",
                    "knp -tab -print-num -anaphora")) // KNPの実行コマンド(現在は「-tab」「-print-num」「-anaphora」オプション必須)
            .setJumanMaxNum(5) // 同時に起動するJumanの最大プロセス数
            .setJumanStartNum(1) // 初期化時に起動するJumanのプロセス数
            .setKnpMaxNum(5) // 同時に起動するKNPの最大プロセス数
            .setKnpStartNum(1) // 初期化時に起動するKNPのプロセス数
            .setRetryNum(0) // 結果の取得に失敗した場合にリトライする回数
            .setResultParser(knpResultParser)
            .start();
    var texts = List.of("テストテキスト1です", "テストテキスト2です", "テストテキスト3です");
    texts
        .parallelStream()
        .map(wrapper::analyze)
        .flatMap(List::stream)
        .map(KnpResult::getSurfaceForm)
        .forEach(System.out::println);
  }
}
