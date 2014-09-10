package org.ukiuni.todo.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.beanutils.BeanUtils;
import org.ukiuni.todo.model.Project;
import org.ukiuni.todo.model.Todo;
import org.ukiuni.todo.model.Todo.Status;
import org.ukiuni.todo.util.DBUtil;

public class TodoService {
	public ProjectService projectService = new ProjectService();

	public void save(Todo... todos) {
		EntityManager em = DBUtil.createEntityManager();
		EntityTransaction transaction = em.getTransaction();
		transaction.begin();
		for (Todo todo : todos) {
			if (0 < todo.getId()) {
				Todo registedTodo = em.find(Todo.class, todo.getId());
				registedTodo.setTitle(todo.getTitle());
				registedTodo.setDescription(todo.getDescription());
				registedTodo.setUpdatedAt(new Date());
				em.merge(registedTodo);
			} else {
				em.persist(todo);
			}
		}
		transaction.commit();
		em.close();
	}

	public List<Todo> LoadByProjectKey(String key) {
		EntityManager em = DBUtil.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Todo> cq = cb.createQuery(Todo.class);
		Root<Todo> r = cq.from(Todo.class);
		Root<Project> pr = cq.from(Project.class);
		r.join("project");
		cq.where(cb.and(cb.equal(pr.get("key"), key), cb.notEqual(r.get("status"), Status.DELETED)));
		List<Todo> myEntities = em.createQuery(cq.select(r)).getResultList();
		Collections.sort(myEntities, new Comparator<Todo>() {
			@Override
			public int compare(Todo o1, Todo o2) {
				return (int) (o1.getIndex() - o2.getIndex());
			}
		});
		return myEntities;
	}

	public void merge(List<Todo> srcTodos, List<Todo> destTodos) {
		Map<Long, Todo> todoMap = new HashMap<Long, Todo>();
		for (Todo todo : destTodos) {
			if (0 != todo.getId()) {
				todoMap.put(todo.getId(), todo);
			}
		}
		for (Todo todo : srcTodos) {
			if (todoMap.containsKey(todo.getId())) {
				try {
					BeanUtils.copyProperties(todo, todoMap.get(todo.getId()));
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			} else {
				destTodos.add(todo);
			}
		}
	}
}
