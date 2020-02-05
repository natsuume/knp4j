package dev.natsuume.knp4j.data.define;

import java.util.List;
import java.util.stream.Collectors;

public interface KnpNode {

  /**
   * 表層文字列を返す.
   *
   * @return 表層文字列
   */
  String getSurfaceForm();

  /**
   * 読みを返す.
   *
   * @return よみがな
   */
  String getReadingForm();

  /**
   * knpのFeature一覧を返す.
   *
   * @return Featureのリスト
   */
  List<KnpFeature> getFeatures();

  /**
   * 指定したいずれかの日本語表記表記に一致するFeatureを持っているかを返す.
   *
   * @param features 任意のFeatureの日本語表記
   * @return いずれかの日本語表記のFeatureを持っているか
   */
  default boolean haveAnyFeatures(String... features) {
    return haveAnyFeatures(List.of(features));
  }

  /**
   * 指定したいずれかの日本語表記表記に一致するFeatureを持っているかを返す.
   *
   * @param features 任意のFeatureの日本語表記
   * @return いずれかの日本語表記のFeatureを持っているか
   */
  default boolean haveAnyFeatures(List<String> features) {
    return getFeatures().parallelStream().map(KnpFeature::getContent).anyMatch(features::contains);
  }

  /**
   * 指定したすべての日本語表記表記に一致するFeatureを持っているかを返す.
   *
   * @param features 任意のFeatureの日本語表記
   * @return いずれかの日本語表記のFeatureを持っているか
   */
  default boolean haveAllFeatures(String... features) {
    return haveAllFeatures(List.of(features));
  }

  /**
   * 指定したすべての日本語表記表記に一致するFeatureを持っているかを返す.
   *
   * @param features 任意のFeatureの日本語表記
   * @return いずれかの日本語表記のFeatureを持っているか
   */
  default boolean haveAllFeatures(List<String> features) {
    return getFeatures()
        .parallelStream()
        .map(KnpFeature::getContent)
        .collect(Collectors.toList())
        .containsAll(features);
  }
}
