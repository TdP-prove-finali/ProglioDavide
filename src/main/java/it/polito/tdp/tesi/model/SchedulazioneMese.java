package it.polito.tdp.tesi.model;

import java.util.ArrayList;

public class SchedulazioneMese {
	
	private int mese;
	private ArrayList<Integer> qta;
	
	public SchedulazioneMese(int mese) {
		super();
		this.mese = mese;
		this.qta = new ArrayList<Integer>();
	}

	public SchedulazioneMese() {
		// TODO Auto-generated constructor stub
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public ArrayList<Integer> getQta() {
		return qta;
	}

	public void setQta(ArrayList<Integer> qta) {
		this.qta = qta;
	}

	@Override
	public String toString() {
		return "SchedulazioneMese mese " + mese + ": QtaOrd = " + qta.get(0) 
			+ "QtaStraord = " + qta.get(1) + "QtaOut = " + qta.get(2);
	}

	public void put(int i, int qtaProdOrd, int qtaProdStraord, int qtaProdEst) {
		this.mese = i;
		this.qta.add(0, qtaProdOrd);
		this.qta.add(1, qtaProdStraord);
		this.qta.add(2, qtaProdEst);
	}

}
