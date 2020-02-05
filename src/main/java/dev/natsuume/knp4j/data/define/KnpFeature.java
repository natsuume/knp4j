package dev.natsuume.knp4j.data.define;

public interface KnpFeature {

  /**
   * Featureの日本語文字列を返す.
   *
   * @return 日本語表記のFeature
   */
  String getContent();

  /**
   * Featureの日本語文字列と一致しているかを返す.
   *
   * @param content 日本語表記のfeature
   * @return 日本語表記のfeatureと一致しているか
   */
  default boolean isContent(String content) {
    return getContent().equals(content);
  }
}
