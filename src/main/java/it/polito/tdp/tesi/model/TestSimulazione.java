package it.polito.tdp.tesi.model;

public class TestSimulazione {

	public static void main(String[] args) {
		
		/*Simulazione2 sim = new Simulazione2(1);
		sim.caricaCoda();
		sim.run();
		
		System.out.println("TH: "+sim.getTH()+"\nCT: "+sim.getCT()
			+"\nWIP: "+sim.getWIP()/*+"\nCTq: "+sim.getCTq()+"\nE[Te]: "+(sim.getCT()-sim.getCTq())*///);
		
		Simulazione3 sim = new Simulazione3();
		System.out.println("linea 3");
		sim.scegliLinea(3);
		sim.caricaLinea(100000);
		sim.run();
		sim.calcolaPerformance();
		
		/*System.out.println("\nlinea 1");
		sim.scegliLinea(1);
		sim.caricaLinea(500);
		sim.run();
		sim.calcolaPerformance();
		
		System.out.println("\nlinea 2");
		sim.scegliLinea(2);
		sim.caricaLinea(500);
		sim.run();
		sim.calcolaPerformance();
		
		System.out.println("\nlinea 1");
		sim.scegliLinea(1);
		sim.caricaLinea(500);
		sim.run();
		sim.calcolaPerformance();
		
		System.out.println("\nlinea 1");
		sim.scegliLinea(1);
		sim.caricaLinea(500);
		sim.run();
		sim.calcolaPerformance();
		
		/*Simulazione s = new Simulazione();
		s.creaLinea(2);
		s.caricaCoda();
		s.run();
		System.out.println("TH: "+s.getTH()+"\nCT: "+s.getCT()
			+"\nWIP: "+s.getWIP());*/
		
		/*double CTavg = 0;
		double CTqavg = 0;
		for(int i=0; i<20; i++) {
			Simulazione3 s = new Simulazione3(1);
			s.caricaCoda();
			s.run();
			CTavg = CTavg + s.getCT();
			CTqavg = CTqavg + s.getCTq();
		}
		
		System.out.println("CTavg: "+CTavg/20+"\nCTqavg: "+CTqavg/20);*/
		
		/*Analisi a = new Analisi();
		for(int w=1; w<=20; w++) {
			a.bestCase(1800, 2, w);
			System.out.println("w: "+w+" Th best: "+a.getTH()+" Ct best: "+a.getCT());
		}
		for(int w=1; w<=20; w++) {
			a.practicalWorstCase(1800, 2, w);
			System.out.println("w: "+w+" Th practical worst: "+a.getTH()+" Ct practical worst: "+a.getCT());
		}
		for(int w=1; w<=20; w++) {
			a.worstCase(1800, 2, w);
			System.out.println("w: "+w+" Th worst: "+a.getTH()+" Ct worst: "+a.getCT());
		}*/
	}

}
