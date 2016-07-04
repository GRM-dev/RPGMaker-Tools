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
@Table(name = "positions")
@Path("pos")
public class Position {
	@Id
	@GeneratedValue
	private int id;
	@OneToOne
	@JoinColumn(name = "player_id")
	private Character player;
	@Column(name = "pos_x")
	private int x;
	@Column(name = "pos_y")
	private int y;
	@Column(name = "map_id", nullable = false)
	private int map_id;
	@Column(name = "f_dir")
	private int direction;
	@ManyToOne
	@JoinColumn(name = "char_id")
	private Character character;
	@Column(name = "f_order")
	private int order;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Character getPlayer() {
		return player;
	}
	public void setPlayer(Character player) {
		this.player = player;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getMap_id() {
		return map_id;
	}
	public void setMap_id(int map_id) {
		this.map_id = map_id;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public Character getCharacter() {
		return character;
	}
	public void setCharacter(Character character) {
		this.character = character;
	}
	
}
