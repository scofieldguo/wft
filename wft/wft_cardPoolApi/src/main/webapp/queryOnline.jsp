<%@page import="com.bw30.open.cardpool.test.ApiTest" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>测试-在线查询接口</title>
<style type="text/css">
	tr {height:40px;}
</style>
</head>
<body>
<center>
<%
final String uri = "/queryOnline.do";
String submit = request.getParameter("submit");
if (submit == null) {
%>
<h1>测试接口：在线查询</h1>
<form action="queryOnline.jsp" method="post">
<input type="hidden" name="submit" value="" />
<table border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td><font color="red">*</font>合作方：</td>
		<td><input type="text" name="partner" value="10001"/></td>
	</tr>
	
	<tr>
		<td><font color="red">*</font>卡类型：</td>
		<td>
			<select name="op">
				<option value="1">移动</option>
				<option value="2" selected="selected">电信</option>
				<option value="3">联通</option>
			</select>
		</td>
	</tr>
	
	<tr>
		<td><font color="red">*</font>卡号：</td>
		<td><input type="text" name="cno" /></td>
	</tr>
	
	<tr>
		<td><font color="red">*</font>密码：</td>
		<td><input type="text" name="pwd" /></td>
	</tr>
	
	<tr>
		<td><font color="red">*</font>wifi接入IP：</td>
		<td><input type="text" name="ip" /></td>
	</tr>
	
	<tr>
		<td>备注：</td>
		<td><input type="text" name="standby" /></td>
	</tr>
	
	<tr>
		<td colspan="2"><input type="submit" value=" OK " /></td>
	</tr>
</table>
</form>
<%
	} else {
	String url = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+uri;
	
	String result = new com.bw30.open.cardpool.test.ApiTest().testQueryOnline(url, 
		request.getParameter("partner"), 
		request.getParameter("op"), 
		request.getParameter("cno"), 
		request.getParameter("pwd"), 
		request.getParameter("ip"),
		request.getParameter("standby"));
%>
<%=result %>	
<%
}
%>
<hr size="1">
<a href="test-api.jsp">[Back to api list]</a>
</center>
</body>
</html>