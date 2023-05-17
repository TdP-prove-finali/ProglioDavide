package it.polito.tdp.tesi.model;

public class Domanda {
	
	private int mese;
	private int qta;
	
	public Domanda(int mese, int qta) {
		super();
		this.mese = mese;
		this.qta = qta;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	public int getQta() {
		return qta;
	}

	public void setQta(int qta) {
		this.qta = qta;
	}

	@Override
	public String toString() {
		return "Mese: " + mese + ", domanda:" + qta;
	}
	
}
