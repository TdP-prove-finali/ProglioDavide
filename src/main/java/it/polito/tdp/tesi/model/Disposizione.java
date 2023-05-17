package it.polito.tdp.tesi.model;

public class Disposizione {
	
	private int qta;
	private String tipo;
	private int mese;
	
	public Disposizione(int qta, String tipo, int mese) {
		super();
		this.qta = qta;
		this.tipo = tipo;
		this.mese = mese;
	}

	public int getQta() {
		return qta;
	}

	public void setQta(int qta) {
		this.qta = qta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getMese() {
		return mese;
	}

	public void setMese(int mese) {
		this.mese = mese;
	}

	@Override
	public String toString() {
		return "Mese: " + mese + ", qta: " + qta + ", tipo: " + tipo +"\n";
	}

}
