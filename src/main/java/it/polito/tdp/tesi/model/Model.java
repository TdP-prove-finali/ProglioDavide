package it.polito.tdp.tesi.model;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.tdp.tesi.db.ordiniDAO;

public class Model {
	
	private ordiniDAO ordiniDAO;
	//private LotSizing2 lotSizing;
	private LotSizing3 lS;
	
	private Simulazione3 sim;
	
	public Model() {
		this.ordiniDAO = new ordiniDAO();
		//this.lotSizing = new LotSizing2();
		this.lS = new LotSizing3();
		this.sim = new Simulazione3();
	}
	
	public ArrayList<Integer> getAnni(){
		return this.ordiniDAO.getYears();
	}
	
	public ArrayList<String> getProdByYear(int anno){
		return this.ordiniDAO.getProdByYear(anno);
	}
	
	/*public ArrayList<Domanda> getDomandaMese(String idProdotto, int anno){
		ArrayList<Domanda> domanda = new ArrayList<Domanda>();
		HashMap<Integer, Integer> qta = new HashMap<Integer, Integer>();
		qta = this.ordiniDAO.getQtaProdottoMese(idProdotto, anno);
		for(int mese : qta.keySet()) {
			domanda.add(new Domanda(mese, qta.get(mese)));
		}
		return domanda;
	}
	
	public HashMap<Integer,Integer> getDomandaMese2(String idProdotto, int anno){
		return this.ordiniDAO.getQtaProdottoMese(idProdotto, anno);
	}*/
	
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
	
	public void simula() {
		//this.sim = new Simulazione3();
		this.sim.caricaLinea();
		this.sim.run();
		this.sim.calcolaPerformance();
	}
	
	public double getTH() {
		return this.sim.getTH();
	}
	
	public double getWIP() {
		return this.sim.getWIP();
	}
	
	public int getWipInt() {
		return (int) this.sim.getWipInt();
	}
	
	public double getCT() {
		return this.sim.getCT();
	}
	
	public ArrayList<Analisi> getBestCase() {
		return this.sim.getBestCase();
	}
	
	public ArrayList<Analisi> getWorstCase() {
		return this.sim.getWorstCase();
	}
	
	public ArrayList<Analisi> getPracticalWorstCase() {
		return this.sim.getPracticalWorstCase();
	}
	
	/*public ArrayList<Produzione> lotSizing(HashMap<Integer, Integer> ordiniMese, int capacitaOrd, int capacitaStraord,
			double costoProdOrd, double costoProdStraord, double costoProdEsterna, double costoStoccaggio) {
		HashMap<Integer, Produzione> schedOttimaMap = new HashMap<Integer, Produzione>();
		schedOttimaMap = this.lotSizing.lotSizing(ordiniMese, capacitaOrd, capacitaStraord, 
				costoProdOrd, costoProdStraord, costoProdEsterna, costoStoccaggio);
		ArrayList<Produzione> schedOttima = new ArrayList<Produzione>();
		for(int mese : schedOttimaMap.keySet()) {
			schedOttima.add(schedOttimaMap.get(mese));
		}
		return schedOttima;
	}*/
}
