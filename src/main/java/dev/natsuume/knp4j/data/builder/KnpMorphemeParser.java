package dev.natsuume.knp4j.data.builder;


import dev.natsuume.knp4j.data.KnpMorpheme;
import java.util.ArrayList;
import java.util.List;

public class KnpMorphemeParser extends KnpElementParser {



  public KnpMorphemeParser(String line) {
    super(line);
    if(!info.isMorpheme()) {
      throw new IllegalArgumentException("This Content is not Morpheme.");
    }
  }
}
