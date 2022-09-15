package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Controllers.SceneNotFoundException;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;

@Service
public class AdventureDBService {

	@Autowired
	SceneRepository sceneRepo;

	private RestTemplate rt = new RestTemplate();

//	public Scene saveScene(Scene scene) {
//		
//		String url = "http://localhost:8080/save-scene";
//		
//		Scene response = rt.postForObject(url, rt, Scene.class, scene);
//		
//		return response;
//	}

	public Scene saveScene(Scene scene) {
		
		String url = "http://localhost:8080/save-scene";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			URI uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		HttpEntity<Scene> httpEntity = new HttpEntity<>(scene, headers);

		Scene response = rt.postForObject(url, httpEntity, Scene.class);
		
		return response;
		
	}
	
	public Story saveStory(Story story) {
		
		String url = "http://localhost:8080/save-story";
		
		Story response = rt.postForObject(url, rt, Story.class, story);
		
		return response;
	}
	
	public Scene getScene(String id) {

		String url = "http://localhost:8080/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return setPathLength(response);
	}

	
	public String getStoryName(String storyId) {

		String url = "http://localhost:8080/find-story-name/{storyId}";

		String response = rt.getForObject(url, String.class, storyId);

		return response;

	}
	// needs testing
	public Story getStory(String storyId) {

		String url = "http://localhost:8080/story/{storyId}";

		Story response = rt.getForObject(url, Story.class, storyId);

		return response;
	}

	public Story[] getAllStories() {

		String url = "http://localhost:8080/allStories";

		Story[] response = rt.getForObject(url, Story[].class);

		return response;
	}

	public void deleteScene(String id) {

		String url = "http://localhost:8080/delete-scene-tree/";
		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}

	public void deleteStory(String id) {

		String url = "http://localhost:8080/delete-story/";

		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}

	public Scene setPathLength(Scene scene) {

		if (scene.getChildList() == null) {
			return scene;
		}
	


		for(Scene s : scene.getChildList()) {
			
//			//testing
//			System.out.println(s);
		
			int pathLength = getScenePathLength(s);
			
			s.setPathLength(pathLength);
//			//testing
//			System.out.println("path length from setPathLength " + s.getPathLength());
		}
		
		return scene;
	}

	private int getScenePathLength(Scene scene) {
//		//testing
//		System.out.println("getScenePathLength method runs...I promise");
		int pathLength = 0;
		// test
		List<Scene> kidList = getScene(scene.getParentId()).getChildList();
		List<Scene> childList = sceneRepo.findByParentId(scene.getId());
		scene.setChildList(childList);
		
		if (scene.getChildList() == null) {
//			//testing
//			System.out.println("childless yo!");
			return pathLength;
		}

		
		for (Scene s : scene.getChildList()) {
//			//testing
//			System.out.println("Pathlength is currently "+ pathLength);
			pathLength = Math.max(pathLength, getScenePathLength(s));
		}

		return pathLength + 1;
	}

	// Error Handling
	@ResponseBody
	@ExceptionHandler(SceneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String sceneNotFoundHandler(SceneNotFoundException ex) {
		return ex.getMessage();
	}


}
