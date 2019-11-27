package dev.natsuume.knp4j.data.define;

import dev.natsuume.knp4j.data.element.KnpClause;
import dev.natsuume.knp4j.data.element.KnpMorpheme;
import dev.natsuume.knp4j.data.element.KnpPhrase;
import java.util.List;

public interface KnpResult {
  KnpResult INVALID_RESULT = new KnpResult() {
    @Override
    public List<KnpClause> getClauses() {
      return List.of();
    }

    @Override
    public List<KnpMorpheme> getMorphemes() {
      return List.of();
    }

    @Override
    public List<KnpPhrase> getPhrases() {
      return List.of();
    }

    @Override
    public KnpClause getRootNode() {
      return null;
    }

    @Override
    public List<String> getRawResultData() {
      return List.of();
    }

    @Override
    public String getSurfaceForm() {
      return "";
    }

    @Override
    public double getScore() {
      return Double.MIN_VALUE;
    }

    @Override
    public boolean isValid() {
      return false;
    }
  };

  /**
   * 文節のリストを返す.
   * @return 文節の一覧
   */
  List<KnpClause> getClauses();

  /**
   * 形態素のリストを返す.
   * @return 形態素の一覧
   */
  List<KnpMorpheme> getMorphemes();

  /**
   * 基本句のリストを返す
   * @return 基本句の一覧
   */
  List<KnpPhrase> getPhrases();

  /**
   * 文末の文節を返す.
   * 要素が存在しないときはnullを返す
   * @return 文末のKnpClause
   */
  KnpClause getRootNode();

  /**
   * KNPの出力結果の文字列リストを返す.
   * @return 出力結果
   */
  List<String> getRawResultData();

  /**
   * 入力された表層文字列を返す.
   * @return 入力文字列
   */
  String getSurfaceForm();

  /**
   * 解析スコアを返す.
   * @return 解析スコア
   */
  double getScore();

  /**
   * 正常な解析結果かどうかを返す.
   * @return 正常な解析結果かどうか
   */
  boolean isValid();
}
