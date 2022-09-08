package co.grandcircus.FinalProject.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Controllers.SceneNotFoundException;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Models.StoryResponse;

@Service
public class AdventureDBService {

	private RestTemplate rt = new RestTemplate();

	public Scene getScene(String id) {

		String url = "localhost:8080/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return response;
	}

	public String getStoryName(String storyId) {
		
		String url = "localhost:8080/find-story-name/{storyId}";

		String response = rt.getForObject(url, String.class, storyId);

		return response;

	}
//	
//	public StoryResponse[] getAllStories() {
//		
//		String url = "http://localhost:8080/allStories";
//
//		StoryResponse[] response = rt.getForObject(url, StoryResponse[].class);
//		
//		
//		return response;
//	}
	
	public void deleteScene(String storyId, String id) {
		
		String url = "http://localhost:8080/delete-scene-tree";
		
		rt.delete(url, storyId, id);
		
	}
}
