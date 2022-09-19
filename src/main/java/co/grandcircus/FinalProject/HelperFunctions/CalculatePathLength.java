package co.grandcircus.FinalProject.HelperFunctions;

import java.util.List;

import co.grandcircus.FinalProject.Models.Scene;

public class CalculatePathLength {

	// can this be moved out of the api service
	public Scene setPathLength(Scene scene) {

		if (scene.getChildList() == null) {
			return scene;
		}
	
		for(Scene s : scene.getChildList()) {
				
			int pathLength = getScenePathLength(s);
			s.setPathLength(pathLength);

			
			setPathBool(scene.getChildList());

		}
		
		return scene;
	}
	// could the path functionality move out of the controller while keeping the tree function?
	private int getScenePathLength(Scene scene) {

		int pathLength = 0;
		// this is referencing the getScene within the DB Service
		List<Scene> childList = scene.getChildList();
		scene.setChildList(childList);
		
		if (scene.getChildList() == null) {

			return pathLength;
		}

		for (Scene s : scene.getChildList()) {

			pathLength = Math.max(pathLength, getScenePathLength(s));
		}

		return pathLength + 1;
	}
	//path boolean method
	
	private static void setPathBool(List<Scene> childList) {
		for(Scene s : childList) {
			s.setLongest(false);
			s.setShortest(false);
		}
		Scene shortest = childList.get(0);
		Scene longest = childList.get(0);
		for(Scene s : childList) {
			if(shortest.getPathLength() > s.getPathLength()) {
				shortest = s;
			}
			if(longest.getPathLength() < s.getPathLength()) {
				longest = s;
			}
		}
		for(Scene s : childList) {
			if(shortest.getPathLength() == s.getPathLength()) {
				s.setShortest(true);
			}
			if(longest.getPathLength() == s.getPathLength()) {
				s.setLongest(true);
			}
		}
		
	}
	
//	// displaying faster but not setting bool length
//	
//	public int setScenePathLength(Scene scene) {
//		int pathLength = scene.getPathLength();
//		// does it have child scenes 
//		
//		
//		if (scene.getChildList() == null) {
//			return pathLength;
//			
//		}
//		List<Scene> childList = scene.getChildList();
//		
//		
//		for (Scene s: childList) {
//			pathLength = s.getPathLength();
//			pathLength = Math.max(pathLength, setScenePathLength(s));
//		}
//		// call setLongestShortest
//
//		return pathLength++;
//		
//	}
//		
//	// boolean for longest & shortest
//	
//	public void setLongestShortest(Scene scene) {
//		// take in scene
//		// get path length
//		int pathLengthLongest = 0;
//		int pathLengthShortest = 0;
//		for (Scene s : scene.getChildList()) {
//			int temp = s.getPathLength();
//			pathLengthShortest = Math.min(temp, pathLengthShortest);
//		
//	
//		}
//		
//		
//		// compare path length to path lengths of siblings
//		// set largest number to longest/true
//		// set smallest number to shortest/true
//		
//		
//		
//		
//	}
}