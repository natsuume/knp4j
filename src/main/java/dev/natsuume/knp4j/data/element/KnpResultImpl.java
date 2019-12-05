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

  public KnpResultImpl(List<String> lines) {
    this.rawData = lines;
    this.score = Double.parseDouble(lines.get(0).split(" ")[4].split(":")[1]);
    knpClauses = new ArrayList<>();
    List<String> clauseLines = new ArrayList<>();
    for (String line : lines.subList(1, lines.size() - 1)) {
      if (line.startsWith("*") && !clauseLines.isEmpty()) {
        //        var clause = new KnpClause(clauseLines);
        //        knpClauses.add(clause);
        clauseLines.clear();
      }
      clauseLines.add(line);
    }
    if (!clauseLines.isEmpty()) {
      //      var clause = new KnpClause(clauseLines);
      //      knpClauses.add(clause);
    }

    for (KnpClause clause : knpClauses) {
      if (clause.getDependencyTargetIdx() == -1) {
        continue;
      }
      //      knpClauses.getType(clause.getDependencyTargetIdx()).addDependencyClause(clause);
    }
  }

  public String toString() {
    return String.join("\n", rawData);
  }

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

  public KnpClause getRootNode() {
    return knpClauses.get(knpClauses.size() - 1);
  }

  @Override
  public List<String> getRawResultData() {
    return new ArrayList<>(rawData);
  }

  public double getScore() {
    return score;
  }

  @Override
  public boolean isValid() {
    return true;
  }
}
