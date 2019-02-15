package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class GestisciFile {
	PrintWriter writer;
	private BufferedReader br = null;
	private String comandi = "1. GETNAME richiesta del nominativo ad un numero-l2. GETNUMBER richiesta del numero associato ad un nominativo-l3. SETNAME mofica del nominativo associato ad un numero-l4. SETNUMBER modifica del numero associato ad un nominativo-l5. ADDENTRY aggiunta di una nuova coppia nominativo-numero-l6. DELENTRY eliminazione di un numero-l7. SHOWALL mostra tutta la rubrica-r1. GETNAME:<numero>-l2. GETNUMBER:<nome>-l3. SETNAME:<numero>,<nome>-l4. SETNUMBER:<nome>,<numero>-l5. ADDENTRY:<numero><nome>-l6. DELENTRY:<nome>-l7. SHOWALL-rComandi disponibili: comandi, info:<nomecomando>,info:<numerocomando>,info,help";
	/*
	 * Metodo che sovrascrive il file con le modifiche apportate
	 * @param rubrica vettore contente le modifiche
	 * 
	 */
	public void modificaFile(String nomeFile, String[] rubrica) {
		try {
			writer = new PrintWriter(nomeFile, "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < rubrica.length; i++) {
			if(!rubrica[i].equals("") && rubrica[i] != null) {
				writer.println(rubrica[i] + "-r");
			}
		}

		writer.close();
	}
	/*
	 * Legge dal file e lo carica su di una stringa
	 * @param nomeFile nome del file da cui leggere
	 * 
	 * @return vettore contente il file
	 */
	public String[] leggiFile(String nomeFile) throws IOException {
		String lineafinale = "";
		try {
			br = new BufferedReader(new FileReader(nomeFile));
		}catch(FileNotFoundException e) {
			writer = new PrintWriter(nomeFile, "UTF-8");
			
			if(nomeFile.equalsIgnoreCase("comandi.txt")) {
				writer.println(comandi);
				writer.close();
			}
			
			writer = null;
			br = new BufferedReader(new FileReader(nomeFile));
		}
		String line = br.readLine();
		while (line != null) {
			lineafinale += line;
			line = br.readLine();
		}
		br.close();
		return lineafinale.split("-r");
	}
}