package it.polito.tdp.poweroutages.model;

public class Vicini implements Comparable<Vicini> {
	
	private Nerc nerc;
	private double peso;
	
	public Vicini(Nerc nerc, double peso) {
		super();
		this.nerc = nerc;
		this.peso = peso;
	}


	@Override
	public int compareTo(Vicini o) {
		return -(int)(this.peso-o.peso);
	}

	public String toString() {
	
		return nerc+" - "+peso;
	}
	
}
