<%--
  Author: Linshan <linshan@bu.edu>
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="photoshare.SearchDao" %>
<%@ page import="photoshare.User" %>
<%@ page import="java.util.List" %>

<jsp:useBean id="searchBean"
             class="photoshare.SearchBean" />
<jsp:setProperty name="searchBean" property="*"/>
<jsp:useBean id="friendBean"
             class="photoshare.FriendBean" />
<jsp:setProperty name="friendBean" property="*"/>

<html>
<head><title>search</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Search for friends</h1>
<!-- We want to show the form unless we successfully create a new user -->
<% boolean showForm = true;
   String err = null; %>

<% 
  // We have valid inputs, try to create the user
  List<User> userList = null;
  SearchDao searchDao = new SearchDao();
  User myself = searchDao.myself(request.getUserPrincipal().getName());
  if (!searchBean.getKeyword().equals(""))
  {
    userList = searchDao.search(searchBean.getKeyword());
    if (userList.size() == 0) {
      err = "Couldn't find user";
    }else{%>
      <h2>Here are the results:</h2>
      <table ALIGN=CENTER>
      <tr>
      <td>Email</td>
      <td>First Name</td>
      <td>Last Name</td>
      <td>Gender</td>
      </tr>
      <% for (User user : userList) { %>
      <tr>
      <td><%= user.getEmail() %></td>
      <td><%= user.getFirst_name() %></td>
      <td><%= user.getLast_name() %></td>
      <td><%= user.getGender() %></td>
      <td>
      <form action="search.jsp" method="post">
      <input type="hidden" name="user_id" value=<%= myself.getUser_id() %> /><br>
      <input type="hidden" name="fid" value=<%= user.getUser_id() %> /><br>
      <input type="submit" value="Add"/><br/>
      </form>
      </td>
      </tr>
      <% } %>
      </table>
      <% } %>
      <% } %>


  <%if (friendBean.getUser_id()!=0) {
      if (friendBean.getUser_id()== friendBean.getFid()){%>
        <p>Sorry, you cannot add yourself as friend!</p>
      <%}else{
    searchDao.addFriend(friendBean.getUser_id(), friendBean.getFid()); %>
    <p>You have succefully added this user.</p>

  <% } } %>
  

<form action="search.jsp" method="post">
  enter keyword: <input type="text" name="keyword"/><br>

  <input type="submit" value="Search"/><br/>
</form>
you can go back <a href="/photoshare/home.jsp">home</a> and view updated friend list.

<% if (err != null) { %>
<font color=red><b>Error: <%= err %></b></font>

<% } %>
</body>
</html>












