package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import collection.HashPassword;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

public class LoginScene extends Scene {

	private TextField emailField;
	private PasswordField passwordField;
	private Label messageLabel;

	public LoginScene() {
		super(new HBox(100),Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
		HBox root = (HBox) getRoot();
		root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		root.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 50% 50%, #FFFFFF, #2f3d7e)");
		root.setPadding(new Insets(15));
		root.setAlignment(Pos.CENTER);
	
		VBox controllerVBox = new VBox(30);
		controllerVBox.setAlignment(Pos.CENTER);
		controllerVBox.getStyleClass().add("login-box");
		controllerVBox.setMaxHeight(600); // Set the preferred height, adjust as needed
		controllerVBox.setMinWidth(350); // Set the preferred height, adjust as needed
		createLoginForm(controllerVBox);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.BLACK);
        shadow.setOffsetX(5);
        shadow.setOffsetY(5);
        controllerVBox.setEffect(shadow);
		Image backgroundImage = new Image(getClass().getResourceAsStream("/library.png"));
		ImageView backgroundImageView = new ImageView(backgroundImage);

		// Then add the controllerVBox
		root.getChildren().addAll(backgroundImageView, controllerVBox);
	}

	private void createLoginForm(VBox root) {
		emailField = new TextField();
		Label emailLabel = new Label("Email:");
		emailField.setPromptText("Email");

		passwordField = new PasswordField();
		Label passwordLabel = new Label("Password:");
		passwordField.setPromptText("Password");

		Label welcomeLabel = new Label("Welcome");
		welcomeLabel.setTextFill(Color.BLACK); // Set the text color to white
		welcomeLabel.setFont(new Font("Arial", 24)); // Set the font size and family
		
		Button loginButton = new Button("Login");
		Button adminButton = new Button();
		adminButton.setOnAction(e->{
			try {
				adminLogin();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		loginButton.setOnAction(e -> {
			try {
				checkCredentials();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		


		emailField.getStyleClass().add("text-field-login");
		emailLabel.getStyleClass().add("another-label");
		passwordField.getStyleClass().add("text-field-login");
		passwordLabel.getStyleClass().add("another-label");
		loginButton.getStyleClass().add("login-button");
		messageLabel = new Label();

		passwordField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER)
				try {
					checkCredentials();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		});

		GridPane form = new GridPane();
		form.setAlignment(Pos.CENTER);
		form.setHgap(20);
		form.setVgap(20);
		form.add(emailLabel, 0, 0);
		form.add(emailField, 1, 0);
		form.add(passwordLabel, 0, 1);
		form.add(passwordField, 1, 1);
		
		updateButtonUI(adminButton);

		root.getChildren().addAll(welcomeLabel, form, loginButton ,messageLabel,adminButton);
	}

	private void checkCredentials() throws SQLException {
		String email = emailField.getText();
		String password = passwordField.getText();
		String Compare_password=null;
		int id=0;
		SceneManager.connectDB();
		String SQL = "select s.Name,s.password,s.librarianID from Librarian s where s.email=" +"'" + email + "'";
		
		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);
		String Person_Name=null;
		
		while ( rs.next() ) { 
			 Compare_password=rs.getString(2);
			 Person_Name=rs.getString(1);
			 id=Integer.parseInt(rs.getString(3));		 
		}
			
		
		rs.close();
		stmt.close();
		SceneManager.getCon().close();
		System.out.print(id+"    "+Person_Name+"   "+Compare_password+"\n");

		if (Person_Name!=null && HashPassword.hashPassword(password).equals(Compare_password)) {
			SceneManager.setCuurent_user(id);
	        SceneManager.setMainScene();

		} else {
			messageLabel.setTextFill(Color.RED);
			messageLabel.setText("Wrong email or password.");
		}
	}
	private void adminLogin() throws SQLException {
	    SceneManager.connectDB();
	    String SQL = "select s.email from Librarian s where s.librarianID=" +"'" + 1 + "'";
	    Statement stmt = SceneManager.getCon().createStatement();
	    ResultSet rs = stmt.executeQuery(SQL);
	    String adminEmail = null;
	    while ( rs.next() ) { 
			 adminEmail=rs.getString(1);		 
		}
	    rs.close();
		stmt.close();
		SceneManager.getCon().close();
	    
	    emailField.setText(adminEmail);
	}
	
	private void updateButtonUI(Button button) {
    	ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/root-access.png")));
    	image.setPreserveRatio(true);
    	image.setFitHeight(58);
    	button.setStyle("-fx-background-color: transparent;");
        button.setGraphic(image);
    }
	
}