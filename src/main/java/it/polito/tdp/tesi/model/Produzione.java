package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Produzione {
	
	private int mese;
	private int qtaProdOrd;
	private int qtaProdStraord;
	private int qtaProdEst;
	
	public Produzione(int mese, int qtaProdOrd, int qtaProdStraord, int qtaProdEst) {
		super();
		this.mese = mese;
		this.qtaProdOrd = qtaProdOrd;
		this.qtaProdStraord = qtaProdStraord;
		this.qtaProdEst = qtaProdEst;
	}

	public Produzione() {
		// TODO Auto-generated constructor stub
	}

	public int getQtaProdOrd() {
		return qtaProdOrd;
	}

	public void setQtaProdOrd(int qtaProdOrd) {
		this.qtaProdOrd = qtaProdOrd;
	}

	public int getQtaProdStraord() {
		return qtaProdStraord;
	}

	public void setQtaProdStraord(int qtaProdStraord) {
		this.qtaProdStraord = qtaProdStraord;
	}

	public int getQtaProdEst() {
		return qtaProdEst;
	}

	public void setQtaProdEst(int qtaProdEst) {
		this.qtaProdEst = qtaProdEst;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	@Override
	public String toString() {
		String s = "Mese: " + mese + ", qtaProdOrd: " + qtaProdOrd + ", qtaProdStraord: " + qtaProdStraord
				+ ", qtaProdEst: " + qtaProdEst;
		return s;
	}
	
}
