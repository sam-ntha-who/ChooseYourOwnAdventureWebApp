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

@Service
public class PexelService {

	@Value("${apiKey}")
	private String apiKey;

	public String getPexels() throws URISyntaxException, IOException, InterruptedException {
	HttpClient client = HttpClient.newHttpClient();

	HttpRequest request = HttpRequest.newBuilder()
	  .GET()
	  .uri(new URI("https://api.pexels.com/v1/"))
	  .header("Authorization", apiKey)
	  .build();

	HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
	System.out.println(response.body());
	return null;
	}
}
