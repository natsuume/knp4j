package dev.natsuume.knp4j.wrapper;

import java.util.ArrayList;
import java.util.List;

public class KnpWrapperBuilder {
  private static final String[] DEFAULT_JUMAN_COMMAND = {"bash", "-c", "jumanpp"};
  private static final String[] DEFAULT_KNP_COMMAND = {
    "bash", "-c", "knp -tab -print-num -anaphora"
  };
  private static final int DEFAULT_MAX_PROCESS_NUM = 1;
  private static final int DEFAULT_FIRST_PROCESS_NUM = 1;
  private static final int DEFAULT_RETRY_NUM = 0;

  private int jumanMaxNum;
  private int jumanStartNum;
  private int knpMaxNum;
  private int knpStartNum;
  private int retryNum;

  private List<String> jumanCommand;
  private List<String> knpCommand;

  public KnpWrapperBuilder() {
    this.retryNum = DEFAULT_RETRY_NUM;
    this.jumanMaxNum = DEFAULT_MAX_PROCESS_NUM;
    this.knpMaxNum = DEFAULT_MAX_PROCESS_NUM;
    this.jumanStartNum = DEFAULT_FIRST_PROCESS_NUM;
    this.knpStartNum = DEFAULT_FIRST_PROCESS_NUM;
    this.jumanCommand = List.of(DEFAULT_JUMAN_COMMAND);
    this.knpCommand = List.of(DEFAULT_KNP_COMMAND);
  }

  public KnpWrapper start() {
    var jumanInitInfo = new ProcessInitInfo(jumanCommand, jumanMaxNum, jumanStartNum);
    var knpInitInfo = new ProcessInitInfo(knpCommand, knpMaxNum, knpStartNum);
    return new KnpWrapper(jumanInitInfo, knpInitInfo, retryNum);
  }

  public KnpWrapperBuilder setJumanCommand(List<String> command) {
    this.jumanCommand = new ArrayList<>(command);
    return this;
  }

  public KnpWrapperBuilder setKnpCommand(List<String> command) {
    this.knpCommand = new ArrayList<>(command);
    return this;
  }

  public KnpWrapperBuilder setJumanMaxNum(int jumanMaxNum) {
    this.jumanMaxNum = jumanMaxNum;
    return this;
  }

  public KnpWrapperBuilder setJumanStartNum(int jumanStartNum) {
    this.jumanStartNum = jumanStartNum;
    return this;
  }

  public KnpWrapperBuilder setKnpMaxNum(int knpMaxNum) {
    this.knpMaxNum = knpMaxNum;
    return this;
  }

  public KnpWrapperBuilder setKnpStartNum(int knpStartNum) {
    this.knpStartNum = knpStartNum;
    return this;
  }

  public KnpWrapperBuilder setRetryNum(int retryNum) {
    this.retryNum = retryNum;
    return this;
  }
}
