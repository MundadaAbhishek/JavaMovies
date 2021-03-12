package training.assg9;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import training.assg7.database.MovieDB;
import training.assg7.structure.Movie;

public class Movies {

	static List<Movie> mList = new ArrayList<>();
	DBServices dbObject = new DBServices();
	class Movie implements Serializable {
		private int movieId;
		private String movieName;
		private Category movieType;
		private Language language;
		private Date releaseDate;
		private List<String> casting;
		private Double rating;
		private Double totalBusinessDone;
		
		public Movie() {
			super();
		}
		
		public Movie(int movieId, String movieName, Category movieType, Language language, Date releaseDate,
				List<String> casting, Double rating, Double totalBusinessDone) {
			super();
			this.movieId = movieId;
			this.movieName = movieName;
			this.movieType = movieType;
			this.language = language;
			this.releaseDate = releaseDate;
			this.casting = casting;
			this.rating = rating;
			this.totalBusinessDone = totalBusinessDone;
		}
		
		public void setMovieId(int movieId) {
			this.movieId = movieId;
		}
		public int getMovieId() {
			return this.movieId;
		}
		public void setMovieName(String movieName) {
			this.movieName = movieName;
		}
		public String getMovieName() {
			return this.movieName;
		}
		public void setMovieType(Category movieType) {
			this.movieType = movieType;
		}
		public Category getMovieType() {
			return this.movieType;
		}
		public void setLanguage(Language language) {
			this.language = language;
		}
		public Language getLanguage() {
			return this.language;
		}
		public void setReleaseDate(Date releaseDate) {
			this.releaseDate = releaseDate;
		}
		public Date getReleaseDate() {
			return this.releaseDate;
		}
		public void setCasting(List<String> casting) {
			this.casting = casting;
		}
		public List<String> getCasting(){
			return this.casting;
		}
		public void setRating(Double rating) {
			this.rating = rating;
		}
		public Double getRating() {
			return this.rating;
		}
		public void setTotalBusinessDone(Double totalBusinessDone) {
			this.totalBusinessDone = totalBusinessDone;
		}
		public Double getTotalBusinessDone() {
			return this.totalBusinessDone;
		}
	}
	
	//populating Movies into list from movieDetails.txt
	List<Movie> populateMovies(File file){
		List<Movie> movieList = new ArrayList<>();
		Scanner sc;
		try {
			sc = new Scanner(file);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yy");
			while(sc.hasNextLine()) {
				Movie m = new Movie();
				String data[] = sc.nextLine().split(",");
				m.movieId = Integer.parseInt(data[0]);
				m.movieName = data[1];
				m.movieType = Category.valueOf(data[2]);
				m.language = Language.valueOf(data[3]);
				m.releaseDate = Date.valueOf(LocalDate.parse(data[4],dateTimeFormatter));
				m.casting = List.of(data[5].split("-"));
				m.rating = Double.parseDouble(data[6]);
				m.totalBusinessDone = Double.parseDouble(data[7]);
				
				movieList.add(m);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Rows inserted");
		
		return movieList;
	}
	
	//Adding all movies to database
	public Boolean addMoviesInDb(List<Movie> movies) {
		dbObject.insertMovies(movies);
		return true;
	}
	
	//adding new movie
	public void addMovie(Movie movie, List<Movie> list) {
		list.add(movie);
		dbObject.addMovie(movie);
	}
	
	//serializing movies data
	public void serializeMovies(List<Movie> movies, String fileName) {
		try {
			FileOutputStream fileout = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileout);
			out.writeObject(movies);
			out.close();
			fileout.close();
			System.out.println("\nSerialization Successful... Checkout your specified output file..\n");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	//Deserializing movies data
	public List<Movie> deserializeMovie(String fileName) {
		List<Movie> list = new ArrayList<>();
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			System.out.println("Deserialized Data: \n" + in.readObject().toString());
			in.close();
			fileIn.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	//Get movies by year from movieList
	@SuppressWarnings("deprecation")
	public List<Movie> getMoviesRealeasedInYear(int year){
		List<Movie> mov = new ArrayList<>();
		for(Movie m : mList) {
			LocalDate date = m.getReleaseDate().toLocalDate();
			if(date.getYear()==year) {
				mov.add(m);
			}
		}
		
		return mov;
	}
	
	//Get movies by actor from movieList
	public List<Movie> getMoviesByActor(String...actorNames){
		List<Movie> mov = new ArrayList<>();
		for(Movie m : mList) {
			for(String actor : actorNames) {
				if(m.getCasting().contains(actor)) {
					mov.add(m);
					break;
				}
			}
		}
		
		return mov;
	}
	
	//Update movie rating
	public void updateRatings(Movie movie, double rating, List<Movie> movies) {
		if(movies.contains(movie)) {
			movie.setRating(rating);
			dbObject.updateRatings(movie, rating);
			System.out.println("Rating Updated");
		}
		else {
			System.out.println("Movie doesn't exist");
		}
	}
	
	//Update Total business done
	public void updateBusiness(Movie movie, double amount, List<Movie> movies) {
		if(movies.contains(movie)) {
			movie.setTotalBusinessDone(amount);
			dbObject.updateBusiness(movie, amount);
			System.out.println("Amount updates");
		}
		else {
			System.out.println("Movie doesn't exist");
		}
	}
	
	public Map<Language,Set<Movie>> businessDone(List<Movie> movies,double amount){
		Set <Movie> movieSet = new TreeSet<>();
		Map <Language,Set<Movie>> movieMap = new HashMap<>();
		for(Movie movie: movies) {
			if(movie.getTotalBusinessDone() > amount) {
				movieSet.add(movie);
				if(movieMap.containsKey(movie.getLanguage())) {
					movieMap.get(movie.getLanguage()).add(movie);
				}
				else {
					movieMap.put(movie.getLanguage(), movieSet);
				}
			}
		}

		return movieMap;
	}
	
	public static void main(String[] args) {
		
		List<Movie> movieList = new ArrayList<>();
		Movies movies = new Movies();
		File fp = new File("C:\\Users\\Abhishek\\eclipse-workspace\\JavaBasic\\src\\training\\assg9\\movieDetails.txt");
		
		//calling populateMovies
		movieList = movies.populateMovies(fp);
		mList = movieList;
		//adding movies into DB
		movies.addMoviesInDb(mList);
		//adding new movie
		
		System.out.println("1. Serialize movie data\n2. Deserialize movie data\n"
				+ "3. Get movies released in particular year\n"
				+ "4. Get movie by actor\n");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch(choice) {
		case 1:
			movies.serializeMovies(mList, "serialized.txt");
			break;
		case 2:
			movies.deserializeMovie("deserialized.txt");
			break;
		case 3:
			movies.getMoviesRealeasedInYear(2012);
			break;
		case 4:
			movies.getMoviesByActor("Akshay Kumar", "Aamir Khan");
			break;
		}
		
	}
}
