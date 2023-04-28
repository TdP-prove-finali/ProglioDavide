package it.polito.tdp.tesi.model;

public class Analisi {
	
	private double TH;
	private double CT;
	private int w;
	
	
	public Analisi(double tH, double cT, int w) {
		super();
		TH = tH;
		CT = cT;
		this.w = w;
	}
	
	public double getTH() {
		return TH;
	}
	
	public void setTH(double tH) {
		TH = tH;
	}
	
	public double getCT() {
		return CT;
	}
	
	public void setCT(double cT) {
		CT = cT;
	}
	
	public int getW() {
		return w;
	}
	
	public void setW(int w) {
		this.w = w;
	}

	@Override
	public String toString() {
		return "w: " + w + ", TH: " + TH + ", CT: " + CT;
	}
	
	
	/*public void bestCase(double t0, int nwk, int w) {
		this.TH = 0;
		this.CT = 0;
		double rb = 1/(t0/3600);
		double T0 = (t0/3600)*nwk;
		double W0 = rb*T0;
		if(w<=W0) {
			this.TH = w/T0;
			this.CT = T0;
		}else {
			this.TH = rb;
			this.CT = w/rb;
		}
	}
	
	public void worstCase(double t0, int nwk, int w) {
		this.TH = 0;
		this.CT = 0;
		double rb = 1/(t0/3600);
		double T0 = (t0/3600)*nwk;
		double W0 = rb*T0;
		this.TH = 1/(T0);
		this.CT = w*T0;
	}
	
	public void practicalWorstCase(double t0, int nwk, int w) {
		this.TH = 0;
		this.CT = 0;
		double rb = 1/(t0/3600);
		double T0 = (t0/3600)*nwk;
		double W0 = rb*T0;
		this.TH = (w/(W0+w-1))*rb;
		this.CT = T0+(w-1)/rb;
	}

	public double getTH() {
		return TH;
	}

	public double getCT() {
		return CT;
	}*/
	
}
