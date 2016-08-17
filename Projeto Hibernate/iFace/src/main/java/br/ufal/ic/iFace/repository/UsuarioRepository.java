package br.ufal.ic.iFace.repository;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.exceptions.UserNotFoundException;
import br.ufal.ic.iFace.model.Comunidade;
import br.ufal.ic.iFace.model.Usuario;

public class UsuarioRepository extends GenericHibernateRepository<Usuario, Integer> {
	
	
	@Override
	public List<Usuario> findAll(){
		List<Usuario> users = findByCriteria(Restrictions.not(Restrictions.eq("id", Main.sessao.getUsuario().getId())));
		return users;
	}
	
	public Usuario login(String email, String senha) throws UserNotFoundException{
		Usuario usuario;
		try{
			usuario = findByCriteria(Restrictions.and(
					Restrictions.eq("email", email),
					Restrictions.eq("senha", senha))).get(0);
			return usuario;
		}catch(IndexOutOfBoundsException e){
			throw new UserNotFoundException("Email ou senha inválidos!");
		}
	}
	
	public Usuario listarUsuarioPorEmail(String email){
		Usuario usuario;
		try{
			usuario = findByCriteria(Restrictions.eq("email", email)).get(0);
			return usuario;
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}

	
	public boolean removerUsuario(Usuario usuario){
		Main.amizadeRepository.removerAmigosUsuario(usuario);
		Main.mensagemRepository.excluirMensagensUsuario(usuario);
		for(Comunidade c:new ComunidadeRepository().listarMinhasComunidades()){
			Main.comunidadeMensagemRepository.removerMensagensComunidade(c);
		}
		
		Main.comunidadeMensagemRepository.removerMensagensComunidadeUsuario(usuario);
		Main.comunidadeRepository.removerComunidadeUsuario(usuario);
		Main.comunidadeParticipantesRepository.removerParticipacoesUsuario(usuario);
		
		
		delete(usuario);
		return true;
	}

}
