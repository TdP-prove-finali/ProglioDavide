package it.polito.tdp.tesi.model;

import java.util.ArrayList;

public class LotSizing3 {
	
	private int capacitaOrd;
	private int capacitaStraord;
	private double costoProdOrd;
	private double costoProdStraord;
	private double costoProdEsterna;
	private double costoStoccaggio;
	private ArrayList<Produzione> schedOttima;
	private double ctMin;
	private double ctTot;
	private ArrayList<Produzione> situazioneIniziale;
	
	public LotSizing3() {
	}
	
	public void lotSizing(ArrayList<Domanda> domanda, int capacitaOrd, int capacitaStraord, 
			double costoProdOrd, double costoProdStraord,
			double costoProdEsterna, double costoStoccaggio) {
		
		this.schedOttima = new ArrayList<Produzione>();
		this.ctTot = 0;
		this.situazioneIniziale = new ArrayList<Produzione>();
		this.capacitaOrd = capacitaOrd;
		this.capacitaStraord = capacitaStraord;
		this.costoProdOrd = costoProdOrd;
		this.costoProdStraord = costoProdStraord;
		this.costoProdEsterna = costoProdEsterna;
		this.costoStoccaggio = costoStoccaggio;
		
		for(Domanda d : domanda) {
			//System.out.println(d.toString());
			if(d.getQta()<=this.capacitaOrd) {
				this.situazioneIniziale.add(new Produzione(d.getMese(), d.getQta(), 0, 0));
				this.schedOttima.add(new Produzione(d.getMese(), d.getQta(), 0, 0));
				domanda.get(d.getMese()-1).setQta(0);
			}else if(d.getQta()<=this.capacitaOrd+this.capacitaStraord){
				this.situazioneIniziale.add(new Produzione(d.getMese(), this.capacitaOrd, 
						(d.getQta()-this.capacitaOrd), 0));
				this.schedOttima.add(new Produzione(d.getMese(), this.capacitaOrd, 
						(d.getQta()-this.capacitaOrd), 0));
				domanda.get(d.getMese()-1).setQta(0);
			}else {
				this.situazioneIniziale.add(new Produzione(d.getMese(), this.capacitaOrd, this.capacitaStraord, 0));
				this.schedOttima.add(new Produzione(d.getMese(), this.capacitaOrd, this.capacitaStraord, 0));
				domanda.get(d.getMese()-1).setQta(d.getQta()-(this.capacitaOrd+this.capacitaStraord));
			}
		}
		
		for(Domanda d : domanda) {
			if(d.getQta()>0) {
				ArrayList<Disposizione> parziale = new ArrayList<Disposizione>();
				int contaMesi = contaMesi();
				parziale.add(new Disposizione(this.schedOttima.get(d.getMese()-1).getQtaProdOrd(), "ord", d.getMese()));
				parziale.add(new Disposizione(this.schedOttima.get(d.getMese()-1).getQtaProdStraord(), "straord", d.getMese()));
				parziale.add(new Disposizione(this.schedOttima.get(d.getMese()-1).getQtaProdEst(), "est", d.getMese()));
				int i = 1;
				while(contaMesi>0) {
					if((d.getMese()-i)>=1) {  
						if(this.costoProdOrd+i*this.costoStoccaggio<this.costoProdEsterna) { //controllo anche se max capacita
							parziale.add(new Disposizione(this.schedOttima.get(d.getMese()-1-i).getQtaProdOrd(), "ord", d.getMese()-i));
						}
						if(this.costoProdStraord+i*this.costoStoccaggio<this.costoProdEsterna) {
							parziale.add(new Disposizione(this.schedOttima.get(d.getMese()-1-i).getQtaProdStraord(), "straord", d.getMese()-i));
						}
					}
					i++;
					contaMesi--;
				}
				this.ctTot = this.ctTot + calcolaCostoIniziale(this.situazioneIniziale.get(d.getMese()-1)); 
				this.ctMin = Double.MAX_VALUE;
				schedula(parziale,d,0);
				this.ctTot = this.ctTot+this.ctMin;
				aggiornaSituazioneIniziale();
			}else {
				this.ctTot = this.ctTot + calcolaCostoIniziale(this.schedOttima.get(d.getMese()-1));
			}
		}
		
		/*System.out.println();
		for(Produzione p : this.schedOttima) {
			System.out.println(p.toString());
		}
		System.out.println("Costo piano produttivo minimo: "+this.ctTot);*/
	}

	private void schedula(ArrayList<Disposizione> parziale, Domanda d, int slotProduttivo) {
		if(d.getQta()<=0) {
			double ct = calcolaCostoGestione(parziale, d.getMese());
			if(ct<this.ctMin) {
				for(Disposizione disp : parziale) {
					if(disp.getTipo().equals("ord")) {
						this.schedOttima.get(disp.getMese()-1).setQtaProdOrd(disp.getQta());
					}
					if(disp.getTipo().equals("straord")) {
						this.schedOttima.get(disp.getMese()-1).setQtaProdStraord(disp.getQta());
					}
					if(disp.getTipo().equals("est")) {
						this.schedOttima.get(disp.getMese()-1).setQtaProdEst(disp.getQta());
					}
				}
				this.ctMin = ct; 
			}
		}else {
			for(int i=slotProduttivo; i<parziale.size(); i++) {
				if(parziale.get(i).getQta()+1<=getCapacita(parziale.get(i).getTipo())) {  
					parziale.get(i).setQta(parziale.get(i).getQta()+1);
					d.setQta(d.getQta()-1);
					schedula(parziale, d, slotProduttivo);
					parziale.get(i).setQta(parziale.get(i).getQta()-1);
					d.setQta(d.getQta()+1);
					slotProduttivo = i+1;
				}
			}
		}
	}
	
	private double calcolaCostoGestione(ArrayList<Disposizione> parziale, int mese) {
		double ct = 0;
		Produzione prodIniziale = new Produzione();
		for(Disposizione d : parziale) {
			for(Produzione p : this.situazioneIniziale) { 
				if(p.getMese()==d.getMese()) {
					prodIniziale = p;
				}
			}
			if(d.getTipo().equals("ord")){
				ct = ct + (d.getQta()-prodIniziale.getQtaProdOrd())*this.costoProdOrd;
				ct = ct + (d.getQta()-prodIniziale.getQtaProdOrd())*(mese-d.getMese())*this.costoStoccaggio;
			}
			if(d.getTipo().equals("straord")){
				ct = ct + (d.getQta()-prodIniziale.getQtaProdStraord())*this.costoProdStraord;
				ct = ct + (d.getQta()-prodIniziale.getQtaProdStraord())*(mese-d.getMese())*this.costoStoccaggio;
			}
			if(d.getTipo().equals("est")) {
				ct = ct + (d.getQta()-prodIniziale.getQtaProdEst())*this.costoProdEsterna;
				ct = ct + (d.getQta()-prodIniziale.getQtaProdEst())*(mese-d.getMese())*this.costoStoccaggio;
			}
		}
		return ct;
	}

	private int getCapacita(String tipo) {
		if(tipo.equals("ord")) {
			return this.capacitaOrd;
		}else if(tipo.equals("straord")) {
			return this.capacitaStraord;
		}else {
			return Integer.MAX_VALUE;
		}
	}
	
	private void aggiornaSituazioneIniziale() {
		for(Produzione p : this.situazioneIniziale) {
			if(p.getQtaProdOrd()!=this.schedOttima.get(p.getMese()-1).getQtaProdOrd()) {
				this.situazioneIniziale.get(p.getMese()-1).setQtaProdOrd(this.schedOttima.get(p.getMese()-1).getQtaProdOrd());
			}
			if(p.getQtaProdStraord()!=this.schedOttima.get(p.getMese()-1).getQtaProdStraord()) {
				this.situazioneIniziale.get(p.getMese()-1).setQtaProdStraord(this.schedOttima.get(p.getMese()-1).getQtaProdStraord());
			}
			if(p.getQtaProdEst()!=this.schedOttima.get(p.getMese()-1).getQtaProdEst()) {
				this.situazioneIniziale.get(p.getMese()-1).setQtaProdEst(this.schedOttima.get(p.getMese()-1).getQtaProdEst());
			}
		}
	}

	private double calcolaCostoIniziale(Produzione p) {
		double ctIniziale = 0;
			ctIniziale = ctIniziale+
						 p.getQtaProdOrd()*this.costoProdOrd+
						 p.getQtaProdStraord()*this.costoProdStraord+
						 p.getQtaProdEst()*this.costoProdEsterna;
		return ctIniziale;
	}

	private Integer contaMesi() {
		int contaMesi = (int) Math.floor((this.costoProdEsterna-this.costoProdOrd)/this.costoStoccaggio);
		if(contaMesi*this.costoStoccaggio+this.costoProdOrd==this.costoProdEsterna) {
			contaMesi--; //a parita meglio produrre esternamente, non si occupa spazio
		}
		return contaMesi;
	}
	
	public ArrayList<Produzione> getSchedOttima(){
		return this.schedOttima;
	}
	
	public double getCostoTot() {
		return Math.round(this.ctTot*1000.0)/1000.0;
	}
}
