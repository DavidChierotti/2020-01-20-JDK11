package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private ArtsmiaDAO dao;
	private SimpleWeightedGraph<Artist,DefaultWeightedEdge> grafo;
	private Map<Integer,Artist> artisti;
	
	
	public Model() {
		this.dao=new ArtsmiaDAO();
		this.artisti=new HashMap<>();
		for(Artist a:dao.artisti())
			artisti.put(a.getId(), a);
	}
	
	
	
	public void creaGrafo(String ruolo) {
		
		this.grafo= new SimpleWeightedGraph<Artist,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		
		//VERTICI
		Graphs.addAllVertices(grafo,dao.vertici(ruolo, artisti));
		
		//ARCHI
		for(Adiacenza a:dao.archi(ruolo)) {
			Graphs.addEdge(grafo,artisti.get(a.getA1()),artisti.get(a.getA2()),a.getPeso());
		}
		
		System.out.print(grafo.vertexSet().size()+"        "+grafo.edgeSet().size());
		
	}
	
	public List<Adiacenza> stampa(){
		List<Adiacenza> ritorno=new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
			Adiacenza a=new Adiacenza(grafo.getEdgeSource(e).getId(),grafo.getEdgeTarget(e).getId(),grafo.getEdgeWeight(e));
			ritorno.add(a);
		}
		Collections.sort(ritorno);
		return ritorno;
	}



	public SimpleWeightedGraph<Artist, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}



	public Map<Integer, Artist> getArtisti() {
		return artisti;
	}
	
	

}
