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
public class P1 {

    public static void main(String[] args) throws IOException {
        Integer threadsNum = 3;

        Server server = new Server(0 , 2, threadsNum);
        server.start();
    }

}
