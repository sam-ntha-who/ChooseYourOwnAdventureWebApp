package co.grandcircus.FinalProject.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;
import co.grandcircus.FinalProject.Repositories.StoryRepository;

@RestController
public class AdventureApiController {

	@Autowired
	private StoryRepository storyRepo;

	@Autowired
	private SceneRepository sceneRepo;

	
	// CRUD Functions

	// Create Story
	@PostMapping("/create-story")
	@ResponseStatus(HttpStatus.CREATED)
	public Story createStory(@RequestBody Story story) {
		storyRepo.insert(story);
		return story;
	}

	// Create Scene
	@PostMapping("/create-scene")
	@ResponseStatus(HttpStatus.CREATED)
	public Scene createScene(@RequestBody Scene scene) {
		sceneRepo.insert(scene);
		return scene;
	}

	// so far unused
	// Create Multiple Scenes
	@PostMapping("/create-all-scenes")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAllScenes(@RequestBody List<Scene> childList) {
		for (Scene scene : childList) {
			sceneRepo.insert(scene);
		}
	}

	// Get list of stories
	@GetMapping("/allStories")
	public List<Story> getStories() {
		return storyRepo.findAll();
	}
	
	// get a single story
	@GetMapping("story/{storyId}")
	public Story getStory(@PathVariable String storyId) {
		return storyRepo.findById(storyId).orElseThrow(() -> new StoryNotFoundException(storyId));
	}
	
	
	// get a scene
	@GetMapping("/read-scene/{id}")
	public Scene getScene(@PathVariable("id") String id) {
		return sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
	}

	// may be replaced in views controller by calling getStory(id).getId()
	// Read a Story Name
	public String findStoryName(@RequestParam String id) {
		
		Story story = storyRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
		
		return story.getTitle();
	}

	// Update a scene
	@PostMapping("/update-scene")
	public Scene updateScene(@RequestBody Scene scene, @RequestParam String id) {
		// maybe change findByStoryIdAndId to a diff method
		Scene sceneToUpdate = sceneRepo.findByStoryIdAndId(scene.getStoryId(), id)
				.orElseThrow(() -> new SceneNotFoundException(scene.getId()));

		sceneToUpdate.setDescription(scene.getDescription());
		sceneToUpdate.setChildList(scene.getChildList());

		return sceneRepo.save(sceneToUpdate);
	}

	@PostMapping("/save-scene")
	public Scene saveScene(@RequestBody Scene scene) {
		return sceneRepo.save(scene);
		
	}
	
	@PostMapping("/save-story")
	public Story saveStory(@RequestBody Story story) {
		return storyRepo.save(story);
		
	}
	
	// Delete Scene (and all connected scenes)
	@DeleteMapping("/delete-scene-tree/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSceneTree(@PathVariable String id) {

		ArrayList<Scene> scenesToDelete = new ArrayList<>();

		Scene sceneToDelete = sceneRepo.findById(id)
				.orElseThrow(() -> new SceneNotFoundException(id));

		scenesToDelete.add(sceneToDelete);

		for (int i = 0; i < scenesToDelete.size(); i++) {
			String sceneId = scenesToDelete.get(i).getId();
			String currentStoryId = scenesToDelete.get(i).getStoryId();
			List<Scene> subList = sceneRepo.findByParentId(sceneId);

			if (subList.size() > 0) {

				for (Scene sc : subList) {
					scenesToDelete.add(sc);
				}
			}
		}

		for (Scene s2d : scenesToDelete) {
			sceneRepo.delete(s2d);
		}
	}
	
	@DeleteMapping("/delete-story/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteStory(@PathVariable String id) {
		
		Story storyToDelete = storyRepo.findById(id).orElseThrow(() -> new StoryNotFoundException(id));
		
		deleteSceneTree(storyToDelete.getStartingSceneId());
		storyRepo.delete(storyToDelete);
	}

	// Error Handling
	@ResponseBody
	@ExceptionHandler(SceneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String sceneNotFoundHandler(SceneNotFoundException ex) {
		return ex.getMessage();
	}

	@ResponseBody
	@ExceptionHandler(StoryNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String storyNotFoundHandler(StoryNotFoundException ex) {
		return ex.getMessage();
	}
	
	// reset data
//	// pretty much depricated at this point
//	@GetMapping("/reset")
//	public String reset() {
//			return "Data reset.";
//	}
}
