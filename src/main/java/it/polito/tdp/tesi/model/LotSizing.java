package it.polito.tdp.tesi.model;

import java.util.ArrayList;

public class LotSizing {
	
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
	
	public LotSizing() {
		this.schedOttima = new ArrayList<Produzione>();
		this.ctTot = 0;
	}
	
	public void lotSizing(ArrayList<Domanda> domanda, int capacitaOrd, int capacitaStraord, double costoProdOrd, double costoProdStraord,
			double costoProdEsterna, double costoStoccaggio) {
		this.capacitaOrd = capacitaOrd;
		this.capacitaStraord = capacitaStraord;
		this.costoProdOrd = costoProdOrd;
		this.costoProdStraord = costoProdStraord;
		this.costoProdEsterna = costoProdEsterna;
		this.costoStoccaggio = costoStoccaggio;
		
		for(Domanda d : domanda) {
			System.out.println(d.toString());
			if(d.getQta()<=this.capacitaOrd) {
				this.schedOttima.add(new Produzione(d.getMese(), d.getQta(), 0, 0));
				domanda.get(d.getMese()-1).setQta(0);
			}else if(d.getQta()<=this.capacitaOrd+this.capacitaStraord){
				this.schedOttima.add(new Produzione(d.getMese(), this.capacitaOrd, 
						(d.getQta()-this.capacitaOrd), 0));
				domanda.get(d.getMese()-1).setQta(0);
			}else {
				this.schedOttima.add(new Produzione(d.getMese(), this.capacitaOrd, this.capacitaStraord, 0));
				domanda.get(d.getMese()-1).setQta(d.getQta()-(this.capacitaOrd+this.capacitaStraord));
			}
		}
		System.out.println();
		for(Domanda d : domanda) {
			System.out.println(d.toString());
		}
		System.out.println();
		for(Produzione p : this.schedOttima) {
			System.out.println(p.toString());
		}
		for(Domanda d : domanda) {
			if(d.getQta()>0) {
				ArrayList<Integer> parziale2 = new ArrayList<Integer>();
				ArrayList<Produzione> parziale = new ArrayList<Produzione>();
				int contaMesi = (int) Math.floor((this.costoProdEsterna-this.costoProdOrd)/this.costoStoccaggio);
				if(contaMesi*this.costoStoccaggio+this.costoProdOrd==this.costoProdEsterna) {
					contaMesi--; //a parita meglio produrre esternamente
				}
				parziale.add(this.schedOttima.get(d.getMese()-1));
				parziale2.add(this.schedOttima.get(d.getMese()-1).getQtaProdOrd());
				parziale2.add(this.schedOttima.get(d.getMese()-1).getQtaProdStraord());
				parziale2.add(this.schedOttima.get(d.getMese()-1).getQtaProdEst());
				int i = 1;
				while(contaMesi>0) {
					if((d.getMese()-i)>=1) {  
						parziale.add(this.schedOttima.get(d.getMese()-1-i)); //parziale.add(this.schedOttima.get(d.getMese()-1-contaMesi));
						parziale2.add(this.schedOttima.get(d.getMese()-1-i).getQtaProdOrd()); //parziale2.add(this.schedOttima.get(d.getMese()-1-contaMesi).getQtaProdOrd());
						parziale2.add(this.schedOttima.get(d.getMese()-1-i).getQtaProdStraord());
						parziale2.add(this.schedOttima.get(d.getMese()-1-i).getQtaProdEst());
						//}
					}
					i++;
					contaMesi--;
				}
				this.situazioneIniziale = new ArrayList<Produzione>(parziale);
				this.ctTot = this.ctTot + calcolaCostoIniziale(this.situazioneIniziale.get(0));
				this.ctMin = Double.MAX_VALUE;
				System.out.println();
				schedula(parziale2,d,0);
				this.ctTot = this.ctTot+this.ctMin;
			}else {
				this.ctTot = this.ctTot + calcolaCostoIniziale(this.schedOttima.get(d.getMese()-1));
			}
		}
		System.out.println();
		for(Produzione p : this.schedOttima) {
			System.out.println(p.toString());
		}
		System.out.println("Costo piano produttivo minimo: "+this.ctTot);
	}

	private void schedula(ArrayList<Integer> parziale2, Domanda d, int tipoProd) {
		if(d.getQta()<=0) {
			double ct = calcolaCosto2(parziale2);
			/*//
			int mese2 = d.getMese();
			for(int i=0; i<parziale2.size(); i=i+3) {
				System.out.println("Mese "+mese2+": qtaOrd= "+parziale2.get(i)+
						", qtaStraord= "+parziale2.get(i+1)+", qtaEst= "+parziale2.get(i+2));
				mese2--;
			}
			System.out.println();
			//*/
			if(ct<this.ctMin) {
				int mese = d.getMese();
				for(int i=0; i<parziale2.size(); i=i+3) {
					this.schedOttima.set(mese-1, new Produzione(mese, parziale2.get(i), parziale2.get(i+1), parziale2.get(i+2)));
					mese--;
				}
				this.ctMin = ct; 
			}
		}else {
			for(int i=tipoProd; i<parziale2.size(); i++) {
				if(parziale2.get(i)+1<=getCapacita(i, parziale2.size())) {  /*this.capacitaOrd*/
					parziale2.set(i, parziale2.get(i)+1);
					d.setQta(d.getQta()-1);
					schedula(parziale2, d, tipoProd);
					parziale2.set(i, parziale2.get(i)-1);
					d.setQta(d.getQta()+1);
					tipoProd = i+1;
				}
			}
		}
	}
	

	private int getCapacita(int i, int size) {
		int j=0;
		boolean trovato = false;
		int capacita = Integer.MAX_VALUE;
		while(j<size && trovato==false) {
			if(j==i) {
				capacita = this.capacitaOrd;
				trovato = true;
			}else if(j+1==i) {
				capacita = this.capacitaStraord;
				trovato = true;
			}else if(j+2==i){
				capacita = Integer.MAX_VALUE;
				trovato = true;
			}
			j=j+3;
		}
		return capacita;
	}

	private double calcolaCosto2(ArrayList<Integer> parziale2) {
		double ct = 0;
		for(int i=0; i<parziale2.size(); i=i+3) {
			ct = ct + (parziale2.get(i)-this.situazioneIniziale.get(i/3).getQtaProdOrd())*this.costoProdOrd 
					+ (parziale2.get(i+1)-this.situazioneIniziale.get(i/3).getQtaProdStraord())*this.costoProdStraord 
					+ (parziale2.get(i+2)-this.situazioneIniziale.get(i/3).getQtaProdEst())*this.costoProdEsterna;
			ct = ct + i/3*((parziale2.get(i)+parziale2.get(i+1)+parziale2.get(i+2))
					-(this.situazioneIniziale.get(i/3).getQtaProdOrd()+this.situazioneIniziale.get(i/3).getQtaProdStraord()+
							this.situazioneIniziale.get(i/3).getQtaProdEst()))*this.costoStoccaggio;
		}
		return ct;
	}
	
	private double calcolaCostoIniziale(Produzione p) {
		double ctIniziale = 0;
			ctIniziale = ctIniziale+
						 p.getQtaProdOrd()*this.costoProdOrd+
						 p.getQtaProdStraord()*this.costoProdStraord+
						 p.getQtaProdEst()*this.costoProdEsterna;
		return ctIniziale;
	}

}
