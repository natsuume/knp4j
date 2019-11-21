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

  public static DependencyType get(String value) {
    return Arrays.stream(values())
        .filter(dependencyType -> dependencyType.typeString.equals(value))
        .findAny()
        .orElse(null);
  }
}
