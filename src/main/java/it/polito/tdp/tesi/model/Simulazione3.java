package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import it.polito.tdp.tesi.model.Event.EventType;

public class Simulazione3 {
	
	private HashMap<Integer, WorkStation> linea = new HashMap<Integer, WorkStation>();
	private ArrayList<String> specifiche = new ArrayList<String>();
	private double tA;
	private int pezziDaProdurre;
	
	private double TH = 0;
	private double CT = 0;
	private double WIP = 0;
	private double rb;
	private double T0;
	private double W0;
	private double tempoImpiegato;
	
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();
	private int pezziCompletati = 0;
	
	/*
	private HashMap<Integer, Integer> codaOg = new HashMap<Integer, Integer>();
	*/
	
	public void scegliLinea(int scelta) {
		switch (scelta) {
			case 1:
				this.tA = 3600;
				this.specifiche.add("tempo interarrivo medio della linea: "+Math.round(this.tA/3600*100.0)/100.0
						+" h\nnumero workstation: "+2+"\n");
				this.linea.put(1, new WorkStation(/*3600, 4318.5,*/ 2000, 2000/2, 28800, 28800/2, 3000, 3000/2,
					10, 1200, 1200/3));
				this.linea.put(2, new WorkStation(/*3600, 4318.5,*/ 1800, 1800/2, 28800, 28800/2, 2500, 2500/2,
					30, 1200, 1200/3));
				for(int i=1; i<=this.linea.size(); i++) {
					this.specifiche.add("WORKSTATION "+i+"\n"+this.linea.get(i).toString());
				}
			break;
			case 2:
				this.tA = 20000;
				this.specifiche.add("tempo interarrivo medio della linea: "+Math.round(this.tA/3600*100.0)/100.0
						+" h\nnumero workstation: "+2+"\n");
				this.linea.put(1, new WorkStation(/*20000, 4318.5,*/ 10000, 2700, 98000, 98000/2, 15000, 15000/3,
					10, 10000, 2400));
				this.linea.put(2, new WorkStation(/*30000, 4318.5,*/ 10000, 2700, 128800, 128800/2, 15000, 15000/3,
						10, 10000, 2400));
				for(int i=1; i<=this.linea.size(); i++) {
					this.specifiche.add("WORKSTATION "+i+"\n"+this.linea.get(i).toString());
				}
			break;
			case 3:
				this.tA = 7000;
				this.specifiche.add("tempo interarrivo medio della linea: "+Math.round(this.tA/3600*100.0)/100.0
						+" h\nnumero workstation: "+3+"\n");
				this.linea.put(1, new WorkStation(/*7000, 4318.5,*/ 3000, 3000/2, 150000, 150000/2, 7000, 7000/3,
					25, 2000, 2000/3));
				this.linea.put(2, new WorkStation(/*7000, 4318.5,*/ 5000, 5000/2, 130000, 130000/2, 4000, 4000/2,
						10, 5000, 5000/3));
				this.linea.put(3, new WorkStation(/*7000, 4318.5,*/ 6000, 6000/2, 100000, 100000/2, 5000, 5000/3,
						17, 5000, 5000/3));
				for(int i=1; i<=this.linea.size(); i++) {
					this.specifiche.add("WORKSTATION "+i+"\n"+this.linea.get(i).toString());
				}
			break;
		}
	}

	public void caricaLinea(int pezziDaProdurre) {
		this.pezziDaProdurre = pezziDaProdurre;
		this.CT=0;
		this.TH=0;
		this.WIP=0;
		this.rb=0;
		this.T0=0;
		this.W0=0;
		this.queue = new PriorityQueue<Event>();
		this.pezziCompletati=0;
		this.tempoImpiegato=0;
		for(int i=1; i<this.linea.size()+1; i++) {
			this.linea.get(i).getIngressi().clear();
			this.linea.get(i).getUscite().clear();
			this.linea.get(i).setTime(0);
		}
		WorkStation wk = this.linea.get(1);
		/*int nProd = 1;
		for(int i=0; i<300000; i=i+this.getPoisson(wk.gettA()/60)*60) { 
			this.queue.add(new Event(i, EventType.NUOVO_JOB, nProd, 1));
			//this.codaOg.put(nProd, i);
			nProd++;
		}*/
		PoissonDistribution pTa = new PoissonDistribution(this.tA);
		int t = 0;
		for(int nProd=1; nProd<=this.pezziDaProdurre; nProd++) {
			this.queue.add(new Event(t, EventType.NUOVO_JOB, nProd, 1));
			if(nProd!=this.pezziDaProdurre) {
				//t = t + this.getPoisson(this.tA/60)*60; //ORIGINALE
				t = t + (int)pTa.sample(); //NUOVA VERSIONE
			}
		}
		//NUOVA VERSIONE
		PoissonDistribution pMf = new PoissonDistribution(wk.getMf());
		for(int i=0; i<t+wk.getT0(); i=i+(int)pMf.sample()) {
			if(i>0) {
				this.queue.add(new Event(i, EventType.GUASTO, -1, 1));
			}
		}
		//ORIGINALE
		/*for(int i=0; i<t+wk.getT0(); i=i+this.getPoisson(wk.getMf()/60)*60) {
			if(i>0) {
				this.queue.add(new Event(i, EventType.GUASTO, -1, 1));
			}
		}*/
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
				double lastExit = wk.getUscite().get(wk.getUscite().size());
				for(Integer nProd : wk.getUscite().keySet()) {
					this.queue.add(new Event(wk.getUscite().get(nProd), 
							EventType.NUOVO_JOB, nProd, i));
				}
				//NUOVA VERSIONE
				WorkStation nextWk = this.linea.get(i);
				PoissonDistribution pMf = new PoissonDistribution(nextWk.getMf());
				for(double t=wk.getUscite().get(1); t<lastExit+nextWk.getT0(); 
						t=t+(int)pMf.sample()) {
					if(t>wk.getUscite().get(1)) {
						this.queue.add(new Event(t, EventType.GUASTO, -1, i));
					}
				}
				//ORIGINALE
				/*for(double t=wk.getUscite().get(1); t<lastExit+this.linea.get(i).getT0(); 
						t=t+this.getPoisson(this.linea.get(i).getMf()/60)*60) {
					if(t>wk.getUscite().get(1)) {
						this.queue.add(new Event(t, EventType.GUASTO, -1, i));
					}
				}*/
			}
			//int prova = 0;
			//while(!this.queue.isEmpty()) {
			while(this.linea.get(i).getUscite().size()!=this.pezziDaProdurre) {
				Event e = this.queue.poll();
				processEvent(e, i);
			}
			this.queue.clear(); //rimozione guasti non pervenuti
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
				/*
				if(e.getnProd()!=wk.getUscite().size()+1) {
					System.out.println("PROBLEMA: ultima uscita: "+wk.getUscite().size()+" nuova lavorazione: "+e.getnProd());
				}
				*/
				//NUOVA VERSIONE
				/*double alpha = -wk.getT0()*(wk.getT0()*(1-wk.getT0())/(Math.pow(wk.getStdv0(), 2)-1));
				double beta = (1-wk.getT0())*(wk.getT0()*(1-wk.getT0())/(Math.pow(wk.getStdv0(), 2)-1));*/
				BetaDistribution bTe = new BetaDistribution(3,3); //più aumentano alfa e beta piu probibilta di estrarre il valore atteso
				double te = (int)((wk.getT0()-wk.getStdv0())+((wk.getT0()+wk.getStdv0())-(wk.getT0()-wk.getStdv0()))*bTe.sample());
				//ORIGINALE
				//double te = this.getPoisson(wk.getT0()/60)*60; //usare distribuzione beta
				//
				wk.setTime(e.getTempo()+te);
				wk.getUscite().put(e.getnProd(), wk.getTime());
				if(wk.getUscite().size()%wk.getNs()==0) { //necessario setUp
					//NUOVA VERSIONE
					BetaDistribution bSu = new BetaDistribution(3,3);
					double tsu = (int)((wk.getTs()-wk.getStdvS())+((wk.getTs()+wk.getStdvS())-(wk.getTs()-wk.getStdvS()))*bSu.sample());
					//ORIGINALE
					//double tsu = this.getPoisson(wk.getTs()/60)*60; //usare un'altra distribuzione di prob
					//
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
				while(trovato==false) {
					if(uscite.get(ultimaUscita)<=e.getTempo()) { //job uscito prima del guasto?
						trovato=true;
					}else {
						ultimaUscita--;
					}
				}
				//NUOVA VERSIONE
				BetaDistribution bMr = new BetaDistribution(3,3);
				double tr = (int)((wk.getMr()-wk.getStdvR())+((wk.getMr()+wk.getStdvR())-(wk.getMr()-wk.getStdvR()))*bMr.sample());
				//ORIGINALE
				//double tr = this.getPoisson(wk.getMr()/60)*60;
				//
				for(int i=ultimaUscita+1; i<=wk.getUscite().size(); i++) {
					wk.getUscite().replace(i, wk.getUscite().get(i)+tr);
				}
				if(wk.getUscite().get(wk.getUscite().size())>wk.getTime()) { 
					wk.setTime(wk.getUscite().get(wk.getUscite().size())); 
				}
			}
		break;
		}
	}
	
	public void calcolaPerformance() {
		HashMap<Integer, Double> uscite = new HashMap<Integer, Double>();
		uscite = this.linea.get(this.linea.size()).getUscite();
		double ultimaUscita = uscite.get(uscite.size());
		this.tempoImpiegato = ultimaUscita/3600;
		this.TH = this.pezziCompletati/(ultimaUscita/3600);
		HashMap<Integer, Double> ingressi = new HashMap<Integer, Double>();
		ingressi = this.linea.get(1).getIngressi();
		for(int i=1; i<=this.pezziCompletati; i++) {
			this.CT = this.CT + uscite.get(i)-ingressi.get(i);
		}
		this.CT = (this.CT/3600)/this.pezziCompletati;
		this.WIP = this.CT * this.TH;
		System.out.println("Tempo impiegato: "+this.tempoImpiegato+" h, che equivalgono a "+this.tempoImpiegato/24+" giorni");
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
	
	public double getCT() {
		return Math.round(this.CT*1000.0)/1000.0;
	}
	
	public double getTempoImpiegato() {
		return Math.round(this.tempoImpiegato*100.0)/100.0;
	}
	
	public double getTempoImpiegatoG() {
		return Math.round((this.tempoImpiegato/24)*100.0)/100.0;
	}
	
	/*public ArrayList<Analisi> getBestCase(){
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
	}*/
	
	/*public ArrayList<Analisi> getWorstCase(){
		System.out.println("\nWORST CASE:\n");
		ArrayList <Analisi> worstCase = new ArrayList <Analisi>();
		for(int w=1; w<=20; w++) {
			double th = 1/this.T0;
			double ct = w*this.T0;
			worstCase.add(new Analisi(th, ct, w));
			System.out.println(worstCase.get(w-1));
		}
		return worstCase;
	}*/
	
	/*public ArrayList<Analisi> getPracticalWorstCase(){
		System.out.println("\nPTRACTICAL WORST CASE:\n");
		ArrayList <Analisi> practWorstCase = new ArrayList <Analisi>();
		for(int w=1; w<=20; w++) {
			double th = (w/(this.W0+w-1))*this.rb;
			double ct = this.T0+(w-1)/this.rb;
			practWorstCase.add(new Analisi(th, ct, w));
			System.out.println(practWorstCase.get(w-1));
		}
		return practWorstCase;
	}*/

	public ArrayList<String> getSpecifiche() {
		return specifiche;
	}
	
}