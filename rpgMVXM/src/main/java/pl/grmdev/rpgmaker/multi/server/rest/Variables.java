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
@Table(name = "variables")
@Path("vars")
public class Variables {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "var_name")
	private String varName;
	@Column(name = "var_value")
	private String varValue;
	@OneToOne
	@JoinColumn(name = "player_id")
	private Character player;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getVarName() {
		return varName;
	}
	public void setVarName(String varName) {
		this.varName = varName;
	}
	public String getVarValue() {
		return varValue;
	}
	public void setVarValue(String varValue) {
		this.varValue = varValue;
	}
	public Character getPlayer() {
		return player;
	}
	public void setPlayer(Character player) {
		this.player = player;
	}
	
}
