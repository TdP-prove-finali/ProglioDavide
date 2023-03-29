package it.polito.tdp.tesi.model;

import it.polito.tdp.tesi.model.Event.EventType;

public class Event2 implements Comparable<Event2>{
	
	public enum Event2Type{
		GUASTO,
		NUOVO_JOB,
	}
	private double tempo;
	private Event2Type type;
	private int nProd;
	
	public Event2(double tempo, Event2Type type, int nProd) {
		super();
		this.tempo = tempo;
		this.type = type;
		this.nProd = nProd;
	}

	public Event2(double tempo, Event2Type type) {
		super();
		this.tempo = tempo;
		this.type = type;
	}


	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public Event2Type getType() {
		return type;
	}

	public void setType(Event2Type type) {
		this.type = type;
	}

	public int getnProd() {
		return nProd;
	}

	public void setnProd(int nProd) {
		this.nProd = nProd;
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
		Event2 other = (Event2) obj;
		if (nProd != other.nProd)
			return false;
		if (Double.doubleToLongBits(tempo) != Double.doubleToLongBits(other.tempo))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Event2 other) {
		if(this.getTempo()==other.getTempo() && this.getType()==Event2Type.NUOVO_JOB 
				&& other.getType()==Event2Type.NUOVO_JOB) {
			return this.getnProd()-other.getnProd();
		}else {
			return (int) (this.getTempo()-other.getTempo());
		}
	}

	@Override
	public String toString() {
		return "tempo: " + tempo + ", type: " + type + ", nProd: " + nProd;
	}
	
	

}
