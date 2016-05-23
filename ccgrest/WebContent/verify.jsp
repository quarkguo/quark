<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<h1>Verify</h1>
<pre>
<%= request.getAttribute("category") %>
</pre>
<form action="upload" method="post" enctype="multipart/form-data">
<input type="hidden" name="requestData" value="<%= request.getAttribute("requestData") %>"/>
<input type="hidden" name="action" value="confirmed" />
<input type="submit" value="Confirm" />
</form>
</body>

</html>