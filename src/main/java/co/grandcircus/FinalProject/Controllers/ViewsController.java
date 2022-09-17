package co.grandcircus.FinalProject.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import co.grandcircus.FinalProject.HelperFunctions.SceneID;
import co.grandcircus.FinalProject.HelperFunctions.StoryID;
import co.grandcircus.FinalProject.Models.Photo;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Services.AdventureDBService;
import co.grandcircus.FinalProject.Services.PexelService;
import co.grandcircus.FinalProject.Services.WordService;

@Controller
public class ViewsController {

	@Autowired
	PexelService service;

	@Autowired
	AdventureDBService dbService;

	@Autowired
	WordService wordService;

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
		// model.addAttribute("type", "Story");
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
			// Scene scene = sceneRepo.findById(id).orElseThrow(() -> new
			// SceneNotFoundException(id));
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
			@RequestParam(required = false) String parentId, @RequestParam(required = false) String sceneChoice) {
		
		Scene scene;
		String newKeyword = wordService.getExtractedKeywords(sceneDescription);

		// if no parentId then you are creating a new story and a new scene.
		if (parentId == null) {
			Story newStory = new Story(storyName);
			newStory.setId(StoryID.createStoryID(storyName));

			scene = new Scene(newStory.getId(), sceneDescription, null);

			String sceneId = SceneID.createSceneID(newStory, scene, null);

			scene.setId(sceneId);

			newStory.setStartingSceneId(sceneId);
			scene.setStoryTitle(storyName);
			newStory.setPhotoUrl(service.getRandomTinyPhotoUrl(newKeyword));
			scene.setPhotoUrl(service.getRandomLandscapePhotoUrl(newKeyword));

			dbService.saveScene(scene);
			dbService.saveStory(newStory);

			model.addAttribute("scene", scene);

		} else {
			List<Scene> addOptions = new ArrayList<Scene>();
			// scene that we are adding a new option to
			Scene parentScene = dbService.getScene(parentId);
			Story thisStory = dbService.getStory(parentScene.getStoryId());
			if (parentScene.getChildList() != null) {
				addOptions = parentScene.getChildList();
			}

			scene = new Scene();
			scene = new Scene(SceneID.createSceneID(thisStory, scene, dbService.getScene(parentScene.getId())),
					thisStory.getId(), parentScene.getId(), sceneChoice, sceneDescription);

			scene.setPhotoUrl(service.getRandomLandscapePhotoUrl(newKeyword));
			scene.setStoryTitle(storyName);
			addOptions.add(scene);
			parentScene.setChildList(addOptions);

			dbService.saveScene(parentScene);
			dbService.saveScene(scene);
			model.addAttribute("scene", parentScene);
		}

//		if (!keyword.equals("")) {
//			List<Photo> picList = service.getPexels(keyword);
//			model.addAttribute("picList", picList);
//			model.addAttribute("scene", scene);
//			return "PicSelect";
//		}

		return "StoryPlay";

	}

	@RequestMapping("/addPicture")
	public String addPicture(@RequestParam String pic, @RequestParam String id, Model model) {
		
		System.out.println(pic);
		Scene scene = dbService.getScene(id);
		scene.setPhotoUrl(pic + "&cs=tinysrgb&fit=crop&h=627&w=1200");
		dbService.saveScene(scene);
		model.addAttribute("scene", scene);
		
		return "StoryPlay";
	}

	@RequestMapping("/test-pexel")
	public String randomName(Model model, @RequestParam String text)
			throws URISyntaxException, IOException, InterruptedException {

		// TODO: logic to autoload photos or allow user to choose keyword

		String keywords = wordService.getExtractedKeywords(text);

		model.addAttribute("keywords", keywords);

//		WordResponse responseMap = wordService.getWordResponse(text);
//		//test
//		System.out.println("WordResponse **************************");
//		
//		ArrayList<String> stringList = new ArrayList<>();
//	    Iterator<Map.Entry<String, Integer>> iterator = responseMap.getKeywordMap().entrySet().iterator();
//	    
//	    while (iterator.hasNext()) {
//	        Map.Entry<String, Integer> entry = iterator.next();
//	        System.out.println(entry.getKey() + ":" + entry.getValue());
//	        stringList.add(entry.getKey());
//	    }
//		
//	  //test
//	  		System.out.println("End of while **************************");
//	    String response = stringList.toString();
//	    
//		model.addAttribute("response", response);

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
