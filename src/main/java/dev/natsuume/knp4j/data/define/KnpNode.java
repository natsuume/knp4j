package dev.natsuume.knp4j.data.define;

import java.util.List;

public interface KnpNode {

  /**
   * 表層文字列を返す.
   * @return 表層文字列
   */
  String getSurfaceForm();

  /**
   * 読みを返す.
   * @return よみがな
   */
  String getReadingForm();

  /**
   * knpのFeature一覧を返す.
   * @return Featureのリスト
   */
  List<KnpFeature> getFeatures();
}
