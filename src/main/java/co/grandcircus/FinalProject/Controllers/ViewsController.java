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
import org.springframework.web.bind.annotation.PostMapping;
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

	// api service call done
	@RequestMapping("/")
	public String index(Model model) {
		Story[] list = dbService.getAllStories();
		List<Story> storyList = Arrays.asList(list);
		model.addAttribute("storyList", storyList);

		return "index";
	}

	// api service call done
	@RequestMapping("/play")
	public String play(Model model, @RequestParam String id) {
		Scene nextScene = dbService.getScene(id);
		model.addAttribute("scene", nextScene);
		return "StoryPlay";
	}

	// api service call done 
	@RequestMapping("/edit")
	public String storyEdit(Model model, @RequestParam String sceneId) {
		Scene editScene = dbService.getScene(sceneId);
		Story story = dbService.getStory(editScene.getStoryId());
		model.addAttribute("storyTitle", story.getTitle());
		model.addAttribute("scene", editScene);
		return "StoryEdit";
	}

	// api service call needs to be checked for save? update instead maybe
	// api service call needs to be made for get sceneid instead of repo - done but see if it works
	@RequestMapping("/save-scene")
	public String saveScene(@RequestBody Scene scene, @PathVariable String parentId) {

		Scene sceneToUpdate = dbService.getScene(parentId);
		List<Scene> listToUpdate;
		if (sceneToUpdate.getChildList()== null) {
			listToUpdate = new ArrayList<>();
		} else {
			listToUpdate = sceneToUpdate.getChildList();
		}

		Story thisStory = dbService.getStory(sceneToUpdate.getStoryId());

		Scene newOption = new Scene();
		newOption = new Scene(SceneID.createSceneID(thisStory, newOption,
				dbService.getScene(scene.getParentId())), 
				thisStory.getId(), sceneToUpdate.getParentId(), sceneToUpdate.getDescription(), newOption.getOption());
		

		listToUpdate.add(newOption);
		sceneToUpdate.setChildList(listToUpdate);
	//	sceneRepo.save(sceneToUpdate);
		dbService.saveScene(sceneToUpdate);

		return "StoryEdit";

	}

	// api service call done
	@RequestMapping("/deleteStory")
	public String storyDelete(Model model, @RequestParam String id) {
		
		dbService.deleteStory(id);
		//model.addAttribute("type", "Story");
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


	// delete api service call done
	// parent scene api call needs to be done - again might be update and not save
	@RequestMapping("/deleteScene")
	public String sceneDelete(Model model, @RequestParam String id, @RequestParam String optionId) {
		Scene thisScene = dbService.getScene(id);
		Scene parentScene = dbService.getScene(thisScene.getParentId());
		Story story = storyRepo.findById(thisScene.getStoryId()).orElseThrow(() -> new SceneNotFoundException(id));
		List<Scene> optionsToChange = parentScene.getChildList();
//		List<Option> optionsToChange = parentScene.getOptions();
		optionsToChange.remove(Integer.parseInt(optionId));
		parentScene.setChildList(optionsToChange);
		//dbService.saveScene(parentScene);
		sceneRepo.save(parentScene);
		dbService.deleteScene(id);
		//model.addAttribute("type", "Scene");
		model.addAttribute("storyTitle", story.getTitle());
		model.addAttribute("scene", parentScene);
		
		return "StoryEdit";
	}

	// api service call done
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
	// api service call needs to be done
	// get story needs testing
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
			
			storyRepo.save(newStory);
			sceneRepo.save(newScene);
// dbService.saveScene(newScene);
// dbService.saveStory(newStory);
			
			model.addAttribute("scene", newScene);

		} else {
			List<Scene> addOptions = new ArrayList<Scene>();
			// scene that we are adding a new option to 
			Scene scene = dbService.getScene(parentId);
			Story thisStory = dbService.getStory(scene.getStoryId());
			if (scene.getChildList() != null) {
			//	addOptions = scene.getOption();
				addOptions = scene.getChildList();
			}
			// check this logic
			Scene newOption = new Scene();
			newOption = new Scene(SceneID.createSceneID(thisStory, newOption,
					dbService.getScene(scene.getId())), 
					thisStory.getId(), scene.getId(), sceneChoice, sceneDescription);
			
			newOption.setPhotoUrl(service.getRandomLandscapePhotoUrl(photoUrl));
			newOption.setStoryTitle(storyName);
		//	Option newOption = new Option(sceneChoice, SceneID.createSceneID(thisStory, new Scene(), scene));
			addOptions.add(newOption);
			scene.setChildList(addOptions);
			sceneRepo.save(scene);
			sceneRepo.save(newOption);

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

	// api calls need to be done
	@PostMapping("/updateScene")
	public String updateScene(Model model, @RequestParam String description, @RequestParam String sceneId) {
		Scene sceneToUpdate = sceneRepo.findById(sceneId).orElseThrow(() -> new SceneNotFoundException(sceneId));
		sceneToUpdate.setDescription(description);
		sceneRepo.save(sceneToUpdate);
		model.addAttribute("scene", sceneToUpdate);
		return "StoryPlay";
	}

}
