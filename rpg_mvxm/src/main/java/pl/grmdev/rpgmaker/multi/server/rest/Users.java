/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.HibernateException;

import com.github.fluent.hibernate.H;

@Path("users")
public class Users {
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		String res = "[";
		try{
			List<User> users = H.<User> request(User.class).list();
		for (User user : users) {
			res += "{\"id\": " + user.getId() + ",";
			res += "\"username\": \"" + user.getUsername() + "\"},";
		}
		res = res.substring(0, res.length() - 1);
		res += "]";
			return Result.json(res);
		} catch (HibernateException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
}