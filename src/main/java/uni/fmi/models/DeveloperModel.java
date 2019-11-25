package uni.fmi.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "developers")
@JsonIgnoreProperties({"games"})
public class DeveloperModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@OneToMany(mappedBy = "developer", fetch = FetchType.EAGER)
	private List<GameModel> games;
	
	// ==================================================================
	
	public DeveloperModel() {
		
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<GameModel> getGames() {
		return games;
	}

	public void setGames(List<GameModel> games) {
		this.games = games;
	}

}
