package dev.natsuume.knp4j.data;

public class DependencyTarget {
  private final int targetIdx;
  private final DependencyType dependencyType;

  public DependencyTarget(String info) {
    String typeString = info.substring(info.length() - 1);
    String targetIdx = info.substring(0, info.length() - 1);
    this.targetIdx = Integer.parseInt(targetIdx);
    this.dependencyType = DependencyType.get(typeString);
  }

  public int getTargetIdx() {
    return targetIdx;
  }

  public DependencyType getDependencyType() {
    return dependencyType;
  }
}
