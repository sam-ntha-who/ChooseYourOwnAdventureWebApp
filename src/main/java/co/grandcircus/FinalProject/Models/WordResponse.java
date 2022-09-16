package co.grandcircus.FinalProject.Models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordResponse {

	@JsonProperty("result_msg")
	private String resultMsg;
	
	private Map<String, Double> keyword;
	
	private Map<String, Integer> topic;

	
	//getters and setters
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public Map<String, Double> getTopicMap() {
		return keyword;
	}

	public void setTopicMap(Map<String, Double> topicMap) {
		this.keyword = topicMap;
	}

	public Map<String, Integer> getKeywordMap() {
		return topic;
	}

	public void setKeywordMap(Map<String, Integer> keywordMap) {
		this.topic = keywordMap;
	}

	
	
}
