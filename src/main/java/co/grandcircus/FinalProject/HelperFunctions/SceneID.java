package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;

public class SceneID {

	// need title keyword from story
	private Story story;
	// need option scene key
	private Scene scene;

	// need story scene key

	public static String SceneID(Story story, Scene scene) {
		String storyId = story.getId();
		String parentId = scene.getParentId();
		Integer optionNum = scene.getOptions().size() + 1;
		String sceneId = storyId + parentId + optionNum;

		return sceneId;
	}

}
