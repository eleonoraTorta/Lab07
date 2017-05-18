package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.List;


// CLASSE USATA SOLO PER LA VERSIONE PROPOSTA IN LABORATORIO

public class Utils {
	
	public static boolean oneDistance(String first, String second) {

		if (first.length() != second.length())
			throw new RuntimeException("Le due parole hanno una lunghezza diversa.");

		int distance = 1;
		for (int i = 0; i < first.length(); i++) {
			if (first.charAt(i) != second.charAt(i))
				distance--;
		}

		if (distance == 0)
			return true;
		else
			return false;
	}

	public static List<String> getAllSimilarWords(List<String> parole, String parola, int numeroLettere) {

		List<String> paroleSimili = new ArrayList<String>();
		for (String p : parole) {
			if (oneDistance(parola, p))
				paroleSimili.add(p);
		}

		return paroleSimili;
	}


}
