package it.polito.tdp.dizionario.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WordDAO {

	/*
	 * Ritorna tutte le parole di una data lunghezza che differiscono per un solo carattere
	 */
	public List<String> getAllSimilarWords(String parola, int numeroLettere) {
		
		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT nome FROM parola WHERE nome LIKE ? AND LENGTH(nome) = ?;";  //LIKE cerca tutte le parole che 
		PreparedStatement st;															//differiscono di una lettera nella
																						//posizione in cui ce il _
		try {
			
			List<String> parole = new ArrayList <String>();
			char[] parolaOriginale = parola.toCharArray();
			System.out.println(parola);
			
			for(int i =0; i< parola.length(); i++){
				
					char temp = parolaOriginale[i];
					parolaOriginale[i] = '_';
					String parolaDaCercare= String.copyValueOf(parolaOriginale);  // uguale a valueOf(char[])  --> ritorna una stringa
					parolaOriginale[i] =temp;
					System.out.println(parolaDaCercare);
		
					st = conn.prepareStatement(sql);
					st.setString(1, parolaDaCercare);
					st.setInt(2, numeroLettere);
					ResultSet res = st.executeQuery();

					while (res.next()){
						String nextWord = res.getString("nome");
						if(parola.compareToIgnoreCase(nextWord)!=0){  //come un compareTo ma case insensitive
							parole.add(nextWord);
						}
					}
			
			}

			return parole;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	/*
	 * Ritorna tutte le parole di una data lunghezza
	 */
	public List<String> getAllWordsFixedLength(int numeroLettere) {

		Connection conn = DBConnect.getInstance().getConnection();
		String sql = "SELECT nome FROM parola WHERE LENGTH(nome) = ?;";
		PreparedStatement st;

		try {

			st = conn.prepareStatement(sql);
			st.setInt(1, numeroLettere);
			ResultSet res = st.executeQuery();

			List<String> parole = new ArrayList<String>();

			while (res.next())
				parole.add(res.getString("nome"));

			return parole;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

}
