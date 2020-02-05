package dev.natsuume.knp4j.data;

import java.util.List;
import java.util.Objects;

public class JumanResult {
  private static final String NEW_LINE = " \\\n";
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
    return String.join(NEW_LINE, rawData);
  }

  @Override
  public String toString() {
    return rawData.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JumanResult that = (JumanResult) o;
    return Objects.equals(rawData, that.rawData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rawData);
  }
}
