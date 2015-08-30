package Servidor;

import Node.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

public class Client extends Thread {

	private Socket client;
	private BufferedReader input;
	private ObjectOutputStream output;

	private Thread thread;
	private String threadName;

	Client(Integer i) {
		threadName = "Client " + i;
		client = null;
		System.out.println("Criando thread cliente: " + threadName);
	}

	// Criando uma thread, no java cada thread executaria a função
	// run, e por isso, devemos fazer uma sobrecarga nesta função
	public void start() {
		if (thread == null) {
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

	public void run() {
		try {
			//Cria o socket com o recurso desejado na porta especificada  
			client = new Socket("127.0.0.1", 8001);

			Node aux = new Node();
			aux.setClock(24);
			aux.setId(23);
			aux.setThank(false);

			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
			
			output.flush();
			output.writeObject(aux);
			output.close();

			//Trata possíveis exceções  
		} catch (IOException e) {
			System.out.println(e);
		} finally {

			try {
				//Encerra o socket cliente  
				client.close();
				System.out.println("fechei");
			} catch (IOException e) {
			}

		}

	}
}
