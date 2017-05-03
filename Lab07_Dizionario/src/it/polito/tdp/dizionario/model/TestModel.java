package it.polito.tdp.dizionario.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		List <String> grafo;
		
		System.out.println(String.format("**Grafo creato** - Trovate #%d parole di lunghezza 3\n",  model.createGraph(4).size()));
		grafo = model.createGraph(3);
		System.out.println(grafo);
		
		
		List<String> vicini = model.displayNeighbours("ora");
		System.out.println("Vicini di ora: " + vicini);
		
		System.out.println();
		
		System.out.println("Cerco il vertice con grado massimo...");
		System.out.println(model.findMaxDegree());
		
		System.out.println();
		System.out.println("Cerco tutti i vicini: ");
		System.out.println(model.trovaTuttiVicini("ora"));
	}

}
