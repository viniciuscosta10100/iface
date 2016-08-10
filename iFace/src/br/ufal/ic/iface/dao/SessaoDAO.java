package br.ufal.ic.iface.dao;

import br.ufal.ic.iface.Main;

public class SessaoDAO {
	
	public void recarregarSessao(){
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Main.sessao.setUsuario(usuarioDAO.listarUsuarioPorId(
					Main.sessao.getUsuario().getId()));
		Main.sessao.getUsuario().setAmigos(usuarioDAO.listarAmigos(Main.sessao.getUsuario()));
		Main.sessao.getUsuario().setMensagens(new MensagemDAO().listarMensagensUsuario(Main.sessao.getUsuario()));
		Main.sessao.getUsuario().setComunidades(new ComunidadeDAO().listarComunidadesParticipante(Main.sessao.getUsuario()));
	}

}
