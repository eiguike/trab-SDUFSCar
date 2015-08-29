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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author floss
 */
public class Server extends Thread {

	private Thread t;
	private String threadName;
	private ServerSocket server = null;
	private Socket client = null;
	//private BufferedReader input = null;
	private ObjectInputStream input = null;

	// Construtor do servidor, ainda não com a thread criada...
	Server(Integer i) {
		threadName = "Servidor" + i;
		System.out.println("Criando thread: " + threadName);
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
		// String utilizada para quando o servidor deve continuar,
		// ou então fechar...
		String manipular;

		// Loop para manter as threads vivas e com o servidor aberto
		do {
			try {
				// Abrindo o socket na porta 8001
				server = new ServerSocket(8001);
				// Aguarda uma conexão na porta especificada e retorna o socket que irá comunicar
				client = server.accept();

				// Cria um BufferedReader para o canal da stream de input de dados do socketclient 
				input = new ObjectInputStream(client.getInputStream());
				// Lê-se a input de dados feita pelo socket
				System.out.println(input.readObject().toString());

				// Quando termina-se o processo de recebimento, fecha-se o socket
				// e assim, é posto para dormir a thread atual para que outra 
				// thread possa realizar o processo de criação de socket
				try {
					client.close();
					server.close();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				// Caso a string que foi mandada foi Fechar, encerra-se o programa...
				// Possível bug é que as threads não se fecharão, apenas uma no caso.
			} catch (IOException e) {
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
			}
		} while (true);
	}
}
