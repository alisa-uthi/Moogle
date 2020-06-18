// Name: Alisa Uthikamporn
// Student ID: 6188025
// Section: 1

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMovieSearchEngine implements BaseMovieSearchEngine 
{
	public Map<Integer, Movie> movies;

	@Override
	public Map<Integer, Movie> loadMovies(String movieFilename) 
	{
		Map<Integer, Movie> loadedMovie = new HashMap<Integer,Movie>();
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(movieFilename));
			String line = null;
			int mid1 = 0, year1 = 0;
			String title1 = null;
			String[] tag1 = null;
			reader.readLine();					//To cut off the header
			
			while((line = reader.readLine()) != null)
			{
				if(line.trim().isEmpty())
					continue;
					
				Pattern pat1 = Pattern.compile("([0-9]+),(.*) \\(([0-9]+)\\),([\\S]+)");		//for pattern <mid>,<title> (<year>),<tag_1>|<tag_2>|<tag_3>|...|<tag_n>
				Matcher mat1 = pat1.matcher(line);
				Pattern pat2 = Pattern.compile("([0-9]+),\"(.*) \\(([0-9]+)\\)\",([\\S]+)");	//for pattern <mid>,"<title> (<year>)",<tag_1>|<tag_2>|<tag_3>|...|<tag_n>
				Matcher mat2 = pat2.matcher(line);
				if(mat1.find())
				{
					mid1 = Integer.parseInt(mat1.group(1));
					title1 = mat1.group(2);
					year1 = Integer.parseInt(mat1.group(3));
					tag1 = mat1.group(4).split("\\|");
				}
				else if(mat2.find())
				{
					mid1 = Integer.parseInt(mat2.group(1));
					title1 = mat2.group(2);
					year1 = Integer.parseInt(mat2.group(3));
					tag1 = mat2.group(4).split("\\|");
				}
				
				Movie mov = new Movie(mid1,title1,year1);
				for(String t : tag1)
					mov.addTag(t);					//add tag into set of tags in movies object
				loadedMovie.put(mid1,mov);			//add info into movies hashmap
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Oops! File is not found. Please check your file's location.");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		return loadedMovie;
	}

	@Override
	public void loadRating(String ratingFilename) 
	{
		BufferedReader reader = null;
		int uid2 = 0, mid2 = 0;
		double rating = 0.0;
		long timestamp;
		try
		{
			reader = new BufferedReader(new FileReader(ratingFilename));
			reader.readLine();				//To cut off the header
			String line = null;
			
			while((line = reader.readLine()) != null)
			{
				if(line.trim().isEmpty())
					continue;
				
				Pattern pat3 = Pattern.compile("(\\d+),(\\d+),(\\d+.\\d+),(\\d+)");			
				Matcher mat3 = pat3.matcher(line);
				if(mat3.matches())
				{
					uid2 = Integer.parseInt(mat3.group(1));
					mid2 = Integer.parseInt(mat3.group(2));
					rating = Double.parseDouble(mat3.group(3));
					timestamp = Long.parseLong(mat3.group(4));
					
					if(rating < 0.5 || rating > 5)		//rating has to be in the range of 0.5 to 5
						continue;
					
					Movie movRate = movies.get(mid2);
					if(movRate != null)
					{		
						// If a user rate the same movie multiple times, the "ratings" object will keep the latest rating
						
						if(movRate.getRating().containsKey(uid2) == false)						//this movie has not been rated yet
							movRate.addRating(new User(uid2),movRate,rating,timestamp);
						else if(movRate.getRating().get(uid2).timestamp < timestamp)			//this movie has already rated by the same user 
							movRate.getRating().replace(uid2, new Rating(new User(uid2),movRate,rating,timestamp));		//replace a new timestamp
					}	
				}
				
				for(Integer midd : movies.keySet())
					movies.get(midd).calMeanRating();			//find the mean rating of each movie
			}
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Oops! File is not found. Please check your file's location.");
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(reader != null)
					reader.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void loadData(String movieFilename, String ratingFilename) 
	{
		movies = loadMovies(movieFilename);
		loadRating(ratingFilename);
	}

	@Override
	public Map<Integer, Movie> getAllMovies() 
	{
		if(movies == null)
			return null;
		else
			return movies;
	}

	@Override
	public List<Movie> searchByTitle(String title, boolean exactMatch)
	{
		List<Movie> movTitle = new ArrayList<Movie>();
		for(Integer mid : movies.keySet())
		{
			if(exactMatch == false)
			{
				//compare two titles with both lower case
				Pattern pat = Pattern.compile(title.toLowerCase());   
				Matcher mat = pat.matcher(movies.get(mid).getTitle().toLowerCase());
				if(mat.find())
					movTitle.add(movies.get(mid));			
			}
			else
			{
				if(movies.get(mid).getTitle().toLowerCase().equals(title.toLowerCase()))
					movTitle.add(movies.get(mid));	
			}
		}
		return movTitle;
	}

	@Override
	public List<Movie> searchByTag(String tag)
	{
		List<Movie> movTag = new ArrayList<Movie>();
		int count = 0;
		for(Integer mid : movies.keySet())
		{
			if(movies.get(mid).getTags().contains(tag))
				movTag.add(movies.get(mid));
			else 
				count++;		//if this tag is not in any movie
		}
		if(count != movies.size())		//if it has some movies that have this tag
			return movTag;
		else 
			return null;		//if this tag is not in any movie
	}

	@Override
	public List<Movie>searchByYear(int year) 
	{
		List<Movie> movYear = new ArrayList<Movie>();
		for(Integer mid : movies.keySet())
		{
			if(movies.get(mid).getYear() == year)
				movYear.add(movies.get(mid));
		}
		return movYear;
	}

	@Override
	public List<Movie> advanceSearch(String title, String tag, int year) 
	{
		List<Movie> searchMovAdv = new ArrayList<Movie>();
		List<Movie> searchTitle = new ArrayList<Movie>();
		List<Movie> searchYear = new ArrayList<Movie>();
		List<Movie> searchTag = new ArrayList<Movie>();
		
		if(title == null)
		{
			searchTag = searchByTag(tag);
			searchYear = searchByYear(year);
			for(Movie mo : searchYear)
			{
				if(searchTag.contains(mo))		//find the intersection between two lists of year and tag
					searchMovAdv.add(mo);
			}
		}
		else if(tag == null)
		{
			searchTitle = searchByTitle(title, false);
			searchYear = searchByYear(year);
			for(Movie mo : searchTitle)
			{
				if(searchYear.contains(mo))		//find the intersection between two lists of year and title
					searchMovAdv.add(mo);
			}
		}
		else if(year == -1)
		{
			searchTag = searchByTag(tag);
			searchTitle = searchByTitle(title, false);
			for(Movie mo : searchTitle)			
			{
				if(searchTag.contains(mo))		//find the intersection between two lists of title and tag
					searchMovAdv.add(mo);
			}
		}
		else
		{
			searchTag = searchByTag(tag);
			searchTitle = searchByTitle(title, false);
			searchYear = searchByYear(year);
			for(Movie mo : searchTitle)			
			{
				if(searchTag.contains(mo) && searchYear.contains(mo))
					searchMovAdv.add(mo);				
			}
		}
		
		return searchMovAdv;
	}

	@Override
	public List<Movie> sortByTitle(List<Movie> unsortedMovies, boolean asc) 
	{
		List<Movie> sortMovTi = unsortedMovies;
		if(sortMovTi != null)
		{
			sortMovTi.sort(Comparator.comparing(Movie::getTitle));	//sort in ascending order
			if(asc == false)
				Collections.reverse(sortMovTi);		//sort in descending order
			return sortMovTi;
		}
		else
			return null;
	}

	@Override
	public List<Movie> sortByRating(List<Movie> unsortedMovies, boolean asc) 
	{
		List<Movie> sortMovRate = unsortedMovies;
		if(sortMovRate != null)
		{
			sortMovRate.sort(Comparator.comparing(Movie::getMeanRating));   //sort in ascending order
			if(asc == false)
				Collections.reverse(sortMovRate);		//sort in descending order
			return sortMovRate;
		}
		else
			return null;
	}
	
	
	////////////// BONUS //////////////
	
	public List<Movie> getListMovies()
	{
		//To add Movies(Hash Map) into an ArrayList
		List<Movie> MovieList = new ArrayList<Movie>();
		for(Integer mid : movies.keySet())
			MovieList.add(movies.get(mid));				
		return MovieList;
	}
	
	public List<Movie> findHighestRating(List<Movie> originalMovies, int numberOfMovies)
	{
		List<Movie> maxRatingMovie =  new ArrayList<Movie>();
		
		while(numberOfMovies != 0)
		{
			double maxRating = 0.0;
			int movieID = 0;
			for(Movie m: originalMovies)
			{	
				//To find the highest rating from all of the movies
				if(m.getMeanRating() >= maxRating)
				{
					maxRating = m.getMeanRating();
					movieID = m.getID();
				}					
			}
			maxRatingMovie.add(movies.get(movieID));		//Add a movie that has the highest rating into a list
			originalMovies.remove(movies.get(movieID));		//Remove that movie from the originalMovies in order to find the next highest movie
			numberOfMovies--;	
		}
		maxRatingMovie = sortByRating(maxRatingMovie, false);
		
		return maxRatingMovie;
	}
	
	public List<Movie> findLowestRating(List<Movie> originalMovies, int numberOfMovies)
	{
		List<Movie> minRatingMovie =  new ArrayList<Movie>();
		
		while(numberOfMovies != 0)
		{
			double minRating = 6.0;
			int movieID = 0;
			for(Movie m: originalMovies)
			{	
				//To find the lowest rating from all of the movies
				if(m.getMeanRating() <= minRating)
				{
					minRating = m.getMeanRating();		
					movieID = m.getID();				
				}					
			}
			minRatingMovie.add(movies.get(movieID));		//Add a movie that has the lowest rating into a list
			originalMovies.remove(movies.get(movieID));		//Remove that movie from the originalMovies in order to find the next lowest movie
			numberOfMovies--;	
		}
		minRatingMovie = sortByRating(minRatingMovie, true);
		
		return minRatingMovie;
	}

	public List<Movie> findTagHighestRating(String tag)
	{
		List<Movie> tagHighestRating =  new ArrayList<Movie>();
		tagHighestRating = searchByTag(tag);		//find the movies that have this tag
		if(tagHighestRating != null)
		{
			tagHighestRating = findHighestRating(tagHighestRating, tagHighestRating.size());     //find and sort the rating from highest to lowest
			return tagHighestRating;
		}
		else
			return null;		
	}
	
	/*public List<Movie> findSimilarMovie(String title, String tag, int year)
	{
		List<Movie> SimilarMovie = advanceSearch(title, tag, year);
		if(SimilarMovie != null)
		{
			SimilarMovie = findHighestRating(SimilarMovie, 3);		//10 highest rating from the movies
			SimilarMovie = sortByRating(SimilarMovie, false);
			return SimilarMovie;
		}
		else
			return null;
	}*/
}


