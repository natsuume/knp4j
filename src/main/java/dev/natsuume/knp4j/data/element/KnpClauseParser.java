package dev.natsuume.knp4j.data.element;

import java.util.ArrayList;
import java.util.List;

public class KnpClauseParser extends KnpElementParser {
  List<KnpPhrase> phrases = new ArrayList<>();
  List<KnpClause> dependencies = new ArrayList<>();


  public KnpClauseParser(String line) {
    super(line);
  }

  public KnpClauseParser addPhrases(List<KnpPhrase> phrases) {
    this.phrases.addAll(phrases);
    return this;
  }

  public KnpClauseParser addPhrase(KnpPhrase phrase) {
    this.phrases.add(phrase);
    return this;
  }

  public KnpClauseParser addDependencyClauses(List<KnpClause> dependencies) {
    this.dependencies.addAll(dependencies);
    return this;
  }


  public KnpClause build() {
    return new KnpClause(this);
  }
}
