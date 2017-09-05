package photoshare;

/**
 * A User object to handle return values user search
 *
 * author linshan (linshan@bu.edu)
 */

public class User{
	  public int user_id = 0;
  	public String first_name = "";
  	public String last_name = "";
  	public String email = "";
  	public String hometown = "";
  	public String gender = "";
    public int score = 0;

  	public User(){

  	}

  	public int getUser_id(){
  		return user_id;
  	}

  	public String getFirst_name(){
  		return first_name;
  	}

  	public String getLast_name(){
  		return last_name;
  	}

  	public String getEmail(){
  		return email;
  	}

  	public String getHometown(){
  		return hometown;
  	}

  	public String getGender(){
  		return gender;
  	}

    public int getScore(){
      return score;
    }


}