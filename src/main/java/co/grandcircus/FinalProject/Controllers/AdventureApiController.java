package co.grandcircus.FinalProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;
import co.grandcircus.FinalProject.Repositories.StoryRepository;

@RestController
public class AdventureApiController {

	@Autowired
	StoryRepository storyRepo;

	@Autowired
	SceneRepository sceneRepo;

	// CRUD Functions

//	// create Story
//	@PostMapping("/createStory")
//	@ResponseStatus(HttpStatus.CREATED)
//	public Story createStory(@RequestBody Story story) {
//		String storyID = StoryID.StoryID(story);
//		story.setId(storyID);
//		return storyRepo.insert(story);
//	}
//	// create Scene
//	@PostMapping("/createScene")
//	@ResponseStatus(HttpStatus.CREATED)
//	public Scene createSceneOption(@RequestBody Scene scene, @RequestBody Scene parentScene, @RequestBody Story story) {
//		String sceneID = SceneID.SceneID(story, parentScene);
//		scene.setId(sceneID);
//		return sceneRepo.insert(scene);
//	}

}
