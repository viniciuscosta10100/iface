package br.ufal.ic.iFace.repository;

import br.ufal.ic.iFace.Main;

public class SessaoRepository {
	
	public void recarregarSessao(){
		try{
			Main.sessao.setUsuario(Main.usuarioRepository.findById(Main.sessao.getUsuario().getId()));
			Main.sessao.getUsuario().setAmigos(Main.amizadeRepository.listarAmigos(Main.sessao.getUsuario()));
			Main.sessao.getUsuario().setMensagens(Main.mensagemRepository.listarMensagensUsuario(Main.sessao.getUsuario()));
			Main.sessao.getUsuario().setComunidades(Main.comunidadeParticipantesRepository.listarComunidadesParticipante(Main.sessao.getUsuario()));
		}catch(NullPointerException e){}
	}

}
