package dev.natsuume.knp4j.data.builder;

import dev.natsuume.knp4j.data.KnpResult;
import java.util.List;

public class KnpResultParser {
  private List<String> rawData;


  public KnpResultParser(List<String> results) {
    this.rawData = results;

  }

  private void parseMetaData() {
    var metaData = rawData.get(0);
  }

  public KnpResult build() {
    return null;
  }

}
