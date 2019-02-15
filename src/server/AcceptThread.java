package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptThread extends Thread {

	private ServerSocket acceptSocket;
	private GestisciRichieste gestisciRichieste;
	
	public AcceptThread(GestisciRichieste gestisciRichieste, int port) throws IOException{
		acceptSocket = new ServerSocket(port);
		this.gestisciRichieste = gestisciRichieste;
	}
	public void run(){
		//Ciclo che accetta le richieste fatte dai clienti e crea thread che le gestiscono
		while (true) {
			Socket socket;
			try {
				socket = acceptSocket.accept();
				ServerThread st = new ServerThread(socket, gestisciRichieste);
				st.start();
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
	}
}
