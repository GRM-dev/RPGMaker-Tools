/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

/**
 * @author Levvy055
 *
 */
public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4431482150325195432L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			ServletOutputStream os = resp.getOutputStream();
			os.write("Hello from RPG Maker VX Server ;)  V: 1".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
