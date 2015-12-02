/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.util.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.JSONObject;

import com.github.fluent.hibernate.H;

import pl.grmdev.rpgmaker.multi.server.database.*;

/**
 * @author Levvy055
 * 		
 */
@Entity
@Table(name = "tokens")
@Path("token")
public class Token {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "f_token", nullable = false, unique = true)
	private char[] token;
	@Column(name = "f_expiration_time", nullable = false)
	private Date expirationTime;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne
	private User user;
	private static final Random random = new Random();
	public static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@_-$";
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generate(String body, @Context HttpServletRequest request) {
		if (body == null || body.isEmpty()) {
			return Result.badRequest(true, "Received empty request!");
		}
		try {
			JSONObject obj = new JSONObject(body);
			String username = obj.getString("username");
			String pswd = obj.getString("password");
			if (username == null || username.isEmpty()
					|| pswd == null | pswd.isEmpty()) {
				return Result.badRequest(true, "Wrong credentials!");
			}
			DatabaseHandler.initConnection();
			User user = H.<User> request(User.class).eq("username", username)
					.eq("password", pswd).first();
			if (user == null) {
				return Result.badRequest(true, "Wrong credentials!");
			}
			char[] token = null;
			boolean exists = true;
			int attempts = 0;
			do {
				attempts++;
				token = genToken(username, user.getId());
				Token tokenTemp = H.<Token> request(getClass())
						.eq("token", token).first();
				exists = tokenTemp != null;
				
				if (tokenTemp != null) {
					System.out.println(new String(tokenTemp.getToken()));
					System.out.println(tokenTemp.getId() + " | " + attempts);
				} else
					System.out.println("null tt");
				if (token == null)
					System.out.println("t null");
				else
					System.out.println(new String(token));
					
				if (attempts > 6) {
					return Result.exception(new Exception(
							"There was problem with token generator."));
				}
			} while (exists);
			Token tokenObj = new Token();
			tokenObj.setToken(token);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, 1);
			tokenObj.setExpirationTime(cal.getTime());
			tokenObj.setUser(user);
			Token tokenRespond = H.save(tokenObj);
			startCleanupThread(user, request.getRemoteAddr());
			if (tokenObj.equals(tokenRespond)) {
				return Result.created(false,
						"Token generated. " + new String(token));
			} else {
				return Result.created(true,
						"Token generated but smth went wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	/**
	 * @return
	 */
	private char[] genToken(String name, int id) {
		StringBuilder token = new StringBuilder();
		for (int i = 0; i < 120; i++) {
			token.append(CHARS.charAt(random.nextInt(CHARS.length())));
		}
		return token.toString().toCharArray();
	}
	
	/**
	 * @param user
	 * @param address
	 */
	private void startCleanupThread(User user, String address) {
		Thread thread = new Thread(() -> {
			System.out.println("Started token cleanup thread for " + address);
			List<Object> list = H.request(Token.class).eq("user", user).list();
			for (Object object : list) {
				Token token = (Token) object;
				if (token != null) {
					Date expTime = token.getExpirationTime();
					if (expTime.before(new Date())) {
						H.delete(token);
					}
				}
			}
		});
		thread.setName("token cleanup thread: " + address);
		thread.start();
	}
	
	@GET
	@Path("/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getExpirationDate(@PathParam("username") String username, @QueryParam("authToken") String token) {
		if (username == null || username.isEmpty() || token == null || token.isEmpty()) {
			return Result.badRequest(true, "No username specified and/or token", username, token);
		}
		System.out.println(token);
		try {
			DatabaseHandler.initConnection();
			Token tokenObj = H.<Token> request(Token.class).eq("token", token.toCharArray()).first();
			if (tokenObj == null) {
				return Result.notFound(true, "token not exists!");
			}
			return Result.success(tokenObj.getExpirationTime().toString());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
		
	}
	
	/**
	 * @param token
	 * @return
	 */
	public static boolean verify(String token, int id) {
		if (token != null && !token.isEmpty()) {
			Token tokenObj = H.<Token> request(Token.class).eq("token", token.toCharArray()).first();
			if (tokenObj != null) {
				if (tokenObj.getUser().getId() == id) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((expirationTime == null) ? 0 : expirationTime.hashCode());
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(token);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		Token other = (Token) obj;
		if (expirationTime == null) {
			if (other.expirationTime != null)
				return false;
		} else if (!expirationTime.equals(other.expirationTime))
			return false;
		if (id != other.id)
			return false;
		if (!Arrays.equals(token, other.token))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public char[] getToken() {
		return token;
	}
	
	public void setToken(char[] token) {
		this.token = token;
	}
	
	public Date getExpirationTime() {
		return expirationTime;
	}
	
	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
}
