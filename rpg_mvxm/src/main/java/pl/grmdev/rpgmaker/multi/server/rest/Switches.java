/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.persistence.*;
import javax.ws.rs.Path;

@Entity
@Table(name = "switches")
@Path("switches")
public class Switches {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "switch_name")
	private String name;
	@Column(name = "switch_state")
	private boolean state;
	@OneToOne
	@JoinColumn(name = "player_id")
	private Character player;
	
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
	
	public boolean isState() {
		return state;
	}
	
	public void setState(boolean state) {
		this.state = state;
	}
	
	public Character getPlayer() {
		return player;
	}
	
	public void setPlayer(Character player) {
		this.player = player;
	}
	
}
