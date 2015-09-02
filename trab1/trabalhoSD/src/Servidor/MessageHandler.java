/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Node.Node;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiago
 */
public class MessageHandler extends Thread {
    
    private ArrayList<Node> queue;
    private Node node;
    private Thread thread;
    private String threadName;
    private Integer threadsNum;
    private Integer clock;
    private Integer ACKs;
    
    MessageHandler(Integer i, Integer clock, Integer threadsNum) {
	node = new Node();
	node.setClock(clock);
	node.setId(i);
                
	threadName = "Handler" + i;
        
        this.threadsNum = threadsNum;
    }
    
    public void start() {
	if (thread == null) {
            thread = new Thread(this, threadName);
            thread.start();
	}
    }
    
    public void run() {
	Random seed = new Random();
        Boolean alreadySent = false;
        Client auxClient;
        this.queue = new ArrayList<Node>();
        
	try {
		Thread.sleep(seed.nextInt(1000));
	} catch (InterruptedException ex) {
		Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
	}
        
        do {
            if (!queue.isEmpty()) {
                System.out.println(node.getId() + ": Fila contem mensagens");
                Node aux = queue.get(0);
                if (aux.isSend()) {
                    // caso a mensagem seja de comando, então é feito o envio para todas os
                    // processos que o processo atual quer utilizar tal recurso
                    System.out.println(node.getId() + ": Recebi comando de enviar mensagem...");
                    node.setThank(false);
                    node.setSend(false);
                    //actualNode.setClock(actualNode.getClock() + 1);
                    auxClient = new Client(node.getId(), node, threadsNum);
                    auxClient.start();
                } else {
                    System.out.println(node.getId() + " clock: " + node.getClock());
                    if (!aux.isThank()) {
                        // quando um processo manda mensagem, seus acks são zerados
                        this.ACKs = 0;
                        alreadySent = true;
                        node.setClock(Math.max(aux.getClock(), node.getClock()) + 1);
                        // e enviado uma mensagem de agradecimento, posteriormente
                        // é adicionado na fila
                        System.out.println(node.getId() + ": Recebi mensagem de: " + aux.getId());
                        node.setThank(true);
                        node.setIdTarget(aux.getId());
                        auxClient = new Client(node.getId(), node, threadsNum);
                        auxClient.start();
                    } else {
                        // se não for uma mensagem de comando, é avisado que recebeu ACK do processo
                        // e então adicionado no númeor de ACKs, caso os ACKs sejam iguais o númeor
                        // de threads existentes, é setado em true uma flag waitingToSend
                        System.out.println(node.getId() + ": Recebi ACK de " + aux.getId());
                        this.ACKs++;
                        if ((ACKs.equals(this.threadsNum))) {
                            System.out.println(node.getId() + ": Só esperando minha vez...");
                            //this.waitingToSend = true;
                        }
                    // mensagem normal de pedido
                    }
                }
                queue.remove(0);
            }
        } while (true);
    }
    
    public void addMessage(Node message) {
        System.out.println(node.getId() + ": Mensagem adicionada na fila");
        if (queue.isEmpty()) {
            queue.add(message);
        } else {
            queue.add(message);
        }
    }
}
