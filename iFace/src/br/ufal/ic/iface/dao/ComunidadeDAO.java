package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.Main;
import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.Comunidade;
import br.ufal.ic.iface.vo.Usuario;

public class ComunidadeDAO {
	
	private String tableName = "comunidade";
	private String participanteTableName = "comunidade_participante";
	
	public boolean cadastrarComunidade(Comunidade comunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+tableName+" (nome, descricao, responsavel_id) "
					+ "VALUES (?,?,?)";
			PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, comunidade.getNome());
			stmt.setString(2,  comunidade.getDescricao());
			stmt.setInt(3, Main.sessao.getUsuario().getId());
			stmt.execute();
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next()){
				comunidade.setId(rs.getInt(1));
				adicionarParticipanteComunidade(Main.sessao.getUsuario(), comunidade);
				new SessaoDAO().recarregarSessao();
				return true;
			}
			return false;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean adicionarParticipanteComunidade(Usuario participante, Comunidade comunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+participanteTableName+" (comunidade_id, participante_id) "
					+ "VALUES (?,?)";
			PreparedStatement stmt =  conexao.prepareStatement(sql);
			stmt.setInt(1, comunidade.getId());
			stmt.setInt(2, participante.getId());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void removerParticipanteComunidade(Usuario participante, Comunidade comunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+participanteTableName+" WHERE comunidade_id = ? AND participante_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, comunidade.getId());
			stmt.setInt(2, participante.getId());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void removerComunidadeUsuario(int idUsuario){
		removerParticipantesComunidadeUsuario(idUsuario);
		
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+tableName+" WHERE responsavel_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			stmt.execute();
			new SessaoDAO().recarregarSessao();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void removerParticipantesComunidadeUsuario(int idUsuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE p FROM "+participanteTableName+" p "
					+ "INNER JOIN "+tableName+" c ON p.comunidade_id = c.id "
							+ "WHERE c.responsavel_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			stmt.execute();
			new SessaoDAO().recarregarSessao();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void removerParticipacoesUsuario(int idUsuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+participanteTableName+" WHERE participante_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			stmt.execute();
			new SessaoDAO().recarregarSessao();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public Comunidade listarComunidadePorId(int id){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Comunidade comunidade = new Comunidade();
			while(rs.next()){
				comunidade.setId(rs.getInt("id"));
				comunidade.setDescricao(rs.getString("descricao"));
				comunidade.setNome(rs.getString("nome"));
				comunidade.setResponsavel(new UsuarioDAO().listarUsuarioPorId(rs.getInt("responsavel_id")));
			}
			return comunidade;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Comunidade> listarMinhasComunidades(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE responsavel_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			ResultSet rs = stmt.executeQuery();
			List<Comunidade> comunidades = new ArrayList<Comunidade>();
			while(rs.next()){
				comunidades.add(listarComunidadePorId(rs.getInt("id")));
			}
			return comunidades;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Comunidade> procurarComunidades(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id NOT IN ("
					+ "SELECT comunidade_id FROM "+participanteTableName+" WHERE participante_id = ?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			ResultSet rs = stmt.executeQuery();
			List<Comunidade> comunidades = new ArrayList<Comunidade>();
			while(rs.next()){
				comunidades.add(listarComunidadePorId(rs.getInt("id")));
			}
			return comunidades;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Comunidade> listarComunidadesParticipante(Usuario u){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+participanteTableName+" WHERE participante_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			ResultSet rs = stmt.executeQuery();
			List<Comunidade> comunidades = new ArrayList<Comunidade>();
			while(rs.next()){
				comunidades.add(listarComunidadePorId(rs.getInt("comunidade_id")));
			}
			return comunidades;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

}
