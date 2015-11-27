/**
 * 
 */
package pl.grmdev.rpgmaker.multi.server.rest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Levvy055
 *		
 */
@Entity
@Table(name = "actors")
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
