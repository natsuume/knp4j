package dev.natsuume.knp4j.wrapper;

import dev.natsuume.knp4j.parser.KnpResultParser;
import dev.natsuume.knp4j.parser.ResultParser;
import java.util.ArrayList;
import java.util.List;

public class KnpWrapperBuilder<OutputT> {
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

  private ResultParser<OutputT> resultParser;

  /** constructor. */
  public KnpWrapperBuilder() {
    this.retryNum = DEFAULT_RETRY_NUM;
    this.jumanMaxNum = DEFAULT_MAX_PROCESS_NUM;
    this.knpMaxNum = DEFAULT_MAX_PROCESS_NUM;
    this.jumanStartNum = DEFAULT_FIRST_PROCESS_NUM;
    this.knpStartNum = DEFAULT_FIRST_PROCESS_NUM;
    this.jumanCommand = List.of(DEFAULT_JUMAN_COMMAND);
    this.knpCommand = List.of(DEFAULT_KNP_COMMAND);
  }

  /**
   * KnpWrapperを作成し、プロセスを実行する.
   * @return KnpWrapper
   */
  public KnpWrapper<OutputT> start() {
    var jumanInitInfo = new ProcessInitInfo(jumanCommand, jumanMaxNum, jumanStartNum);
    var knpInitInfo = new ProcessInitInfo(knpCommand, knpMaxNum, knpStartNum);
    return new KnpWrapper<>(jumanInitInfo, knpInitInfo, retryNum, resultParser);
  }

  /**
   * Jumanプロセスを実行するコマンドを設定する.
   * デフォルトはList.of("bash", "-c", "jumanpp")と等価
   * @param command コマンド
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setJumanCommand(List<String> command) {
    this.jumanCommand = new ArrayList<>(command);
    return this;
  }

  /**
   * KNPプロセスを実行するコマンドを設定する.
   * デフォルトはList.of("bash", "-c", "knp -tab -print-num -anaphora")と等価
   * -tab, -print-num, -anaphoraオプションが現在は必須
   * @param command コマンド
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setKnpCommand(List<String> command) {
    this.knpCommand = new ArrayList<>(command);
    return this;
  }

  /**
   * Jumanのプロセスの最大起動数を設定する.
   * @param jumanMaxNum Jumanの最大同時実行数(デフォルトは1)
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setJumanMaxNum(int jumanMaxNum) {
    this.jumanMaxNum = jumanMaxNum;
    return this;
  }

  /**
   * 初期化時のJumanの実行プロセス数を設定する.
   * @param jumanStartNum Jumanの初期実行数(デフォルトは1)
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setJumanStartNum(int jumanStartNum) {
    this.jumanStartNum = jumanStartNum;
    return this;
  }

  /**
   * KNPのプロセスの最大起動数を設定する.
   * @param knpMaxNum KNPの最大同時実行数(デフォルトは1)
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setKnpMaxNum(int knpMaxNum) {
    this.knpMaxNum = knpMaxNum;
    return this;
  }

  /**
   * 初期化時のKNP実行プロセス数を設定する.
   * @param knpStartNum KNPの初期実行数(デフォルトは1)
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setKnpStartNum(int knpStartNum) {
    this.knpStartNum = knpStartNum;
    return this;
  }

  /**
   * 解析に失敗した際に再試行する回数を設定する.
   * @param retryNum 再試行数(デフォルトは0)
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setRetryNum(int retryNum) {
    this.retryNum = retryNum;
    return this;
  }

  /**
   * KNPの出力を任意のオブジェクトに変換するParserを設定する.
   * @param resultParser KNPの出力Parser
   * @return このインスタンス
   */
  public KnpWrapperBuilder<OutputT> setResultParser(ResultParser<OutputT> resultParser) {
    this.resultParser = resultParser;
    return this;
  }
}
