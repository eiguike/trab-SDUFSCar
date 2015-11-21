import java.io.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
  private Video stub;

  public void setStub(String [] args){
    try {
      String host = (args.length < 1) ? null : args[0];
      Registry registry = LocateRegistry.getRegistry(host);
      stub = (Video) registry.lookup("Video");
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }

  private void enviarVideo(String id){
    FileInputStream fileInputStream = null;
    File file = new File("./teste1/arquivoteste.mp4");

    byte[] bFile = new byte[(int) file.length()];
    boolean response = false;
    try{
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bFile);
      fileInputStream.close();
      response = this.stub.uploadVideo(bFile, id);
    }catch(IOException e){
      System.out.println(e);
    }

    if(response == false)
      System.out.println("Não foi possível o envio");
    else
      System.out.println("O vídeo foi enviado com sucesso!");
  }

  private boolean downloadVideo(String id){
    System.out.println("Recebendo vídeo "+id);
    try{
      byte [] bFile = stub.downloadVideo(id);
      FileOutputStream fileOutput = new FileOutputStream(id+".mp4");
      fileOutput.write(bFile);
      fileOutput.close();
    } catch (IOException e){
      System.out.println(e);
      return false;
    }
    System.out.println("Video "+id+"  salvo!");
    return true;
  }

  private Client() {}
  public static void main(String[] args) {
    Client client  = new Client();
    client.setStub(args);
    //client.enviarVideo("123");
    client.downloadVideo("123");
  }
}
