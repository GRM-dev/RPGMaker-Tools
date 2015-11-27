/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.hibernate.HibernateException;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	@Column(name = "f_name", unique = true, nullable = false)
	private String username;
	@Column(name = "register_date", nullable = false)
	private Date registerDate;
	@Column(name = "time_last_active")
	private Date lastActive;
	@Column(name = "f_pasword")
	private String password;
	@Column(name = "f_email", nullable = false, unique = true)
	private String email;
	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Transient
	private List<Player> players;
	// @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Transient
	private List<User> friends;
	
	@Path("/{name}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("name") String name, @QueryParam("authToken") String token) {
		try {
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("username", name).first();
			if (user == null) {
				return Result.notFound(false, "User with name " + name + "was not found");
			}
			String res;
			if (token != null && !token.isEmpty() && token.trim().equals("token")) {
				res = user.toString();
			} else {
				res = "{\"id\": " + user.getId() + ", \"name\": \"" + user.getUsername() + "\"}";
			}
			System.out.println("name: " + name + "\ntoken: " + token);
			return Result.json(res);
		} catch (HibernateException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@POST
	@Path("/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@PathParam("user") String name, String payload) {
		if (name == null || name.isEmpty() || payload == null || payload.isEmpty()) {
			return Result.badRequest(true, null, name, payload);
		}
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			User user = mapper.readValue(payload, getClass());
			if (user == null) {
				return Result.badRequest(true, "Payload wrong format or incomplete");
			}
			if (!name.equals(user.getUsername())) {
				return Result.badRequest(true, "Name in parameter is not the same as in body.");
			}
			if (user.getPassword() == null) {
				return Result.badRequest(true, "No password provided.");
			}
			if (user.getEmail() == null) {
				return Result.badRequest(true, "No e-mail provided.");
			}
			user.setRegisterDate(new Date());
			DatabaseHandler.initConnection();
			User user2 = H.save(user);
			if (user.equals(user2)) {
				return Result.created(false, "User created successfully");
			} else {
				return Result.created(true,
						"User propably created because some problems occured during request execution.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@PUT
	@Path("/pswd/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("user") String username, String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) {
				return Result.noAuth(true, "Wrong token: " + token);
			}
			if (!username.equals(jObj.getString("username"))) {
				return Result.noAuth(true, "No access for that user!");
			}
			String oldPswd = jObj.getString("oldPassword");
			String newPswd = jObj.getString("newPassword");
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("username", username).first();
			if (!user.getPassword().equals(oldPswd)) {
				return Result.noAuth(true, "Wrong credentials!");
			}
			if (newPswd == null || newPswd.length() <= 4) {
				return Result.badRequest(true, "Wrong Password! It should be longer than 3 letters.");
			}
			user.setPassword(newPswd);
			H.saveOrUpdate(user);
			return Result.success("Password updated");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@PUT
	@Path("/mail/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeEmail(@PathParam("user") String username, String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) {
				return Result.noAuth(true, "Wrong token: " + token);
			}
			if (!username.equals(jObj.getString("username"))) {
				return Result.noAuth(true, "No access for that user!");
			}
			String mail = jObj.getString("mail");
			Pattern pattern=Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			Matcher matcher = pattern.matcher(mail);
			if (mail == null || mail.length() <= 4 || !matcher.matches()) {
				return Result.badRequest(true, "No mail provided or wrong mail syntax!");
			}
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("username", username).first();
			user.setEmail(mail);
			H.saveOrUpdate(user);
			return Result.success("Mail updated");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@DELETE
	@Path("/{user}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("user") String username, String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) {
				return Result.noAuth(true, "Wrong token: " + token);
			}
			if (!username.equals(jObj.getString("username"))) {
				return Result.noAuth(true, "No access for that user!");
			}
			String pswd = jObj.getString("password");
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("username", username).first();
			if (!user.getPassword().equals(pswd)) {
				return Result.noAuth(true, "Wrong credentials!");
			}
			H.delete(user);
			return Result.success("User '" + username + "' deleted");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((registerDate == null) ? 0 : registerDate.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (registerDate == null) {
			if (other.registerDate != null)
				return false;
		} else if (!registerDate.equals(other.registerDate))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":\"");
		builder.append(id);
		builder.append("\",\"");
		builder.append("name\":\"");
		builder.append(username);
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
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String name) {
		this.username = name;
	}
	
	public Date getRegisterDate() {
		return registerDate;
	}
	
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	public Date getLastActive() {
		return lastActive;
	}
	
	public void setLastActive(Date lastActive) {
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
