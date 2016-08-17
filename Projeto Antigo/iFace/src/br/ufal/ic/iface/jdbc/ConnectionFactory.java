package br.ufal.ic.iface.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	public Connection getConnection() {
	     try {
	    	 Class.forName("com.mysql.jdbc.Driver");
	         return DriverManager.getConnection(
	 "jdbc:mysql://localhost/iface", "root", "");
	     } catch (SQLException|ClassNotFoundException e) {
	         throw new RuntimeException(e);
	     }
	 }

}
