package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.tdp.tesi.db.ordiniDAO;

public class Model {
	
	private ordiniDAO ordiniDAO;
	private LotSizing3 lS;
	private Simulazione3 sim;
	
	public Model() {
		this.ordiniDAO = new ordiniDAO();
		this.lS = new LotSizing3();
		this.sim = new Simulazione3();
	}
	
	public ArrayList<Integer> getAnni(){
		return this.ordiniDAO.getYears();
	}
	
	public ArrayList<String> getProdByYear(int anno){
		return this.ordiniDAO.getProdByYear(anno);
	}
	
	public ArrayList<Domanda> getDomandaMensile(String idProdotto, int anno){
		return this.ordiniDAO.getDomandaMese(idProdotto, anno);
	}
	
	public ArrayList<Produzione> lotSizing(ArrayList<Domanda> ordiniMese, int capacitaOrd, int capacitaStraord,
			double costoProdOrd, double costoProdStraord, double costoProdEsterna, double costoStoccaggio){
		this.lS = new LotSizing3();
		this.lS.lotSizing(ordiniMese, capacitaOrd, capacitaStraord, costoProdOrd,
				costoProdStraord, costoProdEsterna, costoStoccaggio);
		return this.lS.getSchedOttima();
	}
	
	public double getCostoSchedOttima() {
		return this.lS.getCostoTot();
	}
	
	public double calcolaProfitto(double prezzo) {
		int totPezziProdotti = 0;
		for(Produzione p : this.lS.getSchedOttima()) {
			totPezziProdotti = totPezziProdotti + p.getQtaProdOrd() + p.getQtaProdStraord() + p.getQtaProdEst();
		}
		return totPezziProdotti*prezzo-this.lS.getCostoTot();
	}
	
	public ArrayList<String> scegliLinea(int scelta){
		this.sim = new Simulazione3();
		this.sim.scegliLinea(scelta);
		return this.sim.getSpecifiche();
	}
	
	public void resetLinea() {
		this.sim = new Simulazione3();
	}
	
	public void simula(int pezziDaProdurre) {
		this.sim.caricaLinea(pezziDaProdurre);
		this.sim.run();
		this.sim.calcolaPerformance();
	}
	
	public double getTH() {
		return this.sim.getTH();
	}
	
	public double getWIP() {
		return this.sim.getWIP();
	}
	
	public double getCT() {
		return this.sim.getCT();
	}
	
	public double getTempoImpiegato() {
		return this.sim.getTempoImpiegato();
	}
	
	public double getTempoImpiegatoG() {
		return this.sim.getTempoImpiegatoG();
	}

}
