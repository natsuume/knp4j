package dev.natsuume.knp4j.data.element;

import java.util.ArrayList;
import java.util.List;

public class KnpPhraseParser extends KnpElementParser {
  List<KnpMorpheme> morphemes = new ArrayList<>();

  public KnpPhraseParser(String line) {
    super(line);
  }

  public KnpPhraseParser addMorphemes(List<KnpMorpheme> morphemes) {
    this.morphemes.addAll(morphemes);
    return this;
  }

  public KnpPhraseParser addMorpheme(KnpMorpheme morpheme) {
    this.morphemes.add(morpheme);
    return this;
  }


  public KnpPhrase build(){
    var phrase = new KnpPhrase(this);
    this.morphemes = new ArrayList<>();
    return phrase;
  }
}
