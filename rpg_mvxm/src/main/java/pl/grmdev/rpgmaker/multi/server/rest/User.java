/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.HibernateException;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.request.HibernateRequest;

/**
 * @author Levvy055
 * 		
 */
@Entity
@Table(name = "users")
@Path("user")
public class User {
	
	@Id
	@GeneratedValue(
			strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "f_name", unique = true, nullable = false)
	private String username;
	@Column(name = "f_pasword", nullable = false)
	private String password;
	@Column(name = "f_email", nullable = false, unique = true)
	private String email;
	@Column(name = "register_date", nullable = false)
	private Date registerDate;
	@Column(name = "time_last_active")
	private Date lastActive;
	@Column(name = "f_user_level", nullable = false)
	private int level;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Token> tokens;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Fetch(FetchMode.SUBSELECT)
	private Set<Character> characters;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "friends", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") , inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id") )
	private List<User> friends;
	public static final String USERNAME_PATTERN = "^[a-z0-9A-Z_-]{3,15}$";
	
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(@PathParam("username") String username, String payload) {
		if (username == null || username.isEmpty() || payload == null || payload.isEmpty()) { return Result.badRequest(true, null, username, payload); }
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			User user = mapper.readValue(payload, getClass());
			if (user == null) { return Result.badRequest(true, "Payload wrong format or incomplete"); }
			if (!username.toLowerCase().equals(user.getUsername().toLowerCase())) { return Result.badRequest(true, "Name in parameter is not the same as in body."); }
			if (user.getPassword() == null) { return Result.badRequest(true, "No password provided."); }
			if (user.getEmail() == null) { return Result.badRequest(true, "No e-mail provided."); }
			user.setUsername(user.getUsername().toLowerCase());
			user.setRegisterDate(new Date());
			user.setLastActive(new Date());
			User user2 = H.save(user);
			if (user.equals(user2)) {
				return Result.created(false, "User created successfully");
			}
			else {
				return Result.created(true, "User propably created because some problems occured during request execution.");
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}

	@Path("/{username}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("username") String username,
			@QueryParam("authToken") String token) {
		try {
			HibernateRequest<User> request = H.<User> request(User.class);
			List<User> userList = request.fetchJoin("friends", "characters").eq("username", username).list();
			User user = null;
			if (userList == null || userList.isEmpty()
					|| (user = userList.get(0)) == null) { return Result.notFound(true, false, "User with name " + username + " was not found"); }
			String res;
			if (token != null && !token.isEmpty() && Token.verify(token, user.getId())) {
				res = user.toString();
				user.setLastActive(new Date());
			}
			else {
				res = "{\"id\": " + user.getId() + ", \"name\": \""
						+ user.getUsername() + "\"}";
			}
			System.out.println("name: " + username + "\ntoken: " + token);
			return Result.json(res);
		} catch (HibernateException e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@PUT
	@Path("/pswd/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("username")
	String username,
			String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) {
				return Result.noAuth(false,true, "Wrong token: " + token);
			}
			username = username.toLowerCase();
			if (!username.equals(jObj.getString("username").toLowerCase())) {
				return Result.noAuth(false,true, "No access for that user!");
			}
			String oldPswd = jObj.getString("oldPassword");
			String newPswd = jObj.getString("newPassword");
			User user = H.<User> request(User.class).eq("username", username)
					.first();
			if (user == null) {
				return Result.notFound(false,true, "User '" + username + "' not found");
			}
			if (!user.getPassword().equals(oldPswd)) {
				return Result.noAuth(false,true, "Wrong credentials!");
			}
			if (newPswd == null || newPswd.length() <= 4) {
				return Result.badRequest(true,
						"Wrong Password! It should be longer than 3 letters.");
			}
			user.setPassword(newPswd);
			user.setLastActive(new Date());
			H.saveOrUpdate(user);
			return Result.success("Password updated");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@PUT
	@Path("/mail/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeEmail(@PathParam("username")
	String username, String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) { return Result.noAuth(false, true, "Wrong token: " + token); }
			username = username.toLowerCase();
			if (!username.equals(jObj.getString("username").toLowerCase())) { return Result.noAuth(false, true, "No access for that user!"); }
			String mail = jObj.getString("mail");
			Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			Matcher matcher = pattern.matcher(mail);
			if (mail == null || mail.length() <= 4 || !matcher.matches()) { return Result.badRequest(true, "No mail provided or wrong mail syntax!"); }
			User user = H.<User> request(User.class).eq("username", username).first();
			if (user == null) { return Result.notFound(false, true, "User '" + username + "' not found"); }
			user.setLastActive(new Date());
			user.setEmail(mail);
			H.saveOrUpdate(user);
			return Result.success("Mail updated");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@DELETE
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(@PathParam("username")
	String username, String payload) {
		try {
			JSONObject jObj = new JSONObject(payload);
			String token = jObj.getString("authToken");
			if (!token.equals("token")) { return Result.noAuth(false, true, "Wrong token: " + token); }
			username = username.toLowerCase();
			if (!username.equals(jObj.getString("username").toLowerCase())) { return Result.noAuth(false, true, "No access for that user!"); }
			String pswd = jObj.getString("password");
			User user = H.<User> request(User.class).eq("username", username).first();
			if (!user.getPassword().equals(pswd)) { return Result.noAuth(false, true, "Wrong credentials!"); }
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
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
			return false;
		}
		else if (!email.equals(other.email)) return false;
		if (id != other.id) return false;
		if (username == null) {
			if (other.username != null)
			return false;
		}
		else if (!username.equals(other.username)) return false;
		if (password == null) {
			if (other.password != null)
			return false;
		}
		else if (!password.equals(other.password)) return false;
		if (registerDate == null) {
			if (other.registerDate != null)
			return false;
		}
		else if (!registerDate.equals(other.registerDate)) return false;
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
		Set<Character> charsT = this.getCharacters();
		if (charsT != null) {
			builder.append("\",\"");
			builder.append("characters\":[");
			for (Iterator<Character> iterator = charsT.iterator(); iterator.hasNext();) {
				Character character = iterator.next();
				builder.append("{\"id\":" + character.getId() + ", \"name\":\"" + character.getName() + "\"}");
				if (iterator.hasNext()) {
					builder.append(',');
				}
			}
			builder.append("], \"chars_count\":\"" + charsT.size());
		}
		List<User> friendsT = this.getFriends();
		if (friendsT != null) {
			builder.append("\",\"");
			builder.append("friends\":[");
			for (Iterator<User> iterator = friendsT.iterator(); iterator.hasNext();) {
				User user = iterator.next();
				builder.append("{\"id\":" + user.getId() + ", \"name\":\"" + user.getUsername() + "\"}");
				if (iterator.hasNext()) {
					builder.append(',');
				}
			}
			builder.append("], \"friends_count\":\"" + friendsT.size());
		}
		if (email != null) {
			builder.append("\",\"");
			builder.append("email\":\"");
			builder.append(email);
		}
		builder.append("\",\"");
		builder.append("level\":\"");
		builder.append(level);
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
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<Token> getTokens() {
		return tokens;
	}
	
	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}
	
	public Set<Character> getCharacters() {
		return characters;
	}
	
	public void setCharacters(Set<Character> characters) {
		this.characters = characters;
	}
}
