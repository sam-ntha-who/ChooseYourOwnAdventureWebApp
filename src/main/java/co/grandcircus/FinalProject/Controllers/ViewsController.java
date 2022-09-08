package co.grandcircus.FinalProject.Controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import co.grandcircus.FinalProject.Models.Photo;
import co.grandcircus.FinalProject.Repositories.SceneRepository;
import co.grandcircus.FinalProject.Repositories.StoryRepository;
import co.grandcircus.FinalProject.Services.PexelService;
import co.grandcircus.FinalProject.Services.PexelsResponse;

@Controller
public class ViewsController {
	
	@Autowired
	StoryRepository storyRepo;

	@Autowired
	SceneRepository sceneRepo;
	
	@Autowired
	PexelService service;
	
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

	
	@RequestMapping("/test-pexel")
	public String randomName(Model model) throws URISyntaxException, IOException, InterruptedException {
		List<Photo> response = service.getPexels("Tiger");
		model.addAttribute("response", response);
		return "hello";
	}
	
	
}
