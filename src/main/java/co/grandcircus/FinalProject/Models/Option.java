package co.grandcircus.FinalProject.Models;

public class Option {

	// string of user choice that points to scene 
	private String description;
	
	// sceneId we need to show next scene 
	private String nextSceneId;
	
	private int pathLength;
	
	
	public int getPathLength() {
		return pathLength;
	}

	public void setPathLength(int pathLength) {
		this.pathLength = pathLength;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	//TODO: should be getNextSceneId
	public String getSceneId() {
		return nextSceneId;
	}
	public void setSceneId(String nextSceneId) {
		this.nextSceneId = nextSceneId;
	}

	public Option(String description, String nextSceneId) {
		this.description = description;
		this.nextSceneId = nextSceneId;
	}
	
	public Option() {};
	
	
}
