package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {
	private InetAddress addr;
	private Socket socket = null;
	private PrintWriter os;
	private BufferedReader is;

	/*
	 * Creazione del client
	 * 
	 * @param host stringa che contiene l'indirizzo IP del server
	 * 
	 * @param port numero della porta TCP
	 */
	public TCPClient(String host, int porta) throws IOException {
		addr = InetAddress.getByName(host);
		socket = new Socket(addr, porta);
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		os = new PrintWriter(socket.getOutputStream());
	}

	/*
	 * Metodo in grado di inviare messaggi al server a cui si è connessi
	 * 
	 * @param msg messaggio da inviare al server
	 * 
	 * @return la risposta del server al messaggio
	 */
	public String comunicaServer(String msg){
		String serverAnswer = null;
		String userInput = null;
		if (!msg.equals("")) {
			userInput = msg;

			msg = "";
			os.println(userInput);
			os.flush();

			try {
				serverAnswer = is.readLine();
			} catch (Exception e) {
				serverAnswer = "Server Chiuso riattivare";
			}
			
			
			if (serverAnswer.contains("-l"))
				serverAnswer = serverAnswer.replaceAll("-l", "\n");

			if (serverAnswer.equals("STOP")) {
				try {
					socket.close();
					return "-e-n-d";
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return serverAnswer;
	}

}