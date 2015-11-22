/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Levvy055
 *
 */
@Path("user")
public class User {
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getUser() {
		return "<p>Called User API</p>";
	}
	
	@Path("/name")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getUserName() {
		return "<p>Username: _______</p>";
	}
	
}
