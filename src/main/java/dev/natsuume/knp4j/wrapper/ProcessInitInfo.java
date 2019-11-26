package dev.natsuume.knp4j.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ProcessInitInfo {
  private final List<String> command;
  private final int maxNum;
  private final int startNum;

  public ProcessInitInfo(List<String> command, int maxNum, int startNum) {
    this.command = new ArrayList<>(command);
    this.maxNum = maxNum;
    this.startNum = startNum;
  }

  public List<String> getCommand() {
    return command;
  }

  public int getMaxNum() {
    return maxNum;
  }

  public int getStartNum() {
    return startNum;
  }
}
