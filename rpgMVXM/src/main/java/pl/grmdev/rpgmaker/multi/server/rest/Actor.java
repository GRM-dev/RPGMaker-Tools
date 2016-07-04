/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.persistence.*;
import javax.ws.rs.Path;

/**
 * @author Levvy055
 *		
 */
@Entity
@Table(name = "actors")
@Path("actor")
public class Actor {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "f_name")
	private String name;
	@Column(name = "f_active")
	private boolean active;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "char_id")
	private Character character;
	
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
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
}
