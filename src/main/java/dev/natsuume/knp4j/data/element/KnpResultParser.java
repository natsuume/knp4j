package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.define.KnpResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KnpResultParser {
  private static final char CLAUSE_SYMBOL = '*';
  private static final char PHRASE_SYMBOL = '+';
  private static final String EOS = "EOS";
  private static final int META_DATA_IDX = 0;
  private static final int SCORE_IDX = 4;
  private static final int SCORE_VALUE_IDX = 1;
  private static final String BASIC_INFO_DELIMITER = " ";
  private static final String SCORE_DELIMITER = ":";
  private static final String IGNORE_CHARACTER = "#";
  List<String> rawData;
  double score;
  List<KnpClause> knpClauses = new ArrayList<>();
  private boolean isValid;

  public KnpResultParser(List<String> results) {
    this.rawData = results;
    this.isValid = isValid(results);
    if (!isValid) {
      return;
    }
    var scoreString =
        results.get(META_DATA_IDX)
            .split(BASIC_INFO_DELIMITER)[SCORE_IDX]
            .split(SCORE_DELIMITER)[SCORE_VALUE_IDX];
    this.score = Double.parseDouble(scoreString);
    parseResults(results.subList(1, results.size()));
  }

  private void parseResults(List<String> results) {
    KnpClauseParser clauseParser = null;
    KnpPhraseParser phraseParser = null;

    for (String line : results) {
      switch (line.charAt(0)) {
        case CLAUSE_SYMBOL:
          if (clauseParser != null) {
            final int idx = clauseParser.idx;
            var dependencies =
                knpClauses.stream()
                    .filter(clause -> clause.getDependencyTargetIdx() == idx)
                    .collect(Collectors.toList());
            var clause =
                clauseParser
                    .addPhrase(phraseParser.build())
                    .addDependencyClauses(dependencies)
                    .build();

            knpClauses.add(clause);
            phraseParser = null;
          }
          clauseParser = new KnpClauseParser(line);
          break;
        case PHRASE_SYMBOL:
          if (phraseParser != null) {
            var phrase = phraseParser.build();
            clauseParser.addPhrase(phrase);
          }
          phraseParser = new KnpPhraseParser(line);
          break;
        default:
          if (line.equals(EOS)) {
            final int idx = clauseParser.idx;
            var dependencies =
                knpClauses.stream()
                    .filter(clause -> clause.getDependencyTargetIdx() == idx)
                    .collect(Collectors.toList());
            var clause =
                clauseParser
                    .addPhrase(phraseParser.build())
                    .addDependencyClauses(dependencies)
                    .build();

            knpClauses.add(clause);
            break;
          }
          var morpheme = new KnpMorphemeParser(line).build();
          phraseParser.addMorpheme(morpheme);
      }
    }
  }

  private boolean isValid(List<String> results) {
    return results.get(META_DATA_IDX).startsWith(IGNORE_CHARACTER) || results.size() > 2;
  }

  private void parseMetaData() {
    var metaData = rawData.get(META_DATA_IDX);
  }

  public KnpResult build() {
    return isValid ? new KnpResultImpl(this) : KnpResult.INVALID_RESULT;
  }
}
