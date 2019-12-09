package dev.natsuume.knp4j.data;

import java.util.Arrays;

public enum DetailPartOfSpeech {
  COMMON_NOUN("普通名詞"),
  SA_TRANSFORMATION_NOUN("サ変名詞"),
  PROPER_NOUN("固有名詞"),
  PLACE_NAME("地名"),
  PERSON_NAME("人名"),
  ORGANIZATION_NAME("組織名"),
  NUMERAL("数詞"),
  FORMAL_NOUN("形式名詞"),
  ADVERBIAL_NOUN("副詞的名詞"),
  TEMPORAL_NOUN("時相名詞"),
  NOUN_FORM_INDICATOR("名詞形態指示詞"),
  CONJUNCTIVE_FORM_INDICATOR("連体詞形態指示詞"),
  ADVERB_FORM_INDICATOR("副詞形態指示詞"),
  CASE_PARTICLE("格助詞"),
  ADVERBIAL_PARTICLE("副助詞"),
  CONJUNCTIVE_PARTICLE("接続助詞"),
  SENTENCE_ENDING_PARTICLE("終助詞"),
  NOUN_PREFIX("名詞接頭辞"),
  VERB_PREFIX("動詞接頭辞"),
  I_ADJECTIVE_PREFIX("イ形容詞接頭辞"),
  NA_ADJECTIVE_PREFIX("ナ形容詞接頭辞"),
  NOUNISTIC_PREDICATE_SUFFIX("名詞性述語接尾辞"),
  NOUNISTIC_NOUN_SUFFIX("名詞性名詞接尾辞"),
  NOUNISTIC_NOUN_NUMERAL("名詞性名詞助数辞"),
  NOUNISTIC_SPECIAL_SUFFIX("名詞性特殊接尾辞"),
  ADJECTIVAL_PREDICATE_SUFFIX("形容詞性述語接尾辞"),
  ADJECTIVAL_NOUN_SUFFIX("形容詞性名詞接尾辞"),
  PERIOD("句点"),
  COMMA("読点"),
  OPENING_BRACKET("括弧始"),
  CLOSING_BRACKET("括弧終"),
  SYMBOL("記号"),
  BLANK("空白"),
  OTHER("その他"),
  KATAKANA("カタカナ"),
  ALPHABET("アルファベット"),
  NONE("*"),
  ;

  private final String japaneseForm;

  private DetailPartOfSpeech(String japaneseForm) {
    this.japaneseForm = japaneseForm;
  }

  /**
   * 日本語文字列を返す.
   *
   * @return 品詞の日本語文字列
   */
  public String getJapaneseForm() {
    return japaneseForm;
  }

  /**
   * 入力の文字列に一致する品詞を取得する.
   *
   * @param japaneseForm 品詞の日本語文字列
   * @return 該当する品詞. 一致するものがない場合はDetailPartOfSpeech.NONEを返す
   */
  public static DetailPartOfSpeech find(String japaneseForm) {
    return Arrays.stream(values())
        .parallel()
        .filter(pos -> pos.japaneseForm.equals(japaneseForm))
        .findAny()
        .orElse(NONE);
  }
}
