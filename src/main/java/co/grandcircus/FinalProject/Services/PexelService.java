package co.grandcircus.FinalProject.Services;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Models.PexelsResponse;
import co.grandcircus.FinalProject.Models.Photo;

@Service
public class PexelService {

	@Value("${apiKey}")
	private String apiKey;

	private RestTemplate restTemplate = new RestTemplate();

	public List<Photo> getPexels(String search) {
		String url = "https://api.pexels.com/v1/search?query={search}&per_page=9";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<PexelsResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				PexelsResponse.class, search);
		return result.getBody().getPhotos();
	}

//	Depreciated
//	public String getThumbnailUrl(String keyword) {
//		String url = "https://api.pexels.com/v1/search?query={keyword}&per_page=1";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("Authorization", apiKey);
//
//		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//		ResponseEntity<PexelsResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//				PexelsResponse.class, keyword);
//		return result.getBody().getPhotos().get(0).getSrc().getTiny();
//	}

	
	public String getRandomTinyPhotoUrl(String keyword) {

		Random num = new Random();

		String url = "https://api.pexels.com/v1/search?query={keyword}";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<PexelsResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				PexelsResponse.class, keyword);

		List<Photo> photoList = result.getBody().getPhotos();

		if (photoList.size() == 0) {

			return "https://images.pexels.com/photos/4271933/pexels-photo-4271933.jpeg?auto=compress&cs=tinysrgb&h=650&w=940";
		}
		
		int i = num.nextInt(photoList.size());

		String photoUrl = photoList.get(i).getSrc().getTiny();

		return photoUrl;
	}
	
	public String getRandomLandscapePhotoUrl(String keyword) {

		Random num = new Random();

		String url = "https://api.pexels.com/v1/search?query={keyword}";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<PexelsResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				PexelsResponse.class, keyword);

		List<Photo> photoList = result.getBody().getPhotos();

		if (photoList.size() == 0) {

			return "https://images.pexels.com/photos/4271933/pexels-photo-4271933.jpeg?auto=compress&cs=tinysrgb&h=650&w=940";
		}
		
		int i = num.nextInt(photoList.size());

		String photoUrl = photoList.get(i).getSrc().getLandscape();

		return photoUrl;
	}

	// TODO -- handle exceptions
//	public PexelsResponse getPexels() throws URISyntaxException, IOException, InterruptedException {
//		
//	HttpClient client = HttpClient.newHttpClient();
//
//	HttpRequest request = HttpRequest.newBuilder()
//	  .GET()
//	  .uri(new URI("https://api.pexels.com/v1/search?query=Tiger"))
//	  .header("Authorization", apiKey)
//	  .build();
//
//	HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
//	System.out.println(response.body());
//	return null;
//	}

	// public
}
