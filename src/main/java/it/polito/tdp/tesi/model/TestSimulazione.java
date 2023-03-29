package it.polito.tdp.tesi.model;

public class TestSimulazione {

	public static void main(String[] args) {
		
		Simulazione3 sim = new Simulazione3(1);
		sim.caricaCoda();
		sim.run();
		
		System.out.println("TH: "+sim.getTH()+"\nCT: "+sim.getCT()
			+"\nWIP: "+sim.getWIP()+"\nCTq: "+sim.getCTq()+"\nE[Te]: "+(sim.getCT()-sim.getCTq()));
		
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
	}

}
