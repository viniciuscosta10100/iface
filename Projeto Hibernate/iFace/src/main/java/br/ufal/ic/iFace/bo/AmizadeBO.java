package br.ufal.ic.iFace.bo;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.exceptions.FriendshipException;
import br.ufal.ic.iFace.model.Amizade;
import br.ufal.ic.iFace.model.Usuario;

public class AmizadeBO {

	public boolean enviarSolicitacaoAmizade(Usuario usuario) throws FriendshipException{
		Amizade solicitacao = Main.amizadeRepository.procurarSolicitacao(usuario);
		
		if(solicitacao != null){
			throw new FriendshipException("Solicitação já existente");
		}
		return Main.amizadeRepository.enviarSolicitacaoAmizade(usuario);
	}
}
