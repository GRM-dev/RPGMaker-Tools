/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.listeners;

import javax.servlet.*;

import org.apache.log4j.Logger;

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
		Logger l = Logger.getLogger("sql");
		l.info("RPG Server App initialization");
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
		CLogger.closeLogger();
	}
	
}
