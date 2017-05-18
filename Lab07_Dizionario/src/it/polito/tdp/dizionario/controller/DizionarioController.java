package it.polito.tdp.dizionario.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.dizionario.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DizionarioController {
	
	private Model model;
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;
	@FXML
	private TextArea txtResult;
	@FXML
	private TextField inputNumeroLettere;
	@FXML
	private TextField inputParola;
	@FXML
	private Button btnGeneraGrafo;
	@FXML
	private Button btnTrovaVicini;
	@FXML
	private Button btnTrovaGradoMax;
	@FXML
	private Button btnTrovaTuttiVicini;
	

	@FXML
	void doReset(ActionEvent event) {
		inputNumeroLettere.clear();
		inputParola.clear();
		txtResult.clear();
		
		inputNumeroLettere.setDisable(false);
		btnGeneraGrafo.setDisable(false);
		inputParola.setDisable(true);
		btnTrovaVicini.setDisable(true);
		btnTrovaGradoMax.setDisable(true);

	}

	@FXML
	void doGeneraGrafo(ActionEvent event) {

		inputParola.clear();
		txtResult.clear();
		
		try {
			String numeroLettere = inputNumeroLettere.getText();
			if( inputNumeroLettere.getText().isEmpty()){
				txtResult.setText("ERRORE, nessun numero inserito!");
			}
			int numero = Integer.parseInt(numeroLettere);
			List <String> grafo = this.model.createGraph(numero);
			if(grafo != null){
				txtResult.appendText("Trovate " + grafo.size() + " parole di lunghezza "+ numero + "\n");
			} else{
				txtResult.appendText("Trovate 0 parole di lunghezza "+ numero + "\n");
			}
			for(String s : grafo){
				txtResult.appendText(s);
			}
			
			inputNumeroLettere.setDisable(true);
			btnGeneraGrafo.setDisable(true);
			inputParola.setDisable(false);
			btnTrovaVicini.setDisable(false);
			btnTrovaGradoMax.setDisable(false);
			
		} catch (NumberFormatException nfe) {
			txtResult.setText("Inserire un numero corretto di lettere!");
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}  
	}

	@FXML
	void doTrovaGradoMax(ActionEvent event) {
		
		try {
			
			txtResult.setText(model.findMaxDegree());

		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
	}

	@FXML
	void doTrovaVicini(ActionEvent event) {
		txtResult.clear();
	
		try {
			String parola = inputParola.getText();
			if( parola == null || parola.length() ==0){
				txtResult.setText("ERRORE, nessuna parola inserita!");
				return;
			}
			inputParola.setText(parola);
			inputNumeroLettere.setText(String.valueOf(parola.length()));
			txtResult.appendText("Lista dei vicini di '" + parola + "':\n");
			List <String> vicini = model.displayNeighbours(parola);
			if( vicini== null){
				txtResult.appendText("Non e` stato trovato nessun risultato\n");
			}else{
				txtResult.appendText(vicini.size() + "\n");
				for(String s : vicini){
					txtResult.appendText(s + " ");
				}
			}

		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
	}
	
	@FXML
	void doTrovaTuttiVicini(ActionEvent event) {
		txtResult.clear();
		try {
			String parola = inputParola.getText();
			if( parola == null || parola.length() ==0){
				txtResult.setText("ERRORE, nessuna parola inserita!");
			}
			inputParola.setText(parola);
			inputNumeroLettere.setText(String.valueOf(parola.length()));
			txtResult.appendText("Lista di tutti i vertici connessi a '" + parola + "':\n");
			List <String> tuttiVicini = model.trovaTuttiVicini(parola);
			if( tuttiVicini== null){
				txtResult.appendText("Non e` stato trovato nessun risultato\n");
			}else{
				txtResult.appendText(tuttiVicini.size() + "\n");
				for(String s : tuttiVicini){
					if(!s.equals(parola)){
						txtResult.appendText(s + "\n");
					}
				}
			}

		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
	}
	

	@FXML
	void initialize() {
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputNumeroLettere != null : "fx:id=\"inputNumeroLettere\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert inputParola != null : "fx:id=\"inputParola\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnGeneraGrafo != null : "fx:id=\"btnGeneraGrafo\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaVicini != null : "fx:id=\"btnTrovaVicini\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaGradoMax != null : "fx:id=\"btnTrovaTutti\" was not injected: check your FXML file 'Dizionario.fxml'.";
		assert btnTrovaTuttiVicini != null : "fx:id=\"btnTrovaTuttiVicini\" was not injected: check your FXML file 'Dizionario.fxml'.";
		
		inputParola.setDisable(true);
		btnTrovaVicini.setDisable(true);
		btnTrovaGradoMax.setDisable(true);
		
	}

	public void setModel(Model model) {
		this.model = model;
		
	}
}