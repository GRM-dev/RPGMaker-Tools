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
@Table(name = "positions")
@Path("pos")
public class Position {
	@Id
	@GeneratedValue
	private int id;
	@OneToOne
	@JoinColumn(name = "player_id")
	private Player player;
	@Column(name = "pos_x")
	private int x;
	@Column(name = "pos_y")
	private int y;
	@Column(name = "map_id")
	private int map_id;
	@Column(name = "f_dir")
	private int direction;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
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
	
}
