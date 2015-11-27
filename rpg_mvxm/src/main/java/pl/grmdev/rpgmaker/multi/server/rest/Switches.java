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

@Entity
@Table(name = "switches")
@Path("switches")
public class Switches {
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "switch_name")
	private String name;
	@Column(name = "switch_vlue")
	private boolean value;
	@OneToOne
	@JoinColumn(name = "player_id")
	private Player player;
	
}
