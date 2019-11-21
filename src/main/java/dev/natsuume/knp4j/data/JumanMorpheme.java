package dev.natsuume.knp4j.data;

import java.util.regex.Pattern;

public class JumanMorpheme {
  protected final String surfaceForm;
  private static final String INFO_SPLIT_PATTERN = " (?=<)";
  private static final String IDX_INFO_SEPARATOR = " ";
  public JumanMorpheme(String line) {
    this.surfaceForm = line.split(IDX_INFO_SEPARATOR)[0];
  }

  public String getSurfaceForm(){
    return surfaceForm;
  }
}
