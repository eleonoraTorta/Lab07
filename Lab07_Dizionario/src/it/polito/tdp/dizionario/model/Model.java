package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {
	
	private WordDAO w;
	private UndirectedGraph <String, DefaultEdge> grafo; 
	private List <String> listaParole;
	private List <String> listaTuttiVicini = new ArrayList <String>();
	private List <String> listaTuttiVicini2 = new ArrayList<String>();
	private int numeroLettere = 0;
	

	public Model(){
		w = new WordDAO();
	}
	
	public List<String> getWordsFixedLength (int numeroLettere){
		return listaParole = w.getAllWordsFixedLength(numeroLettere);
	}
	
	public List<String> createGraph(int numeroLettere) {
		
		this.numeroLettere = numeroLettere;
		this.getWordsFixedLength(numeroLettere);
		grafo = new SimpleGraph <String, DefaultEdge>(DefaultEdge.class);
		
		//Aggiungo i vertici
		for(String s : listaParole){
			grafo.addVertex(s);
		}
		
		//Aggiungo gli archi- VERSIONE MIA
		for( String vertex  : grafo.vertexSet() ){
			listaParole.remove(vertex);
			for(int j =0; j<vertex.length(); j++){
				for(char c = 'a'; c<= 'z'; c++){
					String primaMeta = vertex.substring(0,j);
					String secondaMeta = vertex.substring(j+1);
					String parola = primaMeta + c + secondaMeta;
					
					for( String s: listaParole){
						if( s.equals(parola) ){
							grafo.addEdge(vertex,  s);
						}
					}
				}
			}
			listaParole.add(vertex);
		}
		
		//Aggiungo gli archi - VERSIONI CLASSE
		
		//for(String vertex : listaParole){
		
		//Alternativa 1: uso il database: LENTO!!
		// List <String> paroleSimili = w.getAllSimilarWords(vertex, numeroLettere);
		
		//ALternativa 2: uso il mio algoritmo in Java
		//List <String> paroleSimili = Utils.getAllSimilarWords(listaParole, vertex, numeroLettere);
		// for( String parolaSimile : paroleSimili)
		//		grafo.addEdge(vertex, parolaSimile)
		//}
		
	
		//stampo il grafo
		
		List <String> archi = new ArrayList<String>();
		for(DefaultEdge arch : grafo.edgeSet() ){
			
			String arco = "{" + grafo.getEdgeSource(arch) + "," + grafo.getEdgeTarget(arch) + "}\n";
			archi.add(arco);
		}
		
		List <String> nodi = new ArrayList <String>();
		for( String vertex  : grafo.vertexSet() ){
			String nodo = vertex + "\n";
			nodi.add(nodo);
		}
	
		nodi.addAll(archi);
		return nodi;
	
	}

	public List<String> displayNeighbours(String parolaInserita) {
		
		if (numeroLettere != parolaInserita.length())
			throw new RuntimeException("La parola inserita ha una lunghezza differente rispetto al numero inserito.");

		return Graphs.neighborListOf(grafo, parolaInserita);
	}

	public String findMaxDegree() {
		String maxVertex="";
		int max=0;
		for( String vertex  : grafo.vertexSet() ){
			if( grafo.degreeOf(vertex) > max){
				max = grafo.degreeOf(vertex);
				maxVertex = vertex;
			}
		}
		if( max != 0){
		String risultato = "Grado massimo:\n";
		risultato += maxVertex + ": grado " + max + "\n";
		risultato += this.displayNeighbours(maxVertex);
		return risultato;
		}
		else{
			return "Non trovato.\n";
		}
	}
	
	//Ora voglio ottenere una lista di TUTTI i nodi raggiungibili nel grafo.
	//Sono possibili 3 versioni
	
	//1)
	//VERSIONE LIBRERIA JGRAPHT
	public List<String> trovaTuttiVicini(String parolaInserita){
		
		if (numeroLettere != parolaInserita.length())
			throw new RuntimeException("La parola inserita ha una lunghezza differente rispetto al numero inserito.");
		
		this.createGraph(parolaInserita.length());
		
		//Con BreadthFirstIterator
		BreadthFirstIterator <String, DefaultEdge> bfv = new BreadthFirstIterator <> (grafo, parolaInserita);
		while(bfv.hasNext()){
			listaTuttiVicini.add(bfv.next());
		}
		
		//Con DepthFirstIterator
		//DepthFirstIterator <String, DefaultEdge> dfv = new DepthFirstIterator <> (grafo, parolaInserita);
		//while(dfv.hasNext()){
		//	listaTuttiVicini.add(dfv.next());
		//}
		
		return listaTuttiVicini;
	}
	
	//2)
	//VERSIONE RICORSIVA
	public List<String> trovaTuttiVicini2(String parolaInserita){
		if (numeroLettere != parolaInserita.length())
			throw new RuntimeException("La parola inserita ha una lunghezza differente rispetto al numero inserito.");
		
		this.createGraph(parolaInserita.length());
		
		recursive(parolaInserita);
		return listaTuttiVicini2;
	}
	
	public void recursive(String vertex){
		if(Graphs.neighborListOf(grafo,vertex).size() == 0){
			return;
		}
		for(String s : Graphs.neighborListOf(grafo,vertex) ){
			if(!listaTuttiVicini2.contains(s)){
				listaTuttiVicini2.add(s);
				recursive(s);
			}
		}
	}
	
	//Funzione Ricorsiva opzione 2
	protected void recursiveVisit(String n, List<String> visited) {
		// Do always
		visited.add(n);

		for (String c : Graphs.neighborListOf(grafo, n)) {
			if (!visited.contains(c))
				recursiveVisit(c, visited);
		}
	}
	
	
	//3)
	//VERSIONE ITERATIVA
	public List<String> trovaTuttiVicini3(String parolaInserita) {

		if (numeroLettere != parolaInserita.length())
			throw new RuntimeException("La parola inserita ha una lunghezza differente rispetto al numero inserito.");

		// Creo due liste: quella dei noti visitati ..
		List<String> visited = new LinkedList<String>();
		// .. e quella dei nodi da visitare
		List<String> toBeVisited = new LinkedList<String>();

		// Aggiungo alla lista dei vertici visitati il nodo di partenza.
		visited.add(parolaInserita);
		// Aggiungo ai vertici da visitare tutti i vertici collegati a quello inserito
		toBeVisited.addAll(Graphs.neighborListOf(grafo, parolaInserita));

		while (!toBeVisited.isEmpty()) {

			// Rimuovi il vertice in testa alla coda
			String temp = toBeVisited.remove(0);

			// Aggiungi il nodo alla lista di quelli visitati
			visited.add(temp);

			// Ottieni tutti i vicini di un nodo
			List<String> listaDeiVicini = Graphs.neighborListOf(grafo, temp);

			// Rimuovi da questa lista tutti quelli che hai già visitato..
			listaDeiVicini.removeAll(visited);
			// .. e quelli che sai già che devi visitare.
			listaDeiVicini.removeAll(toBeVisited);

			// Aggiungi i rimanenenti alla coda di quelli che devi visitare.
			toBeVisited.addAll(listaDeiVicini);
		}

		// Ritorna la lista di tutti i nodi raggiungibili
		return visited;
	}

	
}
