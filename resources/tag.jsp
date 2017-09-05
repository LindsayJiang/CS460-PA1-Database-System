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
<%@ page import="java.util.ArrayList"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head><title>tag</title></head>

<body ALIGN=CENTER style="background-color:lightblue">
<h1>Shan'stagram</h1>
<%  SearchDao searchDao = new SearchDao();
	AlbumDao albumDao = new AlbumDao();
	User myself = new User();
	if (request.getUserPrincipal()!= null){
		myself = searchDao.myself(request.getUserPrincipal().getName());

	}
	AlbumTagSearchDao albumTagSearchDao = new AlbumTagSearchDao();
	String own = request.getParameter("own");
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
	}else if (request.getParameter("picture_id") != null){
		int picture_id = Integer.parseInt(request.getParameter("picture_id"));
		albumDao.deletePicture(picture_id);
		albumDao.updateScore(myself.getScore()-1, myself.getUser_id());
	}
%>


<%
if (own.equals("yes")){
	String tag = request.getParameter("tag");
	List<Integer> ownPhotoByTag = albumTagSearchDao.ownPhotoByTag(myself.getUser_id(), request.getParameter("tag"));%>
	<a href="/photoshare/home.jsp">home</a>
	<a href="/photoshare/albumTagSearch.jsp">back to search</a>
	<%if (ownPhotoByTag!= null){%>
	

	<table ALIGN=CENTER>
	
    <%
        for (Integer pictureId : ownPhotoByTag) {%>

        	<%Picture picture = albumDao.getPicture(pictureId);
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
          	  <form action="tag.jsp?tag=<%=tag%>&own=yes" method="post">
          	  <input type="hidden" name="like" value=<%= pictureId %>>
			  <input type="submit" value="like this photo"/>
			  </form>
        <td>
        <tr>

        <tr>
        <td>
        <%List<String> tags = albumDao.getTag(pictureId);%>
        <%=tags.size()%>&nbsp tags--
        <%for(String t: tags){%>
        	##<a href="/photoshare/tag.jsp?tag=<%=t%>&own=no"><%=t%></a>&nbsp&nbsp
        <%}%>
        <td>
        <tr>

        <tr>
        <td>
			<form action="tag.jsp?tag=<%=tag%>&own=yes" method="post">
    		<input type="hidden" name="picture_id" value=<%= pictureId %> />
            <input type="text" name="addTag"/>
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


    </tr>
    <%
        }
    %>

</table>


<%}%>
<%}else{


	if (request.getParameter("conjuncTag")!=null){

		String conjuncTag = request.getParameter("conjuncTag");
		String[] conjuncTagList = conjuncTag.split(" ");
		List<List<Integer>> prepareList = new ArrayList<List<Integer>>();
		for (int i = 0; i<conjuncTagList.length; i++){
			String tt = conjuncTagList[i];
			List<Integer> oneTagList = albumTagSearchDao.allPhotoByTag(tt);
			prepareList.add(oneTagList);

	    }
	    List<Integer> resultList = albumTagSearchDao.conjuncTagSearch(prepareList);%>
	    <a href="/photoshare/home.jsp">home</a>
		<a href="/photoshare/albumTagSearch.jsp">back to search</a>
		<%if (resultList!= null){%>
	

		<table ALIGN=CENTER>
	
    	<%
        for (Integer pictureId : resultList) {%>
        
        	<%Picture picture = albumDao.getPicture(pictureId);
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
          	  <%if (request.getUserPrincipal()!=null){%>
          	  <form action="tag.jsp?conjuncTag=<%=conjuncTag%>&own=no" method="post">
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
			<form action="tag.jsp?conjuncTag=<%=conjuncTag%>&own=no" method="post">
    		<input type="hidden" name="addComment" value=<%= pictureId %> />
    		<input type="text" name="comment"/>
    		<input type="submit" value="add comment"/><br/>
			</form>
			<%}%>
			<%}%>
			<td>
			<tr>
        
    	</table>


	<%} }else{
	String tag = request.getParameter("tag");
	List<Integer> allPhotoByTag = albumTagSearchDao.allPhotoByTag(request.getParameter("tag"));%>
	<a href="/photoshare/home.jsp">home</a>
	<a href="/photoshare/albumTagSearch.jsp">back to search</a>
	<%if (allPhotoByTag!= null){%>
	

	<table ALIGN=CENTER>
	
    <%
        for (Integer pictureId : allPhotoByTag) {%>
        	<%Picture picture = albumDao.getPicture(pictureId);
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
          	  <%if (request.getUserPrincipal()!=null){%>
          	  <form action="tag.jsp?tag=<%=tag%>&own=no" method="post">
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
			<form action="tag.jsp?tag=<%=tag%>&own=no" method="post">
    		<input type="hidden" name="addComment" value=<%= pictureId %> />
    		<input type="text" name="comment"/>
    		<input type="submit" value="add comment"/><br/>
			</form>
			<%}%>
			<%}%>
			<td>
			<tr>
        <%}%>
</table>
    <%
        }
    %>





<%}%>




</body>
</html>