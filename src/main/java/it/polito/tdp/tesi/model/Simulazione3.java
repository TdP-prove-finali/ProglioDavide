package it.polito.tdp.tesi.model;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

import it.polito.tdp.tesi.model.Event2.Event2Type;

public class Simulazione3 {

	//PARAMETRI IN INPUT
	private int numeroWs;
	private double tA = 4500; //3600
	private double stdvA = 4500; //4318.5
	private double t0 = 3480; //tempo di processo 1800
	private double stdv0 = 2009; //2700
		
	//PARAMETRI IN OUTPUT
	private double TH; //throughput 
	private double CT; //tempo ciclo
	private double WIP; //work in progress
	private double CTq;

	//STATO DEL MONDO
	private HashMap<Integer, Double> jobs = new HashMap<Integer, Double>();
	private int lastJob;
	private double time;
	private int pezziCompletati;
	private HashMap<Integer, Double> codaWs = new HashMap<Integer, Double>();
		
	//CODA EVENTI
	private PriorityQueue<Event2> queue = new PriorityQueue<Event2>();
	
	public Simulazione3(int nws) {
		this.numeroWs = nws;
		this.TH = 0;
		this.CT = 0;
		this.WIP = 0;
		this.CTq = 0;
		this.lastJob = 0;
		this.pezziCompletati = 0;
	}

	public void caricaCoda() {
		int i = 0;
		int nProd = 1;
		this.queue.add(new Event2(0, Event2Type.NUOVO_JOB , nProd));
		nProd++;
		while(i<600*12*3600) {
			Random r = new Random();
			double d = r.nextGaussian();
			d = d*this.tA;
			if(i%this.tA==0 && i>0) {
				/*double ti;
				if(Math.random()*((100+0)+1)>=50) {
					ti = i + Math.random()*((this.stdvA+0)+1);
				}else {
					ti = i - Math.random()*((this.stdvA+0)+1);
				}*/
				double ti = i + Math.random()*this.stdvA;
				this.queue.add(new Event2(ti, Event2Type.NUOVO_JOB , nProd));
				nProd++;
			}
			/*if(i%this.mf==0 && i!=0) {
				this.queue.add(new Event2(i, Event2Type.GUASTO, -1));
			}*/
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
			if(!wkOccupata(e.getTempo())) {  //e.getTempo()>=this.time
				/*if(this.pezziCompletati%this.Ns==0 && this.pezziCompletati!=0) {
					this.time = this.time + this.ts + Math.random()*this.stdvS;
				}else {*/
				//double te;
				/*if(Math.random()*((100+0)+1)>=50) {
					te = this.t0 + Math.random()*((this.stdv0+0)+1);
				}else {
					te = this.t0 - Math.random()*((this.stdv0+0)+1);
				}*/
				double te = this.t0 + Math.random()*this.stdv0;
				this.time = e.getTempo()+te;
				if(this.codaWs.containsKey(e.getnProd())) {
					te = te + this.codaWs.get(e.getnProd());
				}
				this.pezziCompletati++;
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
		/*case GUASTO:
			if(e.getTempo()>=this.time) {
				//this.time = this.time + this.mr + Math.random()*this.stdvF;
				this.time = e.getTempo() + this.mr + Math.random()*this.stdvF;
			}else {
				double te = this.mr + Math.random()*this.stdvF;
				//this.time = this.time + te;
				this.time = e.getTempo() + te;
				this.jobs.put(this.lastJob, te+this.jobs.get(this.lastJob));
				this.pezziCompletati++;
			}
			break;*/
		}
	}
	
	public boolean wkOccupata(double t) {
		if(t>=this.time) {
			return false;
		}else {
			return true;
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

	public double getCTq() {
		int pezziCoda = 0;
		for(double t : this.codaWs.values()) {
			this.CTq = this.CTq + t;
			pezziCoda++;
		}
		this.CTq = (this.CTq/3600)/pezziCoda;
		return CTq;
	}
		
}
