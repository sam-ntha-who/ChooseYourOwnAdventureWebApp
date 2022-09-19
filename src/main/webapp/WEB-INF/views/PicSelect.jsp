<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Picture Selection</title>
<link rel="stylesheet" type="text/css" href="style.css" />
</head>
<body>
	<div class="picList">
	<c:forEach var="pic" items="${picList}" varStatus="i">
	<div class="column">
	<a href="addPicture?id=${scene.id}&pic=${pic.src.landscape}&" class="imgLink"><img src="${pic.src.tiny}"></a></div></c:forEach></div>
</body>
</html>