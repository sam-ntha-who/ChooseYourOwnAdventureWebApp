package co.grandcircus.FinalProject.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import co.grandcircus.FinalProject.Models.Story;

public interface StoryRepository extends MongoRepository<Story, String> {

	List<Story> findAll();

	Optional<Story> findById(String id);

}
