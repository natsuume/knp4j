package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.DetailPartOfSpeech;
import dev.natsuume.knp4j.data.MajorPartOfSpeech;
import dev.natsuume.knp4j.data.define.KnpFeature;
import dev.natsuume.knp4j.data.define.KnpNode;
import java.util.ArrayList;
import java.util.List;

public class KnpMorpheme implements KnpNode {
  private final String surfaceForm;
  private final String readingForm;
  private final String baseForm;
  private final MajorPartOfSpeech majorPartOfSpeech;
  private final DetailPartOfSpeech detailPartOfSpeech;
  private final String conjugationalType;
  private final String conjugationalForm;
  private final List<KnpFeature> features;

  KnpMorpheme(KnpMorphemeParser parser) {
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
  public String getSurfaceForm() {
    return surfaceForm;
  }

  @Override
  public String getReadingForm() {
    return readingForm;
  }

  public String getBaseForm() {
    return baseForm;
  }

  public MajorPartOfSpeech getMajorPartOfSpeech() {
    return majorPartOfSpeech;
  }

  public DetailPartOfSpeech getDetailPartOfSpeech() {
    return detailPartOfSpeech;
  }

  public String getConjugationalType() {
    return conjugationalType;
  }

  public String getConjugationalForm() {
    return conjugationalForm;
  }

  @Override
  public List<KnpFeature> getFeatures() {
    return new ArrayList<>(features);
  }
}
