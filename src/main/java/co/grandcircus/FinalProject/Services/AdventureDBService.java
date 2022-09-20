package co.grandcircus.FinalProject.Services;

import java.net.URI;
import java.net.http.HttpRequest;
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

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/save-scene";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Scene> httpEntity = new HttpEntity<>(scene, headers);

		Scene response = rt.postForObject(url, httpEntity, Scene.class);

		return response;
	}

	public Story saveStory(Story story) {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/save-story";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Story> httpEntity = new HttpEntity<>(story, headers);

		Story response = rt.postForObject(url, httpEntity, Story.class);

		return response;
	}

	public Scene getScene(String id) {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/read-scene/{id}";

		Scene response = rt.getForObject(url, Scene.class, id);

		return response;
	}

	public Story getStory(String storyId) {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/story/{storyId}";

		Story response = rt.getForObject(url, Story.class, storyId);

		return response;
	}

	public Story[] getAllStories() {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/allStories";

		Story[] response = rt.getForObject(url, Story[].class);

		return response;
	}

	public void deleteScene(String id) {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/delete-scene-tree/";

		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}

	public void deleteStory(String id) {

		String url = "http://cyoawebappapi-env.eba-nvqgee5v.us-east-2.elasticbeanstalk.com/delete-story/";

		HttpRequest httpRequest = HttpRequest.newBuilder().DELETE().uri(URI.create(url + id)).build();

		rt.delete(httpRequest.uri());

	}
}
