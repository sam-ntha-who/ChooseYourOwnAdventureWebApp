package co.grandcircus.FinalProject.Controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.grandcircus.FinalProject.HelperFunctions.DataLoader;
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
	// Get list of stories
	@GetMapping("/allStories")
	public List<Story> getStories() {
		return storyRepo.findAll();
	}
	
	
	// Read a scene
	@GetMapping("/read-scene/{id}")
	public Scene getScene(@PathVariable("id") String id) {
		return sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
	}
	
	// Read a Story Name
	public String findStoryName(@RequestParam String id) {
		
		Story story = storyRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
		
		return story.getTitle();
	}

	// Update a scene
	@PostMapping("/update-scene")
	public Scene updateScene(@RequestBody Scene scene, @RequestParam String id) {

		Scene sceneToUpdate = sceneRepo.findByStoryIdAndId(scene.getStoryId(), id)
				.orElseThrow(() -> new SceneNotFoundException(scene.getId()));

		sceneToUpdate.setDescription(scene.getDescription());
		sceneToUpdate.setOptions(scene.getOptions());

		return sceneRepo.save(sceneToUpdate);
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
	
	@DeleteMapping("/delete-story/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteStory(@PathVariable String id) {
		
		Story storyToDelete = storyRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
		
		deleteSceneTree(storyToDelete.getStartingSceneId());
		storyRepo.delete(storyToDelete);
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
	// this needs to be in its own class
	@GetMapping("/reset")
	public String reset() {

//		DataLoader doesn't want to work unless data loader is static. doesn't wanna work when data loader is static tho...
//		also doesn't want to work with a new instance of data loader but there may be something that we can tweak with that
		
	//		DataLoader.resetData();
			
	//		return "Data Reset";
		
// currently for test data

		// delete all data
		storyRepo.deleteAll();
		sceneRepo.deleteAll();


		// TakeAHike.root
		String title = "Take A Hike";
		Story story = new Story(title);
		story.setId(StoryID.createStoryID(title));
		// starting scene
		Scene startingScene = new Scene(story.getId(),
				"You arrive at the trailhead and meet your friends Stefon and Katya for a lovely little hike up Sugarloaf Mountain. After saying hi and adjusting your backpack, it's time to head out. You:",
				null);
		String sceneId = SceneID.createSceneID(story, startingScene, null);
		startingScene.setId(sceneId);
		// set starting scene for story
		story.setStartingSceneId(sceneId);
		startingScene.setStoryTitle(title);
		// add options to story.
		List<Option> options = new ArrayList<Option>();
		Option option = new Option("Confidently lead the group into the forest.",
				SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		// setting between each option to ensure count increments - surely there's another way to do this
		startingScene.setOptions(options);
		// add option
		option = new Option("Hang back and let someone else take the lead.",
				SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// add option
		option = new Option("Let everyone else go and assert your position at the back of the pack.",
				SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// add option
		option = new Option("Turn around and get back into your car, why am I in the wilderness again?",
				SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// save scene + story
		storyRepo.save(story);
		sceneRepo.save(startingScene);

			// TakeAHike.option-1
			// need to add a way to not hardcode the index for the getOptions().get(index)
			Scene aPathRootScene = new Scene(startingScene.getOptions().get(0).getSceneId(), story.getId(),
					"As you're walking, you start pointing out different native plants that you recognize, excited to share your wilderness knowledge with your friends - it's gotta come in handy somewhere, right? You quickly come upon a fork in the trail, you:",
					startingScene.getId());
			aPathRootScene.setStoryTitle(title);
			options = new ArrayList<Option>();
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
				Scene aPathScene1 = new Scene(aPathRootScene.getOptions().get(0).getSceneId(), story.getId(),
						"You took the path to the left and it starts gently winding its way around the mountain. The three of you spot a bald eagle and get pretty excited about it. Katya stops to take a picture. You:",
						aPathRootScene.getId());
				aPathScene1.setStoryTitle(title);
				options = new ArrayList<Option>();
				option = new Option(
						"Get your phone out and take a photo. You probably won't look at it again, but hey, posterity.",
						SceneID.createSceneID(story, new Scene(), aPathScene1));
				options.add(option);
				aPathScene1.setOptions(options);
				option = new Option("Look down the path to see if it looks challenging.",
						SceneID.createSceneID(story, new Scene(), aPathScene1));
				options.add(option);
				aPathScene1.setOptions(options);
				option = new Option("You pull out your water and take a drink.",
						SceneID.createSceneID(story, new Scene(), aPathScene1));
				options.add(option);
				aPathScene1.setOptions(options);
				sceneRepo.save(aPathScene1);
				
					// TakeAHike.option11-1
					Scene aPathScene1A = new Scene(aPathScene1.getOptions().get(0).getSceneId(), story.getId(),
							"As you continue up the mountain, you absentmindedly trip on a root. You:",
							aPathScene1.getId());
					aPathScene1A.setStoryTitle(title);
					options = new ArrayList<Option>();
					option = new Option(
							"Fall forward.",
							SceneID.createSceneID(story, new Scene(), aPathScene1A));
					options.add(option);
					aPathScene1A.setOptions(options);
					option = new Option("Fall to the left.",
							SceneID.createSceneID(story, new Scene(), aPathScene1A));
					options.add(option);
					aPathScene1A.setOptions(options);
					option = new Option("Fall to the right.",
							SceneID.createSceneID(story, new Scene(), aPathScene1A));
					options.add(option);
					aPathScene1A.setOptions(options);
					option = new Option("Somehow manage to fall backwards.",
							SceneID.createSceneID(story, new Scene(), aPathScene1A));
					options.add(option);
					aPathScene1A.setOptions(options);
					sceneRepo.save(aPathScene1A);
					
						// TakeAHike.option111-1
						Scene aPathScene1A1 = new Scene(aPathScene1A.getOptions().get(0).getSceneId(), story.getId(),
								"You land on an ant hill and they are crawling all over you, but they aren't fire ants so you brush them off and keep going. Eventually you come upon a smallish cabin. You:",
								aPathScene1A.getId());
						aPathScene1A1.setStoryTitle(title);
						options = new ArrayList<Option>();
						option = new Option(
								"Nope outta there really fast and continue up the trail.",
								SceneID.createSceneID(story, new Scene(), aPathScene1A1));
						options.add(option);
						aPathScene1A1.setOptions(options);
						option = new Option("Peek in a window.",
								SceneID.createSceneID(story, new Scene(), aPathScene1A1));
						options.add(option);
						aPathScene1A1.setOptions(options);
						option = new Option("Haphazardly shove the door open.",
								SceneID.createSceneID(story, new Scene(), aPathScene1A1));
						options.add(option);
						aPathScene1A1.setOptions(options);
						option = new Option("Decide this is too creepy to be allowed and turn back. ",
								SceneID.createSceneID(story, new Scene(), aPathScene1A1));
						options.add(option);
						aPathScene1A1.setOptions(options);
						sceneRepo.save(aPathScene1A1);
							
							// TakeAHike.option1111-1
							Scene aPathScene1A1A = new Scene(aPathScene1A1.getOptions().get(0).getSceneId(), story.getId(),
									"You continue walking up the trail and quickly reach the peak of this tiny mountain. You are rewarded with a beautiful view of the forest.",
									aPathScene1A1.getId());
							aPathScene1A1A.setStoryTitle(title);
							sceneRepo.save(aPathScene1A1A);
							
							// TakeAHike.option1111-2
							Scene aPathScene1A1B = new Scene(aPathScene1A1.getOptions().get(1).getSceneId(), story.getId(),
									"A single candle is flickering opposite the window. A rocking chair is rocking by itself and you're pretty sure there's music coming from inside.",
									aPathScene1A1.getId());
							aPathScene1A1B.setStoryTitle(title);
							options = new ArrayList<Option>();
							option = new Option(
									"You run as fast as your little legs can carry you back to the car.",
									SceneID.createSceneID(story, new Scene(), aPathScene1A1B));
							options.add(option);
							aPathScene1A1B.setOptions(options);
							option = new Option(
									"You slowly back away from whatever demon probably lives inside and continue up the trail.",
									SceneID.createSceneID(story, new Scene(), aPathScene1A1B));
							options.add(option);
							aPathScene1A1B.setOptions(options);
							option = new Option(
									"You recklessly shove the door open to put out the candle because it's a fire hazard.",
									SceneID.createSceneID(story, new Scene(), aPathScene1A1B));
							options.add(option);
							aPathScene1A1B.setOptions(options);
							sceneRepo.save(aPathScene1A1B);

								// TakeAHike.option11112-1
								Scene aPathScene1A1B1 = new Scene(aPathScene1A1B.getOptions().get(0).getSceneId(), story.getId(),
										"You made it to the car and survived the day.",
										aPathScene1A1B.getId());
								aPathScene1A1B1.setStoryTitle(title);
								sceneRepo.save(aPathScene1A1B1);
								
								// TakeAHike.option11112-2
								Scene aPathScene1A1B2 = new Scene(aPathScene1A1B.getOptions().get(1).getSceneId(), story.getId(),
										"You continue walking up the trail, a little faster than you would normally and arrive at the peak! You are rewarded with a beautiful view of the forest.",
										aPathScene1A1B.getId());
								aPathScene1A1B2.setStoryTitle(title);
								sceneRepo.save(aPathScene1A1B2);
								
								// TakeAHike.option11112-3
								Scene aPathScene1A1B3 = new Scene(aPathScene1A1B.getOptions().get(2).getSceneId(), story.getId(),
										"You blow out the candle and the demon swiftly steals your soul. You die.",
										aPathScene1A1B.getId());
								aPathScene1A1B3.setStoryTitle(title);
								sceneRepo.save(aPathScene1A1B3);
								
							// TakeAHike.option1111-3
							Scene aPathScene1A1C = new Scene(aPathScene1A1.getOptions().get(2).getSceneId(), story.getId(),
									"A raccoon is sitting on the mantle just staring at you. It's a real weird vibe in there. You turn around and run straight home, the car lives there now.",
									aPathScene1A1.getId());
							aPathScene1A1C.setStoryTitle(title);
							sceneRepo.save(aPathScene1A1C);
							
							// TakeAHike.option1111-4
							Scene aPathScene1A1D = new Scene(aPathScene1A1.getOptions().get(3).getSceneId(), story.getId(),
									"You run back to the cars faster than you knew possible, get in and never come back. ",
									aPathScene1A1.getId());
							aPathScene1A1D.setStoryTitle(title);
							sceneRepo.save(aPathScene1A1D);
								
						// TakeAHike.option111-2
						Scene aPathScene1A2 = new Scene(aPathScene1A.getOptions().get(1).getSceneId(), story.getId(),
								"You start tumbling downhill towards certain doom and hit a tree on your way down. It stops you! You decide to be done hiking for the day.",
								aPathScene1A.getId());
						aPathScene1A2.setStoryTitle(title);
						sceneRepo.save(aPathScene1A2);
						
						// TakeAHike.option111-3
						Scene aPathScene1A3 = new Scene(aPathScene1A.getOptions().get(2).getSceneId(), story.getId(),
								"You fall into some sort of greenery. Hopefully it isn't poisonous! All three of you keep heading up the trail, passing a sketchy looking cabin. Stefon wants to check it out. You say:",
								aPathScene1A.getId());
						aPathScene1A3.setStoryTitle(title);
						options = new ArrayList<Option>();
						option = new Option(
								"\"Too bad you're not leading the hike, we're not going in there.\"",
								SceneID.createSceneID(story, new Scene(), aPathScene1A3));
						options.add(option);
						aPathScene1A3.setOptions(options);
						option = new Option("\"Go ahead, I'll wait outside.\"",
								SceneID.createSceneID(story, new Scene(), aPathScene1A3));
						options.add(option);
						aPathScene1A3.setOptions(options);
						option = new Option("\"Why do you want to go in the murder cabin, man?\"",
								SceneID.createSceneID(story, new Scene(), aPathScene1A3));
						options.add(option);
						aPathScene1A3.setOptions(options);
						sceneRepo.save(aPathScene1A3);
						
							// TakeAHike.option1113-1
							Scene aPathScene1A3A = new Scene(aPathScene1A3.getOptions().get(0).getSceneId(), story.getId(),
									"Stefon ignores you and goes inside. You peek inside and notice that he is casually chatting with a racoon. You leave.",
									aPathScene1A3.getId());
							aPathScene1A3A.setStoryTitle(title);
							sceneRepo.save(aPathScene1A3A);
							
							// TakeAHike.option1113-2
							Scene aPathScene1A3B = new Scene(aPathScene1A3.getOptions().get(1).getSceneId(), story.getId(),
									"Stefon walks into the cabin and immediately turns back around and runs. You follow and everyone goes to brunch instead.",
									aPathScene1A3.getId());
							aPathScene1A3B.setStoryTitle(title);
							sceneRepo.save(aPathScene1A3B);
							
							// TakeAHike.option1113-3
							Scene aPathScene1A3C = new Scene(aPathScene1A3.getOptions().get(2).getSceneId(), story.getId(),
									"Stefon walks inside and you hear a scream. You pretend you didn't hear a scream and run back to your car.",
									aPathScene1A3.getId());
							aPathScene1A3C.setStoryTitle(title);
							sceneRepo.save(aPathScene1A3C);
						
						// TakeAHike.option111-4
						Scene aPathScene1A4 = new Scene(aPathScene1A.getOptions().get(3).getSceneId(), story.getId(),
								"You hit your head and die.",
								aPathScene1A.getId());
						aPathScene1A4.setStoryTitle(title);
						sceneRepo.save(aPathScene1A4);
						
					// TakeAHike.option11-2
		
					Scene aPathScene1B = new Scene(aPathScene1.getOptions().get(1).getSceneId(), story.getId(),
							"The three of you keep walking, Stefon trips over his own shoelace while looking at his phone. He gets up and acts like nothing happened, but you notice him limping a little. You casually ask if everyone wants to go to brunch instead. Everyone says yes.",
							aPathScene1.getId());
					aPathScene1B.setStoryTitle(title);
					sceneRepo.save(aPathScene1B);
					
					// TakeAHike.option11-3
		
					Scene aPathScene1C = new Scene(aPathScene1.getOptions().get(2).getSceneId(), story.getId(),
							"Embarassingly, you choke on your water and die.",
							aPathScene1.getId());
					aPathScene1C.setStoryTitle(title);
					sceneRepo.save(aPathScene1C);
		
				// TakeAHike.option1-2
				Scene aPathScene2 = new Scene(aPathRootScene.getOptions().get(1).getSceneId(), story.getId(),
						"You took the middle path. The trail starts easy enough, but quickly starts to get pretty steep. As you keep climbing, Katya starts to fall behind. You stop and rest so she can catch up with you and Stefon.",
						aPathRootScene.getId());
				aPathScene2.setStoryTitle(title);
				options = new ArrayList<Option>();
				option = new Option(
						"Look around a bit to gauge whether or not you should continue up the path. You decide it's fine.",
						SceneID.createSceneID(story, new Scene(), aPathScene2));
				options.add(option);
				aPathScene2.setOptions(options);
				option = new Option(
						"Look around a bit to gauge whether or not you should continue up the path. You decide to turn back and find an easier route.",
						SceneID.createSceneID(story, new Scene(), aPathScene2));
				options.add(option);
				aPathScene2.setOptions(options);
				sceneRepo.save(aPathScene2);
					
					// option 1-2 endpoints
					// TakeAHike.option12-1
					Scene aPathScene2A = new Scene(aPathScene2.getOptions().get(0).getSceneId(), story.getId(),
							"When you turn around, you run smack dab into the ax murderer that's been following you since you left the cars. Hopefully you out run him and make it to your car.",
							aPathScene2.getId());
					aPathScene2A.setStoryTitle(title);
					sceneRepo.save(aPathScene2A);
					
					// TakeAHike.option12-2
					Scene aPathScene2B = new Scene(aPathScene2.getOptions().get(1).getSceneId(), story.getId(),
							"When you get back to the fork in the trail, you decide ulitmately that the easiest route is straight back to your car and off to brunch.",
							aPathScene2.getId());
					aPathScene2B.setStoryTitle(title);
					sceneRepo.save(aPathScene2B);

		
				// TakeAHike.option1-3
				Scene aPathScene3 = new Scene(aPathRootScene.getOptions().get(2).getSceneId(), story.getId(),
						"You took the path to the right. As you're walking around a curve, you are treated to a beautiful little pond with a bench. Everyone sits down for a moment to take in the scenery. Katya is taking photos with her phone when she accidentally drops it and it tumbles into the pond. You:",
						aPathRootScene.getId());
				aPathScene3.setStoryTitle(title);
				options = new ArrayList<Option>();
				option = new Option("Say \"Rough luck, the water looks yucky.\"",
						SceneID.createSceneID(story, new Scene(), aPathScene3));
				options.add(option);
				aPathScene3.setOptions(options);
				option = new Option("Say \"We can probably fish it out with this suspiciously placed net over here.\"",
						SceneID.createSceneID(story, new Scene(), aPathScene3));
				options.add(option);
				aPathScene3.setOptions(options);
				option = new Option(
						"Say \"I could use a swim, I'll get it!\" You remove the top layer of your clothes and jump into the water after it.",
						SceneID.createSceneID(story, new Scene(), aPathScene3));
				options.add(option);
				aPathScene3.setOptions(options);
				sceneRepo.save(aPathScene3);
				
					// option 1-3 endpoints
				
					// TakeAHike.option13-1
					Scene aPathScene3A = new Scene(aPathScene3.getOptions().get(0).getSceneId(), story.getId(),
							"Katya seems reluctant to get her phone from the water and asks if we can reschedule the hike so she can go deal with her life.",
							aPathScene3.getId());
					aPathScene3A.setStoryTitle(title);
					sceneRepo.save(aPathScene3A);
					
					// TakeAHike.option13-2
					Scene aPathScene3B = new Scene(aPathScene3.getOptions().get(1).getSceneId(), story.getId(),
							"Stefon grabs the net and suddenly seems as though he's been possessed by... something. You and Katya make eye contact and run all the way back to the cars. No one speaks of this again.",
							aPathScene3.getId());
					aPathScene3B.setStoryTitle(title);
					sceneRepo.save(aPathScene3B);
					
					// TakeAHike.option13-3
					Scene aPathScene3C = new Scene(aPathScene3.getOptions().get(2).getSceneId(), story.getId(),
							"You jump in the water, scoop up the phone from the muck and toss it on the shore. Unfortunately, you become entangled in some seaweed and you die.",
							aPathScene3.getId());
					aPathScene3C.setStoryTitle(title);
					sceneRepo.save(aPathScene3C);
				
		
			// TakeAHike.option-2
			Scene bPathRootScene = new Scene(startingScene.getOptions().get(1).getSceneId(), story.getId(),
					"As you're walking, Stefon, who took the lead, starts pointing out some edible berries that he says he's definitely eaten a few times before. He offers to collect some and let you try them. You:",
					startingScene.getId());
			bPathRootScene.setStoryTitle(title);
			options = new ArrayList<Option>();
			option = new Option("Try the berries, Stefon wouldn't lead you astray.",
					SceneID.createSceneID(story, new Scene(), bPathRootScene));
			options.add(option);
			bPathRootScene.setOptions(options);
			option = new Option(
					"Say \"After you my dude.\" And let Stefon eat a berry first, it looks like a wild blueberry, but are we sure?",
					SceneID.createSceneID(story, new Scene(), bPathRootScene));
			options.add(option);
			bPathRootScene.setOptions(options);
			option = new Option(
					"Say \"Maybe we shouldn't eat these berries dude, are you sure it's the right plant?\" and refuse the berries.",
					SceneID.createSceneID(story, new Scene(), bPathRootScene));
			options.add(option);
			bPathRootScene.setOptions(options);
			option = new Option(
					"You put one berry in your mouth, don't chew it, and then spit it out when no one is looking.",
					SceneID.createSceneID(story, new Scene(), bPathRootScene));
			options.add(option);
			bPathRootScene.setOptions(options);
			sceneRepo.save(bPathRootScene);
	
				// TakeAHike.option2-1
				Scene bPathScene1 = new Scene(bPathRootScene.getOptions().get(0).getSceneId(), story.getId(),
						"You pop a handful of berries into your mouth. They are delicious and actually taste like blueberries. Crisis averted.",
						bPathRootScene.getId());
				bPathScene1.setStoryTitle(title);
				options = new ArrayList<Option>();
				option = new Option(
						"Continue exploring the path, now looking for some wild snacks to impress your friends with.",
						SceneID.createSceneID(story, new Scene(), bPathScene1));
				options.add(option);
				bPathScene1.setOptions(options);
				option = new Option("Take a picture of the blueberry plant, just in case it wasn't blueberries.",
						SceneID.createSceneID(story, new Scene(), bPathScene1));
				options.add(option);
				bPathScene1.setOptions(options);
				sceneRepo.save(bPathScene1);
				
					// TakeAHike.option21-1
					Scene bPathScene1A = new Scene(bPathScene1.getOptions().get(0).getSceneId(), story.getId(),
							"You spot a patch of yummy looking mushrooms that are definitely not poison. Stefon eats one and all seems well until he starts talking about how much the trail is moving ahead of him. The hike is over but the trip is definitely not.",
							bPathScene1.getId());
					bPathScene1A.setStoryTitle(title);
					sceneRepo.save(bPathScene1A);
				
					// TakeAHike.option21-2
					Scene bPathScene1B = new Scene(bPathScene1.getOptions().get(1).getSceneId(), story.getId(),
							"You spot a patch of yummy looking mushrooms that are definitely not poison. Stefon eats one and all seems well until he starts talking about how much the trail is moving ahead of him. The hike is over but the trip is definitely not.",
							bPathScene1.getId());
					bPathScene1B.setStoryTitle(title);
					sceneRepo.save(bPathScene1B);
					
					
				// TakeAHike.option2-2
				Scene bPathScene2 = new Scene(bPathRootScene.getOptions().get(1).getSceneId(), story.getId(),
						"Stefon eats a berry. He immediately pretends to have a reaction to it and everyone panics, until he starts laughing. You do not eat any berries and you walk back to your car, because Stefon sucks.",
						bPathRootScene.getId());
				bPathScene2.setStoryTitle(title);
				sceneRepo.save(bPathScene2);
				
				// TakeAHike.option2-3
				Scene bPathScene3 = new Scene(bPathRootScene.getOptions().get(2).getSceneId(), story.getId(),
						"Stefon eats a handful of berries and you all keep walking. Moments later, he keels over on the trail. Looks like your hike is over for today.",
						bPathRootScene.getId());
				bPathScene3.setStoryTitle(title);
				sceneRepo.save(bPathScene3);
				
				// TakeAHike.option2-4
				Scene bPathScene4 = new Scene(bPathRootScene.getOptions().get(3).getSceneId(), story.getId(),
						"Your mouth starts tingling a bit even though you spit the berry out and didn't chew it. Your probably being dramatic but you run back to civilization shouting about an epipen.",
						bPathRootScene.getId());
				bPathScene4.setStoryTitle(title);
				sceneRepo.save(bPathScene4);
	
			// TakeAHike.option-3
			Scene cPathRootScene = new Scene(startingScene.getOptions().get(2).getSceneId(), story.getId(),
					"You follow your friends for a while and realize you left your water in the car, you:",
					startingScene.getId());
			cPathRootScene.setStoryTitle(title);
			options = new ArrayList<Option>();
			option = new Option("Ask the group to come back with you to the car and grab it.",
					SceneID.createSceneID(story, new Scene(), cPathRootScene));
			options.add(option);
			cPathRootScene.setOptions(options);
			option = new Option("Ask them to wait here and you'll be right back.",
					SceneID.createSceneID(story, new Scene(), cPathRootScene));
			options.add(option);
			cPathRootScene.setOptions(options);
			option = new Option("Tell them you'll catch up.", SceneID.createSceneID(story, new Scene(), cPathRootScene));
			options.add(option);
			cPathRootScene.setOptions(options);
			sceneRepo.save(cPathRootScene);
				
				// Option 3 endpoints 
			
				Scene cPathScene1 = new Scene(cPathRootScene.getOptions().get(0).getSceneId(), story.getId(),
						"You all get back to the cars and decide to go to brunch instead.", cPathRootScene.getId());
				cPathScene1.setStoryTitle(title);
				sceneRepo.save(cPathScene1);
				
				Scene cPathScene2 = new Scene(cPathRootScene.getOptions().get(1).getSceneId(), story.getId(),
						"You get back to the car, text Stefon and Katya and tell them that you're going to brunch instead.", cPathRootScene.getId());
				cPathScene2.setStoryTitle(title);
				sceneRepo.save(cPathScene2);
				
				Scene cPathScene3 = new Scene(cPathRootScene.getOptions().get(2).getSceneId(), story.getId(),
						"You go back to the car and get your water, head back towards them but get lost trying to find them. A boy scout finds you and takes you to your car.", cPathRootScene.getId());
				cPathScene3.setStoryTitle(title);
				sceneRepo.save(cPathScene3);
	
			// TakeAHike.option-4
			// has its own readymade endpoint - 0 options
			Scene dPathRootScene = new Scene(startingScene.getOptions().get(3).getSceneId(), story.getId(),
					"You drive back towards civilization.", startingScene.getId());
			dPathRootScene.setStoryTitle(title);
			sceneRepo.save(dPathRootScene);

		// Test data story #2
		title = "This Is A Long Story Title";
		story = new Story(title);
		story.setId(StoryID.createStoryID(title));
		startingScene = new Scene(story.getId(),
				"You start reading another weird story. You make some decisions, for example:", null);
		sceneId = SceneID.createSceneID(story, startingScene, null);
		// set scene Id
		startingScene.setId(sceneId);
		// set starting scene - Scene(String id, String storyId, String description)
		story.setStartingSceneId(sceneId);
		startingScene.setStoryTitle(title);
		// add options to story.
		options = new ArrayList<Option>();
		option = new Option("You want a glass of lemonade", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		// setting each time to ensure count increments
		startingScene.setOptions(options);
		// add second option
		option = new Option("Close the book. It's probably for the best.",
				SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// add third option
		option = new Option("Kill it with fire.", SceneID.createSceneID(story, new Scene(), startingScene));
		options.add(option);
		startingScene.setOptions(options);
		// save scene + story
		storyRepo.save(story);
		sceneRepo.save(startingScene);
			
			// fake scenes so that the options aren't empty
			Scene s1 = new Scene(startingScene.getOptions().get(0).getSceneId(), story.getId(),
					"Words.",
					startingScene.getId());
			s1.setStoryTitle(title);
			sceneRepo.save(s1);
			
			// fake scene #2
			Scene s2 = new Scene(startingScene.getOptions().get(1).getSceneId(), story.getId(),
					"These are certainly words.",
					startingScene.getId());
			s2.setStoryTitle(title);
			sceneRepo.save(s2);
			
			// fake scene #3
			Scene s3 = new Scene(startingScene.getOptions().get(2).getSceneId(), story.getId(),
					"These are also certainly words.",
					startingScene.getId());
			s3.setStoryTitle(title);
			sceneRepo.save(s3);
	
			return "Data reset.";
	}
}
