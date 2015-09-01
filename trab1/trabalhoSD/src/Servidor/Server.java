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

	// variável que valida quando todos os processos
	// enviaram ACK par auma mensagem
	private Integer ACKs;
	private Boolean waitingToSend;
	private Socket clientMessage = null;

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i, Integer clock, Integer threadsNum) {
		// define clock e a identificação do nó
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
		Integer i;
		Client auxClient;
		Node aux = null;

		// Loop para manter as threads vivas e com o servidor aberto
		do {
			try {
				// Abrindo o socket na porta 8000
				server = new ServerSocket(8000 + actualNode.getId());
				// Aguarda uma conexão na porta especificada e retorna o socket que irá comunicar
				//System.out.println(actualNode.getId()+ ": Pronto para receber mensagens...");
				client = server.accept();

				input = new ObjectInputStream(client.getInputStream());

				//quando a mensagem é recebida, ele adiciona na fila
				aux = (Node) input.readObject();

				// incrementa o clock para o máximo entre seu próprio clock e o da mensagem recebida
				if ((aux.isSend() == true) && (aux.isThank() == true)) {
					// se for de mensagem de comando não faça nada...
				}else{
					actualNode.setClock(Math.max(aux.getClock(), actualNode.getClock()) + 1);
				}

				// verifica se a mensagem recebida é um agradecimento
				if (aux.isThank() != true) {
					// mensagem não é um agradecimento, é feito um aviso qu recebeu
					// e enviado uma mensagem de agradecimento, posteriormente
					// é adicionado na fila
					System.out.println(actualNode.getId() + ": Recebi mensagem de: " + aux.getId());
					actualNode.setThank(true);
					actualNode.setIdTarget(aux.getId());
					if (!actualNode.getId().equals(aux.getId())) {
						auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
						auxClient.start();
					}
					queue.add(aux);
				} else {
					// caso a mensagem seja de fato um agradecimenot, é verificado
					// se não é uma mensagem de comando para entrar na fila
					if (aux.isSend() == false) {
						// se não for uma mensagem de comando, é avisado que recebeu ACK do processo
						// e então adicionado no númeor de ACKs, caso os ACKs sejam iguais o númeor
						// de threads existentes, é setado em true uma flag waitingToSend
						System.out.println(actualNode.getId() + ": Recebi ACK de " + aux.getId());
						this.ACKs++;
						if ((ACKs.equals(this.threadsNum - 1))) {
							System.out.println(actualNode.getId() + ": Só esperando minha vez...");
							this.waitingToSend = true;
						}
					} else {
						// caso a mensagem seja de comando, então é feito o envio para todas os
						// processos que o processo atual quer utilizar tal recurso
						System.out.println(actualNode.getId() + ": Recebi comando de enviar mensagem...");
						actualNode.setThank(false);
						actualNode.setSend(false);
						actualNode.setClock(actualNode.getClock()+1);
						auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
						auxClient.start();
					}
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
