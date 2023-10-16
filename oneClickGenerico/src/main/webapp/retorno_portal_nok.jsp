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
		action="<%=request.getAttribute("url_retorno")%>">
		<input type="hidden" name="status" value="<%=request.getAttribute("status")%>" />
		<input type="hidden" name="id_company" value="<%=request.getAttribute("id_company")%>" />
		<input type="hidden" name="description" value="<%=request.getAttribute("description")%>" />
		<input type="hidden" name="id_client" value="<%=request.getAttribute("id_client")%>" />
		<input style="display: none;" type="submit" />
	</form>
</body>
</html>