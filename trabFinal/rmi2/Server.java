import java.io.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Video{
  public Server(){}

  public Item downloadVideo(String name){
    FileInputStream fileInputStream = null;
    File file = new File("./"+name+".mp4");

    byte[] bFile = new byte[(int) file.length()];
    boolean response = false;

    try{
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bFile);
      fileInputStream.close();
      Item data = new Item(name, bFile);

      return data;
    }catch(IOException e){
      System.out.println(e);
    }
    return null;
  }


  public boolean uploadVideo(byte [] bFile, String name){
    System.out.println("Recebendo vídeo "+name);
    try{
      FileOutputStream fileOutput = new FileOutputStream(name+".mp4");
      fileOutput.write(bFile);
      fileOutput.close();
    } catch (IOException e){
      System.out.println(e);
      return false;
    }
    System.out.println("Video "+name+"  salvo!");
    return true;
  }

  public static void main(String args[]){
    try{
      System.setProperty("java.rmi.server.hostname", "192.168.1.7");
      Server obj = new Server();
      Video stub = (Video) UnicastRemoteObject.exportObject(obj,0);
      Registry registry = LocateRegistry.getRegistry();
      registry.bind("Video", stub);
      System.err.println("Server Ready");
    } catch(Exception e){
      System.err.println("Server exception:" + e.toString());
      e.printStackTrace();
    }
  }
}
