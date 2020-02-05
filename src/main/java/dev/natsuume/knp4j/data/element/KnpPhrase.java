package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import dev.natsuume.knp4j.data.define.KnpFeature;
import dev.natsuume.knp4j.data.define.KnpParentNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnpPhrase implements KnpParentNode {
  private final List<KnpMorpheme> morphemes;
  private final int idx;
  private final DependencyTarget dependencyTarget;
  private final List<KnpFeature> features;

  KnpPhrase(KnpPhraseBuilder parser) {
    this.morphemes = parser.morphemes;
    this.idx = parser.idx;
    this.dependencyTarget = parser.dependencyTarget;
    this.features = parser.features;
  }

  @Override
  public String getSurfaceForm() {
    return getRecursiveForm(KnpMorpheme::getSurfaceForm);
  }

  @Override
  public String getReadingForm() {
    return getRecursiveForm(KnpMorpheme::getReadingForm);
  }

  private String getRecursiveForm(Function<KnpMorpheme, String> memberGetter) {
    return morphemes.stream().map(memberGetter).collect(Collectors.joining());
  }

  @Override
  public List<KnpFeature> getFeatures() {
    return new ArrayList<>(features);
  }

  @Override
  public DependencyTarget getDependencyTarget() {
    return dependencyTarget;
  }

  @Override
  public int getDependencyTargetIdx() {
    return dependencyTarget.targetIdx;
  }

  @Override
  public DependencyType getDependencyType() {
    return dependencyTarget.dependencyType;
  }

  @Override
  public int getIdx() {
    return idx;
  }

  @Override
  public List<KnpMorpheme> getMorphemes() {
    return new ArrayList<>(morphemes);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnpPhrase knpPhrase = (KnpPhrase) o;
    return idx == knpPhrase.idx
        && Objects.equals(morphemes, knpPhrase.morphemes)
        && Objects.equals(dependencyTarget, knpPhrase.dependencyTarget)
        && Objects.equals(features, knpPhrase.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(morphemes, idx, dependencyTarget, features);
  }
}
