package dev.natsuume.knp4j.wrapper;

import java.util.ArrayList;
import java.util.List;

public class ProcessInitInfo {
  private final List<String> command;
  private final int maxNum;
  private final int startNum;

  /**
   * constructor.
   * @param command コマンド
   * @param maxNum 最大実行数
   * @param startNum 初期実行数
   */
  public ProcessInitInfo(List<String> command, int maxNum, int startNum) {
    this.command = new ArrayList<>(command);
    this.maxNum = maxNum;
    this.startNum = startNum;
  }

  /**
   * コマンドを返す.
   * @return コマンド
   */
  public List<String> getCommand() {
    return new ArrayList<>(command);
  }

  /**
   * 最大実行数を返す.
   * @return 最大実行数
   */
  public int getMaxNum() {
    return maxNum;
  }

  /**
   * 初期実行数を返す.
   * @return 初期実行数
   */
  public int getStartNum() {
    return startNum;
  }
}
