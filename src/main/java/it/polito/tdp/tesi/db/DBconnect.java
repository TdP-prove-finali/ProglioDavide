package it.polito.tdp.tesi.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;

public class DBconnect {
	
	static private HikariDataSource ds = null;
	static private String url = "jdbc:mysql://localhost:3306/ordini_produzione";
	
	public static Connection getConnection() {
		
		if(ds==null) {
			ds = new HikariDataSource();
			ds.setJdbcUrl(url);
			ds.setUsername("root");
			ds.setPassword("orion22");
		}
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
