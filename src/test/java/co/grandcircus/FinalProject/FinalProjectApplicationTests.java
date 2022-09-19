package co.grandcircus.FinalProject;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class FinalProjectApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
	@Test
	void wordServiceOOB() {
		
		String extractedKeywords = "";
		try {
			
				Map<String, Double> topicsMap = new TreeMap<String, Double>();
			
			// map JSON response ('keyword' of key-value pairs) to a Map<String, Integer>
			// 'keyword' is a map of words and their frequency (Integer) in the text
				Map<String, Integer> keywordsMap = new TreeMap<String, Integer>();
			
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
				
				extractedKeywords = "bunny";
			}
			
			assertEquals("bunny", extractedKeywords);
	}

}
