package co.grandcircus.FinalProject.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Models.Scene;

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
}
