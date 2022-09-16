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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;
import co.grandcircus.FinalProject.Models.Photo;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Services.AdventureDBService;
import co.grandcircus.FinalProject.Services.PexelService;

@Controller
public class ViewsController {

	@Autowired
	PexelService service;

	@Autowired
	AdventureDBService dbService;

	@RequestMapping("/")
	public String index(Model model) {
		Story[] list = dbService.getAllStories();
		List<Story> storyList = Arrays.asList(list);
		model.addAttribute("storyList", storyList);

		return "index";
	}

	@RequestMapping("/play")
	public String play(Model model, @RequestParam String id) {
		Scene nextScene = dbService.getScene(id);
		model.addAttribute("scene", nextScene);
		return "StoryPlay";
	}
	
	@RequestMapping("/edit")
	public String storyEdit(Model model, @RequestParam String sceneId) {
		Scene editScene = dbService.getScene(sceneId);
		Story story = dbService.getStory(editScene.getStoryId());
		model.addAttribute("storyTitle", story.getTitle());
		model.addAttribute("scene", editScene);
		return "StoryEdit";
	}

	@RequestMapping("/deleteStory")
	public String storyDelete(Model model, @RequestParam String id) {
		
		dbService.deleteStory(id);
		//model.addAttribute("type", "Story");
		Story[] list = dbService.getAllStories();
		List<Story> storyList = Arrays.asList(list);
		model.addAttribute("storyList", storyList);
		
		return "index";
	}

	@RequestMapping("/deleteScene")
	public String sceneDelete(Model model, @RequestParam String id, @RequestParam String optionId) {
		Scene thisScene = dbService.getScene(id);
		Scene parentScene = dbService.getScene(thisScene.getParentId());
		Story story = dbService.getStory(thisScene.getStoryId());
		List<Scene> optionsToChange = parentScene.getChildList();
		optionsToChange.remove(Integer.parseInt(optionId));
		parentScene.setChildList(optionsToChange);
		dbService.saveScene(parentScene);
		dbService.deleteScene(id);

		model.addAttribute("storyTitle", story.getTitle());
		model.addAttribute("scene", parentScene);
		
		return "StoryEdit";
	}

	@RequestMapping("/addScene")
	public String addScene(Model model, @RequestParam(required = false) String id, @RequestParam String msg) {
		if (id != null) {
			//Scene scene = sceneRepo.findById(id).orElseThrow(() -> new SceneNotFoundException(id));
			Scene scene = dbService.getScene(id);
			model.addAttribute("id", id);
			model.addAttribute("title", scene.getStoryTitle());
		} else {
			model.addAttribute("title", "Enter Story Name");
		}
		model.addAttribute("msg", msg);
		return "AddScene";
	}

	// create story + starting scene
	@RequestMapping("/createScene")
	public String createScene(Model model, @RequestParam String storyName, @RequestParam String sceneDescription,
			@RequestParam(required = false) String parentId, @RequestParam(required = false) String sceneChoice, @RequestParam String photoUrl) {
		// if no parentId then you are creating a new story and a new scene.
		if (parentId == null) {
			Story newStory = new Story(storyName);
			newStory.setId(StoryID.createStoryID(storyName));

			Scene newScene = new Scene(newStory.getId(), sceneDescription, null);

			String sceneId = SceneID.createSceneID(newStory, newScene, null);

			newScene.setId(sceneId);

			newStory.setStartingSceneId(sceneId);
			newScene.setStoryTitle(storyName);
			newStory.setPhotoUrl(service.getRandomTinyPhotoUrl(photoUrl));
			newScene.setPhotoUrl(service.getRandomLandscapePhotoUrl(photoUrl));

			dbService.saveScene(newScene);
			dbService.saveStory(newStory);
			
			model.addAttribute("scene", newScene);

		} else {
			List<Scene> addOptions = new ArrayList<Scene>();
			// scene that we are adding a new option to 
			Scene scene = dbService.getScene(parentId);
			Story thisStory = dbService.getStory(scene.getStoryId());
			if (scene.getChildList() != null) {
				addOptions = scene.getChildList();
			}

			Scene newOption = new Scene();
			newOption = new Scene(SceneID.createSceneID(thisStory, newOption,
					dbService.getScene(scene.getId())),  
					thisStory.getId(), scene.getId(), sceneChoice, sceneDescription);
			
			newOption.setPhotoUrl(service.getRandomLandscapePhotoUrl(photoUrl));
			newOption.setStoryTitle(storyName);
			addOptions.add(newOption);
			scene.setChildList(addOptions);
	
			dbService.saveScene(scene);
			dbService.saveScene(newOption);
			model.addAttribute("scene", scene);
		}

		
		return "StoryPlay";

	}

	@RequestMapping("/test-pexel")
	public String randomName(Model model)
			throws URISyntaxException, IOException, InterruptedException {
		
		String photo = service.getRandomTinyPhotoUrl("asdfkjhalkjherfkjlhio3e89743");
		
		model.addAttribute("photo", photo);

		return "testing";
	}

	@RequestMapping("/updateScene")
	public String updateScene(Model model, @RequestParam String description, @RequestParam String sceneId) {
		Scene scene = dbService.getScene(sceneId);
		model.addAttribute("scene", scene);
		scene.setDescription(description);
		
		dbService.saveScene(scene);
		
		return "StoryPlay";
	}

}
