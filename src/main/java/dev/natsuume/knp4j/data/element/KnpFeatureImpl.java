package dev.natsuume.knp4j.data.element;

import dev.natsuume.knp4j.data.define.KnpFeature;
import java.util.Objects;

class KnpFeatureImpl implements KnpFeature {
  private final String content;

  /**
   * 指定した日本語表記を持つFeatureインスタンスを生成する.
   *
   * @param content featureの日本語表記
   */
  KnpFeatureImpl(String content) {
    if (content == null) {
      throw new IllegalArgumentException();
    }

    this.content = content;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return getContent();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KnpFeatureImpl that = (KnpFeatureImpl) o;
    return Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(content);
  }
}
