package dev.natsuume.knp4j.data;

import java.util.List;

public class JumanResult {
  private final List<String> rawData;

  public JumanResult(List<String> lines) {
    this.rawData = lines;
  }

  /**
   * knpの入力に与える文字列を返す.
   *
   * @return knpの入力に与える文字列
   */
  public String toKnpInput() {
    return String.join(" \\\n", rawData);
  }

  @Override
  public String toString() {
    return rawData.toString();
  }
}
