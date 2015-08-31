/*
 Trabalho de Redes - Servidor Multithread de Socket
 Henrique Teruo Eihara 	RA: 490016
 Charles David 		RA: 489662 
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Node.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author floss
 */
public class Server extends Thread {

	// informações de outros nós e do nó atual
	private ArrayList<Node> queue;
	private Node actualNode;

	// variáveis necessárias para os sockets
	private Thread t;
	private String threadName;
	private ServerSocket server = null;
	private Socket client = null;
	private ObjectInputStream input = null;
        private Integer threadsNum;

	private Socket clientMessage = null;

	public void sentMessage(Integer clock, Integer id, Boolean thank) {
		Integer i;
		Node aux = new Node();
		aux.setClock(clock);
		aux.setId(id);
		aux.setThank(thank);

		for (i = 0; i < 5; i++) {
			try {
				clientMessage = new Socket("127.0.0.1", 8000 + i);
				ObjectOutputStream output = new ObjectOutputStream(clientMessage.getOutputStream());
				output.flush();
				output.writeObject(aux);
				output.close();
			} catch (IOException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			} finally {
				try {
					clientMessage.close();
				} catch (IOException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i, Integer clock, Integer threadsNum) {
		// define clock e a identificação do nó
		actualNode = new Node();
		actualNode.setClock(clock);
		actualNode.setId(i);
                
                this.threadsNum = threadsNum;

		// nome da thread, ou o q identifica o nó
		threadName = "Servidor" + i;
		System.out.println("Criando thread: " + threadName);
		queue = new ArrayList<Node>();
	}

	// Criando uma thread, no java cada thread executaria a função
	// run, e por isso, devemos fazer uma sobrecarga nesta função
	public void start() {
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
	}

	// Sobre carga da função run da biblioteca Thread
	public void run() {
		Integer i;
		Client auxClient;
		Node aux = null;

		if(actualNode.getId() == 1){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
			actualNode.setClock(actualNode.getClock()+1);
			auxClient = new Client(actualNode.getId(),actualNode, threadsNum);
			auxClient.start();
		}
		// Loop para manter as threads vivas e com o servidor aberto
		do {
			try {
				// Abrindo o socket na porta 8000
				server = new ServerSocket(8000+actualNode.getId());
				// Aguarda uma conexão na porta especificada e retorna o socket que irá comunicar
				//System.out.println(actualNode.getId()+ ": Pronto para receber mensagens...");
				client = server.accept();

				// Cria um BufferedReader para o canal da stream de input de dados do socketclient 
				input = new ObjectInputStream(client.getInputStream());
				// Lê-se a input de dados feita pelo socket
				//System.out.println(input.readObject().toString());

				// quando a mensagem é recebida, ele adiciona na fila
				aux = (Node) input.readObject();
                                
                                // incrementa o clock para o máximo entre seu próprio clock e o da mensagem recebida
                                actualNode.setClock(Math.max(aux.getClock(), actualNode.getClock())+1);

				// verifica se a mensagem recebida é um agradecimento
				if(aux.isThank() != true){
					System.out.println(actualNode.getId() + ": Recebi mensagem de: "+aux.getId());
					actualNode.setThank(true);
					actualNode.setIdTarget(aux.getId());
					auxClient = new Client(actualNode.getId(),actualNode, threadsNum);	
					auxClient.start();
					queue.add(aux);
				}else{
					System.out.println(actualNode.getId()+": Recebi ACK de "+aux.getId());
				}

				server.close();
				client.close();

			} catch (IOException e) {
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		} while (true);
	}
}
