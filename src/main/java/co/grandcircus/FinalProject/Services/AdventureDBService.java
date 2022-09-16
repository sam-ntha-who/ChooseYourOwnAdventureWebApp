package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;


@Service
public class AdventureDBService {


	private RestTemplate rt = new RestTemplate();

	public Scene saveScene(Scene scene) {
		
		String url = "http://localhost:8081/save-scene";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		HttpEntity<Scene> httpEntity = new HttpEntity<>(scene, headers);

		Scene response = rt.postForObject(url, httpEntity, Scene.class);
		return response;
				
	}
	
	public Story saveStory(Story story) {
		
		String url = "http://localhost:8081/save-story";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	
		HttpEntity<Story> httpEntity = new HttpEntity<>(story, headers);

		Story response = rt.postForObject(url, httpEntity, Story.class);
		return response;
	}
	
	public Scene getScene(String id) {

		String url = "http://localhost:8081/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return setPathLength(response);
	}
	
	public Story getStory(String storyId) {

		String url = "http://localhost:8081/story/{storyId}";

		Story response = rt.getForObject(url, Story.class, storyId);

		return response;
	}

	public Story[] getAllStories() {

		String url = "http://localhost:8081/allStories";

		Story[] response = rt.getForObject(url, Story[].class);

		return response;
	}

	public void deleteScene(String id) {

		String url = "http://localhost:8081/delete-scene-tree/";
		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}

	public void deleteStory(String id) {

		String url = "http://localhost:8081/delete-story/";

		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}

	// can this be moved out of the api service
	public Scene setPathLength(Scene scene) {

		if (scene.getChildList() == null) {
			return scene;
		}
	
		for(Scene s : scene.getChildList()) {
				
			int pathLength = getScenePathLength(s);
			s.setPathLength(pathLength);

			
			setPathBool(scene.getChildList());

		}
		
		return scene;
	}
	// could the path functionality move out of the controller while keeping the tree function?
	private int getScenePathLength(Scene scene) {

		int pathLength = 0;

		List<Scene> childList = getScene(scene.getId()).getChildList();
		scene.setChildList(childList);
		
		if (scene.getChildList() == null) {

			return pathLength;
		}

		for (Scene s : scene.getChildList()) {

			pathLength = Math.max(pathLength, getScenePathLength(s));
		}

		return pathLength + 1;
	}
	//path boolean method
	
	private static void setPathBool(List<Scene> childList) {
		for(Scene s : childList) {
			s.setLongest(false);
			s.setShortest(false);
		}
		Scene shortest = childList.get(0);
		Scene longest = childList.get(0);
		for(Scene s : childList) {
			if(shortest.getPathLength() > s.getPathLength()) {
				shortest = s;
			}
			if(longest.getPathLength() < s.getPathLength()) {
				longest = s;
			}
		}
		for(Scene s : childList) {
			if(shortest.getPathLength() == s.getPathLength()) {
				s.setShortest(true);
			}
			if(longest.getPathLength() == s.getPathLength()) {
				s.setLongest(true);
			}
		}
		
	}

}
