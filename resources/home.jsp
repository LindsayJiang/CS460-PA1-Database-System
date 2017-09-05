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
<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="imageUploadBean"
             class="photoshare.ImageUploadBean">
    <jsp:setProperty name="imageUploadBean" property="*"/>
</jsp:useBean>

<html>
<head><title>home</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Shan'stagram</h1>

Hello <b><code><%= request.getUserPrincipal().getName()  %></code></b>, click here to
<a href="/photoshare/logout.jsp">log out</a>
<a href="/photoshare/index.jsp">Shan'stagram</a>
<%
    AlbumTagSearchDao albumTagSearchDao = new AlbumTagSearchDao(); 
    AlbumDao albumDao = new AlbumDao();
    SearchDao searchDao = new SearchDao();
    User myself = searchDao.myself(request.getUserPrincipal().getName());

    if (request.getParameter("addComment")!=null){
        int picture_id = Integer.parseInt(request.getParameter("addComment"));
        int user_id = 0;
        String text = request.getParameter("comment");
        if (request.getUserPrincipal()!= null){
          user_id = myself.getUser_id();
          albumDao.updateScore(myself.getScore()+1, myself.getUser_id());
        }
        albumDao.addComment(user_id, picture_id, text);


  }else if (request.getParameter("like")!=null){
    int picture_id = Integer.parseInt(request.getParameter("like"));
    albumDao.addLike(myself.getUser_id(), picture_id);
  }else if (request.getParameter("addTag")!=null && !request.getParameter("addTag").equals("")){
    int picture_id = Integer.parseInt(request.getParameter("picture_id"));
    String addTag = request.getParameter("addTag");
    albumDao.addTag(picture_id, addTag);
  }
  


  if (request.getParameter("albumName")!= null && (!request.getParameter("albumName").equals(""))){
    
    User myself2 = searchDao.myself(request.getUserPrincipal().getName());
    albumDao.addAlbum(myself2.getUser_id(), request.getParameter("albumName"));

}%>
your score: <%=myself.getScore()%>
<h2>Your album list</h2>
<a href="/photoshare/albumTagSearch.jsp">view your albums in tag or search for other's albums</a>
<form action="home.jsp" method="post">
<input type="text" name="albumName">
<input type="submit" value="create new album"/><br/>
</form>

    
<%    List<Albums> albumsList= null;
    

    
    if (request.getParameter("aid")!=null){
        List<Integer> list = albumDao.nonregisterView(Integer.parseInt(request.getParameter("aid")));
        int numPic = list.size();
        albumDao.updateScore(myself.getScore()-numPic, myself.getUser_id());
        albumDao.deleteAlbum(Integer.parseInt(request.getParameter("aid")));

    }
    albumsList = albumDao.userAlbums(myself.getUser_id());
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
      <td>
        <form action="home.jsp" method="post">
        <input type="hidden" name="aid" value=<%= album.getAid() %> />
        <input type="submit" value="Delete album"/><br/>
        </form>
      </td>
      </tr>
      <% } %>
      </table>
  <% } %>




<h2>Your friend list</h2><br/>
<a href="/photoshare/search.jsp">search for new friends</a>
<%  //SearchDao searchDao1 = new SearchDao();
    List<User> friendList = null;
    User myself1 = searchDao.myself(request.getUserPrincipal().getName());
    friendList = searchDao.friendList(myself1.getUser_id());
    if (friendList!=null){%>
      <table ALIGN=CENTER>
      <tr>
      <td>Email</td>
      <td>First Name</td>
      <td>Last Name</td>
      <td>Gender</td>
      <td>Score</td>
      </tr>
      <% for (User user : friendList) { %>
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

<h2>You may also like</h2>

<%List<String> topFiveTagUser = searchDao.topFiveTagUser(myself.getUser_id());
  List<List<Integer>> prepareList = new ArrayList<List<Integer>>();
  for (String tt : topFiveTagUser){
      List<Integer> oneTagList = albumTagSearchDao.allPhotoByTag(tt);
      prepareList.add(oneTagList);
 } %>
<table ALIGN=CENTER>
  
<%
  List<Integer> resultList = albumTagSearchDao.disjuncTagSearch(prepareList);
  for(Integer pictureId: resultList){
    Picture picture = albumDao.getPicture(pictureId);%>
  <tr>
      <td> <%=picture.getCaption()%> <td>
  <tr>

  <tr>
  <td>
    <a href="/photoshare/img?picture_id=<%= pictureId %>">
    <img src="/photoshare/img?t=1&picture_id=<%= pictureId %>"/></a>
  </td>
  </tr>

  <tr>
  <td>
    <%List<String> like = albumDao.getLike(pictureId);%>
    <%=like.size()%>&nbsp likes--
    <%for(String email: like){%>
      ~<%= email%>&nbsp
    <%}%>
    <%if (request.getUserPrincipal()!=null){%>
    <form action="home.jsp" method="post">
    <input type="hidden" name="like" value=<%= pictureId %>>
    <input type="submit" value="like this photo"/>
    </form>
    <%}%>
  <td>
  <tr>

  <tr>
  <td>
  <%List<String> tags = albumDao.getTag(pictureId);%>
  <%=tags.size()%>&nbsp tags--
  <%for(String t: tags){%>
  #<a href="/photoshare/tag.jsp?tag=<%=t%>&own=no"><%=t%></a>&nbsp
  <%}%>
  <td>
  <tr>
  
  <tr>
      
      <%List<String> comment = albumDao.getComment(pictureId);%>
      <td><%=comment.size()%>&nbsp comments-- <td>
      <tr>

            <%for(String c: comment){%>
            <tr>
            <td>
              comment:<%= c%>
            <td>
            <tr>
            <%}%>

            <tr>
      <td>
      <%boolean isOwner = false;
        if (request.getUserPrincipal()!=null){

          isOwner = albumTagSearchDao.isOwner(myself.getUser_id(),pictureId);
        }
        if (!isOwner){%>
      <form action="home.jsp?" method="post">
        <input type="hidden" name="addComment" value=<%= pictureId %> />
        <input type="text" name="comment"/>
        <input type="submit" value="add comment"/><br/>
      </form>
      <%}%>
      
      <td>
      <tr>
<%}%>

</table>

<h2>Tag recommendation</h2>
give us some tags you already have in mind!<br>
please seperate by spaces.
<form action="home.jsp" method="post">
  <input type="text" name="tagsLike"/>
  <input type="submit" value="try these tags!"/><br/>
</form>
<% if (request.getParameter("tagsLike")!=null){
    String tagsLike = request.getParameter("tagsLike");
    String[] tagsLike2 = tagsLike.split(" ");
    List<List<Integer>> prepareList1 = new ArrayList<List<Integer>>();
      for (int i = 0; i<tagsLike2.length; i++){
        String tt1 = tagsLike2[i];
        List<Integer> oneTagList1 = albumTagSearchDao.allPhotoByTag(tt1);
        prepareList1.add(oneTagList1);

      }
      List<Integer> resultList1 = albumTagSearchDao.conjuncTagSearch(prepareList1);
      List<String> finalResult = albumTagSearchDao.recommendation(resultList1, tagsLike2);%>
      
We recommend these tags!<br>

      <%for(String tagss:finalResult){%>
      #<a href="/photoshare/tag.jsp?tag=<%=tagss%>&own=no"><%=tagss%></a>&nbsp&nbsp
      <%}%>

<%}%>

    



</body>
</html>
