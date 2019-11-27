package dev.natsuume.knp4j.wrapper;

import dev.natsuume.knp4j.data.JumanResult;
import dev.natsuume.knp4j.data.define.KnpResult;
import dev.natsuume.knp4j.data.element.KnpResultImpl;
import dev.natsuume.knp4j.process.ProcessExecutor;
import dev.natsuume.knp4j.process.ProcessExecutorBuilder;
import dev.natsuume.knp4j.process.ProcessManager;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class KnpWrapper {
  private static final String NEW_LINE_REGEX = ".*(\r\n|\n|\r).*";
  private static final String INVALID_INPUT_REGEX = ".*[+*].*";
  private final ProcessInitInfo jumanInitInfo;
  private final ProcessInitInfo knpInitInfo;
  private final List<String> jumanCommand;
  private final List<String> knpCommnad;
  private final int retryNum;
  private ProcessManager<String, JumanResult> jumanManager;
  private ProcessManager<JumanResult, KnpResultImpl> knpManager;

  public KnpWrapper(ProcessInitInfo jumanInitInfo, ProcessInitInfo knpInitInfo, int retryNum) {
    this.jumanInitInfo = jumanInitInfo;
    this.knpInitInfo = knpInitInfo;
    this.jumanCommand = jumanInitInfo.getCommand();
    this.knpCommnad = knpInitInfo.getCommand();
    this.retryNum = retryNum;

    this.jumanManager = startJumanManager();
    this.knpManager = startKnpManager();
  }

  private ProcessManager<String, JumanResult> startJumanManager() {
    return new ProcessManager<>(
        jumanInitInfo.getMaxNum(), jumanInitInfo.getStartNum(), this::getJumanExecutor);
  }

  private ProcessManager<JumanResult, KnpResultImpl> startKnpManager() {
    return new ProcessManager<>(
        knpInitInfo.getMaxNum(), knpInitInfo.getStartNum(), this::getKnpExecutor);
  }

  private ProcessExecutor<String, JumanResult> getJumanExecutor() {
    return new ProcessExecutorBuilder<String, JumanResult>()
        .setCommand(jumanCommand)
        .setOutputConverter(JumanResult::new)
        .start();
  }

  private ProcessExecutor<JumanResult, KnpResultImpl> getKnpExecutor() {
    return new ProcessExecutorBuilder<JumanResult, KnpResultImpl>()
        .setCommand(knpCommnad)
        .setInputConverter(JumanResult::toKnpInput)
        .setOutputConverter(KnpResultImpl::new)
        .start();
  }

  private <T, R> R retryAnalyze(Function<T, R> func, T input) {
    for (int i = 0; i < retryNum; i++) {
      try {
        return func.apply(input);
      } catch (Exception e2) {
        continue;
      }
    }
    return null;
  }

  private JumanResult analyzeJuman(String input) throws InterruptedException, IOException {
    try {
      return jumanManager.exec(input);
    } catch (InterruptedException | IOException e) {
      for (int i = 0; i < retryNum; i++) {
        try {
          return jumanManager.exec(input);
        } catch (InterruptedException | IOException e2) {
          continue;
        }
      }
      throw e;
    }
  }

  private KnpResult analyzeKnp(JumanResult input) throws InterruptedException, IOException {
    try {
      return knpManager.exec(input);
    } catch (InterruptedException | IOException e) {
      for (int i = 0; i < retryNum; i++) {
        try {
          return knpManager.exec(input);
        } catch (InterruptedException | IOException e2) {
          continue;
        }
      }
      throw e;
    }
  }

  public KnpResult analyze(String input) throws InterruptedException, IOException {
    if(input.matches(INVALID_INPUT_REGEX) || input.matches(NEW_LINE_REGEX)){
      return KnpResult.INVALID_RESULT;
    }
    JumanResult jumanResult = analyzeJuman(input);
    KnpResult knpResult = analyzeKnp(jumanResult);
    return knpResult;
  }

  public void close() throws InterruptedException, IOException {
    jumanManager.close();
    knpManager.close();
  }
}
