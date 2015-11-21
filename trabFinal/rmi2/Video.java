import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Video extends Remote{
  boolean uploadVideo(byte [] bFile, String name) throws RemoteException;
  byte[] downloadVideo(String id) throws RemoteException;
}


