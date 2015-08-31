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
		ArrayList <Server> list = new ArrayList<Server>();
		Integer i;
		Server server;
                Integer threadsNum = 3;

		for(i=0;i<threadsNum;i++){
			server = new Server(0+i,2+i, threadsNum);
			server.start();
			list.add(server);
		}
                
	}

}
