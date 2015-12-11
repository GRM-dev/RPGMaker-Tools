/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

import pl.grmdev.rpgmaker.multi.server.database.DatabaseHandler;
import pl.grmdev.rpgmaker.utils.CLogger;


/**
 * @author Levvy055
 *
 */
public class RpgServerContextListener implements ServletContextListener {
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		CLogger.initLogger();
		CLogger.info("RPG Server App initializing ...");
		DatabaseHandler.initConnection();
		CLogger.info("Connection initialized");
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		HibernateSessionFactory.closeSessionFactory();
		CLogger.info("RPG Server App closing ...");
		CLogger.closeLoggers();
	}
	
}
