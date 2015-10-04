
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thiago
 */
public class Node {
	// para identificar qual processo que é
	private Integer pid;
	
	// variáveis necessárias para o funcionamento do socket do servidor
	private Socket serverSocket;
	private ObjectInputStream input;
	private ServerSocket server;
	private ArrayList<Package> message;

	// necessário para a construção do algoritmo valentão
	private Integer clock;

	// variável que identifica quem esta eleito
	private Integer theChosenOne;

	// Thread do servidor
	private Thread serverThread;

	// nodes que estão conectados
	private PriorityQueue<Integer> nodesConected;
	
	// construtor
	public Node(Integer pid, Integer clock) {
		this.clock = clock;
		this.pid = pid;

		message = new ArrayList<Package>();
		
		server();
		startMessageHandler();
		inputCommands();
	}

	public void server() {
		serverThread = (new Thread() {
			@Override
			public void run() {
				try {
					server = new ServerSocket(8000+pid);
					do {
						
						serverSocket = server.accept();
						try {
							input = new ObjectInputStream(serverSocket.getInputStream());
						} catch(IOException ex){
							System.out.println(ex);
						}
						synchronized(message) {
							try{
								message.add((Package) input.readObject());
							} catch (ClassNotFoundException ex) {
								Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
							}
							message.notify();
						}
					} while(true);
				} catch (IOException ex) {
					System.out.println(ex);
				}
			}
		});
		serverThread.start();
	}
	
	// função que trata as mensagens recebidas
	public void startMessageHandler(){
		(new Thread(){
			@Override
			public void run() {
				Package aux;
				do {
					aux = null;
					// acesso atômico da variável message
					synchronized (message){
						if(!message.isEmpty()) {
							aux = message.remove(0);
							System.out.println("node" + pid + ": mensagem de node" + aux.getPidSource()+ " recebida");
						} else {
							try {
								System.out.println("node" + pid + ": esperando mensagens");
								message.wait();
							} catch(InterruptedException e) {
								System.out.println(e);
							}
						}
					}
					if(aux != null){
						switch(aux.getCmd()){
							case 1:
								// mensagem de ok	
								
								break;
							case 2:
								// mensagem de eleicao

								break;
							case 3:
								// mensagem de definir clock
								break;
							case 4:
								// mensagem para desligar thread servidor

								break;
							default:
								break;
						}


					}
				} while(true);
			}
			
		}).start();
	}
	
	public void sendMessages() {
		Client auxClient;

	//	
	//	for(Integer i = 0; i < mindist.size(); i++) {
	//		// se estiver conectado, enviará
	//		if(connected.get(i).equals(Boolean.TRUE)) {
	//			System.out.println("Mandando mensagem de node" + pid + " para node" + i);
	//			auxClient = new Client(pid, i, mindist);
	//			auxClient.start();
	//		}
	//	}
	}
	
	public void inputCommands(){
		(new Thread(){
			@Override
			public void run(){
				Client message = null;
				Scanner input = new Scanner(System.in);
				Integer cmd;
				do {
					System.out.println("Digite o novo valor de clock para este processo");
					cmd = input.nextInt();

					if(cmd.equals(-1)){
						System.out.println("Thread parará por 20 segundos, envie o comando -2 para recomeça-la");
						try {
							synchronized(serverThread){
								serverThread.wait();
							}
						} catch (InterruptedException ex) {
							Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
						}
					}else{
						if(cmd.equals(-2)){
							if(serverThread.isInterrupted() == false)
								serverThread.notify();
						}else{
							clock = cmd;
						}
						
					}
				} while (true);				
			}
		}).start();
		
		
		
	}
	
}
