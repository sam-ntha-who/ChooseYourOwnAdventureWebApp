package co.grandcircus.FinalProject.Services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import co.grandcircus.FinalProject.Models.Photo;

@Service
public class PexelService {

	@Value("${apiKey}")
	private String apiKey;
	
	private RestTemplate restTemplate = new RestTemplate();
	

	
	public List<Photo> getPexels(String search) {
		String url = "https://api.pexels.com/v1/search?query={search}";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<PexelsResponse> result = restTemplate.exchange (url, HttpMethod.GET, requestEntity, PexelsResponse.class, search);
		return result.getBody().getPhotos();
	}
	
	public String getThumbnailUrl(String keyword) {
		String url = "https://api.pexels.com/v1/search?query={keyword}&per_page=1";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", apiKey);

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

		ResponseEntity<PexelsResponse> result = restTemplate.exchange (url, HttpMethod.GET, requestEntity, PexelsResponse.class, keyword);
		return result.getBody().getPhotos().get(0).getSrc().getTiny();
	}
	//TODO -- handle exceptions
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
	
	//public 
}
