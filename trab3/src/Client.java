
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author floss
 */
public class Client extends Thread{
	// variáveis necessárias para criação de threads
	private Thread thread;
	
	// Mensagem em si
	private Package message;
	
	// variáveis necessárias para o socket
	private Socket client;
	private BufferedReader input;
	private ObjectOutputStream output;
	
	Client(Integer sourceid, Integer destid, ArrayList<Integer> minscost){
		message = new Package(sourceid, destid, minscost);
	}
	
	public void start(){
		if(thread == null){
			thread = new Thread(this, "messagem");
			thread.start();
		}
	}
	
	public void run(){
		Integer i;
		
		// loop que enviara apenas para os nodes conectados entre si
		try {
			// tenta estabelecer uma conexão
			client = new Socket("127.0.0.1", 8000 + message.getDest());
			// prepara stream de saída
			output = new ObjectOutputStream(client.getOutputStream());
			// limpa a stream de saída
			output.flush();
			// manda o objeto message
			output.writeObject(message);
			// fecha a strema de saída
			output.close();
		} catch (IOException ex) {
			Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			// finaliza client
			try{
				if(client!=null)
					client.close();
			}catch (IOException e){
				System.out.println(e);
			}
		}
	}
	
}
