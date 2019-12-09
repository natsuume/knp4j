package dev.natsuume.knp4j.data.element;

import java.util.ArrayList;
import java.util.List;

public class KnpClauseBuilder extends KnpElementBuilder {
  List<KnpPhrase> phrases = new ArrayList<>();
  List<KnpClause> dependencies = new ArrayList<>();


  public KnpClauseBuilder(String line) {
    super(line);
  }

  public KnpClauseBuilder addPhrases(List<KnpPhrase> phrases) {
    this.phrases.addAll(phrases);
    return this;
  }

  public KnpClauseBuilder addPhrase(KnpPhrase phrase) {
    this.phrases.add(phrase);
    return this;
  }

  public KnpClauseBuilder addDependencyClauses(List<KnpClause> dependencies) {
    this.dependencies.addAll(dependencies);
    return this;
  }


  public KnpClause build() {
    return new KnpClause(this);
  }
}
