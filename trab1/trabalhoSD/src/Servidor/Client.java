package Servidor;

import Node.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Comparator;
import java.util.Date;
import java.util.Random;
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
		Random seed = new Random();
		try {
			Thread.sleep(seed.nextInt(1000));
		} catch (InterruptedException ex) {
			Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
		}
		Boolean flag = true;
		Integer i;
		do {
			if (actualNode.isThank() == true) {
				if (actualNode.isSend()) {
					System.out.println("Estou enviando comando para " + actualNode.getIdTarget());
				} else {
                                        actualNode.setClock(actualNode.getClock() + 1);
                                        System.out.println(actualNode.getId() + " clock: " + actualNode.getClock());
                                        
					System.out.println(actualNode.getId() + ": Enviando mensagem de agradecimento para " + actualNode.getIdTarget());
				}
				try {
					//Cria o socket com o recurso desejado na porta especificada  
					client = new Socket("127.0.0.1", 8000 + actualNode.getIdTarget());
					ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
					output.flush();
					output.writeObject(actualNode);
					output.close();
					flag = false;
				} catch (IOException e) {
					flag = true;
					System.out.println("Não foi possível entregar a mensagem");
					System.out.println(e);
				} finally {
					try {
						//Encerra o socket cliente  
						client.close();
					} catch (IOException e) {
						flag = true;
						System.out.println("Não foi possível entregar a mensagem");
						System.out.println(e);
					}
				}
			} else {
                            
                                actualNode.setClock(actualNode.getClock() + 1);
                                System.out.println(actualNode.getId() + " clock: " + actualNode.getClock());
                                
				System.out.println(actualNode.getId() + ": Enviando mensagem para todos");
				for (i = 0; i < threadsNum; i++) {
					try {
						//Cria o socket com o recurso desejado na porta especificada  
						client = new Socket("127.0.0.1", 8000 + i);
						ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
						output.flush();
						output.writeObject(actualNode);
						output.close();
						flag = false;
					} catch (IOException e) {
						System.out.println(e);
					} finally {
						try {
							//Encerra o socket cliente  
							client.close();
						} catch (IOException e) {
							System.out.println(e);
							flag = true;
						}
					}
				}
			}
		} while (flag);
	}
}
