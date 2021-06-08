package com.zerti.utilities.repository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public abstract class AbstractRepository<K, E> implements CrudRepository<K, E> {

	protected Class<K> entityClass;

	@PersistenceContext
	protected EntityManager em;
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.entityClass = (Class<K>) genericSuperclass.getActualTypeArguments()[0];
	}

	
	@Override
	@Transactional
	public <S extends K> S save(S entity) {
		em.persist(entity);
		return entity;
	}
	
	@Transactional
	public <S extends K> S modify(S entity) {
		em.merge(entity);
		return entity;
	}

	@Override
	@Transactional
	public <S extends K> Iterable<S> saveAll(Iterable<S> entities) {
		for (S s : entities) {
			em.persist(s);
		}
		return entities;
	}

	@Override
	public Optional<K> findById(E id) {
		K k = em.find(entityClass, id);
		if(k != null) {
			return Optional.of(k);
		}
		return Optional.empty();
	}

	@Override
	public boolean existsById(E id) {
		return Optional.of(em.find(entityClass, id)).isPresent();
	}

	@Override
	public List<K> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<K> cq = cb.createQuery(entityClass);
	    Root<K> rootEntry = cq.from(entityClass);
	    CriteriaQuery<K> all = cq.select(rootEntry);
	    TypedQuery<K> allQuery = em.createQuery(all);
	    return allQuery.getResultList();
	}

	@Override
	public Iterable<K> findAllById(Iterable<E> ids) {
		List<K> list = new ArrayList<>();
		for (E e : ids) {
			Optional<K> opt = Optional.of(em.find(entityClass, e));
			if(opt.isPresent()) {
				list.add(opt.get());
			}
		}
		return list;
	}

	@Override
	public long count() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<K> cq = cb.createQuery(entityClass);
	    Root<K> rootEntry = cq.from(entityClass);
	    CriteriaQuery<K> all = cq.select(rootEntry);
	    TypedQuery<K> allQuery = em.createQuery(all);
	    return allQuery.getResultList().size();
	}

	@Override
	public void deleteById(E id) {
		Optional<K> opt = Optional.of(em.find(entityClass, id));
		if(opt.isPresent()){
			em.remove(opt.get());
		}
	}

	@Override
	@Transactional
	public void delete(K entity) {
		em.remove(entity);
		
	}

	@Override
	public void deleteAll(Iterable<? extends K> entities) {
		for (K k : entities) {
			em.remove(k);
		}
	}

	@Override
	public void deleteAll() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaDelete<K> query = builder.createCriteriaDelete(entityClass);
		query.from(entityClass);
		em.createQuery(query).executeUpdate();
	}
	
	public List<K> findAllByField(String field, String value) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
	    CriteriaQuery<K> cq = cb.createQuery(entityClass);
	    Root<K> rootEntry = cq.from(entityClass);
	    cq.select(rootEntry).where(cb.equal(rootEntry.get(field), value));
	    return em.createQuery(cq).getResultList();
	}
	
}
