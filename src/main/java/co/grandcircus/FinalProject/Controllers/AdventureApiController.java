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
	
	// Delete Scene (and all connected scenes)
	@DeleteMapping("/delete-scene-tree")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteSceneTree(@RequestBody Scene scene) {
		
		//without recursion
		ArrayList<Scene> scenesToDelete = new ArrayList<>();
		
		if (sceneRepo.existsById(scene.getId())) {
			scenesToDelete.add(scene);
			
		} else return;
		
		int startIndex = 0;
		
		while (!(startIndex - 1 == scenesToDelete.size())) {
			
			for (int i = startIndex; i < scenesToDelete.size(); i++) {
				List<Scene> subList = sceneRepo.findByParentId(scenesToDelete.get(i).getId());
				
				if (subList.size() > 0) {
					
					for (Scene sc : subList) {
						scenesToDelete.add(sc);
				}
				}
			}
			
		}
		for (Scene s2d : scenesToDelete) {
			deleteScene(s2d);
		}
		
	}

	// deletes a specific scene. Is private so only methods within this class can call it, not API users directly.
	private void deleteScene(Scene s2d) {
		sceneRepo.delete(s2d);
	}
	
}

