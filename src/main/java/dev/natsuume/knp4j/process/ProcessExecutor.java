package dev.natsuume.knp4j.process;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.List;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class ProcessExecutor<InputT, OutputT> {
  private final Process process;

  public ProcessExecutor(List<String> commands) throws IOException {
    this.process = new ProcessBuilder(commands)
        .redirectError(Redirect.INHERIT)
        .start();
  }

  public OutputT exec(InputT input){
    return null;
  }
}
