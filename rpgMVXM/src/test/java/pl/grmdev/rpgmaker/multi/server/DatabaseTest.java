/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server;

import static org.junit.Assert.fail;

import org.junit.Test;

import pl.grmdev.rpgmaker.multi.server.database.DatabaseHandler;

/**
 * @author Levvy055
 *
 */
public class DatabaseTest {
	
	@Test
	public void initConnectionTest() {
		try {
			System.out.println("_");
			DatabaseHandler.initConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			// fail("Ecxeption: " + e);
		}
	}
	
	@Test
	public void closeConnectionTest() {
		try {
			DatabaseHandler.closeConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Exception: " + e.getMessage());
		}
	}
	
}
