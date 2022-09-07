package co.grandcircus.FinalProject.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import co.grandcircus.FinalProject.Repositories.SceneRepository;
import co.grandcircus.FinalProject.Repositories.StoryRepository;

@Controller
public class ViewsController {
	
	@Autowired
	StoryRepository storyRepo;

	@Autowired
	SceneRepository sceneRepo;
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/play")
	public String storyPlay() {
		return "StoryPlay";
	}
	
	@RequestMapping("/edit")
	public String storyEdit() {
		return "StoryEdit";
	}
	
	@RequestMapping("/delete")
	public String storyDelete() {
		return "StoryDeleted";
	}
	
	@RequestMapping("/addScene")
	public String addScene() {
		return "AddScene";
	}
	
}
