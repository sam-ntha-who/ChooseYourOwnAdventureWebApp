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

	// TESTING -create scene in DB via id only as requestparam
	@PostMapping("/test-add")
	@ResponseStatus(HttpStatus.CREATED)
	public void testAdd(@RequestParam String id) {
		sceneRepo.insert(new Scene(id));
	}

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
	public Scene getScene(@RequestParam String id) {
		return sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
	}
	
	
	
	// Delete Scene (and all connected scenes)
	@DeleteMapping("/delete-scene-tree")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSceneTree(@RequestParam String id) {

		// without recursion
		ArrayList<Scene> scenesToDelete = new ArrayList<>();

			Scene sceneToDelete = sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
			
			scenesToDelete.add(sceneToDelete);

		int startIndex = 0;

		while (!(startIndex == scenesToDelete.size())) {

			for (int i = startIndex; i < scenesToDelete.size(); i++) {
				String currentSceneId = scenesToDelete.get(i).getId();
				List<Scene> subList = sceneRepo.findByParentId(currentSceneId);

				if (subList.size() > 0) {

					for (Scene sc : subList) {
						scenesToDelete.add(sc);
					}
				}
			}
			startIndex ++;

		}
		for (Scene s2d : scenesToDelete) {
			deleteScene(s2d);
		}

	}

	// deletes a specific scene. Is private so only methods within this class can
	// call it, not API users directly.
	private void deleteScene(Scene s2d) {
		sceneRepo.delete(s2d);
	}

	// Error Handling
	@ResponseBody
	@ExceptionHandler(SceneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String sceneNotFoundHandler(SceneNotFoundException ex) {
		return ex.getMessage();
	}
}
