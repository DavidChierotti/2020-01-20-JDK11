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
	private List<Artist> migliore;
	private double peso;
	
	
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

	public boolean isPresente(int i) {
		boolean ritorno=false;
		if(grafo.vertexSet().contains(artisti.get(i)))
			ritorno=true;
		return ritorno;
	}

	
	public List<Artist> cercaCammino(int id){
		Artist partenza=artisti.get(id);
		
		migliore=new ArrayList<>();
		
		List<Artist> parziale=new ArrayList<>();
		
		parziale.add(partenza);
		this.cerca(parziale);
		
		return migliore;
	}

	private void cerca(List<Artist> parziale) {
		if(this.calcolaPeso(parziale)>this.calcolaPeso(migliore)) {
			migliore=new ArrayList<>(parziale);
		}
		peso=0;
		if(parziale.size()>=2) {
		DefaultWeightedEdge e=grafo.getEdge(parziale.get(0),parziale.get(1));
		peso=grafo.getEdgeWeight(e);
		}
		List<Artist> possibili=new ArrayList<>(this.possibili(parziale,peso));
		for(Artist a: possibili ) {
			parziale.add(a);
			this.cerca(parziale);
			parziale.remove(a);
		}
		
		
	}
	
	public double calcolaPeso(List<Artist> parziale) {
		double tot=0;
		if(parziale.size()>1) {
		for(int i=1;i<parziale.size();i++) {
			DefaultWeightedEdge e=grafo.getEdge(parziale.get(i-1),parziale.get(i));
			tot+=grafo.getEdgeWeight(e);
		}
	}
		return tot;
		
	}
	
	private List<Artist> possibili(List<Artist> parziale,double peso){
        List<Artist> possibili=new ArrayList<>();
		
		Artist a=parziale.get(parziale.size()-1);
		
		for(Artist aa:Graphs.neighborListOf(grafo,a)) {
			DefaultWeightedEdge e=grafo.getEdge(a,aa);
			if(parziale.size()>1) {
			if(!parziale.contains(aa)&&grafo.getEdgeWeight(e)==peso) {
				possibili.add(aa);
			}
			
		  }
			else {
				if(!parziale.contains(aa)) {
					possibili.add(aa);
				}
			}
		}
		
		return possibili;
	}



	public SimpleWeightedGraph<Artist, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}



	public Map<Integer, Artist> getArtisti() {
		return artisti;
	}
	
	

}
