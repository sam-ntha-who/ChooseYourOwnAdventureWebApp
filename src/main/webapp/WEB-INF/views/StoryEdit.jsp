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

			<br>
			<textarea id="description" name="description" rows="100" cols="400">${scene.description}</textarea>
			<br> <input type="submit" value="Save" />
		</form>
		<br> <br>
	</div>

	<div class="options">
		<c:forEach items="${scene.childList}" var="child" varStatus="status">

			<div class="option">
				${child.option} <br> <a
					href="/deleteScene?id=${child.id}&optionId=${status.index}"
					onclick="return confirm('Are you Sure?')"><br> <br>
					<button type="button" class="button-primary">Delete</button></a>
			</div>
			<br>
		</c:forEach>
	</div>
	<div class="addOption">
		<br> <a href="/addScene?id=${scene.id}&msg=scene"><button
				type="button" class="button-primary">Add Option</button></a>
	</div>

	<footer>
		<br> <br> <img src="${scene.photoUrl}" width="175px"><br>
		<a href="/changePicture?id=${scene.id}">Change Picture</a><br> <br>
		<a href="/play?id=${scene.id}">&#9746; Stop Editing</a>&emsp;&emsp;<a
			href="/">&#8962; Home</a>
	</footer>
</body>
</html>