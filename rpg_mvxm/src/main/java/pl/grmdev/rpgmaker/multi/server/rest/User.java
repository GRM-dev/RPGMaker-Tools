/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.HibernateException;

import com.github.fluent.hibernate.H;

import pl.grmdev.rpgmaker.multi.server.database.*;

/**
 * @author Levvy055
 *
 */
@Entity
@Table(name = "users")
@Path("user")
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "f_name")
	private String name;
	@Column(name = "register_date")
	private Timestamp registerDate;
	@Column(name = "time_last_active")
	private Timestamp lastActive;
	@Column(name = "f_pasword")
	private String password;
	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Transient
	private List<Player> players;
	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Transient
	private List<User> friends;
	@Column(name = "f_email")
	private String email;
	
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("name") String name, @QueryParam("authToken") String token) {
		try {
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("name", name).first();
			if (user == null) {
				return Result.notFound(false, "User with name " + name + "was not found");
			}
			String res;
			if (token != null && !token.isEmpty() && token.trim().equals("token")) {
				res = user.toString();
			} else {
				res = "{\"id\": " + user.getId() + ", \"name\": \"" + user.getName() + "\"}";
			}
			System.out.println("name: " + name + "\ntoken: " + token);
			return Result.json(res);
		} catch (HibernateException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(String payload) {
		try {
			DatabaseHandler.initConnection();
			System.out.println("payload:" + payload);
			return Result.created(false, "User created");
		} catch (HibernateException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":\"");
		builder.append(id);
		builder.append("\",\"");
		builder.append("name\":\"");
		builder.append(name);
		if (registerDate != null) {
			builder.append("\",\"");
			builder.append("registerDate\":\"");
			builder.append(registerDate);
		}
		if (lastActive != null) {
			builder.append("\",\"");
			builder.append("lastActive\":\"");
			builder.append(lastActive);
		}
		if (players != null) {
			builder.append("\",\"");
			builder.append("players\":\"");
			builder.append("{" + players + "}");
			
		}
		if (friends != null) {
			builder.append("\",\"");
			builder.append("friends\":\"");
			builder.append(friends);
		}
		if (email != null) {
			builder.append("\",\"");
			builder.append("email\":\"");
			builder.append(email);
		}
		builder.append("\"} ");
		return builder.toString();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Timestamp getRegisterDate() {
		return registerDate;
	}
	
	public void setRegisterDate(Timestamp registerDate) {
		this.registerDate = registerDate;
	}
	
	public Timestamp getLastActive() {
		return lastActive;
	}
	
	public void setLastActive(Timestamp lastActive) {
		this.lastActive = lastActive;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	
	public List<User> getFriends() {
		return friends;
	}
	
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
}
