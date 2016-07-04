/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest.inv;

import java.util.Set;

import javax.persistence.*;
import javax.ws.rs.Path;

import pl.grmdev.rpgmaker.multi.server.rest.Character;
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
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
	private Set<Item> items;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
	private Set<Weapon> weapons;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
	private Set<Armor> armors;
	@OneToOne
	@JoinColumn(name = "char_id")
	private Character character;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Set<Item> getItems() {
		return items;
	}
	
	public void setItems(Set<Item> items) {
		this.items = items;
	}
	
	public Set<Weapon> getWeapons() {
		return weapons;
	}
	
	public void setWeapons(Set<Weapon> weapons) {
		this.weapons = weapons;
	}
	
	public Set<Armor> getArmors() {
		return armors;
	}
	
	public void setArmors(Set<Armor> armors) {
		this.armors = armors;
	}
	
	public Character getCharacter() {
		return character;
	}
	
	public void setCharacter(Character character) {
		this.character = character;
	}
	
}
