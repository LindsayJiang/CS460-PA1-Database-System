package photoshare;

import java.sql.Date;

/**
 * A User object to handle return values of Albums
 * author linshan (linshan@bu.edu)
 */

public class Albums{
	public int aid;
	public int user_id;
	public String name;
	public Date date_of_creation;

	public Albums(){

	}

	public int getAid(){
		return aid;
	}

	public int getUser_id(){
		return user_id;
	}

	public String getName(){
		return name;
	}

	public Date getDate_of_creation(){
		return date_of_creation;
	}
}