package it.polito.tdp.tesi.model;

import it.polito.tdp.tesi.db.ordiniDAO;

public class TestLotSizing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LotSizing2 ls = new LotSizing2();
		ordiniDAO dao = new ordiniDAO();
		ls.lotSizing(dao.getQtaProdottoMese("12451C", 2018), 250, 100, 3.5, 4, 8, 2.5, 50);
	}

}
