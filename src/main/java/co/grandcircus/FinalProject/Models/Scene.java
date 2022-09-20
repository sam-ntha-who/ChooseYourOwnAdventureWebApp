package co.grandcircus.FinalProject.Models;

import java.util.List;

public class Scene {

	private String id;
	private String storyId;
	private String storyTitle;
	private String parentId;
	private String description;
	private String option;
	private String photoUrl;
	private List<Scene> childList;
	private int pathLength;
	boolean shortest;
	boolean longest;

	// constructors
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
	public Scene(String id, String storyId, String storyTitle, String parentId, String description, String option,
			String photoUrl, List<Scene> childList) {
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

	// getters and setters
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

	public boolean isShortest() {
		return shortest;
	}

	public void setShortest(boolean shortest) {
		this.shortest = shortest;
	}

	public boolean isLongest() {
		return longest;
	}

	public void setLongest(boolean longest) {
		this.longest = longest;
	}

}
