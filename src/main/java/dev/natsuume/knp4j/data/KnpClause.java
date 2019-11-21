package dev.natsuume.knp4j.data;

import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KnpClause {
  private static final Pattern FEATURE_PATTERN = Pattern.compile("(?<=<).+(?=>)");
  private static final String INFO_SPLIT_PATTERN = " (?=<)";
  private static final String IDX_INFO_SEPARATOR = " ";

  private final List<KnpPhrase> phrases;
  private final int idx;
  private final DependencyTarget dependencyTarget;
  private final List<String> rawFeatures;
  private final List<KnpClause> dependencies = new ArrayList<>();

  public KnpClause(List<String> lines) {
    String[] clauseInfoArray = lines.get(0).split(INFO_SPLIT_PATTERN);
    var idxInfo = clauseInfoArray[0].split(IDX_INFO_SEPARATOR);
    this.idx = Integer.parseInt(idxInfo[1]);
    this.dependencyTarget = new DependencyTarget(idxInfo[2]);
    this.rawFeatures =
        FEATURE_PATTERN
            .matcher(clauseInfoArray[1])
            .results()
            .map(MatchResult::group)
            .collect(Collectors.toList());
    this.phrases = createPhrases(lines.subList(1, lines.size()));
  }

  private List<KnpPhrase> createPhrases(List<String> lines) {
    List<KnpPhrase> knpPhrases = new ArrayList<>();
    List<String> phraseLines = new ArrayList<>();
    for (String line : lines) {
      if (line.startsWith("+") && !phraseLines.isEmpty()) {
        var phrase = new KnpPhrase(phraseLines);
        knpPhrases.add(phrase);
        phraseLines.clear();
      }
      phraseLines.add(line);
    }
    if (!phraseLines.isEmpty()) {
      var phrase = new KnpPhrase(phraseLines);
      knpPhrases.add(phrase);
    }
    return knpPhrases;
  }

  public String getSurfaceForm() {
    return phrases.stream().map(KnpPhrase::getSurfaceForm).collect(Collectors.joining());
  }

  public void addDependencyClause(KnpClause clause) {
    this.dependencies.add(clause);
  }

  public int getIdx() {
    return idx;
  }

  public List<String> getDependencySurfaceForm() {
    if(dependencies.isEmpty()){
      return List.of(getSurfaceForm());
    }
    if(dependencies.size() == 1) {
      return dependencies.get(0).getDependencySurfaceForm()
        .stream()
        .map(string -> string + getSurfaceForm())
        .collect(Collectors.toList());
    }

    List<String> surfaceForms = dependencies
      .stream()
      .map(KnpClause::getDependencySurfaceForm)
      .flatMap(List::stream)
      .collect(Collectors.toList());

    Collection<List<String>> forms = Collections2.permutations(surfaceForms);
    System.out.println("forms: " + forms);
    return forms.stream()
      .map(list -> String.join("", list) + getSurfaceForm())
      .collect(Collectors.toList());
  }

  public int getDependencyTargetIdx () {
    return dependencyTarget.getTargetIdx();
  }
}
