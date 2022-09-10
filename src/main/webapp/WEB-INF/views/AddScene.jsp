<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Choose your own adventure</title>
	<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
<div class="banner">
	Create your own story
</div>
<div class="story-body">
	<form action="">
		<textarea id="storyName" name="storyName" rows="50" cols="100">Story name
		</textarea><br>
		<textarea id="sceneName" name="sceneName" rows="50" cols="100">Scene name (Option)
		</textarea><br>
		<textArea id="sceneDescription" name="sceneDescription" rows="50" cols="100">Scene description
		</textArea><br>
		<input type="submit" value="Save"/>
	</form>
	</div>
	
	<footer>
	<div class="home">
			<a href="/">Back to home</a>
		</div>
	</footer>
</body>
</html>