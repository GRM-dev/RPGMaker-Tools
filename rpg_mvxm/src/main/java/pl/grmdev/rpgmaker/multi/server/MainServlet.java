/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * @author Levvy055
 *
 */
public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = -4431482150325195432L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			Configuration conf = new Configuration().configure();
			Properties properties = conf.getProperties();
			ServletOutputStream os = resp.getOutputStream();
			for (Object obj : properties.keySet()) {
				os.write(((String) obj + ": "
						+ properties.getProperty((String) obj)).getBytes());
			}
			SessionFactory sessionFactory = conf.buildSessionFactory();
			os.write(
					("Hello from RPG Maker VX Server ;)  V: 1")
							.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
