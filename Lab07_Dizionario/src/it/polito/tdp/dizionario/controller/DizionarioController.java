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
	void doReset(ActionEvent event) {
		inputNumeroLettere.clear();
		inputParola.clear();
		txtResult.clear();
	//  bisogna "cancellare " il grafo?	
	}

	@FXML
	void doGeneraGrafo(ActionEvent event) {

		try {
			String numeroLettere = inputNumeroLettere.getText();
			if( inputNumeroLettere.getText().isEmpty()){
				txtResult.setText("ERRORE, nessun numero inserito!");
			}
			int numero = Integer.parseInt(numeroLettere);
			List <String> grafo = this.model.createGraph(numero);
			for(String s : grafo){
				txtResult.appendText(s);
			}
			
		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		} 
	}

	@FXML
	void doTrovaGradoMax(ActionEvent event) {
		
		try {
			// se non ce il grafo?
			txtResult.setText(model.findMaxDegree());
			inputNumeroLettere.clear();

		} catch (RuntimeException re) {
			txtResult.setText(re.getMessage());
		}
	}

	@FXML
	void doTrovaVicini(ActionEvent event) {
		
		try {
			String parola = inputParola.getText();
			if( inputParola.getText().isEmpty()){
				txtResult.setText("ERRORE, nessuna parola inserita!");
			}
			inputParola.setText(parola);
			inputNumeroLettere.setText(String.valueOf(parola.length()));
			txtResult.appendText("Lista dei vicini di '" + parola + "':\n");
			List <String> vicini = model.displayNeighbours(parola);
			for(String s : vicini){
				txtResult.appendText(s + " ");
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
	}

	public void setModel(Model model) {
		this.model = model;
		
	}
}