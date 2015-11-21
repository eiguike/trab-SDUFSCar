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
      enviarVideo("123");
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

  private Client() {}
  public static void main(String[] args) {
    Client client  = new Client();
    client.setStub(args);
  }
}
