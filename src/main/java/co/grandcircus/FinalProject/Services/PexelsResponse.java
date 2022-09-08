package co.grandcircus.FinalProject.Services;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.grandcircus.FinalProject.Models.Photo;

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
