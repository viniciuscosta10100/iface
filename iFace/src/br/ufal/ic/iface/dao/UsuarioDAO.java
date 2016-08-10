package br.ufal.ic.iface.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ufal.ic.iface.Main;
import br.ufal.ic.iface.jdbc.ConnectionFactory;
import br.ufal.ic.iface.vo.Comunidade;
import br.ufal.ic.iface.vo.Usuario;

public class UsuarioDAO {
	
	private String tableName = "usuario";
	private String amizadeTableName = "convite_amizade";
	private String participanteComunidadeTableName = "comunidade_participante";
	
	public Usuario login(String email, String senha){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT id FROM "+tableName+" WHERE email = ? AND senha = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, senha);
			ResultSet rs = stmt.executeQuery();
			if(rs.next())
				return listarUsuarioPorId(rs.getInt("id"));
			return null;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean cadastrarUsuario(Usuario usuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+tableName+" (nome, email, senha, sexo_id, estado_civil_id, data_nascimento, mensagem_perfil) "
					+ "VALUES (?,?,?,?,?,?,?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2,  usuario.getEmail());
			stmt.setString(3, usuario.getSenha());
			stmt.setInt(4, usuario.getSexo().getId());
			stmt.setInt(5, usuario.getEstadoCivil().getId());
			stmt.setDate(6, (usuario.getDataNascimento()!=null)
					?new java.sql.Date(usuario.getDataNascimento().getTime()):null);
			stmt.setString(7, usuario.getMensagemPerfil());
			return stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean alterarUsuario(Usuario usuario){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "UPDATE "+tableName+" SET nome = ?, senha = ?, sexo_id = ?, "
					+ " estado_civil_id = ?, data_nascimento = ?, mensagem_perfil = ? "
					+ "WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, usuario.getNome());
			stmt.setString(2, usuario.getSenha());
			stmt.setInt(3, usuario.getSexo().getId());
			stmt.setInt(4, usuario.getEstadoCivil().getId());
			stmt.setDate(5, (usuario.getDataNascimento()!=null)
					?new java.sql.Date(usuario.getDataNascimento().getTime()):null);
			stmt.setString(6, usuario.getMensagemPerfil());
			stmt.setInt(7, usuario.getId());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean enviarSolicitacaoAmizade(Usuario convidado){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "INSERT INTO "+amizadeTableName+" (remetente_id, destinatario_id, status) "
					+ "VALUES (?,?,?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			stmt.setInt(2, convidado.getId());
			stmt.setInt(3, 0);
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean aprovarSolicitacaoAmizade(Usuario remetente){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "UPDATE "+amizadeTableName+" SET status = ? WHERE remetente_id = ? AND destinatario_id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, 1);
			stmt.setInt(2, remetente.getId()); 
			stmt.setInt(3, Main.sessao.getUsuario().getId());
			stmt.execute();
			new SessaoDAO().recarregarSessao();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean removerSolicitacaoAmizade(Usuario amigo){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+amizadeTableName+""
					+ " WHERE (remetente_id = ? AND destinatario_id = ?) "
					+ "OR (remetente_id = ? AND destinatario_id = ?)";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			stmt.setInt(2, amigo.getId());
			stmt.setInt(3, amigo.getId());
			stmt.setInt(4, Main.sessao.getUsuario().getId());
			new SessaoDAO().recarregarSessao();
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void removerAmigosUsuario(int id){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+amizadeTableName+""
					+ " WHERE (remetente_id = ? OR destinatario_id = ?) ";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			stmt.setInt(2, id);
			stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public List<Usuario> listarSolicitacoesAmizade(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			List<Usuario> amigosPendentes = new ArrayList<Usuario>();
			String sql = "SELECT remetente_id FROM "+amizadeTableName+" WHERE destinatario_id = ? AND status = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			stmt.setInt(2, 0);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				Usuario amigo = listarUsuarioPorId(rs.getInt("remetente_id"));
				amigosPendentes.add(amigo);
			}
			return amigosPendentes;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> listarNaoAmigos(Usuario user){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT id FROM "+tableName+" WHERE id NOT IN (SELECT u.id FROM "+tableName+" u "
					+ "INNER JOIN "+amizadeTableName+" a ON (u.id = a.remetente_id) OR (u.id = destinatario_id) "
					+ "WHERE u.id = ?) AND id != ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, user.getId());
			stmt.setInt(2, user.getId());
			ResultSet rs = stmt.executeQuery();
			List<Usuario> usuarios  = new ArrayList<Usuario>();
			while(rs.next()){
				Usuario u = listarUsuarioPorId(rs.getInt("id"));
				usuarios.add(u);
			}
			rs.close();
	        stmt.close();
			return usuarios;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> listarAmigos(Usuario user){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT u.id, a.remetente_id, a.destinatario_id FROM "+tableName+" u "
					+ "INNER JOIN "+amizadeTableName+" a ON (u.id = a.remetente_id) OR (u.id = destinatario_id) "
					+ "WHERE (remetente_id = ? OR destinatario_id = ?) AND status = 1";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, user.getId());
			stmt.setInt(2, user.getId());
			ResultSet rs = stmt.executeQuery();
			List<Usuario> usuarios  = new ArrayList<Usuario>();
			if(rs.next()){
				int id = rs.getInt("a.remetente_id");
				Usuario u;
				if(id == user.getId())
					u = listarUsuarioPorId(rs.getInt("a.destinatario_id"));
				else
					u = listarUsuarioPorId(id);
				usuarios.add(u);
			}
			rs.close();
	        stmt.close();
			return usuarios;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> listarUsuarios(){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			List<Usuario> usuarios = new ArrayList<Usuario>();
			String sql = "SELECT * FROM "+tableName+ " WHERE id != ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				Usuario u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNome(rs.getString("nome"));
				u.setEmail(rs.getString("email"));
				u.setSenha(rs.getString("senha"));
				u.setSexo(new SexoDAO().listarSexoPorId(rs.getInt("sexo_id")));
				u.setEstadoCivil(new EstadoCivilDAO().listarEstadoCivilPorId(rs.getInt("estado_civil_id")));
				u.setDataNascimento(rs.getDate("data_nascimento"));
				u.setMensagemPerfil(rs.getString("mensagem_perfil"));
				usuarios.add(u);
			}
			rs.close();
	        stmt.close();
			return usuarios;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public Usuario listarUsuarioPorId(int id){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			Usuario u  = null;
			if(rs.next()){
				u = new Usuario();
				u.setId(rs.getInt("id"));
				u.setNome(rs.getString("nome"));
				u.setEmail(rs.getString("email"));
				u.setSenha(rs.getString("senha"));
				u.setSexo(new SexoDAO().listarSexoPorId(rs.getInt("sexo_id")));
				u.setEstadoCivil(new EstadoCivilDAO().listarEstadoCivilPorId(rs.getInt("estado_civil_id")));
				u.setDataNascimento(rs.getDate("data_nascimento"));
				u.setMensagemPerfil(rs.getString("mensagem_perfil"));
			}
			rs.close();
	        stmt.close();
			return u;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Usuario listarUsuarioPorEmail(String email){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE email = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();
			Usuario u  = null;
			if(rs.next()){
				u = listarUsuarioPorId(rs.getInt("id"));
			}
			rs.close();
	        stmt.close();
			return u;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> listarUsuariosParticipanteComunidade(Comunidade comunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id != ? AND id IN ("
					+ "SELECT participante_id FROM "+participanteComunidadeTableName+" "
					+ "WHERE comunidade_id = ? )";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, Main.sessao.getUsuario().getId());
			stmt.setInt(2, comunidade.getId());
			ResultSet rs = stmt.executeQuery();
			List<Usuario> usuarios  = new ArrayList<Usuario>();
			while(rs.next()){
				usuarios.add(listarUsuarioPorId(rs.getInt("id")));
			}
			rs.close();
	        stmt.close();
			return usuarios;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Usuario> listarUsuariosNaoParticipanteComunidade(Comunidade comunidade){
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "SELECT * FROM "+tableName+" WHERE id NOT IN ("
					+ "SELECT participante_id FROM "+participanteComunidadeTableName+" "
					+ "WHERE comunidade_id = ? )";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, comunidade.getId());
			ResultSet rs = stmt.executeQuery();
			List<Usuario> usuarios  = new ArrayList<Usuario>();
			while(rs.next()){
				usuarios.add(listarUsuarioPorId(rs.getInt("id")));
			}
			rs.close();
	        stmt.close();
			return usuarios;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	
	public boolean removerUsuario(int id){
		removerAmigosUsuario(id);
		new MensagemDAO().excluirMensagensUsuario(id);
		for(Comunidade c:new ComunidadeDAO().listarMinhasComunidades()){
			new ComunidadeMensagemDAO().removerMensagensComunidade(c.getId());
		}
		new ComunidadeMensagemDAO().removerMensagensComunidadeUsuario(id);
		new ComunidadeDAO().removerComunidadeUsuario(id);
		new ComunidadeDAO().removerParticipacoesUsuario(id);
		
		
		try(Connection conexao = new ConnectionFactory().getConnection()){
			String sql = "DELETE FROM "+tableName+" WHERE id = ?";
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.setInt(1, id);
			return stmt.execute();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

}
