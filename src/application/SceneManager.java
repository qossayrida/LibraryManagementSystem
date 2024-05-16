package application;

import java.sql.*;
import java.util.Properties;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class SceneManager {

	private static String dbURL;
	private static String dbUsername = "root";
	private static String dbPassword = "12135";
	private static String URL = "127.0.0.1";
	private static String port = "3306";
	private static String dbName = "library";
	private static Connection con;
	private static int Cuurent_user=0;
	
    // Reference to the primary stage of the application.
    private static Stage primaryStage;

    // Setter method to set the primary stage.
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Set a general scene for the primary stage.
    public static void setLoginScene() {
        primaryStage.setScene(new LoginScene());
    }
    
    // Set the main scene using a custom MainScene class.
    public static void setMainScene() {
        primaryStage.setScene(new MainScene());
    }
    
    
    public static void connectDB(){
		
    	try {
			dbURL = "jdbc:mysql://" + URL + ":" + port + "/" + dbName + "?verifyServerCertificate=false";
			Properties p = new Properties();
			p.setProperty("user", dbUsername);
			p.setProperty("password", dbPassword);
			p.setProperty("useSSL", "false");
			p.setProperty("autoReconnect", "true");
			Class.forName("com.mysql.jdbc.Driver");
	
			con = DriverManager.getConnection (dbURL, p);
		
    	} catch (ClassNotFoundException | SQLException e) {
    		showAlert("Error","Could not connect to database");
    	}
	}
    
    public static ResultSet ExecuteStatement(String SQL) throws SQLException {
	    Statement stmt = null;
	    ResultSet rs = null;
	    try {
	        stmt = SceneManager.getCon().createStatement();
	        if (SQL.trim().toLowerCase().startsWith("select")) {
	            // For SELECT queries
	            rs = stmt.executeQuery(SQL);
	            return rs;  // Return ResultSet for SELECT queries
	        } else {
	            // For INSERT, UPDATE, DELETE
	            stmt.executeUpdate(SQL);
	            return null;  // Return null for non-SELECT queries
	        }
	    } catch (SQLException s) {
	        throw s;  // Re-throw the exception to be handled by the caller
	    } finally {
	        if (stmt != null && rs == null) {
	            stmt.close();  // Close the statement if ResultSet is not expected
	        }
	    }
	}
	
	public static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
	
	public static Connection getCon() {
		return con;
	}

	public static int getCuurent_user() {
		return Cuurent_user;
	}

	public static void setCuurent_user(int cuurent_user) {
		Cuurent_user = cuurent_user;
	}
	
    
}

