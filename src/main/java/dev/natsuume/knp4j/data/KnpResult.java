package dev.natsuume.knp4j.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KnpResult {
  private final List<String> rawData;
  private final List<KnpClause> knpClauses = new ArrayList<>();
  private final double score;

  public KnpResult(List<String> lines) {
    this.rawData = lines;
    this.score = Double.parseDouble(lines.get(0).split(" ")[4].split(":")[1]);

    List<String> clauseLines = new ArrayList<>();
    for(String line : lines.subList(1, lines.size()-1)) {
      if(line.startsWith("*") && !clauseLines.isEmpty()) {
        var clause = new KnpClause(clauseLines);
        knpClauses.add(clause);
        clauseLines.clear();
      }
      clauseLines.add(line);
    }
    if(!clauseLines.isEmpty()) {
      var clause = new KnpClause(clauseLines);
      knpClauses.add(clause);
    }

    for(KnpClause clause : knpClauses) {
      if(clause.getDependencyTargetIdx() == -1) {
        continue;
      }
      knpClauses.get(clause.getDependencyTargetIdx()).addDependencyClause(clause);
    }
  }

  public String toString() {
    return String.join("\n", rawData);
  }

  public String getSurfaceForm() {
    return knpClauses.stream().map(KnpClause::getSurfaceForm).collect(Collectors.joining());
  }

  public KnpClause getRootNode() {
    return knpClauses.get(knpClauses.size()-1);
  }
  public double getScore(){
    return score;
  }
}
