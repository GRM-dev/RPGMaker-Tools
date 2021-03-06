/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fluent.hibernate.H;

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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pos_id")
	private Position currentPosition;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "character")
	private List<Actor> actors;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inv_id")
	private Inventory inventory;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Position> positions;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vars_id")
	private Variables vars;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "switches_id")
	private Switches switches;
	
	@POST
	@Path("/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCharacter(@PathParam("username") String username, @QueryParam("default") boolean def, String body) {
		if (body == null || body.isEmpty()) {
			return Result.badRequest(true, "Received empty request!");
		}
		if (username == null || username.isEmpty()) {
			return Result.badRequest(true, "Received no username!");
		}
		try {
			JSONObject json = new JSONObject(body);
			String token = json.getString("authToken");
			username = username.toLowerCase();
			User user = H.<User> request(User.class).eq("username", username).first();
			if (user == null) {
 return Result.notFound(false, true, "User " + username + "not found");
			}
			if (!Token.verify(token, user.getId())) {
 return Result.noAuth(false, true, "wrong token");
			}
			user.setLastActive(new Date());
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			Character character = mapper.readValue(json.getJSONObject("character").toString(), Character.class);
			if (character == null) {
				return Result.badRequest(true, "wrong character object received",
						json.getJSONObject("character").toString());
			}
			String cname = character.getName();
			if (cname == null || cname.isEmpty() || !cname.matches(User.USERNAME_PATTERN)) {
				return Result.badRequest(true, "Wrong username format", cname);
			}
			character.setUser(user);
			character.setCreationDate(new Date());
			if (def) {
				character.setNullToDefaults();
			}
			Character p2 = H.save(character);
			if (character.equals(p2)) {
				return Result.created(false, "Character created");
			} else {
				return Result.created(true, "Character created but not sure!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.exception(e);
		}
	}
	
	@GET
	@Path("/{char}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getChar(@PathParam("char") String charName, @QueryParam("username")String username,@QueryParam("authToken") String token) {
		if (token == null || token.isEmpty()) {
 return Result.noAuth(false, true, "No token!");
		}
		if (charName == null || charName.isEmpty()) {
			return Result.badRequest(true, "Received no character name!");
		}
		 
		if (!Token.verify(token, username)) {
 return Result.noAuth(false, true, "Wrong token for specified username or token expired!");
		}
		Character character = H.<Character> request(getClass()).eq("name", charName).first();
		if (character == null) {
 return Result.notFound(true, true, "Character " + charName + " not exist!");
		}
		User user = H.<User> request(User.class).eq("username", username).first();
		user.setLastActive(new Date());
		return Result.json(character.toString());
	}
	
	@GET
	@Path("/id/{char}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCharById(@PathParam("char") int charId, @QueryParam("username") String username,
			@QueryParam("authToken") String token) {
		if (charId == 0) {
			return Result.badRequest(true, "Received no character_id or id = 0!");
		}
		Character character = H.<Character> getById(Character.class, charId);
		if (character == null) {
 return Result.notFound(true, true, "Character with id: " + id + " not found");
		}
		if (token == null || token.isEmpty()) {
 return Result.noAuth(false, false, username);
		}
		return getChar(character.getName(), username, token);
	}
	
	@GET
	@Path("/name/{char}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCharNameById(@PathParam("char") int charId) {
		if (charId == 0) {
			return Result.badRequest(true, "Received no character id or id = 0!");
		}
		Character character = H.<Character> getById(Character.class, charId);
		if (character == null) {
 return Result.notFound(false, false, "Player not exists with that id: " + charId);
		}
		return Result.json("{\"id\":" + charId + ",\"name\":\"" + character.getName() + "\"}");
	}

	@PUT
	@Path("{char}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateChar(@PathParam("char") String charName, String body) {
		if (body == null || body.isEmpty()) {
			return Result.badRequest(true, "No request body!");
		}
		if (charName == null || charName.isEmpty()) {
			return Result.badRequest(true, "Received no character name!");
		}
		try {
			JSONObject json = new JSONObject(body);
			String token = json.getString("authToken");
			if (token == null || token.isEmpty()) {
 return Result.noAuth(false, true, "No token provided!");
			}
			List<Character> charList = H.<Character> request(Character.class).eq("name", charName).list();
			if (charList == null) {
 return Result.notFound(false, true, "Character " + charName + " not found!");
			}
			Character c = charList.get(0);
			if (!Token.verify(token, c.getId())) {
 return Result.noAuth(false, true, "Wrong token!");
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS"));
			Character charNew = mapper.readValue(json.getJSONObject("character").toString(), Character.class);
			c.updateFrom(charNew);
			c.getUser().setLastActive(new Date());
			H.saveOrUpdate(c);
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
	 * @param c
	 *            character from which current should be updated
	 */
	private void updateFrom(Character c) {
		// if (c.getCurrentPosition() != null &&
		// !c.getCurrentPosition().equals(getCurrentPosition())) {
		// getCurrentPosition().updatePosition(getName(),c.getCurrentPosition().toString());
		// TODO: make updates methods in subclasses
		// }
	}
	
	/**
	 * @return true if has name, current position, inventory, vars, switches,
	 *         actors
	 */
	public boolean correct() {
		if (getName() != null && getCurrentPosition() != null && getInventory() != null && getVars() != null
				&& getSwitches() != null && getActors() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * @param c
	 *            character object to check
	 * @return true if not null and has name, current position, inventory, vars,
	 *         switches, actors
	 */
	public static boolean correct(Character c) {
		if (c == null) {
			return false;
		}
		return c.correct();
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
		if (id != 0 && other.id != 0 && id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
