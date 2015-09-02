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
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
	private Integer mId;
        private MessageHandler messageHandler;

	// variáveis necessárias para os sockets
	private Thread t;
	private String threadName;
	private ServerSocket server = null;
	private Socket client = null;
	private ObjectInputStream input = null;
	private Integer threadsNum;

	// variável que valida quando todos os processos
	// enviaram ACK par auma mensagem
	private Integer ACKs;
	private Boolean waitingToSend;
	private Socket clientMessage = null;
	private Boolean alreadySent;

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i, Integer clock, Integer threadsNum) {

                this.mId = i;
            
		this.ACKs = 0;
		this.waitingToSend = false;
		this.threadsNum = threadsNum;
                
                this.messageHandler = new MessageHandler(i, clock, threadsNum);

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
                messageHandler.start();
		Integer i;
                alreadySent = false;
		Client auxClient;

		// Loop para manter as threads vivas e com o servidor aberto
		do {
			try {
				// Abrindo o socket na porta 8000
				server = new ServerSocket(8000 + mId);
				server.setSoTimeout(1000);
				// Aguarda uma conexão na porta especificada e retorna o socket que irá comunicar
				//System.out.println(actualNode.getId()+ ": Pronto para receber mensagens...");
				client = server.accept();

				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}

				input = new ObjectInputStream(client.getInputStream());
				messageHandler.addMessage((Node) input.readObject());
				
				server.close();
				client.close();

			} catch (IOException e) {
				// verificado se todos mandaram acks, se não mandaram
				// reenviar a mensagem
				if (this.alreadySent) {

				}

				try {
					server.close();
					if (client != null) {
						client.close();
					}
				} catch (SocketException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}

		} while (true);
	}
}
