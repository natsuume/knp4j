package dev.natsuume.knp4j.data;

import java.util.List;

public class KnpResult {
  private final List<String> rawData;
  //  private final List<KnpClause> knpClauses;
  //  private final List<KnpPhrase> knpPhrases;

  public KnpResult(List<String> lines) {
    this.rawData = lines;
  }

  public String toString() {
    return String.join("\n", rawData);
  }
}
