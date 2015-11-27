/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
	private Player player;
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
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	
}
