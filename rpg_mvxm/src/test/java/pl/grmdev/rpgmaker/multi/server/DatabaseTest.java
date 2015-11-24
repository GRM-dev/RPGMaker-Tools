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
			DatabaseHandler.initConnection();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Ecxeption: " + e.getMessage());
		}
	}
	
}
