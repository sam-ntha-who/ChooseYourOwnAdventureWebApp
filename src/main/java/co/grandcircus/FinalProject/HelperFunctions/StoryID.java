package co.grandcircus.FinalProject.HelperFunctions;

import co.grandcircus.FinalProject.Models.Story;

public class StoryID {

	private Story story;

	public static String createStoryID(String title) {

		String noSpace = title.replaceAll("\\s", "");
		String storyID;
		
		if (noSpace.length() < 10) {
			storyID = noSpace.substring(0, noSpace.length());
		} else {
		
		storyID = noSpace.substring(0, 10);
		}
		return storyID;
	}

}
