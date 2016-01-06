/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 * @author Levvy055
 *
 */
public class DatabaseTest {
	
	@Test
	public void initConnectionTest() {
		try {
			System.out.println("_");// DatabaseHandler.initConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Ecxeption: " + e.getMessage());
		}
	}
	
	@Test
	public void closeConnectionTest() {
		try {
			HibernateSessionFactory.closeSessionFactory();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.getMessage());
		}
	}
	
}
