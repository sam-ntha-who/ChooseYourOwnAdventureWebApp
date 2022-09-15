package co.grandcircus.FinalProject.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordResponse {

	@JsonProperty("result_msg")
	private String resultMsg;
	
	private Topic topic;
	
	private Keyword keyword;
	
}
