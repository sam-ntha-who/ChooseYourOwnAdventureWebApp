package co.grandcircus.FinalProject.Services;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class WordService {

	@Value("${apiKey2}")
	private String apiKey;

	private RestTemplate restTemplate = new RestTemplate();

	public String getExtractedKeywords(String text) {
		
		//build HttpEntity
		String url = "https://twinword-topic-tagging.p.rapidapi.com/generate/?text={text}";

		HttpHeaders headers = new HttpHeaders();
		headers.set("X-RapidAPI-Key", apiKey);
		headers.set("X-RapidAPI-Host", "twinword-topic-tagging.p.rapidapi.com");

		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		
		//use HttpEntity in restTemplate response and return as JsonNode
		JsonNode jsonResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
				JsonNode.class, text).getBody(); // this is a key-value list of all properties for this object
		
		// map JSON response ('topic' of key-value pairs) to a Map<String, Double>
		// 'topic' is a map of words that most closely relate to the input text and a score (Double)
		// of how well those topics relate to the overall text meaning.
		// additional logic can be added to better use this score, for now, we'll use the top-scoring topic.
		
		String extractedKeywords = "";
		try {
		
		ObjectMapper topicMapper = new ObjectMapper();
		Map<String, Double> topicsMap = topicMapper.convertValue(jsonResponse.get("topic"),
				new TypeReference<Map<String, Double>>() {});
		
		// map JSON response ('keyword' of key-value pairs) to a Map<String, Integer>
		// 'keyword' is a map of words and their frequency (Integer) in the text
		ObjectMapper keywordMapper = new ObjectMapper();
		Map<String, Integer> keywordsMap = keywordMapper.convertValue(jsonResponse.get("keyword"),
				new TypeReference<Map<String, Integer>>() {});
		
		// convert key-value maps to List of map Entry
		List<Entry<String, Double>> topicsEntryList = topicsMap.entrySet()
		        .stream()
		        .limit(2)
		        .collect(Collectors.toList());
		
		List<Entry<String, Integer>> keywordsEntryList = keywordsMap.entrySet()
		        .stream()
		        .limit(2)
		        .collect(Collectors.toList());
		
		// build extractedKeywords
		extractedKeywords = topicsEntryList.get(0).getKey() + " "
				+ keywordsEntryList.get(0).getKey() + " "
				+ keywordsEntryList.get(1).getKey();
		
		} catch (Exception e) {
			
			extractedKeywords = text;
		}
		
		return extractedKeywords;
	}
	
	
//	public Map<String, Double> thingie(String text) {
//		
//		String url = "https://twinword-topic-tagging.p.rapidapi.com/generate/?text={text}";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("X-RapidAPI-Key", apiKey);
//		headers.set("X-RapidAPI-Host", "twinword-topic-tagging.p.rapidapi.com");
//
//		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
////		ResponseEntity<JsonNode> result = restTemplate.getForEntity(url, JsonNode.class, text);
////		ResponseEntity<JsonNode> e = restTemplate.getForEntity(API_URL, JsonNode.class);
//		JsonNode jsonResponse = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//				JsonNode.class, text).getBody();
//		
////		ResponseEntity<JsonNode> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
////				JsonNode.class, text);
////		
////		JsonNode map = result.getBody(); // this is a key-value list of all properties for this object
//		//test
//		System.out.println(jsonResponse);
//
//		ObjectMapper mapper = new ObjectMapper();
//		Map<String, Double> topics = mapper.convertValue(jsonResponse.get("topic"),
//				new TypeReference<Map<String, Double>>() {});
//		//test
//		System.out.println(topics);
//		return topics;
//	}
//
//	public WordResponse getWordResponse(String text) {
//
//		String url = "https://twinword-topic-tagging.p.rapidapi.com/generate/?text={text}";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("X-RapidAPI-Key", apiKey);
//		headers.set("X-RapidAPI-Host", "twinword-topic-tagging.p.rapidapi.com");
//
//		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//		ResponseEntity<WordResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//				WordResponse.class, text);
//		// testing map
//
//		System.out.println(result.getBody().toString());
//
//		return result.getBody();
//
//	}
//
//	public String getKeywords(String text) {
//
//		String url = "https://twinword-topic-tagging.p.rapidapi.com/generate/?text={text}";
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.set("X-RapidAPI-Key", apiKey);
//		headers.set("X-RapidAPI-Host", "twinword-topic-tagging.p.rapidapi.com");
//
//		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
//
//		ResponseEntity<WordResponse> result = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
//				WordResponse.class, text);
//		System.out.println(result.getBody().toString());
//
//		return null;
//
//	}
}