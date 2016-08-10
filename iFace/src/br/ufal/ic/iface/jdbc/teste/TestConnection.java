package br.ufal.ic.iface.jdbc.teste;

import java.sql.Connection;
import java.sql.SQLException;

import br.ufal.ic.iface.jdbc.ConnectionFactory;

public class TestConnection {
	
	public static void main(String[] args){
		try{
			Connection connection = new ConnectionFactory().getConnection();
			System.out.println("Conexão aberta!");
			connection.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

}
