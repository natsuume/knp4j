package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.define.KnpResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KnpResultImpl implements KnpResult {
  private final List<String> rawData;
  private final List<KnpClause> knpClauses;
  private final double score;

  KnpResultImpl(KnpResultParser parser) {
    this.rawData = parser.rawData;
    this.score = parser.score;
    this.knpClauses = parser.knpClauses;
  }

  @Override
  public String toString() {
    return String.join("\n", rawData);
  }

  @Override
  public String getSurfaceForm() {
    return knpClauses.stream().map(KnpClause::getSurfaceForm).collect(Collectors.joining());
  }

  @Override
  public List<KnpClause> getClauses() {
    return new ArrayList<>(knpClauses);
  }

  @Override
  public List<KnpMorpheme> getMorphemes() {
    return knpClauses.stream()
        .map(KnpClause::getMorphemes)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  @Override
  public List<KnpPhrase> getPhrases() {
    return knpClauses.stream()
        .map(KnpClause::getPhrases)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  /**
   * 文末の文節を返す.
   * @return 文末の文節
   */
  public KnpClause getRootNode() {
    return knpClauses.get(knpClauses.size() - 1);
  }

  @Override
  public List<String> getRawResultData() {
    return new ArrayList<>(rawData);
  }

  /**
   * スコアを返す.
   * @return スコア
   */
  public double getScore() {
    return score;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
