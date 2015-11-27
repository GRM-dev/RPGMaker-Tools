/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.ws.rs.Path;

/**
 * @author Levvy055
 *		
 */
@Entity
@Table(name = "invntories")
@Path("inv")
public class Inventory {
	@Id
	@GeneratedValue
	private int id;
	
}
