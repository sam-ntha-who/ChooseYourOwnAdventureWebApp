<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Choose your own adventure - edit</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<div class="banner">Story name</div>
	<br><br>
	<div class="story-body">
	<form action="">
		<textarea id="sceneName" name="sceneName" rows="50" cols="100">Scene description
		</textarea><br>
		<textarea id="sceneDescription" name="sceneDescription" rows="50" cols="100">
			Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
		</textarea><br>
		<input type="submit" value="Save"/>
	</form>
	<br><br>
	</div>
	
	<div class="options">
	<div class="option">
	<form action="">
		<textarea id="option" name="option" rows="50" cols="100">Option 1
		</textarea><br>
		<input type="submit" value="Save"/>
	</form>
	</div>
	
	<div class="option">
	<form action="">
		<textarea id="option" name="option" rows="50" cols="100">Option 2
		</textarea><br>
		<input type="submit" value="Save"/>
	</form>
	</div>
	
	<div class="option">
	<form action="">
		<textarea id="option" name="option" rows="50" cols="100">Option 3
		</textarea><br>
		<input type="submit" value="Save"/>
	</form>
	</div>
	</div>
	
	<footer>
		<div class="edit">
			<a href="/play">Stop Editing</a>
		</div><br><br>
		<div class="home">
			<a href="/">Back to home</a>
		</div>
	</footer>
</body>
</html>