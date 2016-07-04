/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest.inv;

import javax.persistence.*;

/**
 * @author Levvy055
 *
 */
@Entity
@Table(name = "items")
public class Item {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name = "ingame_item_id")
	private int ingameId;
	@Column(name = "f_name")
	private String name;
	@Column(name = "f_amount")
	private int amount;
	@Column(name = "key_item")
	private boolean keyItem;
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
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public boolean isKeyItem() {
		return keyItem;
	}
	
	public void setKeyItem(boolean keyItem) {
		this.keyItem = keyItem;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}
	
}
