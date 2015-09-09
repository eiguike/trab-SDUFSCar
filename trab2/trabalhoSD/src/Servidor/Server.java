/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

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
import java.util.Collections;
import java.util.Comparator;
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
	private ArrayList<Node> queueProcess;
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
	private Integer acks[];
	private Boolean waitingToSend;
	private Socket clientMessage = null;

	// variável que define o estado quando um processo
	// esta acessando um recurso (hardware, etc)
	private Integer QUEROTUILIZAR;
	private Boolean ESTOUUTILIZANDO;

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i, Integer clock, Integer threadsNum) {

		actualNode = new Node();
		actualNode.setClock(clock);
		actualNode.setId(i);

		acks = new Integer[threadsNum];
		Integer i2;
		for (i2 = 0; i2 < threadsNum; i2++) {
			acks[i2] = 0;
		}
		this.waitingToSend = false;
		this.threadsNum = threadsNum;

		// nome da thread, ou o q identifica o nó
		threadName = "Servidor" + i;
		System.out.println("Criando processo: " + threadName);
		queue = new ArrayList<Node>();
		queueProcess = new ArrayList<Node>();

		QUEROTUILIZAR = 0;
		ESTOUUTILIZANDO = false;
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
		Client auxClient;

		// Loop para manter as thre ads vivas e com o servidor aberto
		try {
			// Abrindo o socket na porta 8000
			server = new ServerSocket(8000 + actualNode.getId());
			do {
				client = server.accept();

				try {
					Integer t = (int) (Math.random() * 2000);
					Thread.sleep(t);
				} catch (InterruptedException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}

				try {
					input = new ObjectInputStream(client.getInputStream());
				} catch (IOException ex) {
					Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
				}
				synchronized (queue) {
					try {
						queue.add((Node) input.readObject());
					} catch (IOException ex) {
						Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					} catch (ClassNotFoundException ex) {
						Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
					}
					queue.notify();
				}
			} while (true);

		} catch (IOException ex) {
			Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public void startAcessingResource() {
		(new Thread() {
			@Override
			public void run() {
				Client auxClient;
				Random seed = new Random();
				ESTOUUTILIZANDO = true;

				// simulando o uso do recurso
				try {
					Thread.sleep(8000);
				} catch (InterruptedException ex) {
					Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
				}
				System.out.println("TERMINEI DE USAR O RECURSO...");
				ESTOUUTILIZANDO = false;

				// aqui deve-se mandar para todos que estão
				// na fila esperando oa cesos do recurso
				// uma mensagem de ok...
				// definindo que já foi utilizado o recurso
				while (!queueProcess.isEmpty()) {
					actualNode.setOk(true);
                                        actualNode.setAllowed(true);
					actualNode.setSend(false);
					actualNode.setIdTarget(queueProcess.get(0).getId());

					auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
					auxClient.start();

					queueProcess.remove(0);
				}
			}
		}).start();
	}

	public void startMessageHandler() {
		(new Thread() {
			@Override
			public void run() {
				Client auxClient;
				Node auxNode;
				Integer oks = 0;

				do {
					auxNode = null;
					synchronized (queue) {
						if (!queue.isEmpty()) {
							auxNode = queue.get(0);
						}else{
							try {
								queue.wait();
							} catch (InterruptedException ex) {
								Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
							}
						}
					}

					// se conter uma mensagem, ele continua o passo
					if (auxNode != null) {
						if (auxNode.isSend()) {
							// this.processo deve enviar mensagem para todos
							actualNode.setSend(false);
							actualNode.setOk(false);
                                                        actualNode.setAllowed(false);
							auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
							auxClient.start();
							QUEROTUILIZAR++;
						} else {
							// não é uma mensagem de comando, logo pode ser um 
							// OK ou então uma mensagem que tal processo
							// utilizar o recurso
							
                                                        actualNode.setClock(Math.max(auxNode.getClock(), actualNode.getClock()) + 1);
                                                    
                                                        if (auxNode.isOk() == true && !auxNode.getId().equals(actualNode.getId())) {
                                                                if (auxNode.isAllowed() == true) {        
                                                                        oks++;
                                                                        if (oks.equals(threadsNum - 1) && !ESTOUUTILIZANDO) {
                                                                            System.out.println("TÔ UTILIZANDO ESSA DELICIA");
                                                                            startAcessingResource();
                                                                            QUEROTUILIZAR--;
                                                                            oks = 0;
                                                                        }
								}
							} else if (!auxNode.getId().equals(actualNode.getId())) {
								// mensagem de um processo querendo utilizar o recurso
								if (ESTOUUTILIZANDO == true) {
                                                                        System.out.println(auxNode.getId() + " NÃO VAI USAR AGORA");
									queueProcess.add(auxNode);
                                                                        actualNode.setOk(true);
									actualNode.setSend(false);
                                                                        actualNode.setAllowed(false);
									actualNode.setIdTarget(auxNode.getId());
									auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
									auxClient.start();
								} else {
									if (QUEROTUILIZAR > 0) {
										// ALGUMA HORA QUERO UTILIZAR
										if (auxNode.getClock() < actualNode.getClock()) {
											System.out.println("PERMITI FURAREM A FILA");
											actualNode.setOk(true);
											actualNode.setSend(false);
                                                                                        actualNode.setAllowed(true);
											actualNode.setIdTarget(auxNode.getId());
											auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
											auxClient.start();
										} else {
											// NÃO FAZ NADA, APENAS ESPERA OS OKS DA VIDA
											// ele não adiciona na fila ele mesmo
											// POSSÍVEL ERRO ESSE IF, NÃO SEI.....
											if (!auxNode.getId().equals(actualNode.getId())) {
												queueProcess.add(auxNode);
												Collections.sort(queueProcess, clockComparator);
											}
										}
									} else {
										// ENVIAR OK QUANDO NAO QUERO UTILIZAR
										actualNode.setOk(true);
                                                                                actualNode.setAllowed(true);
										actualNode.setSend(false);
										actualNode.setIdTarget(auxNode.getId());
										auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
										auxClient.start();
									}
								}
							}
						}
						queue.remove(0);
					}
				} while (true);

			}
		}).start();
	}

	public static Comparator<Node> clockComparator = new Comparator<Node>() {
		public int compare(Node message1, Node message2) {
			Integer id1 = message1.getClock() * 10 + message1.getId();
			Integer id2 = message2.getClock() * 10 + message2.getId();

			return id1 - id2;
		}
	};

	public void printQueue(ArrayList<Node> queue) {
		for (Integer i = 0; i < queue.size(); i++) {
			System.out.print(queue.get(i).getId() + " " + queue.get(i).getClock());
			if (i != queue.size() - 1) {
				System.out.print(", ");
			} else {
				System.out.println();
			}
		}
		if (queue.isEmpty()) {
			System.out.println("Fila vazia");
		}
	}
}
