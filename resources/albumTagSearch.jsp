<%--
  Author: linshan Jiang (linshan@bu.edu)
--%>
<%@ page import="photoshare.Picture" %>
<%@ page import="photoshare.PictureDao" %>
<%@ page import="photoshare.SearchDao" %>
<%@ page import="photoshare.AlbumTagSearchDao" %>
<%@ page import="photoshare.User" %>
<%@ page import="photoshare.AlbumDao" %>
<%@ page import="photoshare.Albums" %>
<%@ page import="org.apache.commons.fileupload.FileUploadException" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head><title>albumTagSeach</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Shan'stagram</h1>
<%  SearchDao searchDao = new SearchDao();
	AlbumDao albumDao = new AlbumDao();
	User myself = new User();
	if (request.getUserPrincipal()!= null){
		myself = searchDao.myself(request.getUserPrincipal().getName());

	}
	AlbumTagSearchDao albumTagSearchDao = new AlbumTagSearchDao();
	
%>

<%if (request.getUserPrincipal()!= null){

%>
<h2>View your photos by tag name</h2>
<%
List<String> ownTag = albumTagSearchDao.ownTag(myself.getUser_id());%>
<table ALIGN=CENTER>
<%
for(String t: ownTag){%>
<tr>
<td>
<a href="tag.jsp?tag=<%= t %>&own=yes"><%= t %></a>
<td>
<tr>
<%}%>
</table>
		
<%}%>


<h2>View all photos by tag name</h2>
<%List<String> allTag = albumTagSearchDao.allTag();%>
<table ALIGN=CENTER>
<%
for(String t: allTag){%>
<tr>
<td>
<a href="tag.jsp?tag=<%= t %>&own=no"><%= t %></a>
<td>
<tr>
<%}%>
</table>


<h2>Conjunctive tag queries</h2>
please search by tag names seperate by space.
<form action="tag.jsp?own=no" method="post">
<input type="text" name="conjuncTag">
<input type="submit" value="conjunctive Tag Search"/><br/>
</form>
</body>
</html>
