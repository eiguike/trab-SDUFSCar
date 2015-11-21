import java.io.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Video{
  public Server(){}


  public boolean uploadVideo(byte [] bFile, String name){
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
