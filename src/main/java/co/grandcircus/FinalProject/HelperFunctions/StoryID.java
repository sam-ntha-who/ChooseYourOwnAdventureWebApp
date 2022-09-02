package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Story;

public class StoryID {

	private Story story;

	public static String StoryID(Story story) {

		String noSpace = story.getTitle().replaceAll("\\s", "");

		String storyID = noSpace.substring(0, 5);

		return storyID;
	}

}
