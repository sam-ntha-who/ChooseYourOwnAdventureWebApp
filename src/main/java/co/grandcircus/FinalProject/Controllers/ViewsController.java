package co.grandcircus.FinalProject.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;
import co.grandcircus.FinalProject.Models.Option;
import co.grandcircus.FinalProject.Models.Photo;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;
import co.grandcircus.FinalProject.Repositories.StoryRepository;
import co.grandcircus.FinalProject.Services.AdventureDBService;
import co.grandcircus.FinalProject.Services.PexelService;

@Controller
public class ViewsController {

	@Autowired
	StoryRepository storyRepo;

	@Autowired
	SceneRepository sceneRepo;

	@Autowired
	PexelService service;

	@Autowired
	AdventureDBService dbService;

	@RequestMapping("/")
	public String index(Model model) {
		Story[] list = dbService.getAllStories();
		List<Story> storyList = Arrays.asList(list);
		model.addAttribute("storyList", storyList);

		List<Photo> thumbnailList1 = service.getPexels("Hike");
		List<Photo> thumbnailList2 = service.getPexels("Story");
		List<Photo> thumbnailList3 = service.getPexels("Question");

		Photo thumbnail1 = thumbnailList1.get(0);
		Photo thumbnail2 = thumbnailList2.get(0);
		Photo thumbnail3 = thumbnailList3.get(0);

		List<String> photoList = new ArrayList<>();
		photoList.add(thumbnail1.getSrc().getOriginal());
		photoList.add(thumbnail2.getSrc().getOriginal());
		photoList.add(thumbnail3.getSrc().getOriginal());
		model.addAttribute("photoList", photoList);
		return "index";
	}

	// add "add option" to the play function as last option
	@RequestMapping("/play")
	public String play(Model model, @RequestParam String id) {
		Scene nextScene = dbService.getScene(id);
		model.addAttribute("scene", nextScene);
		return "StoryPlay";
	}

	@RequestMapping("/edit")
	public String storyEdit(Model model, @RequestParam String sceneId) {
		Scene editScene = dbService.getScene(sceneId);
		Story story = storyRepo.findById(editScene.getStoryId()).orElseThrow(() -> new SceneNotFoundException(sceneId));
		model.addAttribute("storyTitle", story.getTitle());
		model.addAttribute("scene", editScene);
		return "StoryEdit";
	}

//	// call directly
//	@PostMapping("/update")
//	public String sceneSave(@RequestBody Scene scene) {
//		dbService.
//		
//	}

	@RequestMapping("/save-scene")
	public String saveScene(@RequestBody Scene scene, @PathVariable String parentId) {

		Scene sceneToUpdate = sceneRepo.findById(parentId).orElseThrow(() -> new SceneNotFoundException(scene.getId()));
		List<Option> listToUpdate = sceneToUpdate.getOptions();

		Story thisStory = storyRepo.findStoryById(sceneToUpdate.getStoryId());

		Option newOption = new Option(scene.getDescription(), SceneID.createSceneID(thisStory, new Scene(),
				sceneRepo.findById(scene.getParentId()).orElseThrow(() -> new SceneNotFoundException(scene.getId()))));

		listToUpdate.add(newOption);
		sceneToUpdate.setOptions(listToUpdate);
		sceneRepo.save(sceneToUpdate);

		return "StoryEdit";

	}

	// no longer need to call directly
	@RequestMapping("/deleteStory")
	public String storyDelete(Model model, @RequestParam String id) {
		dbService.deleteStory(id);
		model.addAttribute("type", "Story");
		return "StoryDeleted";
	}

	// this will wind up in story edit jsp
	// might need to backtrack to parentScene to avoid error of showing scene that
	// doesn't exist
	@RequestMapping("/deleteScene")
	public String sceneDelete(Model model, @RequestParam String id, @RequestParam String optionId) {
		Scene thisScene = dbService.getScene(id);
		Scene parentScene = dbService.getScene(thisScene.getParentId());
		List<Option> optionsToChange = parentScene.getOptions();
		optionsToChange.remove(Integer.parseInt(optionId));
		parentScene.setOptions(optionsToChange);
		sceneRepo.save(parentScene);
		dbService.deleteScene(id);
		model.addAttribute("type", "Scene");
		return "StoryDeleted";
	}

	// may need some functionality here that involves options - ie if story.option
	// == null, should create new List<Option> then add options to it.
	@RequestMapping("/addScene")
	public String addScene(Model model, @RequestParam(required = false) String id, @RequestParam String msg) {
		if (id != null) {
			Scene scene = sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
			model.addAttribute("id", id);
			model.addAttribute("title", scene.getStoryTitle());
		} else {
			model.addAttribute("title", "Enter Story Name");
		}
		model.addAttribute("msg", msg);
		return "AddScene";
	}

	@RequestMapping("/addOption")
	public String showAddOption(Model model, @RequestParam String id) {
		model.addAttribute("id", id);
		return "AddOption";
	}

	// @ Heather have fun with this nonsense
	// add option
	@PostMapping("/addOption")
	public String addOption(Model model, @RequestParam String id, @RequestParam String option,
			@RequestParam String description) {
		model.addAttribute("id", id);
		model.addAttribute("option", option);
		model.addAttribute("description", description);
		// so far the option being added works. the form is not yet there but we have a
		// new option at least.
		Scene scene = sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
		Story thisStory = storyRepo.findStoryById(scene.getStoryId());
		List<Option> addOptions = scene.getOptions();
		Option newOption = new Option(option, SceneID.createSceneID(thisStory, new Scene(), scene));
		addOptions.add(newOption);
		scene.setOptions(addOptions);
		sceneRepo.save(scene);
		// scene description

		String newSceneId = newOption.getSceneId();
		model.addAttribute("newSceneId", newSceneId);
		// set scene info based on this new sceneId
		Scene optionScene = new Scene(newSceneId, thisStory.getId(), description, scene.getId());
		sceneRepo.save(optionScene);

//			public Scene(String id, String storyId, String description, String parentId) 

//			if (scene.getOptions() == null) {
//			
//				scene.setOptions(addOptions);
//				sceneRepo.save(scene);
//			} else {
//				
//				scene.setOptions(addOptions);
//				sceneRepo.save(scene);
//			}

//			model.addAttribute("id", id);
//			model.addAttribute("title", scene.getStoryTitle());
//
//			model.addAttribute("title", "Enter Story Name");
//		
//			model.addAttribute("msg", msg);
		return "StoryPlay";
	}

	// create story + starting scene

	@RequestMapping("/createScene")
	public String createScene(Model model, @RequestParam String storyName, @RequestParam String sceneDescription,
			@RequestParam(required = false) String parentId, @RequestParam(required = false) String sceneChoice) {

		if (parentId == null) {
			Story newStory = new Story(storyName);
			newStory.setId(StoryID.createStoryID(storyName));

			Scene newScene = new Scene(newStory.getId(), sceneDescription, null);

			String sceneId = SceneID.createSceneID(newStory, newScene, null);

			newScene.setId(sceneId);

			newStory.setStartingSceneId(sceneId);
			newScene.setStoryTitle(storyName);

			storyRepo.save(newStory);
			sceneRepo.save(newScene);

			model.addAttribute("scene", newScene);

		} else {
			List<Option> addOptions = new ArrayList<Option>();

			Scene scene = sceneRepo.findById(parentId).orElseThrow(() -> new SceneNotFoundException(parentId));
			Story thisStory = storyRepo.findStoryById(scene.getStoryId());
			if (scene.getOptions() != null) {
				addOptions = scene.getOptions();
			}
			Option newOption = new Option(sceneChoice, SceneID.createSceneID(thisStory, new Scene(), scene));
			addOptions.add(newOption);
			scene.setOptions(addOptions);
			sceneRepo.save(scene);

			String newSceneId = newOption.getSceneId();
			Scene childScene = new Scene(newSceneId, thisStory.getId(), sceneDescription, scene.getId());
			sceneRepo.save(childScene);
			model.addAttribute("scene", scene);
		}

		
		return "StoryPlay";

	}

	@RequestMapping("/test-pexel/{sceneId}")
	public String randomName(Model model, @PathVariable("sceneId") String sceneId)
			throws URISyntaxException, IOException, InterruptedException {
		List<Photo> response = service.getPexels("Tiger");
		Scene test = dbService.getScene(sceneId);
		String title = dbService.getStoryName(sceneId);
		model.addAttribute("storyName", title);
		model.addAttribute("scene", test);
		model.addAttribute("response", response);
		return "testing";
	}

//	@RequestMapping("/test-Story-Name/{storyId}")
//	public String randomerName(Model model, @PathVariable("storyId") String storyId) {
//		
//	}

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

	@PostMapping("/updateScene")
	public String updateScene(Model model, @RequestParam String description, @RequestParam String sceneId) {
		Scene sceneToUpdate = sceneRepo.findById(sceneId).orElseThrow(() -> new SceneNotFoundException(sceneId));
		sceneToUpdate.setDescription(description);
		sceneRepo.save(sceneToUpdate);
		model.addAttribute("scene", sceneToUpdate);
		return "StoryPlay";
	}

}
