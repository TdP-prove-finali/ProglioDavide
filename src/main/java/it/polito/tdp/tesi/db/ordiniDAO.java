package it.polito.tdp.tesi.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import it.polito.tdp.tesi.model.ordineProduttivo;

public class ordiniDAO {
	
	public ArrayList<ordineProduttivo> ordiniProdotto(String idProdotto){
		try {
			Connection conn = DBconnect.getConnection();
			String sql = "SELECT * FROM ordini WHERE id_prod = ?";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, idProdotto);
			ResultSet res = st.executeQuery();
			ArrayList<ordineProduttivo> ordini = new ArrayList<ordineProduttivo>();
			while(res.next()) {
				int idOrdine = res.getInt("id_order");
				int idCliente = res.getInt("id_costumer");
				LocalDate data = res.getDate("date").toLocalDate();
				String idProd = res.getString("id_prod");
				ordini.add(new ordineProduttivo(idOrdine, idCliente, data, idProd));
			}
			st.close(); 
			conn.close();
			return ordini;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public HashMap<Integer, Integer> getQtaProdottoMese(String idProdotto, int anno){
		try {
			Connection conn = DBconnect.getConnection();
			String sql = "SELECT id_prod, MONTH(DATE) AS month, COUNT(id_order) AS qtaProd "
					+ "FROM ordini WHERE id_prod = ? AND YEAR(DATE) = ? GROUP BY MONTH(DATE)";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, idProdotto);
			LocalDate data = LocalDate.of(anno, 1, 1);
			st.setDate(2, Date.valueOf(data));
			ResultSet res = st.executeQuery();
			HashMap<Integer, Integer> ordini = new HashMap<Integer, Integer>();
			while(res.next()) {
				String prodotto = idProdotto;
				int mese = res.getInt("month");
				int qtaProd = res.getInt("qtaProd");
				ordini.put(mese, qtaProd);
			}
			st.close(); 
			conn.close();
			return ordini;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

}
