package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.Comunidade;
import br.ufal.ic.iface.vo.ComunidadeMensagem;

public class ComunidadeMensagemDAO {
	
	private String tableName = "mensagem_comunidade";
	
	public boolean enviarMensagemComunidade(ComunidadeMensagem mensagem){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+tableName+" (remetente_id, comunidade_id, texto) "
					+ "VALUES (?,?,?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, mensagem.getRemetente().getId());
			stmt.setInt(2, mensagem.getComunidade().getId());
			stmt.setString(3, mensagem.getTexto());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public List<ComunidadeMensagem> listarMensagensComunidade(Comunidade comunidade){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ComunidadeDAO comunidadeDAO = new ComunidadeDAO();
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE comunidade_id = ? ORDER BY id DESC";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, comunidade.getId());
			ResultSet rs = stmt.executeQuery();
			List<ComunidadeMensagem> mensagens = new ArrayList<ComunidadeMensagem>();
			while(rs.next()){
				ComunidadeMensagem msg = new ComunidadeMensagem();
				msg.setId(rs.getInt("id"));
				msg.setTexto(rs.getString("texto"));
				msg.setRemetente(usuarioDAO.listarUsuarioPorId(rs.getInt("remetente_id")));
				msg.setComunidade(comunidadeDAO.listarComunidadePorId(rs.getInt("comunidade_id")));
				mensagens.add(msg);
			}
			return mensagens;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean removerMensagensComunidadeUsuario(int idUsuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+tableName+" WHERE remetente_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			return stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean removerMensagensComunidade(int idComunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+tableName+" WHERE comunidade_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idComunidade);
			return stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

}
