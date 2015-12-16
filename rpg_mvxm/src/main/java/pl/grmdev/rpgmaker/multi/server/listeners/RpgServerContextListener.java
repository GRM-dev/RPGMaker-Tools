/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.listeners;

import static pl.grmdev.rpgmaker.utils.CLogger.closeLoggers;
import static pl.grmdev.rpgmaker.utils.CLogger.info;
import static pl.grmdev.rpgmaker.utils.CLogger.initLogger;

import javax.servlet.*;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

import pl.grmdev.rpgmaker.multi.server.database.DatabaseHandler;


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
		initLogger();
		info("RPG Server App initializing ...");
		DatabaseHandler.initConnection();
		info("Connection initialized");
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		HibernateSessionFactory.closeSessionFactory();
		info("RPG Server App closing ...");
		closeLoggers();
	}
	
}
