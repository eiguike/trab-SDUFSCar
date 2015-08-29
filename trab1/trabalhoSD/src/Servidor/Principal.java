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
		Server server = new Server(1);
		Client client = new Client(1);

		server.start();
		client.start();
	}

}
