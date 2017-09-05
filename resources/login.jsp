<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="photoshare.AlbumDao" %>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.List" %>
<%@ page import="photoshare.Albums" %>
<html>
<head><title>Photoshare Login</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Shan'stagram</h1>
<h2>Please login</h2>

<form method="POST" action="j_security_check">
    <table ALIGN=CENTER>
        <tr><th>Email</th><td><input type="text" name="j_username"></td></tr>
        <tr><th>Password</th><td><input type="password" name="j_password"></td>
        </tr>
        <tr><td colspan="2" align="right"><input type="submit" value="Login"/>
        </td></tr>
    </table>
</form>


</body>
</html>