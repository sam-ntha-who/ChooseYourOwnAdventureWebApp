package co.grandcircus.FinalProject.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("scenes")
public class Scene {

	// add additional fields? + getters & setters
	// create constructor(s) from fields

	@Id
	private String id;
	private String title;
	private String storyId;
	private String parentId;
	private String option;
	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Scene(String title, String storyId, String parentId, String option, String description) {
		this.title = title;
		this.storyId = storyId;
		this.parentId = parentId;
		this.option = option;
		this.description = description;
	}

	public Scene() {

	}

}
