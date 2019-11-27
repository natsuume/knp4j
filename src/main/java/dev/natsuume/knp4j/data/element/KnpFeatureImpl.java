package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.define.KnpFeature;

public class KnpFeatureImpl implements KnpFeature {
  private final String content;
  public KnpFeatureImpl(String content) {
    this.content = content;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public String toString(){
    return getContent();
  }
}
