package dev.natsuume.knp4j.data;

public class DependencyTarget {
  public final int targetIdx;
  public final DependencyType dependencyType;

  public DependencyTarget(String dependencyInfo) {
    String typeString = dependencyInfo.substring(dependencyInfo.length() - 1);
    String targetIdx = dependencyInfo.substring(0, dependencyInfo.length() - 1);
    this.targetIdx = Integer.parseInt(targetIdx);
    this.dependencyType = DependencyType.get(typeString);
  }
}
