package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.tesi.model.Event.EventType;
import it.polito.tdp.tesi.model.Event2.Event2Type;

public class Simulazione3 {
	
	private HashMap<Integer, WorkStation> linea = new HashMap<Integer, WorkStation>();
	private ArrayList<String> specifiche = new ArrayList<String>();
	
	private double TH = 0;
	private double CT = 0;
	private double WIP = 0;
	private double rb;
	private double T0;
	private double W0;
	
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();
	private int pezziCompletati = 0;
	
	public void scegliLinea(int scelta) {
		switch (scelta) {
			case 1:
				this.linea.put(1, new WorkStation(3600, 4318.5, 1800, 2700, 28800, 1800, 3600,
					10, 1200, 2400));
				this.specifiche.add("WORKSTATION 1\n"+
						"tempo medio di interarrivo: "+5000/3600+" h\n"+
						"tempo medio di processo: "+1000/60+" min\n"+
						"tempo medio tra guasti successivi: "+28800/3600+" h\n"+
						"tempo medio di riparazione guasto: "+3600/3600+" h\n"+
						"numero di jobs tra setups consecutivi: "+10+" jobs\n"+
						"tempo medio di setup: "+1200/60+" min\n");
				this.linea.put(2, new WorkStation(3600, 4318.5, 1800, 2700, 28800, 1800, 3600,
					300, 1200, 2400));
				this.specifiche.add("WORKSTATION 2\n"+
						"tempo medio di interarrivo: "+3600/3600+" h\n"+
						"tempo medio di processo: "+1800/60+" min\n"+
						"tempo medio tra guasti successivi: "+28800/3600+" h\n"+
						"tempo medio di riparazione guasto: "+3600/3600+" h\n"+
						"numero di jobs tra setups consecutivi: "+300+" jobs\n"+
						"tempo medio di setup: "+1200/60+" min\n");
			break;
			case 2:
				this.linea.put(1, new WorkStation(40000, 4318.5, 20000, 2700, 128800, 15000, 3600,
					10, 10000, 2400));
				this.specifiche.add("WORKSTATION 1\n"+
						"tempo medio di interarrivo: "+40000/3600+" h\n"+
						"tempo medio di processo: "+20000/3600+" h\n"+
						"tempo medio tra guasti successivi: "+128800/3600+" h\n"+
						"tempo medio di riparazione guasto: "+15000/3600+" h\n"+
						"numero di jobs tra setups consecutivi: "+10+" jobs\n"+
						"tempo medio di setup: "+10000/3600+" h\n");
			break;
		}
	}

	public void caricaLinea() {
		WorkStation wk = this.linea.get(1);
		int nProd = 1;
		for(int i=0; i<300000; i=i+this.getPoisson(wk.getT0()/60)*60) {
			this.queue.add(new Event(i, EventType.NUOVO_JOB, nProd, 1));
			nProd++;
		}
		for(int i=0; i<300000; i=i+this.getPoisson(wk.getMf()/60)*60) {
			if(i>0) {
				this.queue.add(new Event(i, EventType.GUASTO, -1, 1));
			}
		}
	}
	
	public int getPoisson(double lambda) {
		double L = Math.exp(-lambda);
		double p = 1.0;
		int k = 0;
		do {
			k++;
		    p *= Math.random();
		} while (p > L);
		return k - 1;
	}
	
	public void run() {
		for(int i=1; i<this.linea.size()+1; i++) {
			if(i>1) {
				WorkStation wk = this.linea.get(i-1);
				for(Integer nProd : wk.getUscite().keySet()) {
					this.queue.add(new Event(wk.getUscite().get(nProd), 
							EventType.NUOVO_JOB, nProd, i));
				}
				for(double t=wk.getUscite().get(1); t<300000; 
						t=t+this.getPoisson(this.linea.get(i).getMf()/60)*60) {
					if(t>wk.getUscite().get(1)) {
						this.queue.add(new Event(t, EventType.GUASTO, -1, i));
					}
				}
			}
			while(!this.queue.isEmpty()) {
				Event e = this.queue.poll();
				processEvent(e, i);
			}
		}
	}

	private void processEvent(Event e, int nwk) {
		WorkStation wk = this.linea.get(nwk);
		switch (e.getType()) {
		case NUOVO_JOB:
			if(!wk.getIngressi().containsKey(e.getnProd())) {
				wk.getIngressi().put(e.getnProd(), e.getTempo());
			}
			if(e.getTempo()>=wk.getTime()) { //wk libera
				double te = this.getPoisson(wk.getT0()/60)*60; //this.getGaussian
				wk.setTime(e.getTempo()+te);
				wk.getUscite().put(e.getnProd(), wk.getTime());
				if(wk.getUscite().size()%wk.getNs()==0) { //necessario setUp
					double tsu = this.getPoisson(wk.getTs()/60)*60; //this.getGaussian
					wk.setTime(wk.getTime()+tsu);
				}
				if(this.linea.size()==nwk) {
					this.pezziCompletati++;
				}
			}else { //wk occupata, job da accodare
				this.queue.add(new Event(wk.getTime(), EventType.NUOVO_JOB, e.getnProd(), nwk));
			}
		break;
		case GUASTO:
			if(e.getTempo()>=wk.getTime()) { //il guasto si verifica quando la macchina è libera
				double tr = this.getPoisson(wk.getMr()/60)*60;
				wk.setTime(wk.getTime()+tr);
			}else { //il guasto si verifica quando la macchina è occupata
				boolean trovato = false;
				HashMap<Integer, Double> uscite = new HashMap<Integer, Double>(wk.getUscite());
				int ultimaUscita = uscite.size();
				//int i = 1;
				while(trovato==false) {
					if(uscite.get(ultimaUscita)<e.getTempo()) { //job uscito prima del guasto?
						trovato=true;
					}else {
						ultimaUscita--;
					}
				}
				double tr = this.getPoisson(wk.getMr()/60)*60;
				for(int i=ultimaUscita+1; i<=wk.getUscite().size(); i++) {
					wk.getUscite().replace(i, wk.getUscite().get(i)+tr);
				}
				wk.setTime(wk.getUscite().get(wk.getUscite().size()));
			}
		break;
		}
	}
	
	public void calcolaPerformance() {
		HashMap<Integer, Double> uscite = new HashMap<Integer, Double>();
		uscite = this.linea.get(this.linea.size()).getUscite();
		double ultimaUscita = uscite.get(uscite.size());
		this.TH = this.pezziCompletati/(ultimaUscita/3600);
		HashMap<Integer, Double> ingressi = new HashMap<Integer, Double>();
		ingressi = this.linea.get(1).getIngressi();
		for(int i=1; i<=this.pezziCompletati; i++) {
			this.CT = this.CT + uscite.get(i)-ingressi.get(i);
		}
		this.CT = (this.CT/3600)/this.pezziCompletati;
		this.WIP = this.CT * this.TH;
		System.out.println("TH: "+this.TH+"\nCT: "+this.CT+"\nWIP: "+this.WIP+"\n");
		double rb = Double.MAX_VALUE;
		double T0 = 0;
		for(WorkStation wk : this.linea.values()) {
			double c = 1/(wk.getT0()/3600);
			if(c<rb) {
				rb = c;
			}
			T0 = T0 + wk.getT0()/3600;
		}
		this.rb = rb;
		this.T0 = T0;
		this.W0 = rb * T0;
		System.out.println("rb: "+this.rb+"\nT0: "+this.T0+"\nW0: "+this.W0);
	}
	
	public double getTH(){
		return Math.round(this.TH*1000.0)/1000.0;
	}
	
	public double getWIP(){
		return Math.round(this.WIP*1000.0)/1000.0;
	}
	
	public double getWipInt() {
		return Math.ceil(this.WIP);
	}
	
	public double getCT() {
		return Math.round(this.CT*1000.0)/1000.0;
	}
	
	public ArrayList<Analisi> getBestCase(){
		System.out.println("\nBEST CASE:\n");
		ArrayList <Analisi> bestCase = new ArrayList <Analisi>();
		for(int w=1; w<=20; w++) {
			double th;
			double ct;
			if(w<=W0) {
				th = w/this.T0;
				ct = this.T0;
			}else {
				th = this.rb;
				ct = w/this.rb;
			}
			bestCase.add(new Analisi(th, ct, w));
			System.out.println(bestCase.get(w-1));
		}
		return bestCase;
	}
	
	public ArrayList<Analisi> getWorstCase(){
		System.out.println("\nWORST CASE:\n");
		ArrayList <Analisi> worstCase = new ArrayList <Analisi>();
		for(int w=1; w<=20; w++) {
			double th = 1/this.T0;
			double ct = w*this.T0;
			worstCase.add(new Analisi(th, ct, w));
			System.out.println(worstCase.get(w-1));
		}
		return worstCase;
	}
	
	public ArrayList<Analisi> getPracticalWorstCase(){
		System.out.println("\nPTRACTICAL WORST CASE:\n");
		ArrayList <Analisi> practWorstCase = new ArrayList <Analisi>();
		for(int w=1; w<=20; w++) {
			double th = (w/(this.W0+w-1))*this.rb;
			double ct = this.T0+(w-1)/this.rb;
			practWorstCase.add(new Analisi(th, ct, w));
			System.out.println(practWorstCase.get(w-1));
		}
		return practWorstCase;
	}

	public ArrayList<String> getSpecifiche() {
		return specifiche;
	}
	
}