package dev.natsuume.knp4j.data;

import java.util.Arrays;

public enum MajorPartOfSpeech {
  NOUN("名詞"),
  INDICATOR("指示詞"),
  VERB("動詞"),
  ADJECTIVE("形容詞"),
  JUDGEMENT("判定詞"),
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

  public static MajorPartOfSpeech find(String japaneseForm) {
    return Arrays.stream(values())
        .parallel()
        .filter(pos -> pos.japaneseForm.equals(japaneseForm))
        .findAny()
        .orElse(NONE);
  }

  public String getJapaneseForm() {
    return japaneseForm;
  }
}
