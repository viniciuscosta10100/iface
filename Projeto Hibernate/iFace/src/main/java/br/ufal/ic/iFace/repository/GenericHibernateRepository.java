package br.ufal.ic.iFace.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;

import br.ufal.ic.iFace.Main;
import br.ufal.ic.iFace.interfaces.Repositorio;

public abstract class GenericHibernateRepository<T, ID extends Serializable> implements Repositorio<T, ID> {

	private Class<T> persistentClass;

	@SuppressWarnings("unchecked")
	public GenericHibernateRepository() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	
	protected Session getSession() {
		return Main.dbsession;
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) {
		T entity = (T) getSession().get(persistentClass, id);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return getSession().createCriteria(persistentClass).list();
	}

	public T save(T entity) {
		getSession().beginTransaction();
		getSession().save(entity);
		getSession().getTransaction().commit();
		Main.sessaoRepository.recarregarSessao();
		return entity;
	}
	
	public T update(T entity) {
		getSession().beginTransaction();
		getSession().update(entity);
		getSession().getTransaction().commit(); 
		Main.sessaoRepository.recarregarSessao();
		return entity;
	}

	public T merge(T entity) {
		getSession().beginTransaction();
		getSession().merge(entity);
		getSession().getTransaction().commit(); 
		return entity;
	}

	public void delete(T entity) {
		getSession().beginTransaction();
		getSession().delete(entity);
		getSession().getTransaction().commit(); 
		Main.sessaoRepository.recarregarSessao();
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		
		List<T> lista = crit.list();
		return lista;
	}

}