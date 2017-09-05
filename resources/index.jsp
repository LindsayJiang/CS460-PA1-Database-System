<%--
  Author: linshan Jiang (linshan@bu.edu)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="photoshare.AlbumDao" %>
<%@ page import="photoshare.SearchDao" %>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.List" %>
<%@ page import="photoshare.Albums" %>
<%@ page import="photoshare.User" %>
<html>
<head><title>index</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Shan'stagram</h1>
<h2>Please login</h2>
<a href="/photoshare/home.jsp">login</a></br>
<h2>new user?</h2></br>
<a href="/photoshare/newuser.jsp">Register now!</a>
<h2>guest visitor?</h2></br>
<a href="/photoshare/albumTagSearch.jsp">view albums/photos as guest</a>


<h2>You can view albums on our website:</h2>
<%
    AlbumDao albumDao = new AlbumDao();
    List<Albums> albumsList= null;
    albumsList = albumDao.webAlbums();
    if (albumsList != null){%>
    <table ALIGN=CENTER>
      <tr>
      <td>Album name</td>
      <td>Date of creation</td>
      </tr>
      <% for (Albums album : albumsList) { %>
      <tr>
      <td><a href="album.jsp?aid=<%= album.getAid() %>"><%= album.getName() %></a></td>
      <td><%= album.getDate_of_creation() %></td>
      </tr>
      <% } %>
      </table>
  <% } %>
<h2> All Users </h2>
<%
    SearchDao searchDaoU = new SearchDao();
    List<User> allUsers = null;
    allUsers = searchDaoU.listAllUsers();
    if (allUsers!= null){%>
      <table ALIGN=CENTER>
      <tr>
      <td>Email</td>
      <td>First Name</td>
      <td>Last Name</td>
      <td>Gender</td>
      <td>Score</td>
      </tr>
      <% for (User user : allUsers) { %>
      <tr>
      <td><%= user.getEmail() %></td>
      <td><%= user.getFirst_name() %></td>
      <td><%= user.getLast_name() %></td>
      <td><%= user.getGender() %></td>
      <td><%= user.getScore() %></td>
      </tr>
      <% } %>
      </table>
  <%}%>
<h2>Top Ten Users</h2>
<%
    SearchDao searchDao = new SearchDao();
    List<User> topTen = null;
    topTen = searchDao.topTen();
    if (topTen != null){%>
    <table ALIGN=CENTER>
      <tr>
      <td>Email</td>
      <td>First Name</td>
      <td>Last Name</td>
      <td>Gender</td>
      <td>Score</td>
      </tr>
      <% for (User user : topTen) { %>
      <tr>
      <td><%= user.getEmail() %></td>
      <td><%= user.getFirst_name() %></td>
      <td><%= user.getLast_name() %></td>
      <td><%= user.getGender() %></td>
      <td><%= user.getScore() %></td>
      </tr>
      <% } %>
      </table>
  <% } %>

<h2>Top Ten Tags</h2>

<%
    List<String> topTenTag = null;
    topTenTag = searchDao.topTenTag();
    if (topTenTag!=null){%>
      <table ALIGN=CENTER>
      <tr>
      <td>tagName<Td>
      <tr>

      <% for (String t : topTenTag) { %>
      <tr>
      <td><a href="/photoshare/tag.jsp?tag=<%=t%>&own=no"><%=t%></a></td>

      <tr>
      
      <%}%>
      </table>
  <%}%>












</body>
</html>