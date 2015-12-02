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

/**
 * @author Levvy055
 * 		
 */
@Entity
@Table(name = "players")
@Path("/player")
public class Player {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "name")
	private String name;
	@OneToOne
	@JoinColumn(name = "pos_id")
	private Position position;
	@OneToOne
	@JoinColumn(name = "inv_id")
	private Inventory inventory;
	@OneToOne
	@JoinColumn(name = "vars_id")
	private Variables vars;
	@OneToOne
	@JoinColumn(name = "switches_id")
	private Switches switches;
	@OneToOne
	@JoinColumn(name = "mdata_id")
	private MultiplayerData mData;
	@Column(name = "date_created")
	private Date creationDate;
	@Column(name = "date_last_save")
	private Date lastSaveDate;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "f_actors")
	private List<Actor> actors;
	
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addPlayer(@PathParam("username") String username, String body) {
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
			Player player = mapper.readValue(json.getJSONObject("player").toString(), Player.class);
			if (player == null) {
				return Result.badRequest(true, "wrong player object received", json.getJSONObject("player").toString());
			}
			String pname = player.getName();
			if (pname == null || pname.isEmpty() || !pname.matches(User.USERNAME_PATTERN)) {
				return Result.badRequest(true, "Wrong username format", pname);
			}
			player.setCreationDate(new Date());
			Player p2 = H.save(player);
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
		Player player = H.<Player> request(getClass()).eq("name", playerName).first();
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
		Player player = H.<Player> getById(Player.class, playerId);
		if (token == null || token.isEmpty()) {
			return Result.noAuth(false, player.getName());
		}
		return getPlayer(player.getName(), token);
	}
	
	@GET
	@Path("/name/{player}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPlayerName(@PathParam("player") int playerId) {
		if (playerId == 0) {
			return Result.badRequest(true, "Received no player id or id = 0!");
		}
		DatabaseHandler.initConnection();
		Player player = H.<Player> getById(Player.class, playerId);
		return Result.json("{\"id\":" + playerId + ",\"name\":\"" + player.getName() + "\"}");
	}

	/**
	 * @return
	 */
	public boolean correct() {
		if (getName() != null && getPosition() != null && getInventory() != null && getVars() != null
				&& getSwitches() != null && getMData() != null && getActors() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public static boolean correct(Player player) {
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
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
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
	
	public MultiplayerData getMData() {
		return mData;
	}
	public void setmData(MultiplayerData mData) {
		this.mData = mData;
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
		if (position != null) {
			builder.append("position\":\"");
			builder.append(position);
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
		if (mData != null) {
			builder.append("mData\":\"");
			builder.append(mData);
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
		Player other = (Player) obj;
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
