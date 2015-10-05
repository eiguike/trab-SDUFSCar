
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.Timer;

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
	private ArrayList<Integer> nodesConected;
	
	// construtor
	public Node(Integer pid, Integer clock) throws InterruptedException {
		this.clock = clock;
		this.pid = pid;
		
		message = new ArrayList<Package>();
		nodesConected = new ArrayList<Integer>();
		
		// todos os nodes não tem um líder estipulado ainda
		this.theChosenOne = -1;
		
		server();
		startMessageHandler();
		inputCommands();
		
		Thread.sleep(5000);
		multicastIAMALIVE();
		
		Thread.sleep(5000);
	}
	
	public void multicastIAMALIVE(){
		int i;
		for(i=0;i<10;i++){
			new Client(0, i, pid, 3);
		}
	}
	
	public void server() {
		serverThread = (new Thread() {
			@Override
			public void run() {
				Package aux;
				ActionListener coordenadorTask = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						int i;
						if(theChosenOne.equals(pid)){
							System.out.println("HEHEHE");
							for(i=0;i<nodesConected.size();i++){
								new Client(0, nodesConected.get(i), pid, 3);
							}
						}
						
					}
				};
				Timer coordenador = new Timer(2000, coordenadorTask);
				coordenador.setRepeats(true);
				
				coordenador.start();
				try {
					server = new ServerSocket(8000+pid);
					do {
						
						serverSocket = server.accept();
						try {
							input = new ObjectInputStream(serverSocket.getInputStream());
						} catch(IOException ex){
							System.out.println(ex);
						}
						
						try {
							aux = (Package) input.readObject();
							if(aux.getCmd() == 4){
								System.out.println(pid+" : Desligando servidor...");
								coordenador.stop();
								synchronized(message) {
									//System.out.println(pid+" : Recebi mensagem, adicionando na fila...");
									message.add(aux);
									message.notify();
								}
								
								return;
							}else{
								synchronized(message) {
									System.out.println(pid+" : Recebi mensagem, adicionando na fila...");
									message.add(aux);
									message.notify();
								}
							}
						} catch (ClassNotFoundException ex) {
							Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
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
				Integer i = 0;
				Timer eleicao = null;
				
				ActionListener taskPerformer2 = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						int i;
						System.out.println(pid+" : Iniciando eleição, coordenador caiu: "+ theChosenOne);
						for(i=0;i<nodesConected.size();i++){
							if(nodesConected.get(i) > pid){
								new Client(0, nodesConected.get(i), pid, 2);
							}
							if(nodesConected.get(i).equals(theChosenOne))
								nodesConected.remove(i);
						}
						
					}
				};
				Timer coordenador = new Timer(5000, taskPerformer2);
				ActionListener taskPerformer = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						Integer i;
						// quando o timer acaba, manda mensagem para todos que
						// é o novo coordenador
						System.out.println(pid+ " : Sou o novo coordenador!");
						for(i=0;i<nodesConected.size();i++)
							new Client(0, nodesConected.get(i), pid, 5);
					}
				};
				do {
					aux = null;
					// acesso atômico da variável message
					synchronized (message){
						if(!message.isEmpty()) {
							aux = message.remove(0);
							//System.out.println("node" + pid + ": mensagem de node" + aux.getPidSource()+ " recebida");
						} else {
							try {
								//System.out.println("node" + pid + ": esperando mensagens");
								message.wait();
							} catch(InterruptedException e) {
								System.out.println(e);
							}
						}
					}
					if(aux != null){
						if(nodesConected.contains(aux.getPidSource()) == false){
							System.out.println(pid+ " : Adicionei "+aux.getPidSource()+" nos Nodes conectados!");
							nodesConected.add(aux.getPidSource());

							if(theChosenOne.equals(pid)){
								new Client(0,aux.getPidSource(),pid, 5);
								System.out.println("HUEHUEHUIAHSDLAJSDKÇJZXHCKÇ");
							}
						}
						switch(aux.getCmd()){
							case 1:
								// caso receba um OK, ele apenas para o timer
								System.out.println(pid + " : Recebi OK, encerro meu trabalho");
								if(eleicao != null)
									eleicao.stop();
								break;
							case 2:
								// mensagem de eleicao
								if(aux.getPidSource() < pid){
									System.out.println(pid+" : Iniciado eleição");
									// quando a mensagem de eleição veiod e um processo menor
									// deve-se enviar uma mensagem de OK
									new Client(0, aux.getPidSource(), pid, 1);
									
									// quando recebe uma mensagem de eleição de um processo menor
									// deve-se enviar uma mensagem de eleição para os outros
									// processos de maior PID que o dele
									for(i = 0; i < nodesConected.size(); i++)
										if(nodesConected.get(i) > pid)
											new Client(0, nodesConected.get(i), pid, 2);
									
									// 5 segundos de espera para ser eleito
									// assim que passa os 5 segundos, é mandado para todos os nodes
									// que ele é o novo coordenador
									int delay = 5000; //milliseconds
									
									if(eleicao == null){
										eleicao = new Timer(delay, taskPerformer);
										eleicao.setRepeats(false);
										eleicao.start();
									}
									else{
										eleicao.restart();
									}
									
								}
								break;
							case 3:
								// mensagem IAMALIVE que tem como siginifcado
								// que tal node está vivo e pode receber/enviar
								// mensagens
								if(aux.getPidSource().equals(theChosenOne)){
									// reseta o timer do coordenador
									if(coordenador != null)
										coordenador.restart();
								}

								new Client(0,aux.getPidSource(),pid,6);	
								
								break;
							case 4:
								System.out.println("Mensagme de auto destruicao");
								coordenador.stop();
								eleicao.stop();
								return;
							case 5:
								theChosenOne = aux.getPidSource();
								int delay = 10000;
								System.out.println(pid+" : Novo coordenador: "+aux.getPidSource());
								if(coordenador != null)
									coordenador.restart();
								else{
									coordenador = new Timer(delay, taskPerformer2);
									coordenador.start();
								}
							
							
							default:
								break;
						}
						
						
					}
				} while(true);
			}
			
		}).start();
	}
	
	// multicast
	public void sendMessages(Integer cmd, Integer pidDest) {
		Integer i;
		Client auxClient;
		for(i = 0; i < nodesConected.size() ; i++)
			auxClient= new Client(0, nodesConected.get(i), pid, cmd);
	}
	
	public void inputCommands(){
		(new Thread(){
			@Override
			public void run(){
				int i;
				Client message = null;
				Scanner input = new Scanner(System.in);
				Integer cmd;
				do {
					System.out.println("Digite o novo valor de clock para este processo");
					cmd = input.nextInt();
					
					switch(cmd){
						case -1:
							new Client(0,pid,pid,4);
							break;
						case 0:
							new Client(0,pid,pid,3);
							break;
						case 1:
							new Client(0,pid,pid, 5);
							break;
						case 2:
							for(i=0;i<nodesConected.size();i++)
								System.out.print(nodesConected.get(i) + " ");
							break;
						case 3:
							for(i=0;i<nodesConected.size();i++)
								new Client(0, nodesConected.get(i), pid, 5);
							break;
						case 4:
							System.out.println(pid+ " : O coordenador é: "+theChosenOne);
					}
					
					if(cmd.equals(-1)){
						new Client(0,pid,pid,4);
					}
				} while (true);
			}
		}).start();
	}
	
}
