package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import it.polito.tdp.tesi.model.Event.EventType;

public class Simulazione {
	
	//PARAMETRI IN INPUT
	private ArrayList<WorkStation> linea = new ArrayList<WorkStation>();
	
	//PARAMETRI IN OUTPUT
	private double TH; //throughput 
	private double CT; //tempo ciclo
	private double WIP; //work in progress
	
	//STATO DEL MONDO
	private HashMap<Integer, Double> jobs = new HashMap<Integer, Double>();
	//private int lastJob;
	//private double time;
	private int pezziCompletati;
	//private HashMap<Integer, Double> codaWs = new HashMap<Integer, Double>();
	
	//CODA EVENTI
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();
	
	public void Simulazione2(int nws) {
		creaLinea(nws);
		//this.numeroWs = nws;
		this.TH = 0;
		this.CT = 0;
		this.WIP = 0;
	}

	public void creaLinea(int nws) {
		for(int i=0; i<nws; i++) {
			this.linea.add(new WorkStation(3600, 4318.5, 1800, 2700, 28800, 1800, 3600,
					300, 1200, 2400));
		}
		
	}

	public void caricaCoda() {
		int i = 0;
		int nProd = 1;
		WorkStation wki = this.linea.get(this.linea.size()-1);
		while(i<300*12*3600) {
			if(i%wki.gettA()==0) {
				double ti = i + Math.random()*wki.getStdvA();
				this.queue.add(new Event(ti, EventType.NUOVO_JOB , nProd, 1));
				this.linea.get(this.linea.size()-1).getIngressi().put(nProd, ti);
				nProd++;
			}
			if(i%wki.getMf()==0 && i!=0) {
				this.queue.add(new Event(i, EventType.GUASTO, -1, 1));
			}
			i++;
		}
	}
	
	public void run() {
		//this.time = 0;
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		WorkStation wk = new WorkStation();
		wk = this.linea.get(this.linea.size()-e.getWk());
		switch(e.getType()) {
		case NUOVO_JOB:
			if(e.getTempo()>=wk.getTime()) {
				double te = wk.getT0() + Math.random()*wk.getStdv0();
				wk.setTime(e.getTempo()+te);
				if(wk.getCoda().containsKey(e.getnProd())) { //forse creare coda come attributo della classe WorkStation
					te = te + wk.getCoda().get(e.getnProd());
				}
				if(this.linea.size()==e.getWk()) { //è l'ultima wk della linea
					this.pezziCompletati++;	
				}else { //in coda sulla wk successiva
					this.queue.add(new Event(wk.getTime(), EventType.NUOVO_JOB, e.getnProd(), e.getWk()+1));
					this.linea.get(e.getWk()-1).getIngressi().put(e.getnProd(), wk.getTime());
				}
				if(!this.jobs.containsKey(e.getnProd())) {
						this.jobs.put(e.getnProd(), te); 
					}else {
						this.jobs.replace(e.getnProd(), this.jobs.get(e.getnProd())+te);
					}
					wk.setLasJob(e.getnProd());
					wk.getUscite().put(e.getnProd(), wk.getTime());
				if(this.pezziCompletati%wk.getNs()==0 && this.pezziCompletati!=0) {
					wk.setTime(wk.getTime() + wk.getTs() + Math.random()*wk.getStdvS());
				}
			}else {
				double tempoInCoda = wk.getTime() - e.getTempo();
				if(!wk.getCoda().containsKey(e.getnProd())) {
					wk.getCoda().put(e.getnProd(), tempoInCoda);
				}else {
					wk.getCoda().put(e.getnProd(), wk.getCoda().get(e.getnProd())+tempoInCoda);
				}
				this.queue.add(new Event(wk.getTime(), EventType.NUOVO_JOB, e.getnProd(), e.getWk()));
			}
			break;
		case GUASTO:
			if(e.getTempo()>=wk.getTime()) {
				wk.setTime(e.getTempo() + wk.getMr() + Math.random()*wk.getStdvR());
			}else { //il guasto avviene mentre si sta producendo, si ripara la wk e si termina il job interrotto
				double te = wk.getMr() + Math.random()*wk.getStdvR();
				/*
				int ultimaUscita = wk.getUscite().size()-1;
				int i=wk.getUscite().size()-1;
				boolean trovato = false;
				while(i>0 && trovato==false){
					if(wk.getUscite().get(i)<=e.getTempo()) {
						trovato = true;
						ultimaUscita = i+1;
					}
				}
				if(ultimaUscita==wk.getUscite().size()) {
					this.jobs.replace(ultimaUscita, te+this.jobs.get(ultimaUscita));
					wk.setTime(wk.getTime() + te);
				}else {
					this.jobs.replace(ultimaUscita, te+this.jobs.get(ultimaUscita));
					wk.setTime(wk.getUscite().get(ultimaUscita)+te);
					for(int j=ultimaUscita+1; j<wk.getUscite().size(); j++) {
						this.queue.add(new Event(wk.getTime(), EventType.NUOVO_JOB, j, e.getWk()));
						this.jobs.remove(j);
					}
				}*/
				
				wk.setTime(wk.getTime() + te); //NON CANCELLARE
				if(!this.jobs.containsKey(wk.getLasJob())) {
					this.jobs.put(wk.getLasJob(), te+this.jobs.get(wk.getLasJob())); //te+this.jobs.get(wk.getLasJob())
				}else {
					this.jobs.replace(wk.getLasJob(), this.jobs.get(wk.getLasJob())+te); //this.jobs.get(wk.getLasJob())+te+this.jobs.get(wk.getLasJob())
				}
			}
			break;
		}
	}

	public double getTH() {
		this.TH = this.pezziCompletati/(this.linea.get(0).getTime()/3600);
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



