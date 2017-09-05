package photoshare;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.*;


public class AlbumTagSearchDao{
	private static final String OWN_PHOTO_TAG_STMT = "SELECT "+
		"Pictures.picture_id FROM Albums, Pictures, Associate WHERE Albums.user_id = ? AND "+
		"Albums.aid = Pictures.aid AND Pictures.picture_id = Associate.picture_id AND "+
		"Associate.word = ?";
	private static final String OWN_TAG_STMT = "SELECT " +
		"DISTINCT Associate.word FROM Associate, Pictures, Albums WHERE Associate.picture_id = Pictures.picture_id AND "+
		"Pictures.aid = Albums.aid AND Albums.user_id = ?";
	private static final String ALL_TAG_STMT = "SELECT "+
		"DISTINCT Associate.word FROM Associate";
	private static final String ALL_PHOTO_TAG_STMT = "SELECT "+
		"picture_id FROM Associate WHERE word = ?";
	private static final String OWN_PHOTO_STMT = "SELECT "+
		"COUNT (*) FROM Pictures, Albums WHERE Pictures.aid = Albums.aid AND Albums.user_id = ? AND Pictures.picture_id = ?";

	public List<String> recommendation(List<Integer> prepareList, String[] tagsLike){
		// preparelist is a list of list that contains picture_ids of each tag.

		// tagsLike is an array of tags that user entered.


		// single is a list that contains single occurance of tags of pictures.
		// This is for next step where we should compute occurance of each tag.
		List<String> singleTag = new ArrayList<String>();

		AlbumDao albumDao = new AlbumDao();
		
		// all tags of pictures.
		List<String> allTags = new ArrayList<String>();
		
		// dictionary that maps occurance with list of tags with that occurance.
		Map<Integer, List<String>> dictionary = new HashMap<Integer, List<String>>();


		for(Integer pic:prepareList){
			// get a list of tags of this picture.
			List<String> tagOfPic = albumDao.getTag(pic);
			allTags.addAll(tagOfPic);
			// check if singleTag contains tags of this picture and update singleTag.
			for (String eachTag:tagOfPic){
				if (!singleTag.contains(eachTag)){
					singleTag.add(eachTag);
				}
			}
		}

		// remove tags searched in single tag.
		for(int i=0; i<tagsLike.length; i++){
			singleTag.remove(tagsLike[i]);
		}

		// stores list of number of occurances.
		List<Integer> occurList = new ArrayList<Integer>();

		for(String oneTag:singleTag){
			int occurance = Collections.frequency(allTags, oneTag);
			if (!occurList.contains(occurance)){
				occurList.add(occurance);
			}
			if (dictionary.get(occurance) == null){
					List<String> instance = new ArrayList<String>();
					instance.add(oneTag);
					dictionary.put(occurance, instance);
				}else{
					// if dictionay already has that occurance, get the list of tags and add
					// current tag to that list, update the dictionary.
					List<String> instance2 = dictionary.get(occurance);
					instance2.add(oneTag);
					dictionary.remove(occurance);
					dictionary.put(occurance, instance2);
				}
		}

		// sort the occurance list
		// we create an array that is occurList.toArray().
		int[] occurList2 = new int[occurList.size()];
		int index = 0;
		for(int occur : occurList){
			occurList2[index] = occur;
			index++;
		}

		// sort tagNum2
		Arrays.sort(occurList2);

		List<String> finalResult = new ArrayList<String>();
		for (int o = occurList2.length-1; o >= 0; o--){
				finalResult.addAll(dictionary.get(occurList2[o]));
			}

		return finalResult;

	}
	public List<Integer> disjuncTagSearch(List<List<Integer>> prepareList){
		// preparelist is a list of list that contains picture_ids of each tag.

		// single is a list that contains single occurance of picture_id. 
		// This is for next step where we should search through all pictures to 
		// 	get their occurances.
		List<Integer> single = new ArrayList<Integer>();

		// all is a disjunctive list, does not delete duplicate.
		List<Integer> all = new ArrayList<Integer>();
		for (List<Integer> subList : prepareList)
		{
			all.addAll(subList);
		}

		// this array of list is a hash table. Where index stands for rank(occurance)
		// 	and the values of index are the pictures that has this rank.
		List<List<Integer>> hashtable = new ArrayList<List<Integer>>();
		for(int c = 0; c < 6; c++){
			hashtable.add(new ArrayList<Integer>());
		}

		// compute single list.
		for(List<Integer> subList:prepareList){
			for(Integer picture_id: subList){
				if (!single.contains(picture_id)){
					single.add(picture_id);
				}
			}
		}

		// compute rank of every picture_id (how many tags they belong to) and add to
		// hash table.
		for(Integer id : single){
			int rank = Collections.frequency(all, id);
			hashtable.get(rank).add(id);
		}

		List<Integer> finalResult = new ArrayList<Integer>();

		AlbumDao albumDao = new AlbumDao();

		

		// if pictures have the same rank, we have to decide which one is more precise
		//  by comparing the number of tags they have.
		for (int i = 5; i>0; i--){
			Map<Integer, List<Integer>> dictionary = new HashMap<Integer, List<Integer>>();
			// result contains picture ids that have the same rank.
			List<Integer> result = new ArrayList();

			// stores all instances of number of tags occured
			List<Integer> tagNum = new ArrayList<Integer>();

			// result stores the list of pictures that have i number of match tags. 
			result = hashtable.get(i);
			for (Integer pictureId : result){
				// get the tags of this picture.
				List<String> tagOfPic = albumDao.getTag(pictureId);
				// compute number of tags of this picture.
				int numOfTag = tagOfPic.size();
				// add this number to tagNum for later comparation.
				if (!tagNum.contains(numOfTag)){
					tagNum.add(numOfTag);
				}
				// if dictionary does not contain the key.
				if (dictionary.get(numOfTag) == null){
					List<Integer> instance = new ArrayList<Integer>();
					instance.add(pictureId);
					dictionary.put(numOfTag, instance);
				}else{
					// if dictionay already has that key, get the list of picture_ids and add
					// current picture to that list, update the dictionary.
					List<Integer> instance2 = dictionary.get(numOfTag);
					instance2.add(pictureId);
					dictionary.remove(numOfTag);
					dictionary.put(numOfTag, instance2);
				}
			}
			// we create an array that is tagNum.toArray(). -- tagNum2 contains the same values as tagNum.
			int[] tagNum2 = new int[tagNum.size()];
			int index = 0;
			for(int num : tagNum){
				tagNum2[index] = num;
				index++;
			}

			// sort tagNum2
			Arrays.sort(tagNum2);
			// the pictures with the smaller number of tags are more concise results, thus should be added first.
			for (int o = 0; o < tagNum.size(); o++){
				finalResult.addAll(dictionary.get(tagNum2[o]));
			}

		}

		return finalResult;
	}

	public List<Integer> conjuncTagSearch(List<List<Integer>> prepareList){

		List<Integer> intersection = prepareList.remove(0);
		for (List<Integer> l2 : prepareList){			
			intersection = conjuncHelper(intersection, l2);
		}
		return intersection;
	}
	public List<Integer> conjuncHelper(List<Integer> l1, List<Integer> l2){
		List<Integer> result = new ArrayList<Integer>();
		for(Integer i : l1){
			if (l2.contains(i)){
				result.add(i);
			}
		}
		return result;
	}



	public boolean isOwner(int user_id, int picture_id){
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	boolean isOwner = false;
    	try{
    		conn = DbConnection.getConnection();
    		stmt = conn.prepareStatement(OWN_PHOTO_STMT);
    		stmt.setInt(1, user_id);
    		stmt.setInt(2, picture_id);
    		rs = stmt.executeQuery();
    		int result = 0;
    		if (rs.next()){
    			result = rs.getInt(1);
    		}
    		if(result>0){
    			isOwner = true;
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
            return isOwner;
        }
	}
	public List<Integer> allPhotoByTag(String word){
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	List<Integer> allPhotoByTag = new ArrayList<Integer>();
    	try{
    		conn = DbConnection.getConnection();
    		stmt = conn.prepareStatement(ALL_PHOTO_TAG_STMT);
    		stmt.setString(1, word);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			allPhotoByTag.add(rs.getInt("picture_id"));
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
            return allPhotoByTag;
        }

	}
	public List<String> allTag(){
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	List<String> allTag = new ArrayList<String>();
    	try{
    		conn = DbConnection.getConnection();
    		stmt = conn.prepareStatement(ALL_TAG_STMT);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			allTag.add(rs.getString("word"));
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
            return allTag;
        }
	}
	public List<String> ownTag(int user_id){
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	List<String> ownTag = new ArrayList<String>();

    	try{
    		conn = DbConnection.getConnection();
    		stmt = conn.prepareStatement(OWN_TAG_STMT);
    		stmt.setInt(1, user_id);
    		rs = stmt.executeQuery();
    		while(rs.next()){
    			ownTag.add(rs.getString("word"));
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
            return ownTag;
        }
    }


	public List<Integer> ownPhotoByTag(int user_id, String word){
		PreparedStatement stmt = null;
    	Connection conn = null;
    	ResultSet rs = null;
    	List<Integer> ownPhotoByTag = new ArrayList<Integer>();
    	try{
	      conn = DbConnection.getConnection();
	      stmt = conn.prepareStatement(OWN_PHOTO_TAG_STMT);
	      stmt.setInt(1, user_id);
	      stmt.setString(2, word);
	      rs = stmt.executeQuery();
	      while(rs.next()){
	      	ownPhotoByTag.add(rs.getInt("picture_id"));
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
            return ownPhotoByTag;
        }
    }

}