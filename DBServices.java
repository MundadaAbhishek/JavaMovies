package training.assg9;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import training.assg9.Movies.Movie;

public class DBServices {

	Connection conn = Connect.establishConnection();
	
	public void insertMovies(List<Movie> movies) {
		PreparedStatement stmnt = null;
		for(Movie m : movies) {
			try {
				stmnt = conn.prepareStatement("insert into movies values(?,?,?,?,?,?,?)");
				stmnt.setInt(1, m.getMovieId());
				stmnt.setString(2, m.getMovieName());
				stmnt.setString(3, m.getMovieType().name());
				stmnt.setString(4, m.getLanguage().name());
				stmnt.setDate(5, m.getReleaseDate());
				stmnt.setDouble(6, m.getRating());
				stmnt.setDouble(7, m.getTotalBusinessDone());
				stmnt.executeUpdate();
				
				for(String s:m.getCasting()) {
				     stmnt = conn.prepareStatement("insert into casting values (?,?)");
				     stmnt.setInt(1,m.getMovieId());
				     stmnt.setString(2, s);
				     stmnt.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addMovie(Movie m) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("insert into movies values (?,?,?,?,?,?,?)");
			pstmt.setInt(1, m.getMovieId());
			pstmt.setString(2, m.getMovieName());
			pstmt.setString(3, m.getMovieType().name());
			pstmt.setString(4, m.getLanguage().name());
			pstmt.setDate(5, m.getReleaseDate());
			pstmt.setDouble(6, m.getRating());
			pstmt.setDouble(7, m.getTotalBusinessDone());
			pstmt.executeUpdate();
			for(String str:m.getCasting()) {
			     pstmt = conn.prepareStatement("insert into casting values (?,?)");
			     pstmt.setInt(1,m.getMovieId());
			     pstmt.setString(2, str);
			     pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateRatings(Movie movie,double rating) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update movies set rating = ? where movieId = ?");
			pstmt.setDouble(1, rating);
			pstmt.setInt(2, movie.getMovieId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateBusiness(Movie movie, double amount) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("update movies set totalBusinessdone = ? where movieId = ?");
			pstmt.setDouble(1, amount);
			pstmt.setInt(2, movie.getMovieId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
