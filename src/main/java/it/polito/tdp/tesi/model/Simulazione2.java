package it.polito.tdp.tesi.model;

import java.util.HashMap;
import java.util.PriorityQueue;
import it.polito.tdp.tesi.model.Event2.Event2Type;

public class Simulazione2 {
	
	//PARAMETRI IN INPUT
	private int numeroWs;
	private double tA = 3600; 
	private double stdvA = 4318.5; 
	private double t0 = 1800; //tempo di processo 
	private double stdv0 = 2700; //deviazione standard tempi di processo
	//Fermi macchina non schedulati
	private double mf = 28800; //tempo medio tra guasti successivi
	private double mr = 1800; //tempo medio di riparazione guasto 
	private double stdvR = 3600; //deviazione standard tempo di riparazione
	//Fermi macchina schedulati
	private int Ns = 300; //numero medio di jobs tra setups consecutivi 
	private double ts = 1200; //tempo medio di setup 
	private double stdvS = 2400; // deviazione standard tempo di setup 
	
	//PARAMETRI IN OUTPUT
	private double TH; //throughput 
	private double CT; //tempo ciclo
	private double WIP; //work in progress
	
	//STATO DEL MONDO
	private HashMap<Integer, Double> jobs = new HashMap<Integer, Double>();
	private int lastJob;
	private double time;
	private int pezziCompletati;
	private HashMap<Integer, Double> codaWs = new HashMap<Integer, Double>();
	
	//CODA EVENTI
	private PriorityQueue<Event2> queue = new PriorityQueue<Event2>();
	
	public Simulazione2(int nws) {
		this.numeroWs = nws;
		this.TH = 0;
		this.CT = 0;
		this.WIP = 0;
	}

	public void caricaCoda() {
		int i = 0;
		int nProd = 1;
		while(i<300*12*3600) {
			if(i%this.tA==0) {
				double ti = i + Math.random()*this.stdvA;
				this.queue.add(new Event2(ti, Event2Type.NUOVO_JOB , nProd));
				nProd++;
			}
			if(i%this.mf==0 && i!=0) {
				this.queue.add(new Event2(i, Event2Type.GUASTO, -1));
			}
			i++;
		}
	}
	
	public void run() {
		this.time = 0;
		while(!this.queue.isEmpty()) {
			Event2 e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event2 e) {
		switch(e.getType()) {
		case NUOVO_JOB:
			if(e.getTempo()>=this.time) {
				double te = this.t0 + Math.random()*this.stdv0;
				this.time = e.getTempo()+te;
				if(this.codaWs.containsKey(e.getnProd())) {
					te = te + this.codaWs.get(e.getnProd());
				}
				this.pezziCompletati++;
				if(this.pezziCompletati%this.Ns==0 && this.pezziCompletati!=0) {
					this.time = this.time + this.ts + Math.random()*this.stdvS;
				}
				this.jobs.put(e.getnProd(), te);
				this.lastJob = e.getnProd();
			}else {
				double tempoInCoda = this.time - e.getTempo();
				if(!this.codaWs.containsKey(e.getnProd())) {
					this.codaWs.put(e.getnProd(), tempoInCoda);
				}else {
					this.codaWs.put(e.getnProd(), this.codaWs.get(e.getnProd())+tempoInCoda);
				}
				this.queue.add(new Event2(this.time, Event2Type.NUOVO_JOB, e.getnProd()));
			}
			break;
		case GUASTO:
			if(e.getTempo()>=this.time) {
				this.time = e.getTempo() + this.mr + Math.random()*this.stdvR;
			}else {
				double te = this.mr + Math.random()*this.stdvR;
				this.time = this.time + te;
				this.jobs.put(this.lastJob, te+this.jobs.get(this.lastJob));
			}
			break;
		}
	}

	public double getTH() {
		this.TH = this.pezziCompletati/(this.time/3600);
		return TH;
	}

	public double getCT() {
		for(double t : this.jobs.values()) {
			this.CT = this.CT + t;
		}
		this.CT = (this.CT/3600)/this.pezziCompletati;
		return CT;
	}

	public double getWIP() {
		this.WIP = this.TH * this.CT;
		return WIP;
	}
	
	
}
