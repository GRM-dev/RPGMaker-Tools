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
@Table(name = "inventories")
@Path("inv")
public class Inventory {
	@Id
	@GeneratedValue
	private int id;
	
}
