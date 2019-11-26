package dev.natsuume.knp4j.data.builder;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class KnpElementParser {
  private static final char CLAUSE_PREFIX = '*';
  private static final char PHRASE_PREFIX = '+';
  private static final int BASIC_INFO_IDX_POSITION = 1;
  private static final int BASIC_INFO_DEPENDENCY_POSITION = 2;
  private static final int NO_IDX = -1;

  private static final Pattern FEATURE_PATTERN = Pattern.compile("(?<=<).+(?=>)");
  private static final String BASIC_INFO_DELIMITER = " ";
  private static final String EOS = "EOS";

  protected final int idx;
  protected final DependencyTarget dependencyTarget;
  protected final ElementInfo info;
  protected final List<String> rawFeatures;

  public KnpElementParser(String line) {
    this.info = new ElementInfo(line);
    this.rawFeatures = parseRawFeatures(info);
    this.idx = parseIdx(info);
    this.dependencyTarget = parseDependencyTarget(info);

  }

  private DependencyTarget parseDependencyTarget(ElementInfo info) {
    if(!info.isPhrase() && !info.isClause()){
      return null;
    }
    var basicInfo = info.basicInfo.split(BASIC_INFO_DELIMITER);
    return new DependencyTarget(basicInfo[BASIC_INFO_DEPENDENCY_POSITION]);
  }

  private List<String> parseRawFeatures(ElementInfo info) {
    return FEATURE_PATTERN
        .matcher(info.featureInfo)
        .results()
        .map(MatchResult::group)
        .collect(Collectors.toList());
  }

  private int parseIdx(ElementInfo info) {
    var basicInfo = info.basicInfo;
    switch (basicInfo.charAt(0)) {
      case CLAUSE_PREFIX:
      case PHRASE_PREFIX:
        var idxString = basicInfo.split(BASIC_INFO_DELIMITER)[BASIC_INFO_IDX_POSITION];
        return Integer.parseInt(idxString);
      default:
        return NO_IDX;
    }
  }

  protected class ElementInfo {
    private static final int BASIC_INFO_IDX = 0;
    private static final int FEATURE_INFO_IDX = 1;
    private static final String FEATURE_BEGIN_DELIMITER = " (?=<)";
    public final String basicInfo;
    public final String featureInfo;

    public ElementInfo(String line) {
      var info = line.split(FEATURE_BEGIN_DELIMITER);
      this.basicInfo = info[BASIC_INFO_IDX];
      this.featureInfo = info[FEATURE_INFO_IDX];
    }

    public boolean isClause() {
      return basicInfo.charAt(0) == CLAUSE_PREFIX;
    }

    public boolean isPhrase() {
      return basicInfo.charAt(0) == PHRASE_PREFIX;
    }

    public boolean isMorpheme() {
      return !isClause() && !isPhrase() && !basicInfo.startsWith(EOS);
    }
  }
}
