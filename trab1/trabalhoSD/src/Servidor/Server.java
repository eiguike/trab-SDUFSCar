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
    private Integer ACKs;
    private Integer acks[];
    private Boolean waitingToSend;
    private Socket clientMessage = null;
    private Boolean alreadySent;

    // Construtor do servidor, ainda não com a thread criada...
    Server(Integer i, Integer clock, Integer threadsNum) {

        actualNode = new Node();
        actualNode.setClock(clock);
        actualNode.setId(i);

        acks = new Integer[threadsNum];
        Integer i2;
        for(i2= 0; i2 < threadsNum; i2++) acks[i2] = 0;
        this.waitingToSend = false;
        this.threadsNum = threadsNum;

        // nome da thread, ou o q identifica o nó
        threadName = "Servidor" + i;
        System.out.println("Criando thread: " + threadName);
        queue = new ArrayList<Node>();
        queueProcess = new ArrayList<Node>();
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
                    Collections.sort(queue, clockComparator);
                }
            } while (true);

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

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


                do {
                    aux = null;
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            aux = queue.get(0);
                        }
                    }
                    if (aux != null) {
                        if (aux.isSend()) {
                            // caso a mensagem seja de comando, então é feito o envio para todas os
                            // processos que o processo atual quer utilizar tal recurso
                            //System.out.println(actualNode.getClock()*10 + actualNode.getId() + ": Recebi comando de enviar mensagem...");
                            actualNode.setThank(false);
                            actualNode.setSend(false);
                            //actualNode.setClock(actualNode.getClock() + 1);
                            auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
                            auxClient.start();
                        } else {
                            if (!aux.isThank()) {
                                // quando um processo manda mensagem, seus acks são zerados
                                ACKs = 0;
                                alreadySent = true;
                                actualNode.setClock(Math.max(aux.getClock(), actualNode.getClock()) + 1);
                                // e enviado uma mensagem de agradecimento, posteriormente
                                // é adicionado na fila
                                System.out.println(actualNode.getClock()*10 + actualNode.getId() + ": Recebi mensagem de: " + aux.getId());
                                actualNode.setThank(true);
                                actualNode.setIdTarget(aux.getId());
                                auxClient = new Client(actualNode.getId(), actualNode, threadsNum);
                                auxClient.start();
                                queueProcess.add(aux);
                                Collections.sort(queueProcess,clockComparator);
                            } else {
                                // se não for uma mensagem de comando, é avisado que recebeu ACK do processo
                                // e então adicionado no númeor de ACKs, caso os ACKs sejam iguais o númeor
                                // de threads existentes, é setado em true uma flag waitingToSend
                                acks[aux.getIdTarget()]++;
                                //System.out.println("|0:| " + acks[0] + "|1:| "+acks[1]+"|2:| "+acks[2]);
                                if(!queueProcess.isEmpty() && acks[(queueProcess.get(0)).getId()] == threadsNum){
                                    System.out.println(actualNode.getClock()*10+actualNode.getId() + ": Removi da fila " + (queueProcess.get(0)).getId());
                                    acks[(queueProcess.get(0)).getId()] = 0;
                                    queueProcess.remove(0);
                                }

                            }
                        }
                        synchronized (queue) {
                            queue.remove(0);
                        }
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
}
