package dev.natsuume.knp4j.data;

import java.util.Arrays;

public enum MajorPartOfSpeech {
  NOUN("名詞"),
  INDICATOR("指示詞"),
  VERB("動詞"),
  ADJECTIVE("形容詞"),
  JUDGEMENT("判定詞"),
  POSTPOSITIONAL_PARTICLE("助詞"),
  AUXILIARY_VERB("助動詞"),
  ADVERB("副詞"),
  PARTICLE("接続詞"),
  CONJUNCTION("連体詞"),
  IMPRESSION_VERB("感動詞"),
  PREFIX("接頭辞"),
  SUFFIX("接尾辞"),
  SPECIAL("特殊"),
  UNDEFINED_WORD("未定義語"),
  NONE("*");

  private final String japaneseForm;

  private MajorPartOfSpeech(String japaneseForm) {
    this.japaneseForm = japaneseForm;
  }

  /**
   * 日本語文字列に該当する品詞を返す.
   *
   * @param japaneseForm 日本語の品詞文字列
   * @return 該当する品詞. 該当するものが存在しない場合はMajorPartOfSpeech.NONEを返す
   */
  public static MajorPartOfSpeech find(String japaneseForm) {
    return Arrays.stream(values())
        .parallel()
        .filter(pos -> pos.japaneseForm.equals(japaneseForm))
        .findAny()
        .orElse(NONE);
  }

  /**
   * この品詞を表す日本語文字列を返す.
   *
   * @return 品詞を表す日本語文字列
   */
  public String getJapaneseForm() {
    return japaneseForm;
  }
}
