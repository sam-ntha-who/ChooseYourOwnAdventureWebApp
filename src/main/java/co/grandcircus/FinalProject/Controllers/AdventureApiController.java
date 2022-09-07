package co.grandcircus.FinalProject.Controllers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;
import co.grandcircus.FinalProject.Models.Option;
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
		sceneToUpdate.setOptions(scene.getOptions());

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

	// reset data
	@GetMapping("/reset")
	public String reset() {

		// currently for test data
		// delete all data
		storyRepo.deleteAll();
		sceneRepo.deleteAll();

		// load new data

		// give story a story id and a scene id
		// TakeAHike.root
		String title = "Take A Hike";
		Story story = new Story(title);
		story.setId(StoryID.createStoryID(title));
		Scene startingScene = new Scene(story.getId(), "You arrive at the trailhead and meet your friends Stefon and Katya for a lovely little hike up Sugarloaf Mountain. After saying hi and adjusting your backpack, it's time to head out. You:", null);
		String sceneId = SceneID.createSceneID(story, startingScene, null);
		// set scene Id
		startingScene.setId(sceneId);
		// set starting scene - Scene(String id, String storyId, String description)
		story.setStartingSceneId(sceneId);
		// add options to story.
		List<Option> options = new ArrayList();
		Option option = new Option("Confidently lead the group into the forest.", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		// setting each time to ensure count increments
		startingScene.setOptions(options);
		// add second option
		option = new Option("Hang back and let someone else take the lead.", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// add third option
		option = new Option("Let everyone else go and assert your position at the back of the pack.", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// add fourth option
		option = new Option("Turn around and get back into your car, why am I in the wilderness again?", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		Scene parentScene = startingScene;
		// save scene + story
		storyRepo.save(story);
		sceneRepo.save(startingScene);
		
		// TakeAHike.option-1
		// next scene
		Scene aPathRootScene = new Scene(startingScene.getOptions().get(0).getSceneId(), story.getId(), "As you're walking, you start pointing out different native plants that you recognize, excited to share your wilderness knowledge with your friends - it's gotta come in handy somewhere, right? You quickly come upon a fork in the trail, you:", startingScene.getId());
		options = new ArrayList();
		option = new Option("Choose the path to the left.", SceneID.createSceneID(story, new Scene(), aPathRootScene));
		options.add(option);
		aPathRootScene.setOptions(options);
		option = new Option("Choose the middle path.", SceneID.createSceneID(story, new Scene(), aPathRootScene));
		options.add(option);
		aPathRootScene.setOptions(options);
		option = new Option("Choose the path to the right.", SceneID.createSceneID(story, new Scene(), aPathRootScene));
		options.add(option);
		aPathRootScene.setOptions(options);
		
		sceneRepo.save(aPathRootScene);
		
			// TakeAHike.option1-1
			Scene aPathScene1 = new Scene(aPathRootScene.getOptions().get(0).getSceneId(), story.getId(), "You took the path to the left and it starts gently winding its way around the mountain. The three of you spot a bald eagle and get pretty excited about it. Katya stops to take a picture. You:", aPathRootScene.getId());
			options = new ArrayList();
			option = new Option("Get your phone out and take a photo. You probably won't look at it again, but hey, posterity.", SceneID.createSceneID(story, new Scene(), aPathScene1));
			options.add(option);
			aPathScene1.setOptions(options);
			option = new Option("Look down the path to see if it looks challenging.", SceneID.createSceneID(story, new Scene(), aPathScene1));
			options.add(option);
			aPathScene1.setOptions(options);
			option = new Option("You pull out your water and take a drink.", SceneID.createSceneID(story, new Scene(), aPathScene1));
			options.add(option);
			aPathScene1.setOptions(options);

			sceneRepo.save(aPathScene1);
			
			// TakeAHike.option1-2
			Scene aPathScene2 = new Scene(aPathRootScene.getOptions().get(1).getSceneId(), story.getId(), "You took the middle path. The trail starts easy enough, but quickly starts to get pretty steep. As you keep climbing, Katya starts to fall behind. You stop and rest so she can catch up with you and Stefon.", aPathRootScene.getId());
			options = new ArrayList();
			option = new Option("Look around a bit to gauge whether or not you should continue up the path. You decide it's fine.", SceneID.createSceneID(story, new Scene(), aPathScene2));
			options.add(option);
			aPathScene2.setOptions(options);
			option = new Option("Look around a bit to gauge whether or not you should continue up the path. You decide to turn back and find an easier route.", SceneID.createSceneID(story, new Scene(), aPathScene2));
			options.add(option);
			aPathScene2.setOptions(options);
	
			sceneRepo.save(aPathScene2);
		
		// TakeAHike.option-2
		Scene bPathRootScene = new Scene(startingScene.getOptions().get(1).getSceneId(), story.getId(), "As you're walking, Stefon, who took the lead, starts pointing out some edible berries that he says he's definitely eaten a few times before. He offers to collect some and let you try them. You:", startingScene.getId());
		options = new ArrayList();
		option = new Option("Try the berries, Stefon wouldn't lead you astray.", SceneID.createSceneID(story, new Scene(), bPathRootScene));
		options.add(option);
		bPathRootScene.setOptions(options);
		option = new Option("Say \"After you my dude.\" And let Stefon eat a berry first, it looks like a wild blueberry, but are we sure?", SceneID.createSceneID(story, new Scene(), bPathRootScene));
		options.add(option);
		bPathRootScene.setOptions(options);
		option = new Option("Say \"Maybe we shouldn't eat these berries dude, are you sure it's the right plant?\" and refuse the berries.", SceneID.createSceneID(story, new Scene(), bPathRootScene));
		options.add(option);
		bPathRootScene.setOptions(options);
		option = new Option("You put one berry in your mouth, don't chew it, and then spit it out when no one is looking.", SceneID.createSceneID(story, new Scene(), bPathRootScene));
		options.add(option);
		bPathRootScene.setOptions(options);
		
		sceneRepo.save(bPathRootScene);
		
		// TakeAHike.option-3
		Scene cPathRootScene = new Scene(startingScene.getOptions().get(2).getSceneId(), story.getId(), "You follow your friends for a while and realize you left your water in the car, you:", startingScene.getId());
		options = new ArrayList();
		option = new Option("Ask the group to come back with you to the car and grab it.", SceneID.createSceneID(story, new Scene(), cPathRootScene));
		options.add(option);
		cPathRootScene.setOptions(options);
		option = new Option("Ask them to wait here and you'll be right back.", SceneID.createSceneID(story, new Scene(), cPathRootScene));
		options.add(option);
		cPathRootScene.setOptions(options);
		option = new Option("Tell them you'll catch up.", SceneID.createSceneID(story, new Scene(), cPathRootScene));
		options.add(option);
		cPathRootScene.setOptions(options);
		
		sceneRepo.save(cPathRootScene);

		// TakeAHike.option-4
		// endpoint - 0 options
		Scene dPathRootScene = new Scene(startingScene.getOptions().get(3).getSceneId(), story.getId(), "You drive back towards civilization.", startingScene.getId());
		options = null;
		dPathRootScene.setOptions(options);
		sceneRepo.save(dPathRootScene);
		

		
		return "Data reset.";
	}
}
