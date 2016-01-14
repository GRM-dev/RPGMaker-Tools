/**
* 
*/
package pl.grmdev.rpgmaker.multi.server.rest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import com.github.fluent.hibernate.H;

/**
 * @author Levvy055
 */
@Entity
@Table(name = "tokens")
@Path("token")
public class Token {
	
	@Id
	@GeneratedValue
	private int							id;
	@Column(name = "f_token", nullable = false, unique = true)
	private char[]						token;
	@Column(name = "f_expiration_time", nullable = false)
	private Date						expirationTime;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User						user;
											
	private static final Random	RANDOM	= new Random();
	private static final String	CHARS		= "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890!@_-$";
														
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generate(String body, @Context
	HttpServletRequest request) {
		if (StringUtils.isEmpty(body)) { return Result.badRequest(true, "Received empty request!"); }
		try {
			JSONObject obj = new JSONObject(body);
			String username = obj.getString("username");
			String pswd = obj.getString("password");
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pswd)) { return Result.badRequest(true, "Wrong credentials!"); }
			User user = H.<User> request(User.class).fetchJoin("tokens").eq("username", username).eq("password", pswd).first();
			if (user == null) { return Result.badRequest(true, "Wrong credentials!"); }
			user.setLastActive(new Date());
			H.saveOrUpdate(user);
			char[] token = null;
			boolean exists = true;
			int attempts = 0;
			List<Token> tokens = user.getTokens();
			do {
				attempts++;
				token = genToken(username, user.getId());
				Iterator<Token> it = tokens.iterator();
				Token tokenTemp = null;
				while (it.hasNext()) {
					Token tT = it.next();
					if (tT.getToken().equals(token)) {
						tokenTemp = tT;
					}
				}
				exists = tokenTemp != null;
				if (attempts > 6) { return Result.exception(new Exception("There was problem with token generator.")); }
			}
			while (exists);
			Token tokenObj = new Token();
			tokenObj.setToken(token);
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, 3);
			tokenObj.setExpirationTime(cal.getTime());
			tokenObj.setUser(user);
			Token tokenRespond = H.save(tokenObj);
			startCleanupThread(user, request.getRemoteAddr());
			if (tokenObj.equals(tokenRespond)) {
				Map<String, String> map = new HashMap<>();
				map.put("msg", "Token generated");
				map.put("token", new String(token));
				return Result.created(false, new JSONObject(map));
			}
			else {
				return Result.created(true, "Token generated but smth went wrong.");
			}
		}
		catch (Exception e) {
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
			token.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
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
	public Response getExpirationDate(@PathParam("username")
	String username, @QueryParam("authToken")
	String token) {
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) { return Result.badRequest(true, "No username specified and/or token", username, token); }
		System.out.println(token);
		try {
			Token tokenObj = H.<Token> request(Token.class).eq("token", token.toCharArray()).first();
			if (tokenObj == null) { return Result.notFound(false, true, "token not exists!"); }
			return Result.success(tokenObj.getExpirationTime().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	/**
	 * @param token
	 *           Token object to verify
	 * @param username
	 *           username
	 * @return true if token is valid
	 */
	public static boolean verify(String token, String username) {
		if (token != null && !token.isEmpty()) {
			List<Token> tokenList = H.<Token> request(Token.class).fetchJoin("user").eq("token", token.toCharArray()).list();
			if (tokenList != null && tokenList.size() > 0) {
				Token tokenObj = tokenList.get(0);
				if (tokenObj != null && tokenObj.getUser().getUsername().equals(username) && tokenObj.getExpirationTime().after(new Date())) { return true; }
			}
		}
		return false;
	}
	
	/**
	 * @param token
	 *           Token object to verify
	 * @param userId
	 *           user_id
	 * @return true if token is valid
	 */
	public static boolean verify(String token, int userId) {
		if (token != null && !token.isEmpty()) {
			List<Token> tokenList = H.<Token> request(Token.class).fetchJoin("user").eq("token", token.toCharArray()).list();
			if (tokenList != null && tokenList.size() > 0) {
				Token tokenObj = tokenList.get(0);
				if (tokenObj != null && tokenObj.getUser().getId() == userId && tokenObj.getExpirationTime().after(new Date())) { return true; }
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expirationTime == null) ? 0 : expirationTime.hashCode());
		result = prime * result + id;
		result = prime * result + Arrays.hashCode(token);
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Token other = (Token) obj;
		if (expirationTime == null) {
			if (other.expirationTime != null) return false;
		}
		else if (!expirationTime.equals(other.expirationTime)) return false;
		if (id != other.id) return false;
		if (!Arrays.equals(token, other.token)) return false;
		if (user == null) {
			if (other.user != null) return false;
		}
		else if (!user.equals(other.user)) return false;
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