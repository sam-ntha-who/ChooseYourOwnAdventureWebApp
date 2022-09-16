package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
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
import co.grandcircus.FinalProject.Controllers.StoryNotFoundException;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;

@Service
public class AdventureDBService {


	private RestTemplate rt = new RestTemplate();

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
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			URI uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		HttpEntity<Story> httpEntity = new HttpEntity<>(story, headers);

		Story response = rt.postForObject(url, httpEntity, Story.class);
		return response;
	}
	
	public Scene getScene(String id) {

		String url = "http://localhost:8080/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return setPathLength(response);
	}
	
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
				
			int pathLength = getScenePathLength(s);
			s.setPathLength(pathLength);
			//boolean pathlenght test
			
			setPathBool(scene.getChildList());
			
			
			
//			//testing
//			System.out.println("path length from setPathLength " + s.getPathLength());
		}
		
		return scene;
	}

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
