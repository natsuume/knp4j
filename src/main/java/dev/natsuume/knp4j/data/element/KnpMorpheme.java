package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DetailPartOfSpeech;
import dev.natsuume.knp4j.data.MajorPartOfSpeech;
import dev.natsuume.knp4j.data.define.KnpFeature;
import dev.natsuume.knp4j.data.define.KnpNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class KnpMorpheme implements KnpNode {
  private final String surfaceForm;
  private final String readingForm;
  private final String baseForm;
  private final MajorPartOfSpeech majorPartOfSpeech;
  private final DetailPartOfSpeech detailPartOfSpeech;
  private final String conjugationalType;
  private final String conjugationalForm;
  private final List<KnpFeature> features;

  KnpMorpheme(KnpMorphemeBuilder parser) {
    this.surfaceForm = parser.surfaceForm;
    this.readingForm = parser.readingForm;
    this.baseForm = parser.baseForm;
    this.majorPartOfSpeech = parser.majorPartOfSpeech;
    this.detailPartOfSpeech = parser.detailPartOfSpeech;
    this.conjugationalType = parser.conjugationalType;
    this.conjugationalForm = parser.conjugationalForm;
    this.features = parser.features;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnpMorpheme that = (KnpMorpheme) o;
    return Objects.equals(surfaceForm, that.surfaceForm)
        && Objects.equals(readingForm, that.readingForm)
        && Objects.equals(baseForm, that.baseForm)
        && majorPartOfSpeech == that.majorPartOfSpeech
        && detailPartOfSpeech == that.detailPartOfSpeech
        && Objects.equals(conjugationalType, that.conjugationalType)
        && Objects.equals(conjugationalForm, that.conjugationalForm)
        && Objects.equals(features, that.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        surfaceForm,
        readingForm,
        baseForm,
        majorPartOfSpeech,
        detailPartOfSpeech,
        conjugationalType,
        conjugationalForm,
        features);
  }

  @Override
  public String getSurfaceForm() {
    return surfaceForm;
  }

  @Override
  public String getReadingForm() {
    return readingForm;
  }

  /**
   * 形態素の基本形を返す.
   *
   * @return 形態素の基本形
   */
  public String getBaseForm() {
    return baseForm;
  }

  /**
   * 大分類の品詞を返す.
   *
   * @return 大分類の品詞
   */
  public MajorPartOfSpeech getMajorPartOfSpeech() {
    return majorPartOfSpeech;
  }

  /**
   * 小分類の品詞を返す.
   *
   * @return 小分類の品詞
   */
  public DetailPartOfSpeech getDetailPartOfSpeech() {
    return detailPartOfSpeech;
  }

  /**
   * 活用の種類を返す.
   *
   * @return 活用の種類
   */
  public String getConjugationalType() {
    return conjugationalType;
  }

  /**
   * 活用形を返す.
   *
   * @return 活用形
   */
  public String getConjugationalForm() {
    return conjugationalForm;
  }

  @Override
  public List<KnpFeature> getFeatures() {
    return new ArrayList<>(features);
  }
}
