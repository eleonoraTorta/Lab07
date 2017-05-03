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
	

	public Model(){
		w = new WordDAO();
	}
	
	public List<String> getWordsFixedLength (int numeroLettere){
		return listaParole = w.getAllWordsFixedLength(numeroLettere);
	}
	
	public List<String> createGraph(int numeroLettere) {
		
		this.getWordsFixedLength(numeroLettere);
		grafo = new SimpleGraph <String, DefaultEdge>(DefaultEdge.class);
		
		//Aggiungo i vertici
		for(String s : listaParole){
			grafo.addVertex(s);
		}
		
		//Aggiungo gli archi
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
		this.createGraph(parolaInserita.length());
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
		String risultato = "Grado massimo:\n";
		risultato += maxVertex + ": grado " + max + "\n";
		risultato += this.displayNeighbours(maxVertex);
		return risultato;
	}
	
	public List<String> trovaTuttiVicini(String parolaInserita){
		this.createGraph(parolaInserita.length());
		BreadthFirstIterator <String, DefaultEdge> bfv = new BreadthFirstIterator <> (grafo, parolaInserita);
		while(bfv.hasNext()){
			listaTuttiVicini.add(bfv.next());
		}
		return listaTuttiVicini;
	}
	
	public List<String> trovaTuttiVicini2(String parolaInserita){
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
	
	
}
