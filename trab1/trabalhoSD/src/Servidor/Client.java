package Servidor;

import Node.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

	private Socket client;
	private BufferedReader input;
	private ObjectOutputStream output;
	private Node actualNode;
        private Integer threadsNum;

	private Thread thread;
	private String threadName;

	Client(Integer i, Node message, Integer threadsNum) {
		actualNode = message;
		threadName = "Client " + i;
		client = null;
                this.threadsNum = threadsNum;
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
		Integer i;
		if (actualNode.isThank() == true) {
			System.out.println(actualNode.getId()+": Enviando mensagem de agradecimento para "+actualNode.getIdTarget());
			try {
				//Cria o socket com o recurso desejado na porta especificada  
				client = new Socket("127.0.0.1", 8000 + actualNode.getIdTarget());
				ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
				output.flush();
				output.writeObject(actualNode);
				output.close();
			} catch (IOException e) {
				System.out.println(e);
			} finally {

				try {
					//Encerra o socket cliente  
					client.close();
					System.out.println("fechei");
				} catch (IOException e) {
					System.out.println(e);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			System.out.println(actualNode.getId()+": Enviando mensagem para todos");
			for (i = 0; i < threadsNum; i++) {
				try {
					//Cria o socket com o recurso desejado na porta especificada  
					client = new Socket("127.0.0.1", 8000 + i);
					ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
					output.flush();
					output.writeObject(actualNode);
					output.close();
				} catch (IOException e) {
					System.out.println(e);
				} finally {
					try {
						//Encerra o socket cliente  
						client.close();
					} catch (IOException e) {
						System.out.println(e);
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
}
