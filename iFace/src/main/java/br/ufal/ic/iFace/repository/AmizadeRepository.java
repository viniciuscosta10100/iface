package br.ufal.ic.iFace.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.exceptions.FriendshipException;
import br.ufal.ic.iFace.model.Amizade;
import br.ufal.ic.iFace.model.Usuario;

public class AmizadeRepository extends GenericHibernateRepository<Amizade, Integer>{
	
	public boolean enviarSolicitacaoAmizade(Usuario convidado){
		Amizade amizade = new Amizade();
		amizade.setUsuario1(Main.sessao.getUsuario());
		amizade.setUsuario2(convidado);
		amizade.setStatus(0);
		save(amizade);
		Main.sessaoRepository.recarregarSessao();
		return true;
	}
	
	public Amizade procurarSolicitacao(Usuario outroUsuario){
		try{
			Amizade amizade = findByCriteria(
					Restrictions.or(
							Restrictions.and(
									Restrictions.eq("usuario1", outroUsuario),
									Restrictions.eq("usuario2", Main.sessao.getUsuario())),
							Restrictions.and(
									Restrictions.eq("usuario1", Main.sessao.getUsuario()),
									Restrictions.eq("usuario2", outroUsuario)))).get(0);
			return amizade;
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public void aprovarSolicitacaoAmizade(Usuario remetente) throws FriendshipException{
		try{
			Amizade amizade = procurarSolicitacao(remetente);
			amizade.setStatus(1);
			update(amizade);
		}catch(NullPointerException e){
			throw new FriendshipException("Solicitação não encontrada");
		}
	}
	
	public List<Usuario> listarSolicitacoesAmizade(){
		List<Amizade> amizades = findByCriteria(
				Restrictions.and(
						Restrictions.eq("usuario2", Main.sessao.getUsuario()),
						Restrictions.eq("status", 0)));
		List<Usuario> amigosPendentes = new ArrayList<Usuario>();
		for(Amizade a:amizades){
			amigosPendentes.add(a.getUsuario1());
		}
		return amigosPendentes;
	}
	
	public List<Usuario> listarSolicitacoes(Usuario user){
		List<Amizade> solicitacoes = findByCriteria(
				Restrictions.or(
						Restrictions.eq("usuario1", user), 
						Restrictions.eq("usuario2", user)));
		List<Usuario> outrosUsuarios = new ArrayList<Usuario>();
		for(Amizade a:solicitacoes){
			if(a.getUsuario1().getId() == user.getId())
				outrosUsuarios.add(a.getUsuario2());
			else
				outrosUsuarios.add(a.getUsuario1());
		}
		return outrosUsuarios;
	}
	
	public List<Usuario> listarNaoAmigos(Usuario user){
		List<Usuario> naoAmigos = new ArrayList<Usuario>();
		DetachedCriteria userSubquery1 = DetachedCriteria.forClass(Amizade.class, "a")
			    .setProjection( Projections.property("a.usuario1"))
			    .add(Restrictions.eq("usuario2", user));
		DetachedCriteria userSubquery2 = DetachedCriteria.forClass(Amizade.class, "am")
			    .setProjection( Projections.property("am.usuario2"))
			    .add(Restrictions.eq("usuario1", user));
		naoAmigos = Main.usuarioRepository.findByCriteria(
				Restrictions.and(
					Restrictions.and(
							Restrictions.not(Subqueries.propertyIn("id", userSubquery1)),
							Restrictions.not(Subqueries.propertyIn("id", userSubquery2))),
					Restrictions.not(Restrictions.eq("id", Main.sessao.getUsuario().getId()))));
		return naoAmigos;
	}
	
	public List<Usuario> listarAmigos(Usuario user){
		List<Amizade> amizades = findByCriteria(
				Restrictions.and(
						Restrictions.or(
								Restrictions.eq("usuario1", user), 
								Restrictions.eq("usuario2", user)), 
						Restrictions.eq("status", 1)));
		List<Usuario> amigos = new ArrayList<Usuario>();
		for(Amizade a:amizades){
			if(a.getUsuario1().getId() == user.getId())
				amigos.add(a.getUsuario2());
			else
				amigos.add(a.getUsuario1());
		}
		return amigos;
	}
	
	public void removerSolicitacaoAmizade(Usuario amigo) throws FriendshipException{
		try{
			Amizade amizade = procurarSolicitacao(amigo);
			if(amizade != null)
				delete(amizade);
		}catch(NullPointerException e){
			throw new FriendshipException("Solicitação não encontrada");
		}
	}
	
	public void removerAmigosUsuario(Usuario user){
		List<Usuario> usuariosSolicitacoes = listarSolicitacoes(user);
		try{
			for(Usuario u:usuariosSolicitacoes)
				removerSolicitacaoAmizade(u);
		}catch(FriendshipException e){}
	}
	
}
