/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fluent.hibernate.H;

import pl.grmdev.rpgmaker.multi.server.database.*;
import pl.grmdev.rpgmaker.multi.server.rest.inv.Inventory;

/**
 * @author Levvy055
 * 		
 */
@Entity
@Table(name = "characters")
@Path("/char")
public class Character {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "date_created", nullable = false)
	private Date creationDate;
	@Column(name = "date_last_save")
	private Date lastSaveDate;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@OneToOne
	@JoinColumn(name = "pos_id")
	private Position currentPosition;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "character")
	private List<Actor> actors;
	@OneToOne
	@JoinColumn(name = "inv_id")
	private Inventory inventory;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Position> positions;
	@OneToOne
	@JoinColumn(name = "vars_id")
	private Variables vars;
	@OneToOne
	@JoinColumn(name = "switches_id")
	private Switches switches;
	
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPlayer(@PathParam("username") String username, @QueryParam("default") boolean def, String body) {
		if (body == null || body.isEmpty()) {
			return Result.badRequest(true, "Received empty request!");
		}
		if (username == null || username.isEmpty()) {
			return Result.badRequest(true, "Received no username!");
		}
		try {
			JSONObject json = new JSONObject(body);
			String token = json.getString("authToken");
			DatabaseHandler.initConnection();
			username = username.toLowerCase();
			User user = H.<User> request(User.class).eq("username", username).first();
			if (user == null) {
				return Result.notFound(true, "User " + username + "not found");
			}
			if (!Token.verify(token, user.getId())) {
				return Result.noAuth(true, "wrong token");
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			Character player = mapper.readValue(json.getJSONObject("player").toString(), Character.class);
			if (player == null) {
				return Result.badRequest(true, "wrong player object received", json.getJSONObject("player").toString());
			}
			String pname = player.getName();
			if (pname == null || pname.isEmpty() || !pname.matches(User.USERNAME_PATTERN)) {
				return Result.badRequest(true, "Wrong username format", pname);
			}
			player.setCreationDate(new Date());
			if (def) {
				player.setNullToDefaults();
			}
			Character p2 = H.save(player);
			if (player.equals(p2)) {
				return Result.created(false, "Player created");
			} else {
				return Result.created(true, "Player created but not sure!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@GET
	@Path("/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayer(@PathParam("player") String playerName, @QueryParam("authToken") String token) {
		if (token == null || token.isEmpty()) {
			return Result.noAuth(true, "No token!");
		}
		if (playerName == null || playerName.isEmpty()) {
			return Result.badRequest(true, "Received no player name!");
		}
		DatabaseHandler.initConnection();
		Token tokObj = H.<Token> request(Token.class).eq("token", token.toCharArray()).first();
		if (tokObj == null) {
			return Result.noAuth(true, "Wrong token");
		}
		Character player = H.<Character> request(getClass()).eq("name", playerName).first();
		if (player == null) {
			return Result.notFound(true, "Player " + playerName + " not exist!");
		}
		return Result.json(player.toString());
	}
	
	@GET
	@Path("/id/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerById(@PathParam("player") int playerId, @QueryParam("authToken") String token) {
		if (playerId == 0) {
			return Result.badRequest(true, "Received no player id or id = 0!");
		}
		Character player = H.<Character> getById(Character.class, playerId);
		if (token == null || token.isEmpty()) {
			return Result.noAuth(false, player.getName());
		}
		return getPlayer(player.getName(), token);
	}
	
	@GET
	@Path("/name/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerNameById(@PathParam("player") int playerId) {
		if (playerId == 0) {
			return Result.badRequest(true, "Received no player id or id = 0!");
		}
		DatabaseHandler.initConnection();
		Character player = H.<Character> getById(Character.class, playerId);
		if (player == null) {
			return Result.notFound(false, "Player not exists with that id: " + playerId);
		}
		return Result.json("{\"id\":" + playerId + ",\"name\":\"" + player.getName() + "\"}");
	}

	@PUT
	@Path("{player}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePlayer(@PathParam("player") String playerName, String body) {
		if (body == null || body.isEmpty()) {
			return Result.badRequest(true, "No request body!");
		}
		if (playerName == null || playerName.isEmpty()) {
			return Result.badRequest(true, "Received no player name!");
		}
		try {
			JSONObject json = new JSONObject(body);
			String token = json.getString("authToken");
			if (token == null || token.isEmpty()) {
				return Result.noAuth(true, "No token provided!");
			}
			DatabaseHandler.initConnection();
			Character player = H.<Character> request(Character.class).eq("name", playerName).first();
			if (player == null) {
				return Result.notFound(true, "Player " + playerName + " not found!");
			}
			if (!Token.verify(token, player.getId())) {
				return Result.noAuth(true, "Wrong token!");
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			Character playerNew = mapper.readValue(json.getJSONObject("player").toString(), Character.class);
			player.updateFrom(playerNew);
			H.saveOrUpdate(player);
			return Result.success("updated!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	/**
	 * 
	 */
	private void setNullToDefaults() {
		if (currentPosition == null) {
			Position pos = new Position();
			pos.setX(0);// TODO: make defaults correspond to game true vars
			pos.setY(0);
			pos.setDirection(0);
			pos.setMap_id(0);
			setCurrentPosition(pos);
			DatabaseHandler.initConnection();
			Position pos2 = H.save(pos);
			if (!pos.equals(pos2)) {
				System.out.println("Position saved but can be broken.");
			}
		}
		if (inventory == null) {
			Inventory inv = new Inventory();
			setInventory(inv);
		}
	}
	
	/**
	 * @param playerNew
	 */
	private void updateFrom(Character p) {
		if (p.getCurrentPosition() != null && !p.getCurrentPosition().equals(getCurrentPosition())) {
			// getPosition().updatePosition(getName(),p.getPosition().toString());
			// TODO: make updates methods in subclasses
		}
	}
	
	/**
	 * @return
	 */
	public boolean correct() {
		if (getName() != null && getCurrentPosition() != null && getInventory() != null && getVars() != null
				&& getSwitches() != null && getActors() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public static boolean correct(Character player) {
		if (player == null) {
			return false;
		}
		return player.correct();
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
	public Position getCurrentPosition() {
		return currentPosition;
	}
	public void setCurrentPosition(Position position) {
		this.currentPosition = position;
	}
	public Inventory getInventory() {
		return inventory;
	}
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	public Variables getVars() {
		return vars;
	}
	public void setVars(Variables vars) {
		this.vars = vars;
	}
	public Switches getSwitches() {
		return switches;
	}
	public void setSwitches(Switches switches) {
		this.switches = switches;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastSaveDate() {
		return lastSaveDate;
	}
	public void setLastSaveDate(Date lastSaveDate) {
		this.lastSaveDate = lastSaveDate;
	}
	public List<Actor> getActors() {
		return actors;
	}
	public void setActors(List<Actor> actors) {
		this.actors = actors;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{\"id\":\"");
		builder.append(id);
		builder.append("\",\"");
		if (name != null) {
			builder.append("name\":\"");
			builder.append(name);
			builder.append("\",\"");
		}
		if (currentPosition != null) {
			builder.append("position\":\"");
			builder.append(currentPosition);
			builder.append("\",\"");
		}
		if (inventory != null) {
			builder.append("inventory\":\"");
			builder.append(inventory);
			builder.append("\",\"");
		}
		if (vars != null) {
			builder.append("vars\":\"");
			builder.append(vars);
			builder.append("\",\"");
		}
		if (switches != null) {
			builder.append("switches\":\"");
			builder.append(switches);
			builder.append("\",\"");
		}
		if (creationDate != null) {
			builder.append("creationDate\":\"");
			builder.append(creationDate);
			builder.append("\",\"");
		}
		if (lastSaveDate != null) {
			builder.append("lastSaveDate\":\"");
			builder.append(lastSaveDate);
			builder.append("\",\"");
		}
		if (actors != null) {
			builder.append("actors\":\"");
			builder.append(actors);
		}
		builder.append("\"} ");
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Character other = (Character) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id != 0 && other.id != 0)
			if (id != other.id)
				return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
