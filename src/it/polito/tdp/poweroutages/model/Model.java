package it.polito.tdp.poweroutages.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {
	
	private Graph<Nerc, DefaultWeightedEdge> grafo;
	private PowerOutagesDAO dao;
	private List<Nerc> nercs;
	private Map<Integer, Nerc> idMap;
	
	//Costruttore
	public Model() {
		idMap = new HashMap<>();
		dao = new PowerOutagesDAO();
		nercs = dao.loadAllNercs(idMap);
	}
	
	public List<Nerc> getNercs(){
		return this.nercs;
	}
	
	public void creaGrafo() {
		
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungo nodi
		Graphs.addAllVertices(this.grafo, nercs);
		
		//Conviene ottenere prima i soli archi
		List<Accoppiamento> accoppiamenti = dao.loadAllEdges();
		
		//Poi interroghiamo il database per ottenere il peso
		for (Accoppiamento a : accoppiamenti) {
			dao.getAllWeight(a);
		}
		
		for (Accoppiamento a : accoppiamenti) {
			
			Nerc n1 = idMap.get(a.getId1());
			Nerc n2 = idMap.get(a.getId2());
			double peso = a.getPeso();
			
			//Aggiungo arco (Per il momento senza peso"
			Graphs.addEdge(this.grafo, n1, n2, peso);
			
		}
		
		
		System.out.println("GRAFO CREATO");
		System.out.println("#NODI: "+this.grafo.vertexSet().size());
		System.out.println("#ARCHI: "+this.grafo.edgeSet().size());
	}
	
	public List<Vicini> getVicini(Nerc n){
		
		List<Nerc> vicini = new ArrayList<>();
		List<Vicini> result = new ArrayList<>();
		
		vicini = Graphs.neighborListOf(this.grafo, n);
		
		for (Nerc nerc : vicini) {
			
		   double peso = grafo.getEdgeWeight(grafo.getEdge(n, nerc));
		   
		   Vicini v = new Vicini(nerc, peso);
		   
		   result.add(v);
		   	
		}
		
		Collections.sort(result);
		
		return result;
	}

}
