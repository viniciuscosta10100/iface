package br.ufal.ic.iFace.repository;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.model.Comunidade;
import br.ufal.ic.iFace.model.ComunidadeParticipantes;
import br.ufal.ic.iFace.model.Usuario;

public class ComunidadeRepository extends GenericHibernateRepository<Comunidade, Integer>{
		
	@Override
	public Comunidade save(Comunidade c){
		c = super.save(c);
		ComunidadeParticipantes cp = new ComunidadeParticipantes();
		cp.setComunidade(c);
		cp.setUsuario(c.getResponsavel());
		Main.comunidadeParticipantesRepository.save(cp);
		return c;
	}
	
	public List<Comunidade> listarMinhasComunidades(){
		List<Comunidade> comunidades = findByCriteria(Restrictions.eq("responsavel", Main.sessao.getUsuario()));
		return comunidades;
	}
	
	public List<Comunidade> procurarComunidades(){
		List<Comunidade> comunidades = new ArrayList<Comunidade>();
		DetachedCriteria subQuery = DetachedCriteria.forClass(ComunidadeParticipantes.class, "cp")
			    .setProjection( Projections.property("cp.comunidade"))
			    .add(Restrictions.eq("usuario", Main.sessao.getUsuario()));
		comunidades = findByCriteria(Restrictions.not(Subqueries.propertyIn("id", subQuery)));
		return comunidades;
	}
	
	public void removerComunidadeUsuario(Usuario u){
		List<Comunidade> comunidades = findByCriteria(Restrictions.eq("responsavel", u));
		for(Comunidade c:comunidades){
			Main.comunidadeParticipantesRepository.removerParticipantesComunidade(c);
			delete(c);
		}
	}
}
