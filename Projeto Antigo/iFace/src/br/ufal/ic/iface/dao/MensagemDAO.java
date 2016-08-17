package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.Main;
import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.Mensagem;
import br.ufal.ic.iface.vo.Usuario;

public class MensagemDAO {

	private String tableName = "mensagem";
	
	public boolean criarMensagem(Mensagem mensagem){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+tableName+" (remetente_id, destinatario_id, texto, status) "
					+ "VALUES (?,?,?,?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, mensagem.getRemetente().getId());
			stmt.setInt(2, mensagem.getDestinatario().getId());
			stmt.setString(3, mensagem.getTexto());
			stmt.setInt(4, mensagem.getStatus());
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean criarMensagemComunidade(Mensagem mensagem){
		return false;
	}
	
	public List<Mensagem> listarMensagensUsuario(Usuario u){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE destinatario_id = ? ORDER BY id DESC";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, u.getId());
			ResultSet rs = stmt.executeQuery();
			List<Mensagem> mensagens = new ArrayList<Mensagem>();
			while(rs.next()){
				Mensagem msg = new Mensagem();
				msg.setId(rs.getInt("id"));
				msg.setTexto(rs.getString("texto"));
				msg.setRemetente(usuarioDAO.listarUsuarioPorId(rs.getInt("remetente_id")));
				msg.setDestinatario(usuarioDAO.listarUsuarioPorId(rs.getInt("destinatario_id")));
				msg.setStatus(rs.getInt("status"));
				mensagens.add(msg);
			}
			return mensagens;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean marcarMensagensUsuarioLida(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "UPDATE "+tableName+" SET status = 1 WHERE destinatario_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean excluirMensagensUsuario(int idUsuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+tableName+" WHERE destinatario_id = ? OR remetente_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, idUsuario);
			stmt.setInt(2, idUsuario);
			return stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
}
