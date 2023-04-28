package it.polito.tdp.tesi.model;

import java.util.HashMap;

public class WorkStation {
	
	private double tA; //tempo medio arrivo
	private double stdvA; //deviazione standard tempo di arrivo
	private double t0; //tempo di processo 
	private double stdv0; //deviazione standard tempi di processo
	//Fermi macchina non schedulati
	private double mf; //tempo medio tra guasti successivi
	private double mr; //tempo medio di riparazione guasto 
	private double stdvR; //deviazione standard tempo di riparazione
	//Fermi macchina schedulati
	private int Ns; //numero medio di jobs tra setups consecutivi 
	private double ts; //tempo medio di setup 
	private double stdvS; // deviazione standard tempo di setup
	
	private HashMap<Integer, Double> coda;
	private int lasJob;
	private double time;
	private HashMap<Integer, Double> ingressi;
	private HashMap<Integer, Double> uscite;
	//private HashMap<Integer, Double> jobs;
	
	public WorkStation(double tA, double stdvA, double t0, double stdv0,
			double mf, double mr, double stdvR, int ns, double ts, double stdvS) {
		super();
		this.tA = tA;
		this.stdvA = stdvA;
		this.t0 = t0;
		this.stdv0 = stdv0;
		this.mf = mf;
		this.mr = mr;
		this.stdvR = stdvR;
		Ns = ns;
		this.ts = ts;
		this.stdvS = stdvS;
		this.coda = new HashMap<Integer, Double>();
		//this.jobs = new HashMap<Integer, Double>();
		this.lasJob = 0;
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
		// TODO Auto-generated constructor stub
	}

	public double gettA() {
		return tA;
	}

	public void settA(double tA) {
		this.tA = tA;
	}

	public double getStdvA() {
		return stdvA;
	}

	public void setStdvA(double stdvA) {
		this.stdvA = stdvA;
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

	public HashMap<Integer, Double> getCoda() {
		return coda;
	}

	public int getLasJob() {
		return lasJob;
	}

	/*public HashMap<Integer, Double> getJobs() {
		return jobs;
	}*/

	public void setLasJob(int lasJob) {
		this.lasJob = lasJob;
	}
	
	public HashMap<Integer, Double> getIngressi() {
		return ingressi;
	}

	public HashMap<Integer, Double> getUscite() {
		return uscite;
	}

	@Override
	public String toString() {
		return "WorkStation tA=" + tA + ", stdvA=" + stdvA + ", t0=" + t0 + ", stdv0=" + stdv0 + ", mf=" + mf + ", mr="
				+ mr + ", stdvR=" + stdvR + ", Ns=" + Ns + ", ts=" + ts + ", stdvS=" + stdvS + ", coda=" + coda
				+ ", lasJob=" + lasJob + ", time=" + time + "\n";
	}
	
}
