package dev.natsuume.knp4j.data.define;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import dev.natsuume.knp4j.data.element.KnpMorpheme;
import java.util.List;

public interface KnpParentNode extends KnpNode {

  /**
   * 係り先情報を返す.
   * @return 係り先情報
   */
  DependencyTarget getDependencyTarget();

  /**
   * 係り先idを返す.
   * @return 係り先id
   */
  int getDependencyTargetIdx();

  /**
   * 係り受けタイプを返す.
   * @return 係り受けタイプ
   */
  DependencyType getDependencyType();

  /**
   * このNodeのidを返す.
   * @return id
   */
  int getIdx();

  /**
   * このNodeが持つ形態素一覧を返す.
   * @return 形態素リスト
   */
  List<KnpMorpheme> getMorphemes();
}
