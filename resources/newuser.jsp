<%--
  Author: Giorgos Zervas <cs460tf@cs.bu.edu>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="photoshare.NewUserDao" %>
<%@ page import="java.sql.Date"%>
<%@ page import="java.sql.Date" %>
<jsp:useBean id="newUserBean"
             class="photoshare.NewUserBean" />
<jsp:setProperty name="newUserBean" property="*"/>

<html>
<head><title>New User</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
 <h1>Shan'stagram</h1>
<!-- We want to show the form unless we successfully create a new user -->
<% boolean showForm = true;
   String err = null; %>

<% if (!newUserBean.getEmail().equals("")) {
     if (!newUserBean.getPassword1().equals(newUserBean.getPassword2())) {
       err = "Both password strings must match";


     }
     else if (newUserBean.getPassword1().length() < 4) {
       err = "Your password must be at least four characters long";
     }
     else {
       // We have valid inputs, try to create the user
       boolean success = false;
       NewUserDao newUserDao = new NewUserDao();
       if (newUserBean.getDate() == null){
         success = newUserDao.create(newUserBean.getEmail(),
             newUserBean.getPassword1(), newUserBean.getFirst_name(),
             newUserBean.getLast_name(), newUserBean.getGender(), newUserBean.getHometown(), null);
       } else{
         success = newUserDao.create(newUserBean.getEmail(),
             newUserBean.getPassword1(), newUserBean.getFirst_name(),
             newUserBean.getLast_name(), newUserBean.getGender(), newUserBean.getHometown(), Date.valueOf(newUserBean.getDate()));
     }
       if (success) {
         showForm = false;
       } else {
         err = "Couldn't create user (that email may already be in use)";
       }
     }
   }
%>

<% if (err != null) { %>
<font color=red><b>Error: <%= err %></b></font>
<% } %>

<% if (showForm) { %>

<h2>New user info</h2>

<form action="newuser.jsp" method="post">
  *Email: <input type="text" name="email"/><br>
  *Password: <input type="password" name="password1"/><br>
  *Re-enter password: <input type="password" name="password2"/><br>
  First name: <input type="text" name="first_name"/><br>
  Last name: <input type="text" name="last_name"/><br>
  Gender: <input type="radio" name="gender" value="male" checked />Male   
          <input type="radio" name="gender" value="female" />Female<br>
  Hometown: <input type="text" name="hometown" /><br>
  *Date of birth:<input type="date" name="date"/>
    <br></br>

  <input type="submit" value="Create"/><br/>
</form>

<% }
   else { %>

<h2>Success!</h2>

<p>A new user has been created with email <%= newUserBean.getEmail() %>.
You can now return to the <a href="home.jsp">login page</a>.

<% } %>

</body>
</html>
