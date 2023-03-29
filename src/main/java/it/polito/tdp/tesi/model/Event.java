package it.polito.tdp.tesi.model;

import java.time.LocalTime;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		GUASTO,
		SETUP,
		NUOVO_JOB,
		PRODOTTO_FINITO;
	}
	private LocalTime time;
	private EventType type;
	private int nProd;
	
	public Event(LocalTime time, EventType type, int nProd) {
		super();
		this.time = time;
		this.type = type;
		this.nProd = nProd;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((time == null) ? 0 : time.hashCode());
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
		Event other = (Event) obj;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public int compareTo(Event other) {
		if(this.getTime()==other.getTime() && this.getType()==EventType.NUOVO_JOB && other.getType()==EventType.NUOVO_JOB) {
			return this.getnProd()-other.getnProd();
		}else {
			return this.time.compareTo(other.time);
		}
		//return this.time.compareTo(other.time);
	}

	@Override
	public String toString() {
		return "Event [time=" + time.toSecondOfDay() + ", type=" + type + "]";
	}


}
