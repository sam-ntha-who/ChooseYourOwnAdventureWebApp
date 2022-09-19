package co.grandcircus.FinalProject.Models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PexelsResponse {
	
	@JsonProperty("total_results")
	private Integer totalResults;
	private List<Photo> photos;
	
	
	public Integer getTotalResults() {
		return totalResults;
	}
	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public List<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(List<Photo> photos) {
		this.photos = photos;
	}
		
	
}
