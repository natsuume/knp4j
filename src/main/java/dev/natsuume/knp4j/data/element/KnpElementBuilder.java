package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.define.KnpFeature;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class KnpElementBuilder {
  static final String BASIC_INFO_DELIMITER = " ";
  private static final char CLAUSE_PREFIX = '*';
  private static final char PHRASE_PREFIX = '+';
  private static final int BASIC_INFO_IDX_POSITION = 1;
  private static final int BASIC_INFO_DEPENDENCY_POSITION = 2;
  private static final int NO_IDX = -1;
  private static final Pattern FEATURE_PATTERN = Pattern.compile("(?<=<).+?(?=>)");
  private static final String EOS = "EOS";

  final int idx;
  final DependencyTarget dependencyTarget;
  final ElementInfo info;
  final List<KnpFeature> features;

  KnpElementBuilder(String line) {
    this.info = new ElementInfo(line);
    this.features = parseFeatures(info);
    this.idx = parseIdx(info);
    this.dependencyTarget = parseDependencyTarget(info);
  }

  private DependencyTarget parseDependencyTarget(ElementInfo info) {
    if (!info.isPhrase() && !info.isClause()) {
      return null;
    }
    var basicInfo = info.basicInfo.split(BASIC_INFO_DELIMITER);
    return new DependencyTarget(basicInfo[BASIC_INFO_DEPENDENCY_POSITION]);
  }

  private List<KnpFeature> parseFeatures(ElementInfo info) {
    return FEATURE_PATTERN
        .matcher(info.featureInfo)
        .results()
        .map(MatchResult::group)
        .map(KnpFeatureImpl::new)
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

  final class ElementInfo {
    private static final int BASIC_INFO_IDX = 0;
    private static final int FEATURE_INFO_IDX = 1;
    private static final String FEATURE_BEGIN_DELIMITER = " (?=<)";
    final String basicInfo;
    final String featureInfo;

    ElementInfo(String line) {
      var info = line.split(FEATURE_BEGIN_DELIMITER);
      this.basicInfo = info[BASIC_INFO_IDX];
      this.featureInfo = info[FEATURE_INFO_IDX];
    }

    boolean isClause() {
      return basicInfo.charAt(0) == CLAUSE_PREFIX;
    }

    boolean isPhrase() {
      return basicInfo.charAt(0) == PHRASE_PREFIX;
    }

    boolean isMorpheme() {
      return !isClause() && !isPhrase() && !basicInfo.startsWith(EOS);
    }
  }
}
