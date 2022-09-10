package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.http.HttpRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;

@Service
public class AdventureDBService {

	private RestTemplate rt = new RestTemplate();

	public Scene getScene(String id) {

		String url = "http://localhost:8080/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return response;
	}

	public String getStoryName(String storyId) {
		
		String url = "http://localhost:8080/find-story-name/{storyId}";

		String response = rt.getForObject(url, String.class, storyId);

		return response;

	}
	
	public Story[] getAllStories() {
		
		String url = "http://localhost:8080/allStories";

		Story[] response = rt.getForObject(url, Story[].class);
				
		return response;
	}
	
	public void deleteScene(String id) {
		 
		String url = "http://localhost:8080/delete-scene-tree/";
		HttpRequest httpRequest = HttpRequest
	            .newBuilder()
	            .DELETE()
	            .uri(URI.create(url + id))
	            .build();
		
		rt.delete(httpRequest.uri());

		
	}
	
	public void deleteStory(String id) {
		
		String url = "http://localhost:8080/delete-story/";
		
		HttpRequest httpRequest = HttpRequest
	            .newBuilder()
	            .DELETE()
	            .uri(URI.create(url + id))
	            .build();
		
		rt.delete(httpRequest.uri());
	}
	

}
