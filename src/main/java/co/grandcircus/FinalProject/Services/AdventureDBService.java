package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Controllers.SceneNotFoundException;
import co.grandcircus.FinalProject.Models.Option;
import co.grandcircus.FinalProject.Models.Scene;
import co.grandcircus.FinalProject.Models.Story;
import co.grandcircus.FinalProject.Repositories.SceneRepository;

@Service
public class AdventureDBService {

	@Autowired
	SceneRepository sceneRepo;

	private RestTemplate rt = new RestTemplate();

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

//	public void saveScene(Scene scene) {
//		String url = "http://localhost:8080/save-scene/";
//		
//		rt.postForEntity(url, scene, null, null);
//	
//	}

	public Scene setPathLength(Scene scene) {

		if (scene.getOptions() == null) {
			return scene;
		}
		
		for (Option o : scene.getOptions()) {
			Scene s = sceneRepo.findSceneById(o.getSceneId());
			int plength = getScenePathLength(s);
			o.setPathLength(plength);
		}

		return scene;
	}

	private int getScenePathLength(Scene scene) {
		int pathLength = 0;

		if (scene.getOptions() == null) {
			return pathLength;
		}

		List<Scene> childList = new ArrayList<>();

		for (Option o : scene.getOptions()) {
			childList.add(sceneRepo.findSceneById(o.getSceneId()));
		}
		for (Scene s : childList) {
			pathLength = Math.max(pathLength, getScenePathLength(s));
		}
		return pathLength + 1;
	}

////		public int getTreeHeight(Node<Integer> root) {
//			   int height = 0;
//			    if (root == null ) {
//			    return height;
//			    }
//			    if (root.childern == null) {
//			    return 1;
//			  }
//			   for (Node<Integer> child : root.childern) {
//			    height = Math.max(height, getTreeHeight(child));
//			  }
//			   return height + 1;
//			}

//		ArrayList<Integer> counters = new ArrayList();
//		
//		ArrayList<Scene> scenesToCheckPaths = new ArrayList<>();
//		
//		Scene sceneToCheck = scene;
//		
//		scenesToCheckPaths.add(sceneToCheck);
//		for (int i=0; i < scene.getOptions().size(); i++) {
//			counters.add(0);
//			List<Option> optionList = sceneRepo.findSceneById(scene.getOptions().get(i).getSceneId()).getOptions();
//			if (optionList.size() != 0 || optionList != null ) {
//				counters.set(i, counters.get(i)+1);
//				for (Option o : optionList) {
//					
//				}
//			}
//		}
//	}

	// Error Handling
	@ResponseBody
	@ExceptionHandler(SceneNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String sceneNotFoundHandler(SceneNotFoundException ex) {
		return ex.getMessage();
	}

//	private String id;
//	private String storyId;
//	private String storyTitle;
//	private String parentId;
//	private String description;
//	private List<Option> options;
//	
//	private String description;
//	private String nextSceneId;

}
