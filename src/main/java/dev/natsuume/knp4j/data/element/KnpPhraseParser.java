package dev.natsuume.knp4j.data.element;

import java.util.ArrayList;
import java.util.List;

public class KnpPhraseParser extends KnpElementParser {
  List<KnpMorpheme> morphemes = new ArrayList<>();

  public KnpPhraseParser(String line) {
    super(line);
  }

  /**
   * この句に形態素を追加する.
   *
   * @param morpheme 形態素
   * @return このインスタンス
   */
  public KnpPhraseParser addMorpheme(KnpMorpheme morpheme) {
    this.morphemes.add(morpheme);
    return this;
  }

  /**
   * このParserが持つ情報から句インスタンスを生成する.
   *
   * @return この句を表すインスタンス
   */
  public KnpPhrase build() {
    var phrase = new KnpPhrase(this);
    this.morphemes = new ArrayList<>();
    return phrase;
  }
}
