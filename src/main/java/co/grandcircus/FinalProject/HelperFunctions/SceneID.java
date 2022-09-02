package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;

public class SceneID {

	// need title keyword from story
	private Story story;
	// need option scene key
	private Scene parentScene;

	// need story scene key

	public static String createSceneID(Story story, Scene parentScene) {
		String storyId = story.getId();
		String parentId = parentScene.getParentId();
		Integer optionNum = parentScene.getOptions().size() + 1;
		String sceneId = storyId + parentId + optionNum;

		return sceneId;
	}

}
