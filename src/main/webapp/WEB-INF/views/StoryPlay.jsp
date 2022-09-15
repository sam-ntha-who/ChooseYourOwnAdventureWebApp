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
<body style="background-image: url('${ scene.photoUrl }');">


	<div class="banner">${scene.storyTitle}</div>
	<br>
	<br>
	<div class="story-body">${scene.description}</div>

	<div style="margin: auto">
		<c:if test="${scene.childList == null}">
	The end
	</c:if>
		<br>
		<br>
	</div>

	<div class="options">

			<c:forEach items="${scene.childList}" var="child">
					${child.pathLength}
					<div class="option">
						<br> <a href="play?id=${child.id}"> ${child.option}</a>
					</div>
					<br>
			</c:forEach>
	</div>

	<div class="option">
		<br> <a href="/addScene?id=${scene.id}&msg=scene"><button
				type="button" class="button-primary">Add Option</button></a>
	</div>
	<footer>
		<br>
		<br>
		<div class="footer"><c:if test="${scene.parentId != null}">
			<a href="/play?id=${scene.parentId}">&#8612; Back</a>&emsp;&emsp;</c:if><a
				href="/edit?sceneId=${scene.id}">&#128393; Edit</a>&emsp;&emsp;<a
				href="/">&#8962; Home</a>
		</div>
	</footer>
</body>
</html>