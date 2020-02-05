package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DetailPartOfSpeech;
import dev.natsuume.knp4j.data.MajorPartOfSpeech;

public class KnpMorphemeBuilder extends KnpElementBuilder {
  private static final int SURFACE_FORM_IDX = 0;
  private static final int READING_FORM_IDX = 1;
  private static final int BASE_FORM_IDX = 2;
  private static final int MAJOR_POS_IDX = 3;
  private static final int DETAIL_POS_IDX = 5;
  private static final int CONJUGATIONAL_TYPE_IDX = 7;
  private static final int CONJUGATIONAL_FORM_IDX = 9;

  final String surfaceForm;
  final String readingForm;
  final String baseForm;
  final MajorPartOfSpeech majorPartOfSpeech;
  final DetailPartOfSpeech detailPartOfSpeech;
  final String conjugationalType;
  final String conjugationalForm;

  /**
   * 形態素単位の解析結果文字列からインスタンスを生成する.
   *
   * @param line 形態素解析結果
   */
  public KnpMorphemeBuilder(String line) {
    super(line);
    if (!info.isMorpheme()) {
      throw new IllegalArgumentException("This Content is not Morpheme.");
    }
    var basicInfo = info.basicInfo.split(BASIC_INFO_DELIMITER);
    this.surfaceForm = basicInfo[SURFACE_FORM_IDX];
    this.readingForm = basicInfo[READING_FORM_IDX];
    this.baseForm = basicInfo[BASE_FORM_IDX];
    this.majorPartOfSpeech = MajorPartOfSpeech.find(basicInfo[MAJOR_POS_IDX]);
    this.detailPartOfSpeech = DetailPartOfSpeech.find(basicInfo[DETAIL_POS_IDX]);
    this.conjugationalType = basicInfo[CONJUGATIONAL_TYPE_IDX];
    this.conjugationalForm = basicInfo[CONJUGATIONAL_FORM_IDX];
  }

  /**
   * 現在の内容で形態素インスタンスを生成する.
   *
   * @return 形態素インスタンス
   */
  public KnpMorpheme build() {
    return new KnpMorpheme(this);
  }
}
