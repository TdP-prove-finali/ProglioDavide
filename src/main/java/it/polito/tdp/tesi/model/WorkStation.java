package it.polito.tdp.tesi.model;

import java.util.HashMap;

public class WorkStation {
	
	private double t0; //tempo medio di processo 
	private double stdv0; //variabilità tempi di processo
	//Fermi macchina non schedulati
	private double mf; //tempo medio tra guasti successivi
	private double stdvF; //variabilità tempo tra guasti successivi
	private double mr; //tempo medio di riparazione guasto 
	private double stdvR; //variabilità tempo di riparazione
	//Fermi macchina schedulati
	private int Ns; //numero medio di jobs tra setups consecutivi 
	private double ts; //tempo medio di setup 
	private double stdvS; //variabilità tempo di setup
	
	private double time;
	private HashMap<Integer, Double> ingressi;
	private HashMap<Integer, Double> uscite;
	
	public WorkStation(double t0, double stdv0,
			double mf, double stdvF, double mr, double stdvR, int ns, double ts, double stdvS) {
		super();
		this.t0 = t0;
		this.stdv0 = stdv0;
		this.mf = mf;
		this.stdvF = stdvF;
		this.mr = mr;
		this.stdvR = stdvR;
		Ns = ns;
		this.ts = ts;
		this.stdvS = stdvS;
		this.time=0;
		this.ingressi = new HashMap<Integer, Double>();
		this.uscite = new HashMap<Integer, Double>();
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public WorkStation() {
		
	}

	public double getT0() {
		return t0;
	}

	public void setT0(double t0) {
		this.t0 = t0;
	}

	public double getStdv0() {
		return stdv0;
	}

	public void setStdv0(double stdv0) {
		this.stdv0 = stdv0;
	}

	public double getMf() {
		return mf;
	}

	public void setMf(double mf) {
		this.mf = mf;
	}

	public double getStdvF() {
		return stdvF;
	}

	public void setStdvF(double stdvF) {
		this.stdvF = stdvF;
	}

	public double getMr() {
		return mr;
	}

	public void setMr(double mr) {
		this.mr = mr;
	}

	public double getStdvR() {
		return stdvR;
	}

	public void setStdvR(double stdvR) {
		this.stdvR = stdvR;
	}

	public int getNs() {
		return Ns;
	}

	public void setNs(int ns) {
		Ns = ns;
	}

	public double getTs() {
		return ts;
	}

	public void setTs(double ts) {
		this.ts = ts;
	}

	public double getStdvS() {
		return stdvS;
	}

	public void setStdvS(double stdvS) {
		this.stdvS = stdvS;
	}
	
	public HashMap<Integer, Double> getIngressi() {
		return ingressi;
	}

	public HashMap<Integer, Double> getUscite() {
		return uscite;
	}

	@Override
	public String toString() {
		return "tempo medio di processo: " + Math.round(t0/60*100.0)/100.0 +
				" min\ntempo medio tra guasti successivi: " + Math.round(mf/3600*100.0)/100.0 + 
				" h\ntempo medio di riparazione guasto: " + Math.round(mr/3600*100.0)/100.0 + 
				" h\nnumero di jobs tra setups consecutivi: " + Ns + 
				" jobs\ntempo medio di setup: " + Math.round(ts/3600*100.0)/100.0 + " h\n"; 
	}
	
}
