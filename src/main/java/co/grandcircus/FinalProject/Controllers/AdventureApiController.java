package co.grandcircus.FinalProject.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

//	// TESTING -create scene in DB via id only as requestparam
//	@PostMapping("/test-add")
//	@ResponseStatus(HttpStatus.CREATED)
//	public void testAdd(@RequestParam String id) {
//		sceneRepo.insert(new Scene(id));
//	}

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

	// Create Multiple Scenes
	@PostMapping("/create-all-scenes")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAllScenes(@RequestBody List<Scene> sceneList) {
		for (Scene scene : sceneList) {
			sceneRepo.insert(scene);
		}
	}

	
	// Read a scene
	@GetMapping("/read-scene")
	public Scene getScene(@RequestParam String id, @RequestParam String storyId) {
		return sceneRepo.findByStoryIdAndId(storyId, storyId).orElseThrow(() -> new SceneNotFoundException(id));
	}

	
	// Update a scene
	@PatchMapping("/update-scene")
	public Scene updateScene(@RequestBody Scene scene, @RequestParam String id) {
		
		Scene sceneToUpdate = sceneRepo.findByStoryIdAndId(scene.getStoryId(), id)
				.orElseThrow(() -> new SceneNotFoundException(scene.getId()));
		
		sceneToUpdate.setDescription(scene.getDescription());
		sceneToUpdate.setOption(scene.getOption());
		
		return sceneRepo.save(sceneToUpdate);
	}
	
	
	// Delete Scene (and all connected scenes)
	@DeleteMapping("/delete-scene-tree")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSceneTree(@RequestParam String storyId, @RequestParam String id) {

		ArrayList<Scene> scenesToDelete = new ArrayList<>();

		Scene sceneToDelete = sceneRepo.findByStoryIdAndId(storyId, id)
				.orElseThrow(() -> new SceneNotFoundException(id));

		scenesToDelete.add(sceneToDelete);

		for (int i = 0; i < scenesToDelete.size(); i++) {
			String sceneId = scenesToDelete.get(i).getId();
			String currentStoryId = scenesToDelete.get(i).getStoryId();
			List<Scene> subList = sceneRepo.findByStoryIdAndParentId(currentStoryId, sceneId);

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

//	// deletes a scene from the database
//	private void deleteScene(Scene s2d) {
//		sceneRepo.delete(s2d);
//	}

	
	// Error Handling
	@ResponseBody
	@ExceptionHandler(SceneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String sceneNotFoundHandler(SceneNotFoundException ex) {
		return ex.getMessage();
	}
}
