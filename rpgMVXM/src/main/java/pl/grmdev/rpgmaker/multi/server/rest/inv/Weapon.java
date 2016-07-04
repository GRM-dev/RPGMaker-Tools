/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest.inv;

import javax.persistence.*;

/**
 * @author Levvy055
 */
@Entity
@Table(name = "weapons")
public class Weapon {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "ingame_item_id")
	private int ingameId;
	@Column(name = "f_name")
	private String name;
	@Column(name = "f_durabilty")
	private double durability;
	@ManyToOne
	@JoinColumn(name = "inv_id")
	private Inventory inventory;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getIngameId() {
		return ingameId;
	}
	
	public void setIngameId(int ingameId) {
		this.ingameId = ingameId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getDurability() {
		return durability;
	}
	
	public void setDurability(double durability) {
		this.durability = durability;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
}
