<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Enter the credentials</h1>
<form method="post" action="DisplayServlet">
<p>${errorMessage}</p>
<table>
<tr>
<td>User ID
<td><input type="text" name="userid"/>
</tr>
<tr>
<td>User Name
<td><input type="text" name="username"/>
</tr>
<tr>
<td><input type="submit" value="send" />
</tr>
</table>
</form>
</body>
</html>