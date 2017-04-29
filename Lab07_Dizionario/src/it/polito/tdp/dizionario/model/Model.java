package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {
	
	private WordDAO w = new WordDAO ();
	UndirectedGraph <String, DefaultEdge> grafo = new SimpleGraph <String, DefaultEdge>(DefaultEdge.class);

	public List<String> createGraph(int numeroLettere) {
		ArrayList <String> elenco = (ArrayList<String>) w.getAllWordsFixedLength(numeroLettere);
		
		UndirectedGraph <String, DefaultEdge> grafo = new SimpleGraph <String, DefaultEdge>(DefaultEdge.class);
		
		//Aggiungo i vertici
		for(String s : elenco){
			grafo.addVertex(s);
		}
		
		//Aggiungo gli archi
		for( String vertex  : grafo.vertexSet() ){
			elenco.remove(vertex);
			for(int j =0; j<vertex.length(); j++){
				for(char c = 'a'; c<= 'z'; c++){
					String primaMeta = vertex.substring(0,j);
					String secondaMeta = vertex.substring(j+1);
					String parola = primaMeta + c + secondaMeta;
					
					for( String s: elenco){
						if( parola.equals(s) ){
							grafo.addEdge(vertex,  s);
						}
					}
				}
			}
			elenco.add(vertex);
		}
		
	
		
		//stampo il grafo
		
		List <String> lista = new LinkedList<String>();
		for(DefaultEdge arch : grafo.edgeSet() ){
			
			String arco = "{" + grafo.getEdgeSource(arch) + "," + grafo.getEdgeTarget(arch) + "}\n";
			lista.add(arco);
		}
		
		String nodi = "[";
		for( String vertex  : grafo.vertexSet() ){
			nodi = nodi+ " "+ vertex ;
		}
		nodi = nodi + " ]";
		
		((LinkedList<String>) lista).addFirst(nodi);
	
		return lista;
	}

	public List<String> displayNeighbours(String parolaInserita) {
		this.createGraph(parolaInserita.length());
		return Graphs.neighborListOf(grafo, parolaInserita);
		
	//	return new ArrayList<String>();
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
		String risultato = maxVertex + ": grado " + max + "\n";
		risultato += this.displayNeighbours(maxVertex);
		return risultato;
	}
}
