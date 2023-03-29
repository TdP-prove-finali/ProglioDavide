package it.polito.tdp.tesi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnect {
	
	public static Connection getConnection() {
		String url = "jdbc:mysql://localhost:3306/ordini_produzione?user=root&password=orion22";
		try {
			Connection conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException e) {
			System.out.println("ERRORE di connessione al database");
			e.printStackTrace();
			return null;
		}
	}

}
