package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GestisciRichieste {

	private String infoComandi;
	private String info;
	private String help;
	private String[] comandi;

	private GestisciFile gestisciFile;
	private String result;
	private String closeAll = "";
	private ArrayList<String> rubrica;

	/*
	 * Costruttore che inizializza le stringhe e per le richieste dal file comandi
	 */
	public GestisciRichieste() {
		gestisciFile = new GestisciFile();
		try {
			infoComandi = gestisciFile.leggiFile("comandi.txt")[0];
			info = gestisciFile.leggiFile("comandi.txt")[1];
			help = gestisciFile.leggiFile("comandi.txt")[2];
		} catch (IOException e) {
			e.printStackTrace();
		}

		// prepara il vettore dei comandi
		preparaVettore();
	}

	/*0
	 * prepara il vettore di comandi e li divide
	 */
	private void preparaVettore() {
		comandi = new String[infoComandi.split("-l").length];
		for (int i = 0; i < infoComandi.split("-l").length; i++) {
			comandi[i] = infoComandi.split("-l")[i].split(":")[0].split("\\ ")[1];
		}
		
		try {
			rubrica = new ArrayList<String>(Arrays.asList(gestisciFile.leggiFile("rubrica.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		rubrica.add("");
	}

	/*
	 * Restituisce l'info dei comandi
	 * 
	 * @return info dei comandi
	 */
	public String comandi() {
		return infoComandi;
	}

	/*
	 * Restituisce la string help
	 * 
	 * @return help
	 */
	public String help() {
		return help;
	}

	/*
	 * Restituisce le info relative al comando
	 * 
	 * @param nomeComando il nome del comando il quale bisogna sapere le
	 * informazioni
	 * 
	 * @return le info relative al comando | comando non trovato
	 */
	public String infoComando(String nomeComando) {
		String[] c = infoComandi.split("-l");
		String[] in = info.split("-l");
		for (int i = 0; i < c.length; i++) {
			if (c[i].contains(nomeComando)) {
				return in[i];
			}
		}
		return "Comando non presente";
	}
	
	/*
	 * Gestisce le richieste che contengono comandi del server
	 * 
	 * @param clientInput richiesta del client
	 * 
	 * @return TRUE|FALSE se la richiesta e stata garantita
	 */
	public boolean contieneComando(String clientInput) {
		String[] richiesta = clientInput.split(":");
		try {
			int contatore = 0;
			for( int i = 0; i < clientInput.length(); i++ ) {
			    if(clientInput.charAt(i) == ','  || clientInput.charAt(i) == ':') {
			    	contatore++;
			    } 
			}
			
			if(contatore >= 3) {
				throw new Exception();
			}
			
			for (int i = 0; i < comandi.length; i++) {
				if (clientInput.toLowerCase().contains(comandi[i].toLowerCase())) {
					if (i < 2) {
						result = trova(richiesta[1]);
					} else if (i > 1 && i < 4) {
						result = modifica(richiesta[1].split(",")[0], richiesta[1].split(",")[1]);
					} else if (i == 4) {
						result = aggiungi(richiesta[1].split(",")[0], richiesta[1].split(",")[1]);
					} else if (i == 5) {
						result = elimina(richiesta[1]);
					} else if (i == 6) {
						result = tutto();
					}
					return true;
				}
			}
		} catch (Exception e) {
			result = "COMANDO NON ESEGUITO CONTROLLARE SINTASSI";
			return true;
		}

		return false;
	}

	/*
	 * restituisce il risultato della richiesta
	 * @return result il risultato della "query"
	 */
	public String result() {
		return result;
	}
	
	/*
	 * Trova la stringa richiesta nel file e la restituisce
	 * @param trova stringa da trovare
	 * 
	 * @return tuttla la riga del file contente il nome | numero
	 */
	public String trova(String trova) {
		boolean trovato = false;
		String risultato = "";

		for (int i = 0; i < rubrica.size(); i++) {
			if (rubrica.get(i).toLowerCase().contains(trova.toLowerCase())) {
				String tmp = rubrica.get(i).split(":")[0];
				if (tmp.equals(trova)) {
					trovato = true;
					risultato += trova + ":" + rubrica.get(i).split(":")[1] + "-l";
				} else {
					trovato = true;
					risultato += rubrica.get(i).split(":")[0] + ":" + trova + "-l";
				}
			}
		}
		
		if(trovato) {
			return risultato;
		}else {
			return trova + " non trovato.";
		}
	}
	
	/*
	 * Metodo che modifica una riga del file contente la parola 
	 * 
	 */	
	public synchronized String modifica(String doveCercare, String cosaModificare) {
		for (int i = 0; i < rubrica.size(); i++) {
			if (rubrica.get(i).contains(doveCercare)) {
				try {
					Integer.parseInt(cosaModificare);
					rubrica.set(i, cosaModificare + ":" + doveCercare);
				} catch (Exception e) {
					rubrica.set(i, doveCercare + ":" + cosaModificare);
				}
				return cosaModificare + ":TRUE";
			}
		}
		return cosaModificare + ":FALSE";
	}
	
	
	/*
	 * Metodo che aggiunge un nuovo numero di telefono
	 * @param numero numero di telefono
	 * @param nome nome della persona
	 * 
	 * @return string conferma dell'aggiunta
	 */
	public synchronized String aggiungi(String numero, String nome) {
		
		System.out.println("Numero " + numero + " nome " + nome);
		Long.parseLong(numero);	
		
		for(int i = 0; i < rubrica.size(); i++) {			
			if(rubrica.get(i).contains(numero)) {
				String n = rubrica.get(i).split("\\:")[0];
					if(n.equals(numero)){
						return "Numero già correlato con " +  rubrica.get(i).split("\\:")[1] + " prova con il getname o getnumber";
					}
			}			
		}
		rubrica.add(numero + ":" + nome);
		
		return numero + "," + nome + ":TRUE";
	}
	/*
	 * Elimina il contatto inserito
	 * @return stringa contente il successo del comando 
	 */
	public synchronized String elimina(String nome) {
		for (int i = 0; i < rubrica.size(); i++) {
			if (rubrica.get(i).contains(nome)) {
				rubrica.remove(rubrica.get(i));
				return nome + ":TRUE";
			}
		}
		return nome + ":FALSE";
	}
	/*
	 * Restiruisce tutti i contatti
	 * @return result stringa contente tutti i conttatti
	 */
	public String tutto() {
		String result = "";
		for (int i = 0; i < rubrica.size(); i++) {
			result += rubrica.get(i) + "-l";
		}
		return result;
	}
	
	/*
	 * Restitusce le info di tutti i comandi 
	 * @return result stringa contente tutti i comando
	 */
	public String info() {
		String result = "";
		String[] in = info.split("-l");
		for (int i = 0; i < in.length; i++) {
			result += in[i] + "-l";
		}
		return result;
	}

	public String getCloseAll() {
		return closeAll;
	}

	public void setCloseAll(String closeAll) {
		this.closeAll = closeAll;
	}
	
	public void salva(){
		for(int i = 0; i < rubrica.size(); i++) {
			System.out.println("----> " + rubrica.get(i));
			rubrica.remove("");
		}
		gestisciFile.modificaFile("rubrica.txt", (String[]) rubrica.toArray(new String[rubrica.size()]));
	}
	
}