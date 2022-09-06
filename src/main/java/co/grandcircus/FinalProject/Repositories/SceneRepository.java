package co.grandcircus.FinalProject.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.grandcircus.FinalProject.Models.Scene;

public interface SceneRepository extends MongoRepository<Scene, String> {
	
	List<Scene> findAll();
	
	List<Scene> findByStoryIdAndParentId(String storyId, String parentId);
	
	Optional<Scene> findByStoryIdAndId(String storyId, String id);
}
