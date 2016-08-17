package br.ufal.ic.iFace.repository;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import br.ufal.ic.iFace.model.Comunidade;
import br.ufal.ic.iFace.model.ComunidadeMensagem;
import br.ufal.ic.iFace.model.Usuario;

public class ComunidadeMensagemRepository extends GenericHibernateRepository<ComunidadeMensagem, Integer>{
	
	public List<ComunidadeMensagem> listarMensagensComunidade(Comunidade comunidade){
		List<ComunidadeMensagem> mensagens = findByCriteria(Restrictions.eq("comunidade", comunidade));
		return mensagens;
	}
	
	public boolean removerMensagensComunidadeUsuario(Usuario usuario){
		List<ComunidadeMensagem> mensagens = findByCriteria(Restrictions.eq("remetente", usuario));
		for(ComunidadeMensagem m:mensagens)
			delete(m);
		return true;
	}
	
	public boolean removerMensagensComunidade(Comunidade comunidade){
		List<ComunidadeMensagem> mensagens = findByCriteria(Restrictions.eq("comunidade", comunidade));
		for(ComunidadeMensagem m:mensagens)
			delete(m);
		return true;
	}
}
