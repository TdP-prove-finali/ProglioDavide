package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;

public class LotSizing {
	
	private int capacitaOrd;
	private int capacitaStraord;
	private double costoProdOrd;
	private double costoProdStraord;
	private double costoProdEsterna;
	private double costoStoccaggio;
	private int qtaIniziale;
	private HashMap<Integer, Produzione> schedOttima;
	private double ctMin;
	
	public void lotSizing(HashMap<Integer, Integer> ordiniMese, int capacitaOrd, int capacitaStraord,
			double costoProdOrd, double costoProdStraord, double costoProdEsterna, double costoStoccaggio, int qtaIniziale) {
		this.capacitaOrd = capacitaOrd;
		this.capacitaStraord = capacitaStraord;
		this.costoProdEsterna = costoProdEsterna;
		this.costoProdOrd = costoProdOrd;
		this.costoProdStraord = costoProdStraord;
		this.costoStoccaggio = costoStoccaggio;
		this.qtaIniziale = qtaIniziale;
		this.schedOttima = new HashMap<Integer, Produzione>();
		HashMap<Integer, Produzione> schedule = new HashMap<Integer, Produzione>();
		HashMap<Integer, Integer> residua = new HashMap<Integer, Integer>();
		for(int ord : ordiniMese.keySet()) {
			if(ordiniMese.get(ord)<this.capacitaOrd) {
				schedule.put(ord, new Produzione(ord, ordiniMese.get(ord), 0, 0));
				residua.put(ord, 0);
			}else {
				schedule.put(ord, new Produzione(ord, this.capacitaOrd, 0, 0));
				residua.put(ord, ordiniMese.get(ord)-this.capacitaOrd);
			}
		}
		System.out.println("Domanda: ");
		for(int d : ordiniMese.keySet()) {
			System.out.println("Mese: "+d+", Domanda: "+ordiniMese.get(d));
		}
		System.out.println("\nPiano produttivo: ");
		for(Produzione p : schedule.values()) {
			System.out.println(p.toString());
		}
		System.out.println("\nQuantità rimanenti da produrre: ");
		for(int r : residua.keySet()) {
			System.out.println("Mese: "+r+", QtaRimenente: "+residua.get(r));
		}
		HashMap<Integer, Produzione> parziale = new HashMap<Integer, Produzione>();
		parziale.put(1, schedule.get(1));
		schedulazione(parziale, residua.get(1), 0);
	}

	private void schedulazione(HashMap<Integer, Produzione> parziale, Integer qtaResidua, int tipoProd) {
		/*if(qtaResidua<=0) {
			double ct = calcolaCosto(parziale);
			if(ct<this.ctMin) {
				this.schedOttima = new ArrayList<Integer>(parziale);
				this.ctMin = ct; 
			}
		}else {
			for(int i=tipoProd; i<parziale.size(); i++) {
				if(parziale.get(i)+1<=getCapacita(i)) {  
					parziale.set(i, parziale.get(i)+1);
					qtaResidua = qtaResidua-1;
					schedulazione(parziale, qtaResidua, tipoProd);
					parziale.set(i, parziale.get(i)-1);
					qtaResidua = qtaResidua+1;
					tipoProd = i+1;
				}
			}
		}*/
	}
}