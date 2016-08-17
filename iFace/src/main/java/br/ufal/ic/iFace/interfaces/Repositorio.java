package br.ufal.ic.iFace.interfaces;

import java.util.List;

public interface Repositorio<T, ID> {
	public T findById(ID id);

	public List<T> findAll();

	public T save(T entity);
	
	public T update(T entity);

	public T merge(T entity);

	public void delete(T entity);

	public void clear();
}
