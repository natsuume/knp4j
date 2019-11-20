package dev.natsuume.knp4j.data;

import java.util.List;

public class JumanResult {
  private final List<String> rawData;

  public JumanResult(List<String> lines) {
    this.rawData = lines;
  }

  public String toKnpInput() {
    return String.join(" \\\n", rawData);
  }
}
