package dev.natsuume.knp4j.parser;

import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.data.element.KnpResultBuilder;
import java.util.List;

public class KnpResultParser implements ResultParser<KnpResult> {

  @Override
  public KnpResult parse(List<String> list) {
    return new KnpResultBuilder(list).build();
  }

  @Override
  public KnpResult getInvalidResult() {
    return KnpResult.INVALID_RESULT;
  }
}
