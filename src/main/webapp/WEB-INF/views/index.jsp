<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Choose Your Own Adventure home</title>
	<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<div class="CYOA-header">
		<a href="/addScene?msg=story">Create your own story</a>
	</div>
	<br><br>
	<div class="banner">
		Choose Your Own Adventure
	</div>

      <table class="table" style="">
      	<thead>
      		<tr class="columns">
      			<th></th>
      			<th><h3>Stories</h3></th>
      			<th><h3>Play / Edit</h3></th>
      			<th><h3>Delete</h3></th>
      		</tr>
      	</thead>
      	<tbody>
			
      	<c:forEach items="${storyList}" var="story" varStatus="status">
      		<tr>
      			<th scope="row"><img src="${photoList[status.index]}" width="130"></th>
      			<td>${story.title}</td>
      			<td><a href="play?id=${story.startingSceneId}"><button type="button" class="button-primary">Play / Edit</button></a></td>
      			<td><a href="deleteStory?id=${story.id}" onclick="return confirm('Are you Sure?')"><button type="button" class="button-primary">Delete</button></a><div style="text-align: center;"><span style="font-size: 1rem;"></span></div></td>
      		</tr>
		</c:forEach>
			
		      		
      	</tbody>
      </table>


</body>
</html>