package uni.fmi.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "games")
public class GameModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "name", nullable = false, unique = true, length = 50)
	private String name;
	
	@Column(name = "description", length = 500)
	private String description;
	
	@Column(name = "image", length = 250)
	private String image;
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(
			name = "game_genre",
			joinColumns = { @JoinColumn(name = "game_id") },
			inverseJoinColumns = { @JoinColumn(name = "genre_id") } )
	private List<GenreModel> genres;
	
	@ManyToMany(cascade = CascadeType.DETACH)
	@JoinTable(
			name = "game_platform",
			joinColumns = { @JoinColumn(name = "game_id") },
			inverseJoinColumns = { @JoinColumn(name = "platform_id") } )
	private List<PlatformModel> platforms;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "developer_id")
	private DeveloperModel developer;
	
	// ==================================================================
	
	public GameModel() {
		
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

	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}

	public List<GenreModel> getGenres() {
		return genres;
	}


	public void setGenres(List<GenreModel> genres) {
		this.genres = genres;
	}
	
	

	public List<PlatformModel> getPlatforms() {
		return platforms;
	}


	public void setPlatforms(List<PlatformModel> platforms) {
		this.platforms = platforms;
	}


	public DeveloperModel getDeveloper() {
		return developer;
	}


	public void setDeveloper(DeveloperModel developer) {
		this.developer = developer;
	}
	
}
