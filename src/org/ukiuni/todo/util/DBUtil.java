package org.ukiuni.todo.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.eclipse.persistence.internal.jpa.EntityManagerFactoryImpl;
import org.eclipse.persistence.sessions.server.ServerSession;

public class DBUtil {
	private static EntityManagerFactory factory;

	public static EntityManager createEntityManager() {
		if (factory == null) {
			synchronized (DBUtil.class) {
				if (factory == null) {
					factory = Persistence.createEntityManagerFactory("todo");
				}
			}
		}
		return factory.createEntityManager();
	}

	public static void close() {
		try {
			((EntityManagerFactoryImpl) factory).unwrap(ServerSession.class).disconnect();
		} catch (Throwable e) {
		}
	}
}
