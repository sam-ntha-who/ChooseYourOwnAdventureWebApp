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
		<form action="/updateScene" method="post">
			<input type="hidden" id="sceneId" name="sceneId" value="${scene.id}">
			<%-- *** This code commented out because "Scene Description" shouldn't be editable 
		with the current class structure. Currently, "scene description" or "choice" 
		comes from an array of Option in the previous scene. ***
			<textarea id="sceneName" name="sceneName" rows="50" cols="100">Scene description
			</textarea>
		--%>
			<br>
			<textarea id="description" name="description" rows="100" cols="400">${scene.description}</textarea>
			<br> <input type="submit" value="Save" />
		</form>
		<br> <br>
	</div>


	<%-- editing of options removed since this would require editing 
		both this Scene's list of Options and the subsequent Scenes edited 
		due to current Scene structure. flex goal = allow delete
			<form action="">
				<textarea id="option" name="option" rows="50" cols="100">Option 1
		</textarea>

				<br> <input type="submit" value="Save" />
				</form>
	--%>
	<div class="options">
		<c:forEach items="${scene.childList}" var="child" varStatus="status">
			
			<div class="option">
				${child.option} <br> <a
					href="/deleteScene?id=${child.id}&optionId=${status.index}"  onclick="return confirm('Are you Sure?')"><br><br><button
						type="button" class="button-primary">Delete</button></a>
			</div><br>
		</c:forEach>
	</div>
	<div class="addOption">
	<br>
		<a href="/addScene?id=${scene.id}&msg=scene"><button type="button"
				class="button-primary">Add Option</button></a>
	</div>

	<footer><br><br>
		<a href="/play?id=${scene.id}">&#9746; Stop Editing</a>&emsp;&emsp;<a href="/">&#8962; Home</a>
		</div>
	</footer>
</body>
</html>