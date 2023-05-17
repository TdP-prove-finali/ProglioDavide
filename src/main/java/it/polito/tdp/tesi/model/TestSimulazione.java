package it.polito.tdp.tesi.model;

public class TestSimulazione {

	public static void main(String[] args) {
		
		Simulazione3 sim = new Simulazione3();
		System.out.println("linea 3");
		sim.scegliLinea(3);
		sim.caricaLinea(3000);
		sim.run();
		sim.calcolaPerformance();

	}

}
