<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Choose your own adventure</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<div class="banner">${scene.storyTitle}</div>
	<br>
	<br>
	<div class="story-body">${scene.description}<br>
		<br>
	</div>

	<c:if test="${scene.options == null}">
		<center>
			<br>
			<br>The end<br>
			<br>
		</center>
	</c:if>

	<div class="options">
		<c:forEach items="${scene.options}" var="option">
			<div class="option">
				<br> <a href="play?id=${option.getSceneId()}">
					${option.description}</a>
			</div>
			<br><br>
		</c:forEach>
	</div>

	<div class="addOption">
	<br>
		<a href="/addScene?id=${scene.id}&msg=scene"><button type="button"
				class="button-primary">Add Option</button></a>
	</div>
	<div class="option">
		<a href="/addOption?id=${scene.id}"><button type="button" class="button-primary">Add Option</button></a>
		</div>
	<footer>
		<div class="edit">
			<a href="/edit?sceneId=${scene.id}">Edit</a>
		</div>
		<br> <br>
		<div class="home">
			<a href="/">Back to home</a>
		</div>
	</footer>
</body>
</html>