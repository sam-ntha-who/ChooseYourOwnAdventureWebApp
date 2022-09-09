package co.grandcircus.FinalProject.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
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
//		List<Story> storyList = dbService.getAllStories();
//		model.addAttribute("storyList", storyList);
		return "testing";
	}

	@RequestMapping("/play")
	public String play(Model model, @RequestParam String sceneId) {
		Scene nextScene = dbService.getScene(sceneId);
		model.addAttribute("scene", nextScene);
		return "StoryPlay";
	}

	@RequestMapping("/edit")
	public String storyEdit(Model model, @RequestParam String sceneId) {
		Scene editScene = dbService.getScene(sceneId);
		model.addAttribute("scene", editScene);
		return "StoryEdit";
	}

//	// call directly
//	@PostMapping("/update")
//	public String sceneSave(@RequestBody Scene scene) {
//		dbService.
//		
//	}

//	// call directly
//	@DeleteMapping("/delete/{id}")
//	public String sceneDelete(@PathVariable String id) {
//		sceneRepo.delete
//		return "StoryDeleted";
//	}

	@RequestMapping("/add-scene")
	public String addScene(Model model, @PathVariable String id) {
		model.addAttribute("id", id);
		return "AddScene";
	}
	
	
	// id or parentId???
	@RequestMapping("/save-scene")
	public String saveScene(@RequestBody Scene scene, @PathVariable String parentId) {
		
	Scene sceneToUpdate = sceneRepo.findById(parentId)
			.orElseThrow(() -> new SceneNotFoundException(scene.getId()));
	List<Option> listToUpdate = sceneToUpdate.getOptions();

	// this should be probably set to Optional<Story>

	Story thisStory = storyRepo.findStoryById(sceneToUpdate.getStoryId());
	
	// need scene from parentId
	Option newOption = new Option(scene.getDescription(), SceneID.createSceneID(thisStory, new Scene(), sceneRepo.findById(scene.getParentId()).orElseThrow(() -> new SceneNotFoundException(scene.getId()))));
	
	listToUpdate.add(newOption);
	sceneToUpdate.setOptions(listToUpdate);
	sceneRepo.save(sceneToUpdate);
	// may need to add more to this for actual scene - sam
	return "StoryEdit";
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
}
