package photoshare;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;




/**
 * A data access object (DAO) to handle the album search
 *
 * author linshan (linshan@bu.edu)
 */

public class AlbumDao{

	private static final String ALL_ALBUM_STMT = "SELECT " +
      "* FROM Albums";
  private static final String ALBUM_PICTURE_STMT = "SELECT "+
      "* FROM Pictures WHERE aid=?";
  private static final String USER_ALBUM_STMT = "SELECT "+
      "* FROM Albums WHERE user_id = ?";
  private static final String ADD_ALBUM_STMT = "INSERT INTO " +
      "Albums (user_id, name, date_of_creation) VALUES(?,?,?)";
  private static final String GET_ALBUM_STMT = "SELECT " +
      "* FROM Albums WHERE aid = ?";
  private static final String ADD_SCORE = "UPDATE Users "+
      "SET SCORE = ? WHERE user_id=?";
  private static final String DELETE_PICTURE = "DELETE FROM " +
      "Pictures WHERE picture_id = ?";
  private static final String DELETE_ALBUM = "DELETE FROM " +
      "Albums WHERE aid = ?";
  private static final String ADD_ASSOCIATE_STMT = "INSERT INTO "+
      "Associate (picture_id, word) VALUES(?,?)";
  private static final String ADD_TAG_STMT = "INSERT INTO "+
      "Tags (word) VALUES(?)";
  private static final String CHECK_TAG_STMT = "SELECT " +
      "COUNT(*) FROM Associate WHERE picture_id = ? AND word = ?";
  private static final String CHECK_TAG1_STMT = "SELECT " +
      "COUNT(*) FROM Tags WHERE word = ?";
  private static final String GET_PICTURE_STMT = "SELECT " +
      "* FROM Pictures WHERE picture_id=?"; 

  private static final String GET_TAG_STMT = "SELECT " +
      "word FROM Associate WHERE picture_id = ?";
  private static final String ADD_LIKE_STMT = "INSERT INTO " +
      "Likes (user_id, picture_id) VALUES(?, ?)";
  private static final String CHECK_LIKE_STMT = "SELECT " +
      "COUNT(*) FROM Likes WHERE user_id = ? AND picture_id = ?";
  private static final String GET_LIKE_STMT = "SELECT " +
      "user_id FROM Likes WHERE picture_id = ?";
  private static final String ADD_COMMENT_STMT = "INSERT INTO "+
      "Comments (user_id, picture_id, text, date) VALUES(?,?,?,?)";
  private static final String GET_COMMENT_STMT = "SELECT "+
      "text FROM Comments WHERE picture_id = ?";

  public List<String> getComment(int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    List<String> comment = new ArrayList<String>();
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(GET_COMMENT_STMT);
      stmt.setInt(1, picture_id);
      rs = stmt.executeQuery();
      while (rs.next()){
        comment.add(rs.getString("text"));
      }
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
            return comment;
        }
  }
  public void addComment(int user_id, int picture_id, String text){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(ADD_COMMENT_STMT);
      if (user_id == 0){
      stmt.setNull(1, java.sql.Types.INTEGER);
    }else{
      stmt.setInt(1, user_id);
    }
      stmt.setInt(2, picture_id);
      stmt.setString(3, text);
      stmt.setDate(4, new Date(new java.util.Date().getTime()));
      stmt.executeUpdate();
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
        }
  }
  public List<String> getLike(int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    List<String> like = new ArrayList<String>();
    SearchDao searchDao = new SearchDao();
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(GET_LIKE_STMT);
      stmt.setInt(1, picture_id);
      rs = stmt.executeQuery();

      while(rs.next()){
        int uid = rs.getInt("user_id");
        String uemail = searchDao.myEmail(uid);
        like.add(uemail);
      }
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
            return like;
        }
  }


  public void addLike(int user_id, int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    int result = 0;
    boolean flag = true;
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(CHECK_LIKE_STMT);
      stmt.setInt(1, user_id);
      stmt.setInt(2, picture_id);
      rs = stmt.executeQuery();

      if (rs.next()){
          result = rs.getInt(1);
      }

      if (result >0){
        flag = false;
      }

      try { stmt.close(); }
      catch (Exception e) { }
      if (flag == true){
        stmt = conn.prepareStatement(ADD_LIKE_STMT);
        stmt.setInt(1, user_id);
        stmt.setInt(2, picture_id);
        stmt.executeUpdate();
      }
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }
      }
  public List<String> getTag(int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    List<String> tags = new ArrayList<String>();
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(GET_TAG_STMT);
      stmt.setInt(1, picture_id);
      rs = stmt.executeQuery();
      while(rs.next()){
        tags.add(rs.getString("word"));
      }
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
            return tags;

        }


  }

  public Picture getPicture(int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    Picture myPic = new Picture();
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(GET_PICTURE_STMT);
      stmt.setInt(1, picture_id);
      rs = stmt.executeQuery();
      if (rs.next()){
        myPic.id = rs.getInt("picture_id");
        myPic.aid = rs.getInt("aid");
        myPic.caption = rs.getString("caption");
      }
    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
            return myPic;

        }
  }

  public void addTag(int picture_id, String tag){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try{
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(CHECK_TAG_STMT);
      stmt.setInt(1, picture_id);
      stmt.setString(2, tag);
      rs = stmt.executeQuery();
      int result=0;
      if (rs.next()){
         result= rs.getInt(1);
      }
      boolean flag = true;
      if (result > 0) {
        
        flag=false; 
      }
      try { stmt.close(); }
      catch (Exception e) { }

      stmt = conn.prepareStatement(CHECK_TAG1_STMT);
      stmt.setString(1, tag);
      rs = stmt.executeQuery();
      if (rs.next()){
         result= rs.getInt(1);
      }
      if (result > 0) {
        
        flag=false; 
      }
      try { stmt.close(); }
      catch (Exception e) { }
      if (flag == true){
        stmt = conn.prepareStatement(ADD_TAG_STMT);
        stmt.setString(1, tag);
        stmt.executeUpdate();
        try { stmt.close(); }
        catch (Exception e) { }
      }
          
      stmt = conn.prepareStatement(ADD_ASSOCIATE_STMT);
      stmt.setInt(1, picture_id);
      stmt.setString(2, tag);
      stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }
  }

  public void deleteAlbum(int aid){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(DELETE_ALBUM);
          stmt.setInt(1, aid);
          stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }
  }

  public void deletePicture(int picture_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(DELETE_PICTURE);
          stmt.setInt(1, picture_id);
          stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }
  }

  public void updateScore(int score, int user_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(ADD_SCORE);
          stmt.setInt(1, score);
          stmt.setInt(2, user_id);
          stmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }

  }

    public List<Albums> webAlbums(){
    	List<Albums> albumsList= new ArrayList<Albums>();
    	PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	try {
      		conn = DbConnection.getConnection();
      		stmt = conn.prepareStatement(ALL_ALBUM_STMT);
      		rs = stmt.executeQuery();
      		while (rs.next())
      		{
      			Albums album = new Albums();
      			album.aid = rs.getInt("aid");
      			album.user_id = rs.getInt("user_id");
      			album.name = rs.getString("name");
      			album.date_of_creation = rs.getDate("date_of_creation");
      			albumsList.add(album);
      		}
      		
      	}catch (SQLException e) {
      			e.printStackTrace();
    	  		throw new RuntimeException(e);
    		} finally {
     	 		if (rs != null) {
      	  			try { rs.close(); }
      	  			catch (SQLException e) { ; }
      	 			 rs = null;
      			}
      
     	 		if (stmt != null) {
        			try { stmt.close(); }
        			catch (SQLException e) { ; }
       			 	stmt = null;
      			}
      
      			if (conn != null) {
        			try { conn.close(); }
        			catch (SQLException e) { ; }
        			conn = null;
      			}

            return albumsList;
    		}
    }

    public List<Albums> userAlbums(int user_id){
      List<Albums> albumsList= new ArrayList<Albums>();
      PreparedStatement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(USER_ALBUM_STMT);
          stmt.setInt(1, user_id);
          rs = stmt.executeQuery();
          while (rs.next())
          {
            Albums album = new Albums();
            album.aid = rs.getInt("aid");
            album.user_id = rs.getInt("user_id");
            album.name = rs.getString("name");
            album.date_of_creation = rs.getDate("date_of_creation");
            albumsList.add(album);
          }
          
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

            return albumsList;
        }
    }

    public List<Integer> nonregisterView(int aid){
      List<Integer> picList= new ArrayList<Integer>();
      PreparedStatement stmt = null;
      Connection conn = null;
      ResultSet rs = null;
      try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(ALBUM_PICTURE_STMT);
          stmt.setInt(1, aid);
          rs = stmt.executeQuery();
          while (rs.next())
          {
            int pid = rs.getInt("picture_id");
            picList.add(pid);
          }
          
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

            return picList;
        }

    }

    public void addAlbum(int user_id, String name){
      PreparedStatement stmt = null;
      Connection conn = null;
      ResultSet rs = null;

      try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(ADD_ALBUM_STMT);
          stmt.setInt(1, user_id);
          stmt.setString(2, name);
          stmt.setDate(3, new Date(new java.util.Date().getTime()));
          stmt.executeUpdate();

    }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }

        }
      }

      public Albums getAlbum(int aid){
        PreparedStatement stmt = null;
        Connection conn = null;
        ResultSet rs = null;
        Albums album = new Albums();
        try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(GET_ALBUM_STMT);
          stmt.setInt(1, aid);
          rs = stmt.executeQuery();

          if (rs.next()){
            album.aid = rs.getInt("aid");
            album.user_id =rs.getInt("user_id");
            album.name = rs.getString("name");
            album.date_of_creation = rs.getDate("date_of_creation");

          }
      }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
          if (rs != null) {
                try { rs.close(); }
                catch (SQLException e) { ; }
               rs = null;
            }
      
          if (stmt != null) {
              try { stmt.close(); }
              catch (SQLException e) { ; }
              stmt = null;
            }
      
            if (conn != null) {
              try { conn.close(); }
              catch (SQLException e) { ; }
              conn = null;
            }
            return album;

        }
      }


}