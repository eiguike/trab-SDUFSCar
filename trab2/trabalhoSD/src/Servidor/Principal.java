/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import Servidor.Node;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author floss
 */
public class Principal {

	public static void main(String[] args) throws IOException {
		ArrayList<Server> list = new ArrayList<Server>();
		Integer i;
		Server server;
		Integer threadsNum = 4;

		Node aux = null;
		Client message = null;
		Scanner input = new Scanner(System.in);
		Integer cmd;
		do {
			System.out.println("Estou pronto para receber comandos");
			cmd = input.nextInt();
			aux = new Node();
			aux.setSend(true);
			aux.setOk(true);
			aux.setClock(0);
			aux.setId(-1);
			aux.setIdTarget(cmd);

			message = new Client(-1, aux, 1);
			message.start();

		} while (true);

	}

}
