package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;

public class SceneID {

	// need title keyword from story
	private Story story;
	// need parent/prior scene 
	private Scene parentScene;

	// need story scene 
	private Scene scene;
	// creating scene ids for options
	public static String createSceneID(Story story, Scene scene, Scene parentScene) {
		// will remain the same
		String storyId = story.getId();
		// if there is no parentScene then null, otherwise parentSceneId
		String parentId = "";
		String sceneId = "";
		Integer optionNum = 0;
		if (parentScene == null) {
			parentId = "root";
			sceneId = storyId + parentId;
			return sceneId;
		} 
		// first optionNum should be 1
		// following should increment based on size of scene.options
		
		// are we trying to get the size of options for the parentScene or the Scene???
		if(parentScene.getOptions() == null) {
			optionNum = 1;
			sceneId = storyId + parentId + optionNum;
			// works with an else if but not an else smh
		} else if (parentScene.getOptions() != null){
			// increments work when it is parentscene
			
		optionNum = parentScene.getOptions().size() + 1;
		sceneId = storyId + parentId + optionNum;
		}
		
		
		
		// if parentScene == null then it is the first scene
		if (parentScene != null && parentScene.getId().contains("root")) {
			parentId = parentScene.getId().replaceAll("root", "option");
			sceneId = parentId + "-" + optionNum;
		} else if (parentScene.getId().contains("option")) {
			parentId = parentScene.getId().replaceAll("-", "");
			sceneId = parentId + "-" + optionNum;
		}
		
//		sceneId = storyId + parentId + optionNum;
		

		return sceneId;
	}

}
