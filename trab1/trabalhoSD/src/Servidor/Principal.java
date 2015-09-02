/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/**
 *
 * @author floss
 */
public class Principal {

	public static void main(String[] args) throws IOException {
		Node aux = null;
		Client message = null;
		//Scanner input = new Scanner(System.in);
		Integer cmd;

		ArrayList<Integer> input = new ArrayList<Integer>();

		input.add(2);
		input.add(0);
		input.add(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
		                }

		do {
		                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                }
	
			//cmd = input.nextInt();

			aux = new Node();
			aux.setSend(true);
			aux.setThank(true);
			aux.setClock(0);
			aux.setId(-1);
			aux.setIdTarget(input.get(0));

			input.remove(0);

			message = new Client(-1,aux,1);
			message.start();


		} while(!input.isEmpty());

   try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ex) {
                }

	}

}
