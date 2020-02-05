package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import dev.natsuume.knp4j.data.define.KnpFeature;
import dev.natsuume.knp4j.data.define.KnpParentNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KnpClause implements KnpParentNode {
  private final List<KnpPhrase> phrases;
  private final int idx;
  private final DependencyTarget dependencyTarget;
  private final List<KnpFeature> features;
  private final List<KnpClause> dependencies;

  /**
   * 文節を表すインスタンスを生成する.
   *
   * @param parser 文節情報を持つParser
   */
  public KnpClause(KnpClauseBuilder parser) {
    this.phrases = parser.phrases;
    this.idx = parser.idx;
    this.dependencyTarget = parser.dependencyTarget;
    this.dependencies = parser.dependencies;
    this.features = parser.features;
  }

  @Override
  public String getSurfaceForm() {
    return phrases.stream().map(KnpPhrase::getSurfaceForm).collect(Collectors.joining());
  }

  @Override
  public String getReadingForm() {
    return phrases.stream().map(KnpPhrase::getReadingForm).collect(Collectors.joining());
  }

  /**
   * この文節に係る文節一覧を返す.
   *
   * @return この文節を係り先に持つ文節一覧を返す.
   */
  public List<KnpClause> getDependencies() {
    return new ArrayList<>(dependencies);
  }

  @Override
  public List<KnpFeature> getFeatures() {
    return new ArrayList<>(features);
  }

  @Override
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnpClause knpClause = (KnpClause) o;
    return idx == knpClause.idx
        && Objects.equals(phrases, knpClause.phrases)
        && Objects.equals(dependencyTarget, knpClause.dependencyTarget)
        && Objects.equals(features, knpClause.features)
        && Objects.equals(dependencies, knpClause.dependencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phrases, idx, dependencyTarget, features, dependencies);
  }

  /**
   * この文節に対応する句一覧を返す.
   *
   * @return 句の一覧
   */
  public List<KnpPhrase> getPhrases() {
    return new ArrayList<>(phrases);
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
}
