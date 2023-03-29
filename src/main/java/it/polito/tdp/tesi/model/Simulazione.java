package it.polito.tdp.tesi.model;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.PriorityQueue;

import it.polito.tdp.tesi.model.Event.EventType;

public class Simulazione {
	
	//PARAMETRI IN INPUT
	private int numeroWs;
	//private int dimBufferWs;
	private Duration tA = Duration.ofSeconds(3600);
	private Duration stdvA = Duration.ofSeconds(4318,5);
	private Duration t0 = Duration.ofSeconds(1800); //tempo di processo
	private Duration stdv0 = Duration.ofSeconds(2700); //deviazione standard tempo di processo
	//Fermi macchina non schedulati
	private Duration mf = Duration.ofSeconds(28800); //tempo medio tra guasti successivi 
	private Duration mr = Duration.ofSeconds(1800); //tempo medio di riparazione guasto
	private Duration stdvF = Duration.ofSeconds(3600);
	//Fermi macchina schedulati
	private int Ns = 40; //numero medio di jobs tra setups consecutivi
	//private Duration ms = Duration.ofSeconds(2500); //tempo medio tra set up successivi
	private Duration ts = Duration.ofSeconds(1200); //tempo medio di setup
	private Duration stdvS = Duration.ofSeconds(2400); //coefficiente di variazione del tempo di setup 
	
	//PARAMETRI IN OUTPUT
	private double TH; //throughput 
	private double CT; //tempo ciclo
	private double WIP; //work in progress
	
	//STATO DEL MONDO
	private HashMap<Integer, Duration> jobs = new HashMap<Integer, Duration>();
	private int lastJob;
	private int w; //wip nel sistema
	private Duration ct = Duration.ofSeconds(0);
	private LocalTime time;
	private int pezziCompletati;
	private HashMap<Integer, Integer> codaWs = new HashMap<Integer, Integer>();
	
	private PriorityQueue<Event> queue = new PriorityQueue<Event>();

	
	
	public Simulazione(int numeroWs /*int dimBufferWs, Duration tassoArrivo, Duration t0, Duration stdv, Duration mf,
			Duration mr, Duration ms, Duration ts*/) {
		super();
		this.numeroWs = numeroWs;
		/*this.dimBufferWs = dimBufferWs;
		this.tassoArrivo = tassoArrivo;
		this.t0 = t0;
		this.stdv = stdv;
		this.mf = mf;
		this.mr = mr;
		this.ms = ms;
		this.ts = ts;*/
	}
	
	public void caricaCoda() {
		int i = 0;
		LocalTime t = LocalTime.ofSecondOfDay(0);
		LocalTime tf = LocalTime.ofSecondOfDay(7200);
		/*while(t.isBefore(LocalTime.ofSecondOfDay(7200))) {
			if(t.getSecond()%this.tassoArrivo.getSeconds()==0) {
				this.queue.add(new Event(t, EventType.NUOVO_JOB));
			}else if(t.getSecond()%this.mf.getSeconds()==0){
				this.queue.add(new Event(t, EventType.GUASTO));
			}else if(t.getSecond()%this.ms.getSeconds()==0) {
				this.queue.add(new Event(t, EventType.SETUP));
			}
			t = t.plusSeconds(1);
		}*/
		int nProd = 1;
		//this.queue.add(new Event(LocalTime.ofSecondOfDay(10), EventType.SETUP, -1)); //inserire in base al numero di pezzi non tempo
		while(i<12*3600) {
			/*if(nProd%this.Ns==0 && nProd>0) {
				this.queue.add(new Event(LocalTime.ofSecondOfDay(i), EventType.SETUP, -1));
			}*/
			if(i%this.tA.toSeconds()==0) {
				LocalTime ti = LocalTime.ofSecondOfDay(i).plusSeconds((long) Math.random()*this.stdvA.toSeconds());
				this.queue.add(new Event(ti, EventType.NUOVO_JOB, nProd));
				nProd++;
			}
			if(i%this.mf.toSeconds()==0 && i!=0){
				this.queue.add(new Event(LocalTime.ofSecondOfDay(i), EventType.GUASTO, -1));
			}
			/*if(i%this.ms.toSeconds()==0 && i!=0) {
				this.queue.add(new Event(LocalTime.ofSecondOfDay(i), EventType.SETUP, -2));
			}*/
			i++;
		}
	}

	public void run() {
		this.w = 0;
		this.time = LocalTime.ofSecondOfDay(0);
		//this.time.plusSeconds(0);
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getType()) {
		case NUOVO_JOB:
			//this.ct = e.getTime();
			if(e.getTime().compareTo(this.time)>=0) {
				//this.w++;
				if(this.pezziCompletati%this.Ns==0 && this.pezziCompletati>0) {
					this.time = this.time.plusSeconds(ts.toSeconds()+(long) (Math.random()*this.stdvS.toSeconds()));
				}else {
					Duration te = Duration.ofSeconds(this.t0.toSeconds()+(long) (Math.random()*this.stdv0.toSeconds()));
					this.time = this.time.plusSeconds(te.getSeconds());
					//this.time.plusSeconds(te.toSeconds());
					//this.queue.add(new Event(this.time, EventType.PRODOTTO_FINITO, e.getnProd()));
					//this.ct.plusSeconds(te.toSeconds());
					if(this.codaWs.containsKey(e.getnProd())) {
						te = te.plus(Duration.ofSeconds(this.codaWs.get(e.getnProd())));
						this.codaWs.remove(e.getnProd());
					}
					this.ct = this.ct.plus(te);
					this.w--;
					if(this.time.toSecondOfDay()<=12*3600) {
						this.pezziCompletati++;
					}
					this.jobs.put(e.getnProd(), te);
					this.lastJob = e.getnProd();
				}
			}else {
				this.w++;
				int tempoInCoda = this.time.toSecondOfDay()-e.getTime().toSecondOfDay();
				if(!this.codaWs.containsKey(e.getnProd())) {
					this.codaWs.put(e.getnProd(), tempoInCoda);
				}else {
					this.codaWs.put(e.getnProd(), this.codaWs.get(e.getnProd())+tempoInCoda);
				}
				this.queue.add(new Event(this.time, EventType.NUOVO_JOB, e.getnProd()));
			}
			break;
		/*case SETUP:
			this.time = this.time.plusSeconds(ts.toSeconds()+(long) (Math.random()*this.stdvS.toSeconds()));
			break;*/
		case GUASTO:
			if(e.getTime().compareTo(this.time)>=0) {
				this.time = this.time.plusSeconds(mr.toSeconds()+(long) (Math.random()*this.stdvF.toSeconds()));
			}else {
				//this.w--;
				//this.pezziCompletati--;
				Duration te = Duration.ofSeconds(mr.toSeconds()+(long) (Math.random()*this.stdvF.toSeconds())+this.jobs.get(this.lastJob).toSeconds());
				this.time = this.time.plusSeconds(mr.toSeconds()+(long) (Math.random()*this.stdvF.toSeconds()));
				this.jobs.put(this.lastJob, te);
				//this.queue.add(new Event(this.time, EventType.NUOVO_JOB, this.lastJob));
				this.pezziCompletati++;
			}
			break;
		/*case PRODOTTO_FINITO:
			Duration te = Duration.ofSeconds(this.time.getSecond()-e.getTime().getSecond());
			if(this.codaWs.containsKey(e.getnProd())) {
				te = te.plus(Duration.ofSeconds(this.codaWs.get(e.getnProd())));
				this.codaWs.remove(e.getnProd());
			}
			this.ct = this.ct.plus(te);
			this.w--;
			this.pezziCompletati++;
			break;*/
		}
		
	}

	public Duration getMf() {
		return mf;
	}

	public void setMf(Duration mf) {
		this.mf = mf;
	}

	public void setNumeroWs(int numeroWs) {
		this.numeroWs = numeroWs;
	}

	/*public void setDimBufferWs(int dimBufferWs) {
		this.dimBufferWs = dimBufferWs;
	}*/

	public void setMr(Duration mr) {
		this.mr = mr;
	}

	public void setNs(int ns) {
		Ns = ns;
	}

	public void setTs(Duration ts) {
		this.ts = ts;
	}

	/*public void setCs(double cs) {
		this.cs = cs;
	}*/

	public double getTH() { //1 pz/h
		int tempoTot = this.time.toSecondOfDay();
		this.TH = (double) this.pezziCompletati/(tempoTot/3600);
		return this.TH;
	}

	public double getCT() { //1,786 h
		for(Duration d : this.jobs.values()) {
			this.CT = this.CT + d.toSeconds();
		}
		this.CT = (this.CT/3600)/this.pezziCompletati;
		/*int ctTot = (int) this.ct.getSeconds();
		double c = (double) ctTot/3600;
		this.CT = (double) c/this.pezziCompletati;*/
		return this.CT;
	}

	public double getWIP() { //1,786 pz
		return this.TH*this.CT;
	}

}
