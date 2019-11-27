package dev.natsuume.knp4j.data.define;

import dev.natsuume.knp4j.data.DependencyTarget;
import dev.natsuume.knp4j.data.DependencyType;
import dev.natsuume.knp4j.data.element.KnpMorpheme;
import java.util.List;

public interface KnpParentNode extends KnpNode {
  DependencyTarget getDependencyTarget();
  int getDependencyTargetIdx();
  DependencyType getDependencyType();
  int getIdx();
  List<KnpMorpheme> getMorphemes();
}
