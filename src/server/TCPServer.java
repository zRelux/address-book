package server;

public class TCPServer extends Thread {
	
	private int port;

	public TCPServer(int port){
		this.port = port;
	}
	
	public void run() {
		GestisciRichieste gestisciRichieste = new GestisciRichieste();
		AcceptThread acceptThread = null;
		try {
			acceptThread = new AcceptThread(gestisciRichieste, port);
			acceptThread.start();
		} catch (Exception e) {
		}
		
		int i = 0;
		while(!gestisciRichieste.getCloseAll().equals("SHUTDOWN SERVER")){
			i++;
			if(i == 20000){
				System.out.println("Salvo");
				gestisciRichieste.salva();
				i = 0;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		gestisciRichieste.salva();
		System.exit(1);
	}
}