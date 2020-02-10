package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.define.KnpResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KnpResultBuilder {
  private static final char CLAUSE_SYMBOL = '*';
  private static final char PHRASE_SYMBOL = '+';
  private static final String EOS = "EOS";
  private static final int META_DATA_IDX = 0;
  private static final int SCORE_VALUE_IDX = 1;
  private static final String BASIC_INFO_DELIMITER = " ";
  private static final String SCORE_DELIMITER = ":";
  private static final String SCORE_TEXT = "SCORE";
  private static final String IGNORE_CHARACTER = "#";
  private final boolean isValid;
  List<String> rawData;
  double score;
  List<KnpClause> knpClauses = new ArrayList<>();

  /**
   * KNPの解析結果からParserを生成する.
   *
   * @param results 解析結果文字列リスト
   */
  public KnpResultBuilder(List<String> results) {
    this.rawData = results;
    this.isValid = isValid(results);

    if (!isValid) {
      return;
    }

    var scoreString =
        Arrays.stream(results.get(META_DATA_IDX).split(BASIC_INFO_DELIMITER))
            .parallel()
            .filter(text -> text.startsWith(SCORE_TEXT))
            .findAny()
            .map(text -> text.split(SCORE_DELIMITER)[SCORE_VALUE_IDX])
            .orElseThrow();

    this.score = Double.parseDouble(scoreString);
    initialize(results.subList(1, results.size()));
  }

  private void initialize(List<String> results) {
    KnpClauseBuilder clauseBuilder = null;
    KnpPhraseBuilder phraseBuilder = null;

    for (String line : results) {
      switch (line.charAt(0)) {
        case CLAUSE_SYMBOL:
          if (clauseBuilder != null) {
            buildClause(clauseBuilder, phraseBuilder);
            phraseBuilder = null;
          }
          clauseBuilder = new KnpClauseBuilder(line);
          break;
        case PHRASE_SYMBOL:
          if (phraseBuilder != null) {
            clauseBuilder.addPhrase(phraseBuilder.build());
          }
          phraseBuilder = new KnpPhraseBuilder(line);
          break;
        default:
          if (line.equals(EOS)) {
            buildClause(clauseBuilder, phraseBuilder);
            break;
          }
          var morpheme = new KnpMorphemeBuilder(line).build();
          phraseBuilder.addMorpheme(morpheme);
      }
    }
  }

  private void buildClause(KnpClauseBuilder clauseBuilder, KnpPhraseBuilder phraseBuilder) {
    final int idx = clauseBuilder.idx;
    var dependencies =
        knpClauses.stream()
            .filter(clause -> clause.getDependencyTargetIdx() == idx)
            .collect(Collectors.toList());
    var clause =
        clauseBuilder.addPhrase(phraseBuilder.build()).addDependencyClauses(dependencies).build();

    knpClauses.add(clause);
  }

  private boolean isValid(List<String> results) {
    return results.get(META_DATA_IDX).startsWith(IGNORE_CHARACTER) || results.size() > 2;
  }

  private void parseMetaData() {
    var metaData = rawData.get(META_DATA_IDX);
  }

  /**
   * 情報を構造化したKnpResultインスタンスを返す.
   *
   * @return 解析結果のインスタンス
   */
  public KnpResult build() {
    return isValid ? new KnpResultImpl(this) : KnpResult.INVALID_RESULT;
  }
}
