package photoshare;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * A bean that handles user search data
 *
 * arthor Linshan (linshan@bu.edu)
 */

public class SearchBean{

	private String keyword = "";

	public SearchBean(){

	}

	public String getKeyword(){
		return keyword;
	}

	public void setKeyword(String keyword){
		this.keyword = keyword;
	}


}