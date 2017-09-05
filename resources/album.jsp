<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="photoshare.AlbumDao" %>
<%@ page import="java.sql.Date"%>
<%@ page import="java.util.List" %>
<%@ page import="photoshare.Albums" %>
<%@ page import="photoshare.Picture" %>
<%@ page import="photoshare.PictureDao" %>
<%@ page import="photoshare.SearchDao" %>
<%@ page import="photoshare.User" %>
<%@ page import="org.apache.commons.fileupload.FileUploadException" %>
<jsp:useBean id="imageUploadBean"
             class="photoshare.ImageUploadBean">
    <jsp:setProperty name="imageUploadBean" property="*"/>
</jsp:useBean>

<html>
<head><title>Album <%= request.getParameter("aid") %></title></head>

<body ALIGN=CENTER style="background-color:lightblue">

<%  SearchDao searchDao = new SearchDao();
	User myself = new User();
	if (request.getUserPrincipal()!= null){
		myself = searchDao.myself(request.getUserPrincipal().getName());

	}
	int aid = Integer.parseInt(request.getParameter("aid"));
	AlbumDao albumDao = new AlbumDao();
	
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
	}else if (request.getParameter("tag")!=null && !request.getParameter("tag").equals("")){
		int picture_id = Integer.parseInt(request.getParameter("picture_id"));
		String tag = request.getParameter("tag");
		albumDao.addTag(picture_id, tag);
	}else if (request.getParameter("picture_id") != null){
		int picture_id = Integer.parseInt(request.getParameter("picture_id"));
		albumDao.deletePicture(picture_id);
		albumDao.updateScore(myself.getScore()-1, myself.getUser_id());
	}		
    
    Albums album = albumDao.getAlbum(aid);
	boolean isOwner = false;
	if (album.getUser_id() == myself.getUser_id()){
		isOwner = true;
	}

	if (isOwner){%>
		<h2>Upload a new picture</h2>

		<form action="album.jsp?aid=<%=aid%>" enctype="multipart/form-data" method="post">
    
    	Filename: <input type="file" name="filename"/><br/>
    	Caption: <input type="text" name="caption">
    	<input type="hidden" name="aid" value="aid">
    	<input type="submit" value="Upload"/><br/>
		</form>

<%
    PictureDao pictureDao = new PictureDao();
    try {
        Picture picture = imageUploadBean.upload(request);
        if (picture != null) {
            pictureDao.save(picture);
            albumDao.updateScore(myself.getScore()+1, myself.getUser_id());
        }
    } catch (FileUploadException e) {
        e.printStackTrace();
    }


	}
	List<Integer> picList = null;

	picList = albumDao.nonregisterView(aid);

	if (picList!= null){
		if (!isOwner){%>
		<table ALIGN=CENTER>
        <%
            for (Integer pictureId : picList) {
            	Picture picture = albumDao.getPicture(pictureId);
        %>
        
        	
        	<tr>
        	<td> <%=picture.getCaption()%> <td>
        	<tr>

        	<tr>
        	<td><a href="/photoshare/img?picture_id=<%= pictureId %>">
            <img src="/photoshare/img?t=1&picture_id=<%= pictureId %>"/>
        	</a>
        	</td>
        	<tr>

        	<tr>
            <td>
            <%List<String> like = albumDao.getLike(pictureId);%>
              <%=like.size()%>likes--
              <%for(String email: like){%>
              	~<%= email%>&nbsp
          	  <%}%>
          	 <%if (request.getUserPrincipal()!= null){%>
          	 <form action="album.jsp?aid=<%=aid%>" method="post">
          	  <input type="hidden" name="like" value=<%= pictureId %>>
			  <input type="submit" value="like this photo"/>
			  </form>
			  <%}%>
            <td>
            <tr>

            <tr>
            <td>
            <%List<String> tag = albumDao.getTag(pictureId);%>
            <%=tag.size()%>&nbsp tags--
            <%for(String t: tag){%>
            	#<a href="tag.jsp?tag=<%= t %>&own=no"><%= t %></a>&nbsp
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
			<form action="album.jsp?aid=<%=aid%>" method="post">
    		<input type="hidden" name="addComment" value=<%= pictureId %> />
    		<input type="text" name="comment"/>
    		<input type="submit" value="add comment"/><br/>
			</form>
			<td>
			<tr>

        	
        
        <%
            }
        %>
        <table>
	<%}else{%>
		<a href="/photoshare/home.jsp">home</a>
		<table ALIGN=CENTER>
		
        <%
            for (Integer pictureId : picList) {
            	Picture picture = albumDao.getPicture(pictureId);
        %>
        

        	
        	<tr>
        	<td> <%=picture.getCaption()%> <td>
        	<tr>

        	<tr>
        	<td><a href="/photoshare/img?picture_id=<%= pictureId %>">
            <img src="/photoshare/img?t=1&picture_id=<%= pictureId %>"/>
            </a>
            <td>
            <tr>

            <tr>
            <td>
            <%List<String> like = albumDao.getLike(pictureId);%>
              <%=like.size()%>&nbsp likes--
              <%for(String email: like){%>
              	~<%= email%>&nbsp
          	  <%}%>
          	  <form action="album.jsp?aid=<%=aid%>" method="post">
          	  <input type="hidden" name="like" value=<%= pictureId %>>
			  <input type="submit" value="like this photo"/>
			  </form>
            <td>
            <tr>


            <tr>
            <td>
            <%List<String> tag = albumDao.getTag(pictureId);%>
            <%=tag.size()%>&nbsp tags--
            <%for(String t: tag){%>
            	#<a href="tag.jsp?tag=<%= t %>&own=no"><%= t %></a>&nbsp
            <%}%>
            <td>
            <tr>

            <tr>
            <td>
			<form action="album.jsp?aid=<%=aid%>" method="post">
    		<input type="hidden" name="picture_id" value=<%= pictureId %> />
            <input type="text" name="tag"/>
            <input type="submit" value="addTag"/>
			</form>
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
            <form action="album.jsp?aid=<%=aid%>" method="post">
    		<input type="hidden" name="picture_id" value=<%= pictureId %> />
    		<input type="submit" value="Delete photo"/><br/>
			</form>
			<td>
			<tr>


        <%
            }
        %>
    
		</table>
	<%}%>

<%}%>



</body>
</html>