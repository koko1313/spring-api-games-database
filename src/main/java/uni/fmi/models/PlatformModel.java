package uni.fmi.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "platforms")
@JsonIgnoreProperties({"games"})
public class PlatformModel {

	@Id
	private int id;
	
	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;
	
	@ManyToMany(mappedBy = "platforms")
	private List<GameModel> games;

	
	// ==================================================================
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GameModel> getGames() {
		return games;
	}

	public void setGames(List<GameModel> games) {
		this.games = games;
	}
	
}
