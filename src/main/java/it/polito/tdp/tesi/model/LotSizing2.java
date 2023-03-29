package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;

public class LotSizing2 {
	
	private int capacitaOrd;
	private int capacitaStraord;
	private double costoProdOrd;
	private double costoProdStraord;
	private double costoProdEsterna;
	private double costoStoccaggio;
	private int qtaIniziale;
	//private HashMap<Integer, Produzione> schedOttima;
	private ArrayList<Integer> schedOttima;
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
		//this.schedOttima = new HashMap<Integer, Produzione>();
		this.schedOttima = new ArrayList<Integer>();
		this.ctMin = 1000000;
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
			//schedule.get(ord).setQtaStoccata(ord-1); //
		}
		System.out.println("Domanda: ");
		for(int d : ordiniMese.keySet()) {
			System.out.println("Mese: "+d+", Domanda: "+ordiniMese.get(d));
		}
		/*System.out.println("\nPiano produttivo: ");
		for(Produzione p : schedule.values()) {
			System.out.println(p.toString());
		}*/
		System.out.println("\nQuantità rimanenti da produrre: ");
		for(int r : residua.keySet()) {
			System.out.println("Mese: "+r+", QtaRimenente: "+residua.get(r));
		}
		/*for(int mese : residua.keySet()) {
			if(residua.get(mese)>0) {
				ArrayList<Integer> parziale = new ArrayList<Integer>();
				if(mese==1) {
					parziale.add(schedule.get(mese).getQtaProdOrd());
					parziale.add(schedule.get(mese).getQtaProdStraord());
					parziale.add(schedule.get(mese).getQtaProdEst());
				}else {
					parziale.add(schedule.get(mese-1).getQtaProdOrd());
					parziale.add(schedule.get(mese-1).getQtaProdStraord());
					parziale.add(schedule.get(mese-1).getQtaProdEst());
					parziale.add(schedule.get(mese).getQtaProdOrd());
					parziale.add(schedule.get(mese).getQtaProdStraord());
					parziale.add(schedule.get(mese).getQtaProdEst());
				}
				schedulazione(parziale, residua.get(mese), 0);
			}
		}*/
		/*ArrayList<Integer> parziale = new ArrayList<Integer>();
		parziale.add(schedule.get(1).getQtaProdOrd());
		parziale.add(schedule.get(1).getQtaProdStraord());
		parziale.add(schedule.get(1).getQtaProdEst());*/
		/*ArrayList<Integer> parziale = new ArrayList<Integer>();
		parziale.add(schedule.get(4).getQtaProdOrd());
		parziale.add(schedule.get(4).getQtaProdStraord());
		parziale.add(schedule.get(4).getQtaProdEst());
		parziale.add(schedule.get(5).getQtaProdOrd());
		parziale.add(schedule.get(5).getQtaProdStraord());
		parziale.add(schedule.get(5).getQtaProdEst());*/
		/*schedulazione(parziale, residua.get(1), 0);
		System.out.println(output(this.schedOttima));*/ //aggiornare sched totale
		/*schedule.get(4).setQtaProdOrd(this.schedOttima.get(0));
		schedule.get(4).setQtaProdStraord(this.schedOttima.get(1));
		schedule.get(4).setQtaProdEst(this.schedOttima.get(2));
		schedule.get(5).setQtaProdOrd(this.schedOttima.get(3));
		schedule.get(5).setQtaProdStraord(this.schedOttima.get(4));
		schedule.get(5).setQtaProdEst(this.schedOttima.get(5));*/
		
		for(int mese : residua.keySet()) {
			if(residua.get(mese)>0) {
				ArrayList<Integer> parziale = new ArrayList<Integer>();
				if(mese==1) {
					this.ctMin = 1000000;
					parziale.add(schedule.get(1).getQtaProdOrd());
					parziale.add(schedule.get(1).getQtaProdStraord());
					parziale.add(schedule.get(1).getQtaProdEst());
				}else {
					this.ctMin = 1000000;
					parziale.add(schedule.get(mese-1).getQtaProdOrd());
					parziale.add(schedule.get(mese-1).getQtaProdStraord());
					parziale.add(schedule.get(mese-1).getQtaProdEst());
					parziale.add(schedule.get(mese).getQtaProdOrd());
					parziale.add(schedule.get(mese).getQtaProdStraord());
					parziale.add(schedule.get(mese).getQtaProdEst());
				}
				schedulazione(parziale, residua.get(mese), 0);
				if(mese==1) {
					schedule.get(mese).setQtaProdOrd(this.schedOttima.get(0));
					schedule.get(mese).setQtaProdStraord(this.schedOttima.get(1));
					schedule.get(mese).setQtaProdEst(this.schedOttima.get(2));
				}else {
					schedule.get(mese-1).setQtaProdOrd(this.schedOttima.get(0));
					schedule.get(mese-1).setQtaProdStraord(this.schedOttima.get(1));
					schedule.get(mese-1).setQtaProdEst(this.schedOttima.get(2));
					schedule.get(mese).setQtaProdOrd(this.schedOttima.get(3));
					schedule.get(mese).setQtaProdStraord(this.schedOttima.get(4));
					schedule.get(mese).setQtaProdEst(this.schedOttima.get(5));
				}
			}
		}
		
		System.out.println("\nPiano produttivo: ");
		for(Produzione p : schedule.values()) {
			System.out.println(p.toString());
		}
	}

	private void schedulazione(ArrayList<Integer> parziale, Integer qtaResidua, int tipoProd) {
		if(qtaResidua<=0) {
			double ct = calcolaCosto(parziale);
			if(ct<this.ctMin) {
				this.schedOttima = new ArrayList<Integer>(parziale);
				this.ctMin = ct; 
			}
		}else {
			for(int i=tipoProd; i<parziale.size(); i++) {
				if(parziale.get(i)+1<=getCapacita(i)) {  /*this.capacitaOrd*/
					parziale.set(i, parziale.get(i)+1);
					qtaResidua = qtaResidua-1;
					schedulazione(parziale, qtaResidua, tipoProd);
					parziale.set(i, parziale.get(i)-1);
					qtaResidua = qtaResidua+1;
					tipoProd = i+1;
				}
			}
		}
		
	}
	
	private int getCapacita(int i) {
		if(i==0 || i==3) {
			return this.capacitaOrd;
		}else if(i==1 || i==4) {
			return this.capacitaStraord;
		}else {
			return 100000;
		}
	}

	private double calcolaCosto(ArrayList<Integer> parziale) {
		double ct = 0;
		if(parziale.size()==3) {
			ct = parziale.get(0)*this.costoProdOrd+parziale.get(1)*this.costoProdStraord+parziale.get(2)*this.costoProdEsterna;
		}else {
			ct = (parziale.get(0)+parziale.get(3))*this.costoProdOrd+(parziale.get(1)+parziale.get(4))*this.costoProdStraord
					+(parziale.get(2)+parziale.get(5))*this.costoProdEsterna
					+(parziale.get(0)+parziale.get(1)+parziale.get(2))*this.costoStoccaggio;
		}
		return ct;
	}

	private String output(ArrayList<Integer> parziale) {
		String output;
		if(parziale.size()<=3) {
			output = "QtaOrd: "+parziale.get(0)+" QtaStraord: "+parziale.get(1)+" QtaEst: "+parziale.get(2);
		}else {
			output = "QtaOrd mese precedente: "+parziale.get(0)+" QtaStraord mese precedente: "+parziale.get(1)+" QtaEst mese precedente: "+parziale.get(2)
					+" QtaOrd: "+parziale.get(3)+" QtaStraord: "+parziale.get(4)+" QtaEst: "+parziale.get(5);
		}
		return output;
	}
}
