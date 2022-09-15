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
	<div class="banner">Create your own ${msg}</div>
	<div class="story-body">
	<!-- stuff is getting weirdly centered here and not showing the placeholder -->
		<form action="/createScene">
			<c:if test="${id == null}">
<<<<<<< Updated upstream
				<textarea id="storyName" name="storyName" rows="50" cols="100" placeholder="${title}"></textarea>
=======
<<<<<<< HEAD
				<textarea id="storyName" name="storyName" rows="50" cols="100">${title}</textarea>
=======
				<textarea id="storyName" name="storyName" rows="50" cols="100" placeholder="${title}"></textarea>
>>>>>>> main
>>>>>>> Stashed changes
			</c:if>
			<c:if test="${id != null}">
				<input type="hidden" id="storyName" name="storyName" value="${title}" >
				<input type="hidden" id="parentId" name="parentId" value="${id}" >
				<h3>${title}</h3>
<<<<<<< Updated upstream
				<textArea id="sceneChoice" name="sceneChoice" placeholder="Choice" rows="50"
=======
<<<<<<< HEAD
				<textArea id="sceneChoice" name="sceneChoice" placeholder="choice" rows="50"
=======
				<textArea id="sceneChoice" name="sceneChoice" placeholder="Choice" rows="50"
>>>>>>> main
>>>>>>> Stashed changes
				cols="100"></textArea>
			</c:if>
			<br>
			<textArea id="sceneDescription" name="sceneDescription" placeholder="Scene Description, (where does this choice lead)" rows="50"
				cols="100"></textArea>
			<br>
			<%-- 			<textarea id="sceneName" name="sceneName" rows="50" cols="100">Option
		</textarea>
			<br>
--%>
			<input type="submit" value="Save" />
		</form>
	</div>

	<footer>
		<div class="home">
			<a href="/">&#8962; Home</a>
		</div>
	</footer>
</body>
</html>