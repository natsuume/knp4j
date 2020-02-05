package dev.natsuume.knp4j.data;

import java.util.Arrays;

public enum DependencyType {
  NORMAL_DEPENDENCY("D"),
  COORDINATION_DEPENDENCY("P"),
  APPOSITIVE_DEPENDENCY("A"),
  INCOMPLETE_COORDINATION_DEPENDENCY("I");

  private final String typeString;

  private DependencyType(String typeString) {
    this.typeString = typeString;
  }

  /**
   * 係り受けタイプを文字列から取得する.
   *
   * @param value 係り受けタイプを表す文字列
   * @return 該当する係り受けタイプ. 該当するものがなければnullを返す
   */
  public static DependencyType getType(String value) {
    return Arrays.stream(values())
        .filter(dependencyType -> dependencyType.typeString.equals(value))
        .findAny()
        .orElse(null);
  }
}
