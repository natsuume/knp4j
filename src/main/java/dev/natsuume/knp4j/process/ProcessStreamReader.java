package dev.natsuume.knp4j.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessStreamReader implements Runnable{
  private final InputStream inputStream;
  private List<String> streamLines = new ArrayList<>();

  public ProcessStreamReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public List<String> read() throws InterruptedException{
    synchronized (streamLines){
      while(streamLines.isEmpty()){
        System.out.println("...wait");
        streamLines.wait();
      }
      List<String> lines = new ArrayList<>(streamLines);
      streamLines.clear();
      return lines;
    }
  }

  @Override
  public void run() {
    try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
      while(true) {
        List<String> streamLines = new ArrayList<>();

        String line;
        while(bufferedReader.ready()){
          line = bufferedReader.readLine();
          System.out.println("process: " + line);
          streamLines.add(line);
        }

        if(streamLines.isEmpty()){
          continue;
        }else{
          System.out.println("will add lines :" + streamLines);
        }
        System.out.println("...update");
        synchronized (this.streamLines) {
          this.streamLines.addAll(streamLines);
          this.streamLines.notifyAll();
        }
        System.out.println("...notifyAll");
      }
    } catch(IOException e){
      e.printStackTrace();
    }
  }
}
