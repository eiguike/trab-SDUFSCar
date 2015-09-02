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
import java.util.Random;
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

	// variável que valida quando todos os processos
	// enviaram ACK par auma mensagem
	private Integer ACKs;
	private Boolean waitingToSend;
	private Socket clientMessage = null;
	private Boolean alreadySent;

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i, Integer clock, Integer threadsNum) {

                actualNode = new Node();
                actualNode.setClock(clock);
                actualNode.setId(i);
            
		this.ACKs = 0;
		this.waitingToSend = false;
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
                startMessageHandler();
		Integer i;
                alreadySent = false;
		Client auxClient;

		// Loop para manter as threads vivas e com o servidor aberto
		do {
			try {
				// Abrindo o socket na porta 8000
				server = new ServerSocket(8000 + actualNode.getId());
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
				synchronized(queue) {
                                    queue.add((Node) input.readObject());
                                }
                                        
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
        
        public void startMessageHandler() {
            (new Thread() {
                @Override
                public void run() {
                    Random seed = new Random();
                    Boolean alreadySent = false;
                    Client auxClient;
                    Node aux;
                    queue = new ArrayList<Node>();

                    try {
                            Thread.sleep(seed.nextInt(1000));
                    } catch (InterruptedException ex) {
                            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    do {
                        aux = null;
                        synchronized(queue) {
                            if (!queue.isEmpty()) {
                                aux = queue.get(0);
                            }
                        }
                        if (aux != null) {
                            if (aux.isSend()) {
                                // caso a mensagem seja de comando, então é feito o envio para todas os
                                // processos que o processo atual quer utilizar tal recurso
                                System.out.println(actualNode.getId() + ": Recebi comando de enviar mensagem...");
                                actualNode.setThank(false);
                                actualNode.setSend(false);
                                //actualNode.setClock(actualNode.getClock() + 1);
                                auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
                                auxClient.start();
                            } else {
                                System.out.println(actualNode.getId() + " clock: " + actualNode.getClock());
                                if (!aux.isThank()) {
                                    // quando um processo manda mensagem, seus acks são zerados
                                    ACKs = 0;
                                    alreadySent = true;
                                    actualNode.setClock(Math.max(aux.getClock(), actualNode.getClock()) + 1);
                                    // e enviado uma mensagem de agradecimento, posteriormente
                                    // é adicionado na fila
                                    System.out.println(actualNode.getId() + ": Recebi mensagem de: " + aux.getId());
                                    actualNode.setThank(true);
                                    actualNode.setIdTarget(aux.getId());
                                    auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
                                    auxClient.start();
                                } else {
                                    // se não for uma mensagem de comando, é avisado que recebeu ACK do processo
                                    // e então adicionado no númeor de ACKs, caso os ACKs sejam iguais o númeor
                                    // de threads existentes, é setado em true uma flag waitingToSend
                                    System.out.println(actualNode.getId() + ": Recebi ACK de " + aux.getId());
                                    ACKs++;
                                    if ((ACKs.equals(threadsNum))) {
                                        System.out.println(actualNode.getId() + ": Só esperando minha vez...");
                                        //this.waitingToSend = true;
                                    }
                                // mensagem normal de pedido
                                }
                            }
                            synchronized(queue) {
                                queue.remove(0);
                            }
                        }
                    } while (true);
                }
            }).start();
        }
}
