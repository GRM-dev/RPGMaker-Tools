/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.ws.rs.Path;

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
	@ManyToOne
	@JoinColumn(name = "f_actors")
	private List<Actor> actors;
	
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
	public MultiplayerData getmData() {
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
}
