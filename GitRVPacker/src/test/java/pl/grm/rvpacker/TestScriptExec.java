/**
 * 
 */
package pl.grm.rvpacker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;

import org.junit.Test;

/**
 * @author Levvy055
 *
 */
public class TestScriptExec {

	@Test
	public void testPack() {

	}

	@Test
	public void testUnpack() {

		String type = "unpack";
		String path = "P:\\Ruby22-x64\\bin";
		String dir = "P:\\temp-workspace\\RPGTestProject1";
		try {
			Process process = Runtime.getRuntime().exec(
					path + "\\rvpacker.bat --verbose -f -d " + dir + " -t ace -a " + type);

			new Thread(() -> {
				String line = null;
				int i = 0;
				try {
					BufferedReader bR = new BufferedReader(new InputStreamReader(process.getInputStream()));
					while ((line = bR.readLine()) != null) {
						if (line.isEmpty()) {
							System.out.print("{E}");
						}
						System.out.println(++i + ": " + line);
					}
					System.out.println("NULL got | Closing");
				}
				catch (Exception e) {
					e.printStackTrace();
					fail("Exception");
				}
			}).start();
			new Thread(() -> {
				String line = null;
				try {
					BufferedReader bR = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					while ((line = bR.readLine()) != null) {
						System.out.println("Err " + line);
					}
					System.out.println("NULL got");
				}
				catch (Exception e) {
					e.printStackTrace();
					fail("Exception");
				}
			}).start();;

			process.waitFor();
			assertEquals(0, process.exitValue());
		}
		catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}
		catch (InterruptedException e) {
			e.printStackTrace();
			fail("Exception");
		}
	}
}
