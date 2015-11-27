/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.database;

import java.util.Properties;

import org.hibernate.*;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

import pl.grmdev.rpgmaker.multi.server.rest.*;

/**
 * @author Levvy055
 *
 */
public class DatabaseHandler {
	
	private static SessionFactory factory;

	public static void initConnection() throws HibernateException {
		if (factory == null || factory.isClosed()) {
			Configuration configuration = new Configuration().configure();
			Properties properties = configuration.getProperties();
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(properties).build();
			MetadataSources metadata = new MetadataSources(serviceRegistry);
			metadata.addAnnotatedClass(User.class);
			metadata.addAnnotatedClass(Token.class);
			factory = metadata.buildMetadata().buildSessionFactory();
			try {
				HibernateSessionFactory.Builder.configureFromExistingSessionFactory(factory);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}
	
	public static SessionFactory getSessionFacotry() {
		if (factory == null || factory.isClosed()) {
			initConnection();
		}
		return factory;
	}
	
	public void closeConnection() {
		if (factory != null) {
			Session session;
			try {
				if ((session = factory.getCurrentSession()) != null && session.isConnected()) {
					session.disconnect();
					session.close();
				}
			} catch (Exception e) {}
			finally {
				HibernateSessionFactory.closeSessionFactory();
			}
		}
	}
	
	public static void closeSession(Session session) {
		if (session != null && (session.isConnected() || session.isOpen())) {
			session.close();
		}
	}
}
