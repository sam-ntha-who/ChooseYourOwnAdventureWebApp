package co.grandcircus.FinalProject.Services;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PexelsResposeModel {
	
	Integer page;
	@JsonProperty("per_page")
	Integer perPage;
	@JsonProperty("total_results")
	Integer totalResults;
	List<Photos> photos;

		
	
}
