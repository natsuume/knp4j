package dev.natsuume.knp4j.data;

import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KnpPhrase {
  private static final Pattern FEATURE_PATTERN = Pattern.compile("(?<=<).+(?=>)");
  private static final String INFO_SPLIT_PATTERN = " (?=<)";
  private static final String IDX_INFO_SEPARATOR = " ";
  private final List<KnpMorpheme> morphemes;
  private final int idx;
  private final DependencyTarget dependencyTarget;
  private final List<String> rawFeatures;

  public KnpPhrase(List<String> lines) {
    String[] phraseInfoArray = lines.get(0).split(INFO_SPLIT_PATTERN);
    var idxInfo = phraseInfoArray[0].split(IDX_INFO_SEPARATOR);
    this.idx = Integer.parseInt(idxInfo[1]);
    this.dependencyTarget = new DependencyTarget(idxInfo[2]);
    this.rawFeatures =
        FEATURE_PATTERN
            .matcher(phraseInfoArray[1])
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toList());
    this.morphemes =
        lines.subList(1, lines.size()).stream().map(KnpMorpheme::new).collect(Collectors.toList());
  }

  public String getSurfaceForm() {
    return morphemes.stream().map(KnpMorpheme::getSurfaceForm).collect(Collectors.joining());
  }
}
