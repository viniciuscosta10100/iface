package br.ufal.ic.iface.bo;

import java.util.List;

import br.ufal.ic.iface.dao.UsuarioDAO;
import br.ufal.ic.iface.vo.Usuario;

public class UsuarioBO {
	
	UsuarioDAO dao = new UsuarioDAO();
	
	public boolean cadastrarUsuario(Usuario u){
		if(dao.listarUsuarioPorEmail(u.getEmail())==null){
			return new UsuarioDAO().cadastrarUsuario(u);
		}
		System.out.println("O email informado já existe");
		return false;
	}
	
	public boolean enviarSolicitacaoAmizade(Usuario usuario){
		List<Usuario> solicitacoes = dao.listarSolicitacoesAmizade();
		boolean solicitacaoExistente = false;
		for(Usuario amigoPendente:solicitacoes){
			if(amigoPendente.getId() == usuario.getId())
				solicitacaoExistente = true;
		}
		if(solicitacaoExistente){
			System.out.println("Solicitação já existente");
			return false;
		}
		return dao.enviarSolicitacaoAmizade(usuario);
	}

}
