package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Story;

public class StoryID {

	private Story story;

	public static String createStoryID(String title) {

		String noSpace = title.replaceAll("\\s", "");

		String storyID = noSpace.substring(0, noSpace.length());

		return storyID;
	}

}
