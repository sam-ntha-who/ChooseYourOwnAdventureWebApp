package co.grandcircus.FinalProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mongodb.MongoCommandException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Component
class MyInitializingBean implements InitializingBean {

	@Value("${spring.data.mongodb.uri}")
	private String connectionString;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		String storiesMaster = "stories";
		String scenesMaster = "scenes";
		
		String storiesUser = "storiesTESTING";
		String scenesUser = "scenesTESTING";
		
		try (MongoClient mongoClient = MongoClients.create(connectionString)) {

			MongoDatabase adventureDB = mongoClient.getDatabase("AdventureStoryDB");
			
			// creates 2 new collections titled <String storiesUser> and <String scenesUser>
			// if it exists already, it's cleared and a new one is created in its place
			try {

				adventureDB.createCollection(storiesUser);
				adventureDB.createCollection(scenesUser);

			} catch (MongoCommandException e) {

				adventureDB.getCollection(storiesUser).drop();
				adventureDB.getCollection(scenesUser).drop();

				adventureDB.createCollection(storiesUser);
				adventureDB.createCollection(scenesUser);
			}

			// creates MongoCollection from MASTER collections, and adds them into a List
			// MASTER Stories List created
			MongoCollection<Document> masterStoriesCollection = adventureDB.getCollection(storiesMaster);
			List<Document> masterStoriesList = new ArrayList<>();
			FindIterable<Document> iterStories = masterStoriesCollection.find();
			Iterator<Document> itThruStory = iterStories.iterator();

			while (itThruStory.hasNext()) {

				masterStoriesList.add((Document) itThruStory.next());
			}

			// MASTER Scenes List created
			MongoCollection<Document> masterScenesCollection = adventureDB.getCollection(scenesMaster);
			List<Document> masterScenesList = new ArrayList<>();
			FindIterable<Document> iterScenes = masterScenesCollection.find();
			Iterator<Document> itThruScene = iterScenes.iterator();

			while (itThruScene.hasNext()) {

				masterScenesList.add((Document) itThruScene.next());
			}
			
			// creates MongoCollection from empty USER collections
			MongoCollection<Document> userStoriesCollection = mongoClient.getDatabase("AdventureStoryDB").getCollection(storiesUser);
			MongoCollection<Document> userScenesCollection = mongoClient.getDatabase("AdventureStoryDB").getCollection(scenesUser);

			// copies masterScenesList and masterStories list to userStoriesCollection and userScenesCollection
			userStoriesCollection.insertMany(masterStoriesList);
			userScenesCollection.insertMany(masterScenesList);

		}
	}
}
