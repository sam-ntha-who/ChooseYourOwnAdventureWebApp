package co.grandcircus.FinalProject.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("scenes")
public class Scene{

	@Id
	private String id;
	private String storyId;
	private String storyTitle;
	private String parentId;
	private String description;
	private List<Option> options;
	
	
	//constructors
	public Scene() {
	}
	
	public Scene(String id) {
		this.id = id;
	}
	public Scene(String id, String storyId, String parentId, String description, List<Option> options) {
		this.id = id;
		this.storyId = storyId;
		this.parentId = parentId;
		this.description = description;
		this.options = options;
	}
	
	// for setting starting scene
	public Scene(String storyId, String description, String parentId) {

		this.storyId = storyId;
		this.description = description;
		this.parentId = parentId;
	}
	
	// for setting the rest of the scenes
	public Scene(String id, String storyId, String description, String parentId) {
		this.id = id;
		this.storyId = storyId;
		this.description = description;
		this.parentId = parentId;
	}

	//getters and setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}


}
