package it.polito.tdp.tesi.model;

import java.time.LocalDate;

public class ordineProduttivo {
	
	private int idOrdine;
	private int idCliente;
	private LocalDate data;
	private String idProdotto;
	
	public ordineProduttivo() {
		super();
	}

	public ordineProduttivo(int idOrdine, int idCliente, LocalDate data, String idProdotto) {
		super();
		this.idOrdine = idOrdine;
		this.idCliente = idCliente;
		this.data = data;
		this.idProdotto = idProdotto;
	}

	public int getIdOrdine() {
		return idOrdine;
	}

	public void setIdOrdine(int idOrdine) {
		this.idOrdine = idOrdine;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getIdProdotto() {
		return idProdotto;
	}

	public void setIdProdotto(String idProdotto) {
		this.idProdotto = idProdotto;
	}

	@Override
	public String toString() {
		return "idOrdine: " + idOrdine + ", idCliente: " + idCliente + ", data: " + data
				+ ", idProdotto: " + idProdotto;
	}
	
}
