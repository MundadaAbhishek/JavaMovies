package training.assg9;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

	static Connection conn = null;
	
	public static Connection establishConnection() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/moviesdb","root","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
