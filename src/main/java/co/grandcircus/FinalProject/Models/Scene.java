package co.grandcircus.FinalProject.Models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("scenes")
public class Scene{

	@Id
	// sceneId
	private String id;
	private String storyId;
	private String storyTitle;
	// previous sceneId
	private String parentId;
	// actual scene
	private String description;
	// wording for the choice that the user is making to get to this scene ie "turn left" "open the door" etc (to be accessed from parentScene)
	private String option;
	// photo url
	private String photoUrl;
	// scene tree
	List<Scene> childList;
	// for the DB this may need to wind up being Integer v int	
	private int pathLength;
	
	
//	
//	@Override
//	public String toString() {
//		return "Scene [storyTitle=" + storyTitle + ", option=" + option + "]";
//	}

	//constructors
	public Scene() {
	}
	
	public Scene(String id) {
		this.id = id;
	}
	
	// constructor for setting a scene that isn't startingScene
	public Scene(String id, String storyId, String parentId, String option, String description) {
		this.id = id;
		this.storyId = storyId;
		this.parentId = parentId;
		this.option = option;
		this.description = description;
	}
	

	// constructor with current fields
	public Scene(String id, String storyId, String storyTitle, String parentId, String description,
		String option, String photoUrl, List<Scene> childList) {
		this.id = id;
		this.storyId = storyId;
		this.storyTitle = storyTitle;
		this.parentId = parentId;
		this.description = description;
		this.option = option;
		this.photoUrl = photoUrl;
		this.childList = childList;
	}
	
	// for setting starting scene - will stay the same post treeConversion
	public Scene(String storyId, String description, String parentId) {
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

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public List<Scene> getChildList() {
		return childList;
	}

	public void setChildList(List<Scene> childList) {
		this.childList = childList;
	}

	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

}
