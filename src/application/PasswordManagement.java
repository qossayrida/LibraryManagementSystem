package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Screen;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import collection.*;
import javafx.geometry.*;
import javafx.scene.input.KeyCode;

public class PasswordManagement extends StackPane {
	GridPane gridPane ;
	 Label librarianIDLabel,passwordLabel,passwordLabel1,passwordLabel2;
	 TextField librarianIDTextField,librarianNameTextField,passwordTextField,passwordTextField1,passwordTextField2;
	 Button addButton;
	 DatePicker dueDatePicker;
	 DateTimeFormatter formatter;
	 VBox vBox1 = new VBox(90);
	public  PasswordManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("New Password Management");
	    titelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    titelLabel.setStyle("-fx-text-fill: #000000;");
            
       
       setupGridPane();
       
       
       vBox1.setPadding(new Insets(60));
       vBox1.setAlignment(Pos.TOP_CENTER);
       vBox1.setPrefWidth(700);
       vBox1.getChildren().addAll(titelLabel,gridPane,addButton);
       
       this.getChildren().addAll(vBox1);
       this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
       this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
	
	public void setupGridPane() {
		try {
			gridPane = new GridPane();
			gridPane = new GridPane(); 
			 
			
			librarianIDLabel = new Label("Librarian: ");
			librarianIDTextField = new TextField();
			librarianIDTextField.setPromptText("Enter ID");
			librarianNameTextField = new TextField();
			librarianNameTextField.setPromptText("Name");
			librarianNameTextField.setEditable(false);
			librarianIDTextField.getStyleClass().add("custom-textfield-subscene"); 
			librarianNameTextField.getStyleClass().add("custom-textfield-subscene"); 
			
			librarianIDTextField.setOnKeyPressed(e -> {
				try {
					if (e.getCode() == KeyCode.ENTER) {
						librarianNameTextField.setText(findNameOfLibrarian(Integer.parseInt(librarianIDTextField.getText())));
						if (findNameOfLibrarian(Integer.parseInt(librarianIDTextField.getText()))==null)
							throw new Exception("There is no Librarian has this ID");
					}
				} catch (Exception e2) {
					librarianNameTextField.setText(null);
					SceneManager.showAlert("Error",e2.getMessage());
				}					
			});	
				
			passwordLabel2 = new Label("Old password: ");
			passwordTextField2 =  new PasswordField();
			passwordTextField2.setPromptText("Enter old password");
			passwordLabel = new Label("New password: ");
			passwordTextField =  new PasswordField();
			passwordTextField.setPromptText("Enter new password");
			passwordLabel1 = new Label("New password again: ");
			passwordTextField1 =  new PasswordField();
			passwordTextField1.setPromptText("Rewrite new password");
			passwordTextField2.getStyleClass().add("text-field-login");
			passwordTextField1.getStyleClass().add("text-field-login");
			passwordTextField.getStyleClass().add("text-field-login");

			
			Label Successfel=new Label("THE PASSWORD HAS CHANGED SUCESSFULLY");
			Successfel.setTextFill(Color.GREEN);
			
			
			addButton=new Button("Change");
			addButton.getStyleClass().add("custom-button-subscene");
			
			addButton.setOnAction(e -> {
	        	try {
	        		boolean check=true;
	        		
	        		 if (SceneManager.getCuurent_user()!=1) {
	        		String Compare_password=null;
	        		SceneManager.connectDB();
	        		String SQL = "select s.Name,s.password,s.librarianID from Librarian s where s.librarianID="  + SceneManager.getCuurent_user() ;
	        		
	        		Statement stmt = SceneManager.getCon().createStatement();
	        		ResultSet rs = stmt.executeQuery(SQL);

	        		while ( rs.next() ) { 
	        			 Compare_password=rs.getString(2);
	        	 	 
	        		} 
	        			
	        		rs.close();
	        		stmt.close();
	        		SceneManager.getCon().close();
	        		check=(HashPassword.hashPassword(passwordTextField2.getText())).equals(Compare_password);

	        		 }
	        		
	        		
	        		vBox1.getChildren().remove(Successfel);
	        		if (!(passwordTextField.getText().equals(""))&&passwordTextField.getText().equals(passwordTextField1.getText())&&check==true) {
	        		if (SceneManager.getCuurent_user()==1) {	
	        		updateData(passwordTextField.getText(),Integer.parseInt(librarianIDTextField.getText()));}
	        		else {
		        		updateData(passwordTextField.getText(),SceneManager.getCuurent_user());

	        		}
	        		passwordTextField.clear();
	        		librarianIDTextField.clear();
	        		librarianNameTextField.clear();
	        		passwordTextField1.clear();
	        		passwordTextField2.clear();
	        		
	        		vBox1.getChildren().add(Successfel);
	        		}
	        		else if(check==false) {SceneManager.showAlert("Error"," old Password is not correct !!");}
	        		else if (passwordTextField.getText().equals("")) {SceneManager.showAlert("Error"," Password is empty !!");}
	        		else {
	        			SceneManager.showAlert("Error","Two Password are not match !!");
	        		}
	        		 
	        		
				} catch (Exception e2) {
					SceneManager.showAlert("Error",e2.getMessage());
				}
	        });
		
		    if (SceneManager.getCuurent_user()==1) {
		    	gridPane.addRow(0, librarianIDLabel, librarianIDTextField, new Label("Librarian Name:"), librarianNameTextField);
		    	gridPane.addRow(1, passwordLabel,passwordTextField);
		    	gridPane.addRow(2, passwordLabel1,passwordTextField1);
		    
		    }
		    else {
		    	gridPane.addRow(0, passwordLabel2,passwordTextField2);
		    	gridPane.addRow(1, passwordLabel,passwordTextField);
			    gridPane.addRow(2, passwordLabel1,passwordTextField1);
		    }
		    gridPane.setHgap(20);
	    	gridPane.setVgap(30);
	    	gridPane.setAlignment(Pos.CENTER);
		 }catch (Exception e) {
			SceneManager.showAlert("Error",e.getMessage());
		}		
	}
	
	
	
	
	private String findNameOfLibrarian(int LibrarianID) throws SQLException, ClassNotFoundException {
	
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT Name FROM Librarian Where librarianID="+ LibrarianID);
		    
		if (rs.next()) {
			return rs.getString(1);
		}
		    
		return null;
	}
	
	
	
	private boolean updateData(String newpassword,int id) {
	    try {
	    	String password=HashPassword.hashPassword(newpassword);
	        SceneManager.connectDB();
	        System.out.println(("update Librarian set password = " + password + " where librarianID = " + id));
	        SceneManager.ExecuteStatement("update Librarian set password = '" + password + "' where librarianID = " + id);
	        
	              
	        
	        SceneManager.getCon().close();
	           
	        return true;
	    } catch (Exception e) {
       	SceneManager.showAlert("Error",e.getMessage());
	    }
	    return false;
	}

	



}
