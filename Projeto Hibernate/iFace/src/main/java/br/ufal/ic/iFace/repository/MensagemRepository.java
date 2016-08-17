package br.ufal.ic.iFace.repository;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.model.Mensagem;
import br.ufal.ic.iFace.model.Usuario;

public class MensagemRepository extends GenericHibernateRepository<Mensagem, Integer>{

	public List<Mensagem> listarMensagensUsuario(Usuario u){
		List<Mensagem> mensagens = findByCriteria(Restrictions.eq("destinatario", u));
		return mensagens;
	}
	
	public boolean marcarMensagensUsuarioLida(){
		List<Mensagem> mensagens = findByCriteria(Restrictions.eq("destinatario", Main.sessao.getUsuario()));
		for(Mensagem m:mensagens){
			m.setStatus(1);
			update(m);
		}
		return true;
	}
	
	public boolean excluirMensagensUsuario(Usuario u){
		List<Mensagem> mensagens = findByCriteria(
				Restrictions.or(
						Restrictions.eq("destinatario", Main.sessao.getUsuario()),
						Restrictions.eq("remetente", Main.sessao.getUsuario())));
		for(Mensagem m:mensagens){
			delete(m);
		}
		return true;
	}
	
}
