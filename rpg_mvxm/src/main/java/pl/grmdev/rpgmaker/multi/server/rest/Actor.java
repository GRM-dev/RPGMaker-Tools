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
	@ManyToOne
	@JoinColumn(name = "player_id")
	private Player player;
}
