package application;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import collection.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;

public class ReturnBookManagement extends StackPane {
	
	VBox mainVBox;
	HBox Control1HBox,Control2HBox;
	Label patronIDLabel,patronNameLabel,warrningLabel;
	TextField patronIDTextField,patronNameTextField;
	LocalDate currentDate;
	DateTimeFormatter formatter;
	ArrayList<BorrowingTransaction> data;
	
	public  ReturnBookManagement() {
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		StackPane layout =new StackPane();
		layout.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth()-220);
		layout.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
		layout.getStyleClass().add("background");

        Label titelLabel = new Label("Return Book");
	    titelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    titelLabel.setStyle("-fx-text-fill: #000000;");
             
        
        setupControl1HBox();
        setupControl2HBox();
        mainVBox = new VBox(40);
        mainVBox.setAlignment(Pos.TOP_CENTER);
        
        VBox mainVBox1 = new VBox(40);
        mainVBox1.setPadding(new Insets(50));
        mainVBox1.setAlignment(Pos.TOP_CENTER);
        mainVBox1.getChildren().addAll(titelLabel,Control1HBox,Control2HBox,mainVBox,warrningLabel);
        
        currentDate = LocalDate.now();
	    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        
        layout.getChildren().addAll(mainVBox1);
        
        scrollPane.setContent(layout);
        this.getChildren().addAll(scrollPane);
	}
	
	public void setupControl1HBox() {
		try {
			Control1HBox = new HBox(30);			
			patronIDLabel = new Label("Patron: ");
			patronIDLabel.getStyleClass().add("label-style-subscene");
			patronIDTextField = new TextField();
			patronIDTextField.setPromptText("Enter ID");
			patronNameLabel = new Label("Patron Name:");
			patronNameLabel.getStyleClass().add("label-style-subscene");
			patronNameTextField = new TextField();
			patronNameTextField.setPromptText("Name");
			patronNameTextField.setEditable(false);
			patronIDTextField.getStyleClass().add("custom-textfield-subscene"); 
			patronNameTextField.getStyleClass().add("custom-textfield-subscene"); 
			warrningLabel=new Label();
			warrningLabel.setStyle("-fx-text-fill: red;");
			
			patronIDTextField.setOnKeyPressed(e -> {
				try {
					if (e.getCode() == KeyCode.ENTER) {
						warrningLabel.setText("");
						mainVBox.getChildren().clear();
						patronNameTextField.setText(findNameOfPatron(Integer.parseInt(patronIDTextField.getText())));
						if (findNameOfPatron(Integer.parseInt(patronIDTextField.getText()))==null)
							throw new Exception("There is no patron has this ID");
						
						data = new ArrayList<BorrowingTransaction>();
						getData(Integer.parseInt(patronIDTextField.getText()));
						
						if (data.size()==0) {
							Control2HBox.setVisible(false);
							warrningLabel.setText("No Borrowing Transaction for this user");
						}if (data.size()>1) {
							Control2HBox.setVisible(true);
							setupHBox();
						}else {
							Control2HBox.setVisible(false);
							setupHBox();
						}
					}
				}catch (Exception e2) {
					patronNameTextField.setText(null);
					SceneManager.showAlert("Error",e2.getMessage());
				}					
			});
				
	

	        Control1HBox.getChildren().addAll(patronIDLabel, patronIDTextField,patronNameLabel, patronNameTextField);
	        Control1HBox.setPadding(new Insets(20));
	        Control1HBox.setAlignment(Pos.CENTER);
		} catch (Exception e) {
			SceneManager.showAlert("Error",e.getMessage());
		}		
	}
	
	public void setupHBox() {
		mainVBox.getChildren().clear();
	    try {
	        for (BorrowingTransaction borrowing : data) {
	            HBox hbox = new HBox(20);
	            hbox.getStyleClass().add("custom-hbox-subscene");
	            hbox.setMaxWidth(820);
	            hbox.setPadding(new Insets(20));

	            Button returnButton = new Button("Return Book");
	            returnButton.getStyleClass().add("return-button-subscene");
	            Button undoButton = new Button("Undo Return");
	            undoButton.getStyleClass().add("return-button-subscene");

	            GridPane gridPane = new GridPane();
	            gridPane.setHgap(20);
	            gridPane.setVgap(20);
	            
	            returnButton.setOnAction(e->{
	            	try {
	            		if (!borrowing.isPaymentStatus()) {
	            			SceneManager.connectDB();
		        		    SceneManager.ExecuteStatement("UPDATE BorrowingTransaction SET ReturnDate = '" + currentDate.format(formatter) + "', PaymentStatus = true WHERE BorrowingID = " + borrowing.getBorrowingID());
		        		    SceneManager.connectDB();
		            		SceneManager.ExecuteStatement("UPDATE Book SET reservation_count = reservation_count - 1 WHERE BookID = " +borrowing.getBookID());
		            		borrowing.setReturnDate(currentDate.format(formatter));
		            		borrowing.setPaymentStatus(true);
		            		setupHBox();
	            		}
					} catch (Exception e2) {
						SceneManager.showAlert("Error",e2.getMessage());
					}
	            });
	            
	            undoButton.setOnAction(e->{
	            	try {
	        		    SceneManager.connectDB();
	        		    SceneManager.ExecuteStatement("UPDATE BorrowingTransaction SET ReturnDate = NULL, PaymentStatus = false WHERE BorrowingID = " + borrowing.getBorrowingID());
	        		    SceneManager.connectDB();
	            		SceneManager.ExecuteStatement("UPDATE Book SET reservation_count = reservation_count + 1 WHERE BookID = " +borrowing.getBookID());
	            		borrowing.setReturnDate(null);
	            		borrowing.setPaymentStatus(false);
	            		setupHBox();
					} catch (Exception e2) {
						SceneManager.showAlert("Error",e2.getMessage());
					}
	            });

	            gridPane.add(new Label("Borrowing ID: " + String.valueOf(borrowing.getBorrowingID())), 0, 0);
	            gridPane.add(new Label("Book title: " + borrowing.getTitle()), 0, 1);
	            gridPane.add(new Label("Book ID: " + String.valueOf(borrowing.getBookID())), 1, 1);
	            gridPane.add(new Label("Borrow Date: " + borrowing.getBorrowDate()), 0, 2);
	            gridPane.add(new Label("Due Date: " + borrowing.getDueDate()), 1, 2);
	            gridPane.add(new Label("Price Per Day: " + String.valueOf(borrowing.getPricePerDay())), 0, 3);
	            gridPane.add(new Label("Total Price: " + String.valueOf(borrowing.calculateFinancialPrice())), 1, 3);
	            gridPane.add(new Label("Total Fine: " + String.format("%.2f", borrowing.calculateFinancialPenalty())), 2, 3);
	            gridPane.add(new Label("Librarian ID: " + String.valueOf(borrowing.getLibrarianID())), 0, 4);
	            gridPane.add(new Label("Librarian Name: " + String.valueOf(borrowing.getLibrarianName())), 1, 4);

	            // Apply style to the labels
	            for (Node node : gridPane.getChildren()) {
	                if (node instanceof Label) {
	                    ((Label) node).getStyleClass().add("label-style-subscene");
	                    // Add any other styling properties as needed
	                }
	            }
	            
	            gridPane.add(returnButton, 2, 0);
	            if(borrowing.isPaymentStatus())
	            	gridPane.add(undoButton, 3, 0);

	            hbox.getChildren().addAll(gridPane);
	            hbox.setAlignment(Pos.CENTER);
	            mainVBox.getChildren().add(hbox);
	        }
	    } catch (Exception e) {
	        SceneManager.showAlert("Error", e.getMessage());
	    }
	}
	
	public void setupControl2HBox() {
		try {
			Control2HBox = new HBox(30);			
			Button retuenAllButton = new Button("Retuen All Book"); 
			retuenAllButton.getStyleClass().add("custom-button-subscene");
			retuenAllButton.setMinWidth(200);
			
			retuenAllButton.setOnAction(e -> {
				try {
					for (BorrowingTransaction borrowing : data) {
						if (!borrowing.isPaymentStatus()) {
							SceneManager.connectDB();
		        		    SceneManager.ExecuteStatement("UPDATE BorrowingTransaction SET ReturnDate = '" + currentDate.format(formatter) + "', PaymentStatus = true WHERE BorrowingID = " + borrowing.getBorrowingID());
		        		    SceneManager.connectDB();
		            		SceneManager.ExecuteStatement("UPDATE Book SET reservation_count = reservation_count - 1 WHERE BookID = " +borrowing.getBookID());
		            		borrowing.setReturnDate(currentDate.format(formatter));
		            		borrowing.setPaymentStatus(true);
						}
					}
					setupHBox();
				}catch (Exception e2) {
					patronNameTextField.setText(null);
					SceneManager.showAlert("Error",e2.getMessage());
				}					
			});
			Control2HBox.getChildren().addAll(retuenAllButton);
			Control2HBox.setVisible(false);
			Control2HBox.setAlignment(Pos.CENTER);
		} catch (Exception e) {
			SceneManager.showAlert("Error",e.getMessage());
		}		
	}

	private String findNameOfPatron(int patronID) throws SQLException, ClassNotFoundException {
		
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT Name FROM Patron Where PatronID="+ patronID);
		    
		if (rs.next()) {
			return rs.getString(1);
		}
		    
		return null;
	}
	
	private void getData(int PatronID) throws SQLException, ClassNotFoundException {		
		String SQL;			
		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "SELECT B.BorrowingID, B.BorrowDate, B.DueDate, B.ReturnDate, B.PaymentStatus, K.PricePerDay, B.BookID, B.PatronID, B.LibrarianID, K.Title, P.Name,L.Name "
			    + "FROM BorrowingTransaction B, Book K, Patron P ,Librarian L "
			    + "WHERE B.PatronID = " + PatronID + " AND B.LibrarianID = L.LibrarianID AND B.BookID = K.BookID AND B.PatronID = P.PatronID AND (B.PaymentStatus = false or B.ReturnDate='"+currentDate.format(formatter)+"')";

		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next()){
			data.add(new BorrowingTransaction(
					Integer.parseInt(rs.getString(1)),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getBoolean(5),
					Double.parseDouble(rs.getString(6)),
					Integer.parseInt(rs.getString(7)),
					Integer.parseInt(rs.getString(8)),
					Integer.parseInt(rs.getString(9)),
					rs.getString(10),
					rs.getString(11),
					rs.getString(12)));
			
		}
			
		
		rs.close();
		stmt.close();

		SceneManager.getCon().close();
		System.out.println("Connection closed" + data.size());
	}
	
}
