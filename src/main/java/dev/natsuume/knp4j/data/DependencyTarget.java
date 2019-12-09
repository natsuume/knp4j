package dev.natsuume.knp4j.data;

public class DependencyTarget {
  public final int targetIdx;
  public final DependencyType dependencyType;

  /**
   * 係り受け情報文字列から係り受けタイプとIDを持つインスタンスを生成する.
   * @param dependencyInfo 係り受け情報を表す文字列
   */
  public DependencyTarget(String dependencyInfo) {
    String typeString = dependencyInfo.substring(dependencyInfo.length() - 1);
    String targetIdx = dependencyInfo.substring(0, dependencyInfo.length() - 1);
    this.targetIdx = Integer.parseInt(targetIdx);
    this.dependencyType = DependencyType.getType(typeString);
  }
}
