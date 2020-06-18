// Name: Alisa Uthikamporn
// Student ID: 6188025
// Section: 1

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Movie 
{
	private int mid;
	private String title;
	private int year;
	private Set<String> tags;
	private Map<Integer, Rating> ratings;	//mapping userID -> rating
	private Double avgRating;
	
	public Movie(int _mid, String _title, int _year)
	{
		mid = _mid;
		title = _title;
		year = _year;		
		tags = new HashSet<String>();
		ratings = new HashMap<Integer, Rating>();
	}
	
	public int getID() 
	{
		if(mid < 1)
			return -1;
		else
			return mid;
	}
	public String getTitle()
	{
		if(title == null)
			return null;
		else
			return title;
	}
	
	public Set<String> getTags() 
	{
		if(tags == null)
			return null;
		else
			return tags;
	}
	
	public void addTag(String tag)
	{
		tags.add(tag);
	}
	
	public int getYear()
	{
		if(year < 1)
			return -1;
		else
			return year;
	}
	
	public String toString()
	{
		return "[mid: "+mid+":"+title+" ("+year+") "+tags+"] -> avg rating: " + avgRating;
	}

	public double calMeanRating()
	{
		avgRating = 0.0;
		for(Integer uid : ratings.keySet())
		{
			if(ratings.get(uid).rating <= 0 || ratings.get(uid).rating > 5)
				return -1;
			else
				avgRating += ratings.get(uid).rating;
		}
		avgRating = avgRating / ratings.size();
		return avgRating;
	}
	
	public Double getMeanRating()
	{
		if(avgRating <= 0)
			return -1.0;
		else
			return avgRating;
	}
	   
	public void addRating(User user, Movie movie, double rating, long timestamp) 
	{
		ratings.put(user.uid , new Rating(user,movie,rating,timestamp));
	}
	
	public Map<Integer, Rating> getRating()
	{
		return ratings;
	}
}
