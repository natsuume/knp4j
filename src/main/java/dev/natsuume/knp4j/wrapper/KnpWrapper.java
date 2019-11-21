package dev.natsuume.knp4j.wrapper;

import dev.natsuume.knp4j.data.JumanResult;
import dev.natsuume.knp4j.data.KnpResult;
import dev.natsuume.knp4j.process.ProcessExecutor;
import dev.natsuume.knp4j.process.ProcessExecutorBuilder;
import dev.natsuume.knp4j.process.ProcessManager;
import java.io.IOException;
import java.util.List;

public class KnpWrapper {
  private final ProcessManager<String, JumanResult> jumanManager;
  private final ProcessManager<JumanResult, KnpResult> knpManager;

  public KnpWrapper() {
    this.jumanManager = new ProcessManager<>(this::getJumanExecutor);
    this.knpManager = new ProcessManager<>(this::getKnpExecutor);
  }

  private ProcessExecutor<String, JumanResult> getJumanExecutor() {
    return new ProcessExecutorBuilder<String, JumanResult>()
        .setCommand(List.of("bash", "-c", "jumanpp"))
        .setOutputConverter(JumanResult::new)
        .start();
  }

  private ProcessExecutor<JumanResult, KnpResult> getKnpExecutor() {
    return new ProcessExecutorBuilder<JumanResult, KnpResult>()
        .setCommand(List.of("bash", "-c", "knp -tab -print-num -anaphora"))
        .setInputConverter(JumanResult::toKnpInput)
        .setOutputConverter(KnpResult::new)
        .start();
  }

  public KnpResult analyze(String input) throws InterruptedException, IOException {
    var jumanResult = jumanManager.exec(input);
    return knpManager.exec(jumanResult);
  }
}
