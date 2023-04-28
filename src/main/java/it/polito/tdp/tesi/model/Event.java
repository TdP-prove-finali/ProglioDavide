package it.polito.tdp.tesi.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		GUASTO,
		NUOVO_JOB,
	}
	private double tempo;
	private EventType type;
	private int nProd;
	private int wk;
	
	public Event(double tempo, EventType type, int nProd, int wk) {
		super();
		this.tempo = tempo;
		this.type = type;
		this.nProd = nProd;
		this.wk = wk;
	}

	public Event(double tempo, EventType type, int wk) {
		super();
		this.tempo = tempo;
		this.type = type;
		this.wk = wk;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getnProd() {
		return nProd;
	}

	public void setnProd(int nProd) {
		this.nProd = nProd;
	}

	public int getWk() {
		return wk;
	}

	public void setWk(int wk) {
		this.wk = wk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nProd;
		long temp;
		temp = Double.doubleToLongBits(tempo);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + wk;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (nProd != other.nProd)
			return false;
		if (Double.doubleToLongBits(tempo) != Double.doubleToLongBits(other.tempo))
			return false;
		if (type != other.type)
			return false;
		if (wk != other.wk)
			return false;
		return true;
	}

	@Override
	public int compareTo(Event other) {
		if(this.getTempo()==other.getTempo() /*&& this.getType()==EventType.NUOVO_JOB 
				&& other.getType()==EventType.NUOVO_JOB*/) {
			return this.getnProd()-other.getnProd();
		}else { 
			return (int) (this.getTempo()-other.getTempo());
		}
	}

	@Override
	public String toString() {
		return "tempo: " + tempo + ", type: " + type + ", nProd: " + nProd + ", wk: " + wk + "\n";
	}
	
	
	

}
