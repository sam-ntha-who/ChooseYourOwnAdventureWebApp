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
<body style="background-image: url('${ scene.photoUrl }'); background-repeat: no-repeat; background-position: center top;">


	<div class="banner">${scene.storyTitle}</div>
	<br>
	<div class="story-body"><br>${scene.description}<br><br></div>

<div class="story-body">
		<c:if test="${scene.childList == null}">
	<h2><br>The end<br></h2><br>
	</c:if>
	</div><br>
	
	<!-- this is causing a lot of trouble up here, moved it down to the options display -->
<!-- 	<div class="options"> -->
<%-- 	<c:forEach items="${scene.childList}" var="child">
	${child.pathLength}
	<c:if test="${child.longest != child.shortest}">
	<c:if test ="${child.longest == true}" >Longest </c:if>
	<c:if test ="${child.shortest == true}" >Shortest </c:if></c:if>
	</c:forEach> --%>
<!-- 	<div class="option"><br> -->
<%-- 	<a href="play?id=${child.id}">
	${child.option}</a>
	</div><br> --%>

	<div class="options">

			<c:forEach items="${scene.childList}" var="child">
					<%-- ${child.pathLength} --%>
	<div class="path-length">
	<c:if test="${child.longest != child.shortest}">
	<c:if test ="${child.longest == true}" >Possible Longest Path</c:if>
	<c:if test ="${child.shortest == true}" >Possible Shortest Path</c:if></c:if></div>
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