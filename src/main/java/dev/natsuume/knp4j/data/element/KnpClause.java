package dev.natsuume.knp4j.data.element;

import com.google.common.collect.Collections2;
import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import dev.natsuume.knp4j.data.define.KnpFeature;
import dev.natsuume.knp4j.data.define.KnpParentNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class KnpClause implements KnpParentNode {
  private static final Pattern FEATURE_PATTERN = Pattern.compile("(?<=<).+(?=>)");
  private static final String INFO_SPLIT_PATTERN = " (?=<)";
  private static final String IDX_INFO_SEPARATOR = " ";

  private final List<KnpPhrase> phrases;
  private final int idx;
  private final DependencyTarget dependencyTarget;
  private final List<KnpFeature> features;
  private final List<KnpClause> dependencies;

  public KnpClause(KnpClauseParser parser) {
    this.phrases = parser.phrases;
    this.idx = parser.idx;
    this.dependencyTarget = parser.dependencyTarget;
    this.dependencies = parser.dependencies;
    this.features = parser.features;
  }

  public String getSurfaceForm() {
    return phrases.stream().map(KnpPhrase::getSurfaceForm).collect(Collectors.joining());
  }

  @Override
  public String getReadingForm() {
    return phrases.stream().map(KnpPhrase::getReadingForm).collect(Collectors.joining());
  }

  public List<KnpClause> getDependencies(){
    return new ArrayList<>(dependencies);
  }

  @Override
  public List<KnpFeature> getFeatures() {
    return new ArrayList<>(features);
  }

  public int getIdx() {
    return idx;
  }

  @Override
  public List<KnpMorpheme> getMorphemes() {
    return phrases.stream()
        .map(KnpPhrase::getMorphemes)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  public List<KnpPhrase> getPhrases(){
    return new ArrayList<>(phrases);
  }

  @Deprecated
  public List<String> getDependencySurfaceForm() {
    if (dependencies.isEmpty()) {
      return List.of(getSurfaceForm());
    }
    if (dependencies.size() == 1) {
      return dependencies.get(0).getDependencySurfaceForm().stream()
          .map(string -> string + getSurfaceForm())
          .collect(Collectors.toList());
    }

    List<String> surfaceForms =
        dependencies.stream()
            .map(KnpClause::getDependencySurfaceForm)
            .flatMap(List::stream)
            .collect(Collectors.toList());

    Collection<List<String>> forms = Collections2.permutations(surfaceForms);
    System.out.println("forms: " + forms);
    return forms.stream()
        .map(list -> String.join("", list) + getSurfaceForm())
        .collect(Collectors.toList());
  }

  @Override
  public DependencyTarget getDependencyTarget() {
    return dependencyTarget;
  }

  public int getDependencyTargetIdx() {
    return dependencyTarget.targetIdx;
  }

  @Override
  public DependencyType getDependencyType() {
    return dependencyTarget.dependencyType;
  }
}
