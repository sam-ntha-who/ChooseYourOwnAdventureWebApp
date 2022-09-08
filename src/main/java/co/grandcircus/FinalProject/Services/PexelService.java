package co.grandcircus.FinalProject.Services;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PexelService {

	@Value("${apiKey}")
	private String apiKey;
	
	private RestTemplate restTemplate = new RestTemplate();
	

	//TODO -- handle exceptions
	public PexelsResponse getPexels() throws URISyntaxException, IOException, InterruptedException {
		
	HttpClient client = HttpClient.newHttpClient();

	HttpRequest request = HttpRequest.newBuilder()
	  .GET()
	  .uri(new URI("https://api.pexels.com/v1/search?query=Tiger"))
	  .header("Authorization", apiKey)
	  .build();

	HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
	System.out.println(response.body());
	return null;
	}
	
	//public 
}
