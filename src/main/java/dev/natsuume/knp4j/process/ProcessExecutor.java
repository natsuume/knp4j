package dev.natsuume.knp4j.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class ProcessExecutor<InputT, OutputT> {
  private final Process process;
  private final Function<InputT, String> inputConverter;
  private final Function<List<String>, OutputT> outputConverter;
  private final ProcessStreamReader inputStreamReader;
  private final ProcessStreamReader errorStreamReader;
  private final ExecutorService executorService = Executors.newFixedThreadPool(2);

  public ProcessExecutor(List<String> commands, Function<List<String>, OutputT> outputConverter) throws IOException {
    this(commands, Objects::toString, outputConverter);
  }

  public ProcessExecutor(List<String> commands, Function<InputT, String> inputConverter, Function<List<String>, OutputT> outputConverter) throws IOException{
    this.inputConverter = inputConverter;
    this.outputConverter = outputConverter;
    this.process = new ProcessBuilder(commands)
      .redirectError(Redirect.INHERIT)
      .start();
    this.inputStreamReader = new ProcessStreamReader(process.getInputStream());
    this.errorStreamReader = new ProcessStreamReader(process.getErrorStream());
    executorService.execute(inputStreamReader);
    executorService.execute(errorStreamReader);
  }

  public synchronized OutputT exec(InputT input){
    List<String> readLines = new ArrayList<>();
    try{
      String inputText = inputConverter.apply(input);
      BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      bufferedWriter.write(inputText);
      bufferedWriter.flush();
      System.out.println("...write");

      readLines = inputStreamReader.read();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return outputConverter.apply(readLines);
  }
}
