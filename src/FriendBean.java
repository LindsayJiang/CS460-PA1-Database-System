package photoshare;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * A bean that handles user search data
 *
 * arthor Linshan (linshan@bu.edu)
 */

public class FriendBean{
	private int user_id = 0;
	private int fid = 0;

	public FriendBean(){

	}

	public int getUser_id(){
		return user_id;
	}

	public int getFid(){
		return fid;
	}

	public void setUser_id(int user_id){
		this.user_id = user_id;
	}

	public void setFid(int fid){
		this.fid = fid;
	}
}