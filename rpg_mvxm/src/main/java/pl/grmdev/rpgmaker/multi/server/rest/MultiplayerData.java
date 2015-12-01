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
@Table(name = "multi_data")
@Path("mdata")
public class MultiplayerData {
	
	@Id
	@GeneratedValue
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
