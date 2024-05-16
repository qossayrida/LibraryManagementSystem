
package application;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import collection.BorrowingTransaction;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.util.*;

public class NewBorrowManagement extends StackPane {
	
	 GridPane gridPane ;
	 VBox main_Vbox;
	 HBox dataHBox;
	 Label borrowNumberLabel,patronIDLabel,patronNameLabel,bookIDLabel,bookNameLabel,dueDateLabel,borrowDateLabel,finePerDayLabel,priceLabel;
	 TextField borrowNumberTextField,patronIDTextField,patronNameTextField,
	 bookIDTextField,bookNameTextField,borrowDateTextField,finePerDayTextField,priceTextField;
	 StackPane numberOfBorrow_cont;
	 Button addButton;
	 DatePicker dueDatePicker;
	 DateTimeFormatter formatter;

	public  NewBorrowManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("New Borrow Management");
	    titelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    titelLabel.setStyle("-fx-text-fill: #000000;");
             
        
        setupGridPane();
        
        VBox main_Vbox = new VBox(70);
        main_Vbox.setPadding(new Insets(60));
        main_Vbox.setAlignment(Pos.TOP_CENTER);
        main_Vbox.setPrefWidth(700);
        main_Vbox.getChildren().addAll(titelLabel,numberOfBorrow_cont,dataHBox,addButton);
        
        this.getChildren().addAll(main_Vbox);
        this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}

	public void setupGridPane() {
		try {
			HBox numberOfBorrow_vb = new HBox(20);
			borrowNumberLabel = new Label("Borrow Number: ");
			borrowNumberTextField = new TextField(findNumberOfBorrow()+1+"");
			borrowNumberTextField.setEditable(false);
			borrowNumberTextField.getStyleClass().add("custom-textfield-subscene"); 
			numberOfBorrow_vb.getChildren().addAll(borrowNumberLabel,borrowNumberTextField);
			
			VBox patron_vb = new VBox(20);
	        ImageView patronImageView = new ImageView(new Image(getClass().getResourceAsStream("/patronicon.png")));
	        patronImageView.setFitWidth(40);
	        patronImageView.setFitHeight(40);
			patronIDLabel = new Label("PatronID: ",new TextField());
			((TextField)patronIDLabel.getGraphic()).setPromptText("Enter ID");
			patronIDLabel.setContentDisplay(ContentDisplay.RIGHT);
			
			
			
			patronNameLabel = new Label("Patron Name: ",new TextField());
			((TextField)patronNameLabel.getGraphic()).setPromptText("Name");
			patronNameLabel.setContentDisplay(ContentDisplay.RIGHT);
			((TextField)patronNameLabel.getGraphic()).setEditable(false);
			((TextField)patronNameLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene"); 
			patron_vb.getChildren().addAll(patronImageView,patronIDLabel,patronNameLabel);
			((TextField)patronIDLabel.getGraphic()).setOnKeyPressed(e -> {
				try {
					if (e.getCode() == KeyCode.ENTER) {
						((TextField)patronNameLabel.getGraphic()).setText(findNameOfPatron(Integer.parseInt(((TextField)patronIDLabel.getGraphic()).getText())));
						if (findNameOfPatron(Integer.parseInt(((TextField)patronIDLabel.getGraphic()).getText()))==null)
							throw new Exception("There is no patron has this ID");
					}
				} catch (Exception e2) {
					((TextField)patronNameLabel.getGraphic()).setText(null);
					SceneManager.showAlert("Error",e2.getMessage());
				}					
			});
			VBox book_vb = new VBox(20);
	        ImageView bookImageView = new ImageView(new Image(getClass().getResourceAsStream("/bookicon.png")));
	        bookImageView.setFitWidth(40);
	        bookImageView.setFitHeight(40);
			bookIDLabel = new Label("BookID: ",new TextField());
			((TextField)bookIDLabel.getGraphic()).setPromptText("Enter ID");
			bookIDLabel.setContentDisplay(ContentDisplay.RIGHT);
			
			bookNameLabel = new Label("BookName: ",new TextField());
			bookNameLabel.setContentDisplay(ContentDisplay.RIGHT);
			((TextField)bookNameLabel.getGraphic()).setPromptText("Title");
			((TextField)bookNameLabel.getGraphic()).setEditable(false);	
			((TextField)bookNameLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene"); 
			((TextField)bookNameLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene");
			book_vb.getChildren().addAll(bookImageView,bookIDLabel,bookNameLabel);
			
			VBox price_vb = new VBox(20);
	        ImageView priceImageView = new ImageView(new Image(getClass().getResourceAsStream("/dollar.png")));
	        priceImageView.setFitWidth(40);
	        priceImageView.setFitHeight(40);
			priceLabel = new Label("Price Per Day: ",new TextField("0"));
			((TextField)priceLabel.getGraphic()).setPromptText("Enter Price Per Day");
			priceLabel.setContentDisplay(ContentDisplay.RIGHT);
			finePerDayLabel = new Label("Fine per day: ",new TextField());
			((TextField)finePerDayLabel.getGraphic()).setPromptText("Fine per day");
			finePerDayLabel.setContentDisplay(ContentDisplay.RIGHT);
			((TextField)priceLabel.getGraphic()).setEditable(false);
			((TextField)finePerDayLabel.getGraphic()).setEditable(false);
			((TextField)priceLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene"); 
			((TextField)finePerDayLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene");
			price_vb.getChildren().addAll(priceImageView,priceLabel,finePerDayLabel);
			
			
			((TextField)bookIDLabel.getGraphic()).setOnKeyPressed(e -> {
				try {
					if (e.getCode() == KeyCode.ENTER) {
						((TextField)bookNameLabel.getGraphic()).setText(findTitleOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText())));
						double p=findpriceOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText()));
						((TextField)priceLabel.getGraphic()).setText(""+p);
						((TextField)finePerDayLabel.getGraphic()).setText(""+(p/10.0));
						
						if (findTitleOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText()))==null)
							throw new Exception("There is no book has this ID");
						if(findCopiesOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText()))==findreservationcountOfBook(Integer.parseInt(((TextField) bookIDLabel.getGraphic()).getText())))
							throw new Exception("There are no copies of this book left");
					}		
				} catch (Exception e2) {
					((TextField)bookNameLabel.getGraphic()).setText(null);
					SceneManager.showAlert("Error",e2.getMessage());
				}					
			});
			VBox date_vb = new VBox(20);
	        ImageView dateImageView = new ImageView(new Image(getClass().getResourceAsStream("/calendar.png")));;
	        dateImageView.setFitWidth(40);
	        dateImageView.setFitHeight(40);
			LocalDate currentDate = LocalDate.now();
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			borrowDateLabel = new Label("Borrow Date: ",new TextField(currentDate.format(formatter)));
			((TextField)borrowDateLabel.getGraphic()).setEditable(false);
			borrowDateLabel.setContentDisplay(ContentDisplay.RIGHT);
			dueDateLabel = new Label("Due Date: ",new DatePicker());
			dueDateLabel.setContentDisplay(ContentDisplay.RIGHT);
			((DatePicker)dueDateLabel.getGraphic()).setEditable(false);
			((DatePicker)dueDateLabel.getGraphic()).setConverter(new StringConverter<LocalDate>() {
	            @Override
	            public String toString(LocalDate object) {
	                if (object != null) {
	                    return formatter.format(object);
	                } else {
	                    return "";
	                }
	            }
	            @Override
	            public LocalDate fromString(String string) {
	                if (string != null && !string.isEmpty()) {
	                    return LocalDate.parse(string, formatter);
	                } else {
	                    return null;
	                }
	            }
	        });
			((DatePicker)dueDateLabel.getGraphic()).setOnAction(event -> {
				LocalDate selectedDueDate = ((DatePicker)dueDateLabel.getGraphic()).getValue();
			    if (selectedDueDate != null && selectedDueDate.isBefore(currentDate)) {
			   	 	SceneManager.showAlert("Error","Due Date cannot be earlier than the current date");
			   	 ((DatePicker)dueDateLabel.getGraphic()).setValue(null);
			    }
			});	
			((DatePicker)dueDateLabel.getGraphic()).getStyleClass().add("custom-date-picker"); 
			((TextField)borrowDateLabel.getGraphic()).getStyleClass().add("custom-textfield-subscene"); 	
			date_vb.getChildren().addAll(dateImageView,borrowDateLabel,dueDateLabel);
		 
			 
			
			addButton=new Button("Add");
			addButton.getStyleClass().add("custom-button-subscene");
			
			addButton.setOnAction(e -> {
	        	try {
	        		if(findCopiesOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText()))==findreservationcountOfBook(Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText())))
						throw new Exception("There are no copies of this book left");
	        		LocalDate selectedDate = ((DatePicker)dueDateLabel.getGraphic()).getValue();
	        		BorrowingTransaction rc = new BorrowingTransaction(
	                        Integer.parseInt((borrowNumberTextField.getText())),
	                        	    currentDate.format(formatter),
	                        		selectedDate.format(formatter),
	                        		null,
	                        		false,
	                        		0,
	                        		Integer.parseInt(((TextField)bookIDLabel.getGraphic()).getText()),
	                        		Integer.parseInt(((TextField)patronIDLabel.getGraphic()).getText()),
	                        		SceneManager.getCuurent_user(),
	                        		null,
	                        		null, 
	                        		null);

	        		
	        		
	        		if(insertData(rc)) {
	        			// Replace this query with the actual query needed for your database	
		        		borrowNumberTextField.setText(findNumberOfBorrow()+1+"");
		        		((TextField)bookIDLabel.getGraphic()).setText(null);
		        		((TextField)bookNameLabel.getGraphic()).setText(null);
		        		((TextField)patronIDLabel.getGraphic()).setText(null);
		        		((TextField)patronNameLabel.getGraphic()).setText(null);
		        		((DatePicker)dueDateLabel.getGraphic()).setValue(null);
		        		SceneManager.connectDB();
		        		SceneManager.ExecuteStatement("UPDATE Book SET reservation_count = reservation_count + 1 WHERE BookID = " +rc.getBookID());
	        		}
				} catch (Exception e2) {
					SceneManager.showAlert("Error",e2.getMessage());
				}
	        });

			numberOfBorrow_cont = createContainerForHBox(numberOfBorrow_vb);
			StackPane patron_cont = createContainer(patron_vb);
			StackPane book_cont = createContainer(book_vb);
			StackPane price_cont = createContainer(price_vb);
			StackPane date_cont = createContainer(date_vb);
			
			dataHBox = new HBox(20);
			dataHBox.getChildren().addAll(patron_cont,book_cont,price_cont,date_cont);
			dataHBox.setAlignment(Pos.CENTER);
			
		} catch (Exception e) {
			SceneManager.showAlert("Error",e.getMessage());
		}		
	}
	
	private StackPane createContainer(VBox vb) {
        StackPane infoBox = new StackPane();
        infoBox.setStyle("-fx-background-color: transparent;");    
        // Add a rounded rectangle with a border to the info box
        Rectangle roundedRect = new Rectangle(260, 260);
        roundedRect.setArcWidth(20);
        roundedRect.setArcHeight(20);
        roundedRect.setFill(Color.web("#add8e6"));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        roundedRect.setEffect(dropShadow);
        // Load and set the logo image
        vb.setAlignment(Pos.CENTER);
        // Create an HBox to contain the label and logo
        // Add the contentBox to the infoBox
        infoBox.getChildren().addAll(roundedRect, vb);
        return infoBox;
    }
	
	private StackPane createContainerForHBox(HBox hb) {
        StackPane infoBox = new StackPane();
        infoBox.setStyle("-fx-background-color: transparent;");    
        // Add a rounded rectangle with a border to the info box
        Rectangle roundedRect = new Rectangle(280, 100);
        roundedRect.setArcWidth(20);
        roundedRect.setArcHeight(20);
        roundedRect.setFill(Color.web("#add8e6"));
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.GRAY);
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        roundedRect.setEffect(dropShadow);
        // Load and set the logo image
        hb.setAlignment(Pos.CENTER);
        // Create an HBox to contain the label and logo
        // Add the contentBox to the infoBox
        infoBox.getChildren().addAll(roundedRect, hb);
        return infoBox;
    }
	
	private int findNumberOfBorrow() throws SQLException, ClassNotFoundException {
	    SceneManager.connectDB();
	    // Replace this query with the actual query needed for your database
	    ResultSet rs = SceneManager.ExecuteStatement("SELECT COUNT(*) FROM BorrowingTransaction" );
	    
	    if (rs.next()) {
	        return rs.getInt(1);
	    }
	    return -1;
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
	
	private String findTitleOfBook(int bookID) throws SQLException, ClassNotFoundException {
		
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT Title FROM Book Where BookID="+ bookID);
		    
		if (rs.next()) {
			return rs.getString(1);
		}
		    
		return null;
	}
	
	private double findpriceOfBook(int bookID) throws SQLException, ClassNotFoundException {
		
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT PricePerDay FROM Book Where BookID="+ bookID);
		    
		if (rs.next()) {
			return Double.parseDouble(rs.getString(1));
		} 
		    
		return 0;
	}
	
	private int findCopiesOfBook(int bookID) throws SQLException, ClassNotFoundException {
		
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT Copies FROM Book Where BookID="+ bookID);
		    
		if (rs.next()) {
			return rs.getInt(1);
		}
		    
		return 0;
	}
	
	private int findreservationcountOfBook(int bookID) throws SQLException, ClassNotFoundException {
		
		SceneManager.connectDB();
		    // Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT reservation_count FROM Book Where BookID="+ bookID);
		    
		if (rs.next()) {
			return rs.getInt(1);
		}
		    
		return 0;
	}
	
	private boolean insertData(BorrowingTransaction rc) {
	    try {
	    	
	    	System.out.println(rc.getLibrarianID());
	    	
	        SceneManager.connectDB();
	        SceneManager.ExecuteStatement("Insert into BorrowingTransaction (BorrowingID, BorrowDate, DueDate,PaymentStatus,BookID, PatronID, LibrarianID) values(" +
	                rc.getBorrowingID() +
	                ",'" + rc.getBorrowDate() + "'" +
	                ",'" + rc.getDueDate() +
	                "'," + rc.isPaymentStatus() +
	                "," + rc.getBookID()+"," 
	                + rc.getPatronID()+"," 
	                +rc.getLibrarianID()+ ")");
	        
	        SceneManager.getCon().close();
	           
	        return true;
	    } catch (Exception e) {
        	SceneManager.showAlert("Error",e.getMessage());
	    }
	    return false;
	}
	
}


