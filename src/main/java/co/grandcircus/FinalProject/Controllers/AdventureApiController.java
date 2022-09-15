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
		return storyRepo.findStoryById(storyId);
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
	public Scene saveScene(Scene scene) {
		
		return sceneRepo.save(scene);
		
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
	// this needs to be in its own class
	@GetMapping("/reset")
	public String reset() {

		// delete all data
		storyRepo.deleteAll();
		sceneRepo.deleteAll();

		// TakeAHike.root
		String title = "Take A Hike"; // will not change with update
		Story story = new Story(title); // will not change with update
		story.setId(StoryID.createStoryID(title)); // will not change with update
		// starting scene
		Scene startingScene = new Scene(story.getId(),
				"You arrive at the trailhead and meet your friends Stefon and Katya for a lovely little hike up Sugarloaf Mountain. After saying hi and adjusting your backpack, it's time to head out. You:",
				null); // will not change with update
		String sceneId = SceneID.createSceneID(story, startingScene, null); // will not change with update - but info within SceneID creator will
		startingScene.setId(sceneId); // will not change with update
		// set starting scene for story
		story.setStartingSceneId(sceneId); // will not change with update
		startingScene.setStoryTitle(title); // will not change with update
		
	// add options to scene
	// add 1st scene - this can probably be consolidated but this is just test data so prob not gonna get too wild
		Scene aPathRoot = new Scene();
		// set scene ID
		aPathRoot.setId(SceneID.createSceneID(story, aPathRoot, startingScene));
		// set story within scene so it maps to the right story
		aPathRoot.setStoryId(story.getId());
		// set parentId
		aPathRoot.setParentId(startingScene.getId());
		// set option that leads to this scene
		aPathRoot.setOption("Confidently lead the group into the forest.");
		// set actual scene
		aPathRoot.setDescription("As you're walking, you start pointing out different native plants that you recognize, excited to share your wilderness knowledge with your friends - it's gotta come in handy somewhere, right? You quickly come upon a fork in the trail, you:");
		// add this scene to the list of options
		List<Scene> scenes = new ArrayList<Scene>();
		scenes.add(aPathRoot);
		startingScene.setChildList(scenes);
		
		storyRepo.save(story);
	// add 2nd scene
		Scene bPathRoot = new Scene();
		bPathRoot.setStoryTitle(title);
		bPathRoot.setStoryId(story.getId());
		bPathRoot.setParentId(startingScene.getId());
		bPathRoot.setId(SceneID.createSceneID(story, bPathRoot, startingScene));
		bPathRoot.setOption("Hang back and let someone else take the lead.");
		bPathRoot.setDescription("As you're walking, Stefon, who took the lead, starts pointing out some edible berries that he says he's definitely eaten a few times before. He offers to collect some and let you try them. You:");
		startingScene.getChildList().add(bPathRoot);
		
	// add 3rd scene
		Scene cPathRoot = new Scene();
		cPathRoot = new Scene(SceneID.createSceneID(story, cPathRoot, startingScene), story.getId(), 
				startingScene.getId(), "Let everyone else go and assert your position at the back of the pack.", 
				"You follow your friends for a while and realize you left your water in the car, you:");
		cPathRoot.setStoryTitle(title);
		startingScene.getChildList().add(cPathRoot);
		
	// add 4th scene
		Scene dPathRoot = new Scene();
		dPathRoot = new Scene(SceneID.createSceneID(story, dPathRoot, startingScene), story.getId(), 
				startingScene.getId(), "Turn around and get back into your car, why am I in the wilderness again?", 
				"You drive back towards civilization.");
		dPathRoot.setStoryTitle(title);
		startingScene.getChildList().add(dPathRoot);
		sceneRepo.save(startingScene);
		sceneRepo.save(aPathRoot);
		sceneRepo.save(bPathRoot);
		sceneRepo.save(cPathRoot);
		sceneRepo.save(dPathRoot);
			return "Data reset.";
	}
}
