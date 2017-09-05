package photoshare;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




/**
 * A data access object (DAO) to handle the user search
 *
 * author linshan (linshan@bu.edu)
 */

public class SearchDao{
	private static final String CHECK_SEARCH_STMT = "SELECT " +
      "* FROM Users WHERE email like ? or first_name like ? or last_name like ?";
  private static final String FIND_UID_STMT = "SELECT " +
           "* FROM Users WHERE email = ?";
  private static final String ADD_FRIEND_STMT = "INSERT INTO " +
           "Friends (user_id, fid) VALUES(?, ?)";

  private static final String CHECK_FRIEND_STMT = "SELECT " +
      "COUNT (*) FROM Friends WHERE user_id = ? AND fid = ?";

  private static final String TOP_TEN_STMT = "SELECT " +
      "* FROM Users ORDER BY score DESC LIMIT 10";

  private static final String FRIEND_LIST_STMT = "SELECT " +
      "* FROM Friends, Users WHERE Friends.user_id = ? AND Friends.fid = Users.user_id";
  private static final String FIND_EMAIL_STMT = "SELECT " +
      "email FROM Users WHERE user_id = ?";
  private static final String TOP_TEN_TAG_STMT = "SELECT "+
      "word, COUNT(picture_id) AS num FROM Associate "+
      "GROUP BY word ORDER BY num DESC LIMIT 10";
  private static final String TOP_FIVE_TAG_USER_STMT = "SELECT " +
      "word, COUNT(Pictures.picture_id) AS num FROM Associate, Pictures, Albums WHERE "+
      "Associate.picture_id = Pictures.picture_id AND Pictures.aid = Albums.aid AND "+
      "Albums.user_id = ? GROUP BY word ORDER BY num DESC LIMIT 5";
  private static final String LIST_ALL_USERS = "SELECT "+
      "* FROM Users";

  public List<User> listAllUsers(){
    List<User> listAllUsers = new ArrayList<User>();
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(LIST_ALL_USERS);
          rs = stmt.executeQuery();
          while(rs.next()){
            User user = new User();
            user.user_id = rs.getInt("user_id");
            user.first_name = rs.getString("first_name");
            user.last_name = rs.getString("last_name");
            user.email = rs.getString("email");
            user.hometown = rs.getString("hometown");
            user.gender = rs.getString("gender");
            user.score = rs.getInt("score");
            listAllUsers.add(user);
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

            return listAllUsers;
        }
  
  }
  public List<String> topFiveTagUser(int user_id){
    List<String> topFiveTagUser = new ArrayList<String>();
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(TOP_FIVE_TAG_USER_STMT);
          stmt.setInt(1, user_id);
          rs = stmt.executeQuery();
          while(rs.next()){
            topFiveTagUser.add(rs.getString("word"));
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

            return topFiveTagUser;
        }
  }

  public List<String> topTenTag(){
    List<String> topTenTag = new ArrayList<String>();
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    try {
          conn = DbConnection.getConnection();
          stmt = conn.prepareStatement(TOP_TEN_TAG_STMT);
          rs = stmt.executeQuery();
          while(rs.next()){
            topTenTag.add(rs.getString("word"));
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

            return topTenTag;
        }
  }

	public List<User> search(String keyword){
		List<User> userList = new ArrayList<User>();
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	try {
      		conn = DbConnection.getConnection();
      		stmt = conn.prepareStatement(CHECK_SEARCH_STMT);
      		stmt.setString(1, "%"+keyword+"%");
      		stmt.setString(2, "%"+keyword+"%");
      		stmt.setString(3, "%"+keyword+"%");
      		rs = stmt.executeQuery();

      		while (rs.next())
      		{
      			User user = new User();
      			user.user_id = rs.getInt("user_id");
      			user.first_name = rs.getString("first_name");
      			user.last_name = rs.getString("last_name");
      			user.email = rs.getString("email");
      			user.hometown = rs.getString("hometown");
      			user.gender = rs.getString("gender");
      			userList.add(user);
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

            return userList;
    		}
  		}
  public String myEmail(int user_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    String email = "";
    try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(FIND_EMAIL_STMT);
      stmt.setInt(1, user_id);

      rs = stmt.executeQuery();
      if(rs.next()){
        email = rs.getString("email");
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
      return email;
  }
}

  public User myself(String email){
    

    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    User myself = new User();

   try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(FIND_UID_STMT);
      stmt.setString(1, email);
      rs = stmt.executeQuery();
      if (rs.next()){
            myself.user_id = rs.getInt("user_id");
            myself.first_name = rs.getString("first_name");
            myself.last_name = rs.getString("last_name");
            myself.email = rs.getString("email");
            myself.hometown = rs.getString("hometown");
            myself.gender = rs.getString("gender");
            myself.score = rs.getInt("score");
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
      return myself;
    }
  }

  public void addFriend(int user_id, int fid){
    

    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;


   try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(CHECK_FRIEND_STMT);
      stmt.setInt(1, user_id);
      stmt.setInt(2, fid);
      rs = stmt.executeQuery();
      int result=0;
      if (rs.next()){
           result= rs.getInt(1);
      }
      boolean flag = true;
      if (result > 0) {
        // This email is already in use
        flag=false; 
      }
      try { stmt.close(); }
      catch (Exception e) { }
      if (flag == true){
        stmt = conn.prepareStatement(ADD_FRIEND_STMT);
        stmt.setInt(1, user_id);
        stmt.setInt(2, fid);
        stmt.executeUpdate();
        
      }
    }catch (SQLException e) {
       e.printStackTrace();
      throw new RuntimeException(e);
    } finally {
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

  public List<User> topTen(){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    List<User> topTen = new ArrayList<User>();

   try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(TOP_TEN_STMT);
      rs = stmt.executeQuery();
      while (rs.next()){
            User user = new User();
            user.user_id = rs.getInt("user_id");
            user.first_name = rs.getString("first_name");
            user.last_name = rs.getString("last_name");
            user.email = rs.getString("email");
            user.hometown = rs.getString("hometown");
            user.gender = rs.getString("gender");
            user.score = rs.getInt("score");
            topTen.add(user);
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
      return topTen;
    }
  }

  public List<User> friendList(int user_id){
    PreparedStatement stmt = null;
    Connection conn = null;
    ResultSet rs = null;
    List<User> friendList = new ArrayList<User>();

   try {
      conn = DbConnection.getConnection();
      stmt = conn.prepareStatement(FRIEND_LIST_STMT);
      stmt.setInt(1, user_id);
      rs = stmt.executeQuery();
      while (rs.next()){
            User user = new User();
            user.user_id = rs.getInt("user_id");
            user.first_name = rs.getString("first_name");
            user.last_name = rs.getString("last_name");
            user.email = rs.getString("email");
            user.hometown = rs.getString("hometown");
            user.gender = rs.getString("gender");
            user.score = rs.getInt("score");
            friendList.add(user);
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
      return friendList;
    }



  }
}



