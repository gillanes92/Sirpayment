<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
</head>
<body onload="document.f.submit();">
	<form id="f" name="f" method="POST"
		action="<%=request.getAttribute("formAction")%>">
		<input type="hidden" name="TBK_TOKEN"
			value="<%=request.getAttribute("tokenWs")%>" /> <input
			style="display: none;" type="submit" value="Ir a pagar" />
	</form>
</body>
</html>