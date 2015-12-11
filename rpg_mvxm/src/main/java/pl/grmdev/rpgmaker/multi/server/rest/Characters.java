/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.github.fluent.hibernate.H;

/**
 * @author Levvy055
 *
 */
@Path("chars")
public class Characters {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		List<Character> playerList = H.<Character> request(Character.class).list();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (playerList != null && playerList.size() > 0) {
		for (Character player : playerList) {
			sb.append("{\"id\":" + player.getId() + ",\"name\":\"" + player.getName() + "\"},");
			}
		} else {
			sb.append(' ');
		}
		String r = sb.substring(0, sb.length() - 1) + "]";
		return Result.json(r);
	}
}
