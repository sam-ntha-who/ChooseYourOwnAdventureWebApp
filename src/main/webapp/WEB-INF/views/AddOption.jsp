<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Choose your own adventure - edit</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<div class="banner">${storyTitle}</div>
	<br>
	<br>
	<div class="story-body">
		<form action="/addOption" method="post">
		<input type="hidden" id="id" name="id" value="${scene.id}">
			<%-- *** This code commented out because "Scene Description" shouldn't be editable 
		with the current class structure. Currently, "scene description" or "choice" 
		comes from an array of Option in the previous scene. ***
			<textarea id="sceneName" name="sceneName" rows="50" cols="100">Scene description
			</textarea>
		--%>
			<br>
			<textarea id="option" name="option" placeholder="Your option here" rows="50"
				cols="100">
			Option Here
	</textarea>
			<br>
					<textarea id="description" name="description" placeholder="Your description here" rows="50"
				cols="100">
			Scene Description Here
	</textarea><br>
			 <input type="submit" value="Save" />
		</form>
		<br> <br>
	</div>


	<footer>

		<br> <br>
		<div class="home">
			<a href="/">Back to home</a>
		</div>
	</footer>
</body>
</html>