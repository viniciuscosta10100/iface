package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.EstadoCivil;

public class EstadoCivilDAO {
	
	private String tableName = "estado_civil";
	
	public List<EstadoCivil> listarEstadosCivis(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName;
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			List<EstadoCivil> estadosCivis = new ArrayList<EstadoCivil>();
			while(rs.next()){
				EstadoCivil ec = new EstadoCivil();
				ec.setId(rs.getInt("id"));
				ec.setNome(rs.getString("nome"));
				estadosCivis.add(ec);
			}
			return estadosCivis;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public EstadoCivil listarEstadoCivilPorId(int id){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			EstadoCivil ec  = null;
			if(rs.next()){
				ec = new EstadoCivil();
				ec.setId(rs.getInt("id"));
				ec.setNome(rs.getString("nome"));
			}
			return ec;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
}
