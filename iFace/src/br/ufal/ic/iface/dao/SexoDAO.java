package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.Sexo;

public class SexoDAO {
	
	private String tableName = "sexo";

	public List<Sexo> listarSexos(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName;
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			List<Sexo> sexos = new ArrayList<Sexo>();
			while(rs.next()){
				Sexo s = new Sexo();
				s.setId(rs.getInt("id"));
				s.setNome(rs.getString("nome"));
				sexos.add(s);
			}
			return sexos;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Sexo listarSexoPorId(int id){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Sexo s  = null;
			if(rs.next()){
				s = new Sexo();
				s.setId(rs.getInt("id"));
				s.setNome(rs.getString("nome"));
			}
			return s;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
}
