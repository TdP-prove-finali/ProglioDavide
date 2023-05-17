package it.polito.tdp.tesi.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polito.tdp.tesi.model.Domanda;

public class ordiniDAO {
	
	public ArrayList<Domanda> getDomandaMese(String idProdotto, int anno){
		try {
			Connection conn = DBconnect.getConnection();
			String sql = "SELECT id_prod, MONTH(DATE) AS month, COUNT(id_order) AS qtaProd "
					+ "FROM ordini WHERE id_prod = ? AND YEAR(DATE) = ? GROUP BY MONTH(DATE)";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, idProdotto);
			LocalDate data = LocalDate.of(anno, 1, 1);
			st.setDate(2, Date.valueOf(data));
			ResultSet res = st.executeQuery();
			ArrayList<Domanda> domanda = new ArrayList<Domanda>();
			while(res.next()) {
				String prodotto = idProdotto;
				int mese = res.getInt("month");
				int qtaProd = res.getInt("qtaProd");
				domanda.add(new Domanda(mese, qtaProd));
			}
			ArrayList<Domanda> domandaTot = new ArrayList<Domanda>();
			for(int i=1; i<13; i++) {
				int j=-1;
				for(Domanda d : domanda) {
					if(d.getMese()==i) {
						j=domanda.indexOf(d);
					}
				}
				if(j==-1) {
					domandaTot.add(new Domanda(i, 0));
				}else {
					domandaTot.add(domanda.get(j));
				}
			}
			st.close(); 
			conn.close();
			return domandaTot;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Integer> getYears(){
		try {
			Connection conn = DBconnect.getConnection();
			String sql = "SELECT DISTINCT YEAR(DATE) AS anno "
					+ "FROM ordini";
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			ArrayList<Integer> anni = new ArrayList<Integer>();
			while(res.next()) {
				anni.add(res.getInt("anno"));
			}
			st.close(); 
			conn.close();
			return anni;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<String> getProdByYear(int anno){
		try {
			Connection conn = DBconnect.getConnection();
			String sql = "SELECT DISTINCT id_prod "
					+ "FROM ordini "
					+ "WHERE YEAR(DATE) = ? "
					+ "GROUP BY id_prod "
					+ "HAVING COUNT(*) >= 200";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, String.valueOf(anno));
			ResultSet res = st.executeQuery();
			ArrayList<String> prodotti = new ArrayList<String>();
			while(res.next()) {
				prodotti.add(res.getString("id_prod"));
			}
			st.close(); 
			conn.close();
			return prodotti;
		}catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
