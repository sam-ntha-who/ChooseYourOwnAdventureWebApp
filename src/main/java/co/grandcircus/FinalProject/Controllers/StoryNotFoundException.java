package co.grandcircus.FinalProject.Controllers;

public class StoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public StoryNotFoundException(String id) {
		super("Story id " + id + " not found");
	
}
}
