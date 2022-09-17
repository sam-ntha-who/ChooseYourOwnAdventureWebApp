package co.grandcircus.FinalProject.Models;

public class Story {

	private String id;
	private String title;
	private String startingSceneId;
	private String photoUrl;

	//constructors
	public Story() {
	}
	
	public Story(String title, String startingSceneId) {
		this.title = title;
		this.startingSceneId = startingSceneId;
	}
		
	//getters and setters
	
	public String getPhotoUrl() {
		return photoUrl;
	}
	
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}
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

	public String getStartingSceneId() {
		return startingSceneId;
	}

	public void setStartingSceneId(String startingSceneId) {
		this.startingSceneId = startingSceneId;
	}

	public Story(String id, String title, String startingSceneId) {
		this.id = id;
		this.title = title;
		this.startingSceneId = startingSceneId;
	}

	public Story(String title) {
		this.title = title;
	}
}
