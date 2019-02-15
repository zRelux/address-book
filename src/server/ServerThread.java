package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {

	private Socket socket;
	private PrintWriter os;
	private BufferedReader is;
	private GestisciRichieste gestisciRichieste;

	/*
	 * Crea il thread del server che gestisce
	 * 
	 * @param socket socket dei comunicazione con il client
	 * 
	 * @param gestisciRichieste gestisce le richieste del client
	 */
	public ServerThread(Socket socket, GestisciRichieste gestisciRichieste) throws IOException {
		this.socket = socket;
		this.gestisciRichieste = gestisciRichieste;
		is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		os = new PrintWriter(socket.getOutputStream());
	}

	/*
	 * Metodo run del thread permette la comunicazione del thread con il client
	 * gestisce le richieste del cliente e gli restituisce il risultato
	 */
	@Override
	public void run() {
		super.run();
		String clientInput = "";
		while (true) {
			// Lettura del messaggio del client
			try {
				clientInput = is.readLine();
			} catch (IOException e) {
				break;
			}

			if ((clientInput == null) || (clientInput.equalsIgnoreCase("STOP"))) {
				os.println(clientInput);
				os.flush();
				break;
			}
			
			if(clientInput.equals("SHUTDOWN SERVER")){
				gestisciRichieste.setCloseAll(clientInput);
				os.println("STOP");
				os.flush();
				break;
			}

			/*
			 * Controllo della richiesta del client
			 */
			if (clientInput.equalsIgnoreCase("comandi")) {
				clientInput = gestisciRichieste.comandi();
			} else if (clientInput.contains("info")) {
				if (clientInput.equalsIgnoreCase("info")) {
					clientInput = gestisciRichieste.info();
				} else {
					try{
						clientInput = gestisciRichieste.infoComando(clientInput.split(":")[1]);
					}catch(ArrayIndexOutOfBoundsException e){
					}
				}
			} else if (clientInput.equalsIgnoreCase("help")) {
				clientInput = gestisciRichieste.help();
			} else if (gestisciRichieste.contieneComando(clientInput)) {
				clientInput = gestisciRichieste.result();
			}

			os.println(clientInput);
			os.flush();

		}

		try {
			is.close();
			os.close();
			socket.close();
		} catch (IOException e) {

		}
		System.out.println("Il colloquio con il client è terminato...");
	}

}