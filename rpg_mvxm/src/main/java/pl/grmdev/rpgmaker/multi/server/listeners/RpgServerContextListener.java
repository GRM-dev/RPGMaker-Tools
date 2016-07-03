/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.listeners;

import static pl.grmdev.rpgmaker.utils.CLogger.closeLoggers;
import static pl.grmdev.rpgmaker.utils.CLogger.info;
import static pl.grmdev.rpgmaker.utils.CLogger.initLogger;
import static pl.grmdev.rpgmaker.utils.CLogger.warn;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;

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
		info("RPG Server App closing ...");
		DatabaseHandler.closeConnection();
		try {
			AbandonedConnectionCleanupThread.shutdown();
		}
		catch (InterruptedException e) {
			warn("SEVERE problem cleaning up: " + e.getMessage());
			e.printStackTrace();
		}
		closeLoggers();
	}
}
