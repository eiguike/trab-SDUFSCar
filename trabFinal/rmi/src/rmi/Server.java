/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {
	public Server(){}
	public String sayHello(){
		return "Hello, world!";
	}

	public static void main (String args[]){
		try{
			Server obj = new Server();
			Hello stub = (Hello) UnicastRemoteObject.exportObject(obj,0);
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("Hello",stub);
			System.err.println("Server ready");
		}catch (Exception e){
			System.err.println("Server exception: "+e.toString());
			e.printStackTrace();
		}
	}
}
