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

public class ComunidadeParticipantesRepository extends GenericHibernateRepository<ComunidadeParticipantes, Integer>{
	
	public boolean adicionarParticipanteComunidade(Usuario participante, Comunidade comunidade){
		ComunidadeParticipantes cp = new ComunidadeParticipantes();
		cp.setComunidade(comunidade);
		cp.setUsuario(participante);
		save(cp);
		return true;
	}
	
	public ComunidadeParticipantes procurarComunidadeParticipante(Usuario participante, Comunidade comunidade){
		try{
			ComunidadeParticipantes cp = findByCriteria(
					Restrictions.and(
							Restrictions.eq("usuario", participante), 
							Restrictions.eq("comunidade", comunidade))).get(0);
			return cp;
		}catch(IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public List<Usuario> listarUsuariosParticipanteComunidade(Comunidade comunidade){
		List<ComunidadeParticipantes> cps = findByCriteria(Restrictions.eq("comunidade", comunidade));
		List<Usuario> participantes = new ArrayList<Usuario>();
		for(ComunidadeParticipantes cp:cps){
			if(cp.getUsuario().getId() != Main.sessao.getUsuario().getId())
				participantes.add(Main.usuarioRepository.findById(cp.getUsuario().getId()));
		}
		return participantes;
	}
	
	public List<Usuario> listarUsuariosNaoParticipanteComunidade(Comunidade comunidade){		
		List<Usuario> naoParticipantes = new ArrayList<Usuario>();
		DetachedCriteria subQuery = DetachedCriteria.forClass(ComunidadeParticipantes.class, "cp")
			    .setProjection( Projections.property("cp.usuario"))
			    .add(Restrictions.eq("comunidade", comunidade));
		naoParticipantes = Main.usuarioRepository.findByCriteria(Restrictions.not(Subqueries.propertyIn("id", subQuery)));
		return naoParticipantes;
	}
	
	public List<Comunidade> listarComunidadesParticipante(Usuario u){
		List<ComunidadeParticipantes> cps = findByCriteria(Restrictions.eq("usuario", u));
		List<Comunidade> comunidades = new ArrayList<Comunidade>();
		for(ComunidadeParticipantes cp:cps){
			comunidades.add(cp.getComunidade());
		}
		return comunidades;
	}

	
	public void removerParticipanteComunidade(Usuario participante, Comunidade comunidade){
		ComunidadeParticipantes cp = procurarComunidadeParticipante(participante, comunidade);
		delete(cp);
	}
	
	public void removerParticipacoesUsuario(Usuario u){
		List<ComunidadeParticipantes> cps = findByCriteria(Restrictions.eq("usuario", u));
		for(ComunidadeParticipantes cp:cps){
			delete(cp);
		}
	}
	
	public void removerParticipantesComunidade(Comunidade comunidade){
		List<ComunidadeParticipantes> cps = findByCriteria(Restrictions.eq("comunidade", comunidade));
		for(ComunidadeParticipantes cp:cps){
			delete(cp);
		}
	}

}
