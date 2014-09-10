package org.ukiuni.todo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.ukiuni.todo.model.Project;
import org.ukiuni.todo.model.Todo;
import org.ukiuni.todo.util.DBUtil;

public class ProjectService {

	public void update(Project project) {
		EntityManager em = DBUtil.createEntityManager();
		try {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();
			Project registedProject = em.find(Project.class, project.getId());
			List<Todo> registedTodo = registedProject.getTodos();
			// TODO not moved
			// new TodoService().merge(project.getTodos(), registedTodo);
			// registedProject.setTodos(project.getTodos());
			registedProject.setName("testName");
			em.merge(registedProject);
			transaction.commit();
		} finally {
			em.close();
		}
	}

	public Project load(String key) {
		EntityManager em = DBUtil.createEntityManager();
		try {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Project> cq = cb.createQuery(Project.class);
			Root<Project> r = cq.from(Project.class);
			cq.where(cb.equal(r.get("key"), key));
			return em.createQuery(cq.select(r)).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			em.close();
		}
	}

	public Project create(String projectName) {
		Project project = new Project();
		project.setKey(UUID.randomUUID().toString());
		project.setName(projectName);
		project.setTodos(new ArrayList<Todo>());
		project.setCreatedAt(new Date());
		EntityManager em = DBUtil.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		em.persist(project);
		transaction.commit();
		return project;
	}
}
