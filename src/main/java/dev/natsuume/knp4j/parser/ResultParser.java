package dev.natsuume.knp4j.parser;

import java.util.List;

public interface ResultParser<OutputT> {

  /**
   * Knpの解析結果を入力として任意のインスタンスを返す.
   *
   * @param list Knp解析結果
   * @return 解析結果を表すインスタンス
   */
  OutputT parse(List<String> list);

  /**
   * 解析に失敗した際に使用するインスタンスを返す.
   *
   * @return 解析に失敗した際に返すインスタンス
   */
  OutputT getInvalidResult();
}
