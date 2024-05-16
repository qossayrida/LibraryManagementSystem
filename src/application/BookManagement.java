package application;


import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;


import java.sql.*;
import java.util.*;

import collection.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class BookManagement extends StackPane {
	
	private ArrayList<Book> data;
    private ObservableList<Book> dataList;

	public BookManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("Book Management");
	    titelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
	    titelLabel.setStyle("-fx-text-fill: #000000;");
        
        data = new ArrayList<>();
        VBox vBox =null;
        try {

        	getData();

        	dataList = FXCollections.observableArrayList(data);

        	 vBox = tableView();
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
        
        
        
        VBox vBox1 = new VBox(50);
        vBox1.setPadding(new Insets(60));
        vBox1.setAlignment(Pos.TOP_CENTER);
        vBox1.setPrefWidth(700);
        vBox1.getChildren().addAll(titelLabel,vBox);
        
        this.getChildren().addAll(vBox1);
        this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
        this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}
	
	@SuppressWarnings("unchecked")
	private VBox tableView () {
		
		
	    TableView<Book> myDataTable = new TableView<Book>();
 
        myDataTable.setEditable(true);
        myDataTable.setMinHeight(200);
        myDataTable.setMaxWidth(952);

    
        // name of column for display
        TableColumn<Book, Integer> bookIDCol = 
        		new TableColumn<Book, Integer>("BookID");
        bookIDCol.setMinWidth(90);
        bookIDCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("BookID"));
        
        
         
        TableColumn<Book, String> titelCol = new TableColumn<Book, String>("Title");
        titelCol.setMinWidth(150);
        titelCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Title"));
        titelCol.setCellFactory(TextFieldTableCell.<Book>forTableColumn());


        titelCol.setOnEditCommit(
        		(CellEditEvent<Book, String> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setTitle(t.getNewValue()); //display only
                 updateTitle( t.getRowValue().getBookID(),t.getNewValue()); 
        		});

        
        
        
        TableColumn<Book, Integer> classificationNumberCol = new TableColumn<Book, Integer>("ClassificationNumber");
        classificationNumberCol.setMinWidth(150);
        classificationNumberCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("ClassificationNumber"));       
        classificationNumberCol.setCellFactory(TextFieldTableCell.<Book,Integer> forTableColumn(new IntegerStringConverter()));
       
        classificationNumberCol.setOnEditCommit(        
        		(CellEditEvent<Book, Integer> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setClassificationNumber(t.getNewValue());
                  updateClassificationNumber( t.getRowValue().getBookID(),t.getNewValue());
        		});
        
        TableColumn<Book, Double> pricePerDayCol = new TableColumn<Book, Double>("PricePerDay");
        pricePerDayCol.setMinWidth(90);
        pricePerDayCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("PricePerDay"));       
        pricePerDayCol.setCellFactory(TextFieldTableCell.<Book,Double> forTableColumn(new DoubleStringConverter()));
       
        pricePerDayCol.setOnEditCommit(        
        		(CellEditEvent<Book, Double> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setPricePerDay(t.getNewValue());
                  updatePricePerDay( t.getRowValue().getBookID(),t.getNewValue());
        		});
        
        
        TableColumn<Book, Integer> copiesCol = new TableColumn<Book, Integer>("CopiesNumber");
        copiesCol.setMinWidth(90);
        copiesCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("CopiesNumber"));       
        copiesCol.setCellFactory(TextFieldTableCell.<Book,Integer> forTableColumn(new IntegerStringConverter()));
       
        copiesCol.setOnEditCommit(        
        		(CellEditEvent<Book, Integer> t) -> {
        			try {((Book) t.getTableView().getItems().get(
    	                 t.getTablePosition().getRow())
    	                 ).setCopiesNumber(t.getNewValue());
        				updateCopies( t.getRowValue().getBookID(),t.getNewValue());
        			}
                       catch (Exception e) {
                    	   SceneManager.showAlert("Error",e.getMessage());
					}
        });
       

        
        TableColumn<Book, String> authorCol = new TableColumn<Book, String>("Author");
        authorCol.setMinWidth(120);
        authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("Author"));
        authorCol.setCellFactory(TextFieldTableCell.<Book>forTableColumn());
        authorCol.setOnEditCommit(        
        		(CellEditEvent<Book, String> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setAuthor(t.getNewValue());
                 updateAuthor( t.getRowValue().getBookID(),t.getNewValue());
        });

        TableColumn<Book, String> publicationYearCol = new TableColumn<Book, String>("PublicationYear");
        publicationYearCol.setMinWidth(120);
        publicationYearCol.setCellValueFactory(new PropertyValueFactory<Book, String>("PublicationYear"));
        
        
        publicationYearCol.setCellFactory(TextFieldTableCell.<Book>forTableColumn());
        publicationYearCol.setOnEditCommit(        
        		(CellEditEvent<Book, String> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setPublicationYear(t.getNewValue());
                updatePublicationYear( t.getRowValue().getBookID(),t.getNewValue());
        });
        

        TableColumn<Book, Integer> reservation_count = new TableColumn<Book, Integer>("reservation_count");
        reservation_count.setMinWidth(140);
        reservation_count.setCellValueFactory(new PropertyValueFactory<Book, Integer>("reservation_count"));
        reservation_count.setCellFactory(TextFieldTableCell.<Book,Integer> forTableColumn(new IntegerStringConverter()));
        reservation_count.setOnEditCommit(        
        		(CellEditEvent<Book, Integer> t) -> {
                       ((Book) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setReservation_count(t.getNewValue());
                updateReservation_count( t.getRowValue().getBookID(),t.getNewValue());
        });
        
        myDataTable.setItems(dataList);
        myDataTable.getStyleClass().add("table-view");
        myDataTable.getColumns().addAll(bookIDCol, titelCol,classificationNumberCol,reservation_count,copiesCol, authorCol, publicationYearCol,pricePerDayCol);

        myDataTable.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    int index = row.getIndex();
                    if (myDataTable.getSelectionModel().isSelected(index)) {
                        myDataTable.getSelectionModel().clearSelection(index);
                    } else {
                        myDataTable.getSelectionModel().select(index);
                    }
                }
            });
            return row;
        });
        
        ComboBox<String> columnComboBox = new ComboBox<>();
        columnComboBox.getItems().addAll("BookID", "Title", "ClassificationNumber", "CopiesNumber", "Author", "PublicationYear","ReservationCount");
        columnComboBox.setValue("Title"); // Set default value
        columnComboBox.getStyleClass().add("custom-ComboBox");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search here...");
        searchField.getStyleClass().add("custom-textfield-subscene"); 
        searchField.setPrefHeight(30);
        searchField.setPrefWidth(200);
        FilteredList<Book> filteredData = new FilteredList<>(dataList, p -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedColumn = columnComboBox.getValue();

                switch (selectedColumn) {
                    case "BookID":
                        return String.valueOf(book.getBookID()).contains(lowerCaseFilter);
                    case "Title":
                        return book.getTitle().toLowerCase().contains(lowerCaseFilter);
                    case "ClassificationNumber":
                        return String.valueOf(book.getClassificationNumber()).contains(lowerCaseFilter);
                    case "CopiesNumber":
                        return String.valueOf(book.getCopiesNumber()).contains(lowerCaseFilter);
                    case "Author":
                        return book.getAuthor().toLowerCase().contains(lowerCaseFilter);
                    case "PublicationYear":
                        return book.getPublicationYear().toLowerCase().contains(lowerCaseFilter);
                    case "ReservationCount":
                    	 return String.valueOf(book.getReservation_count()).contains(lowerCaseFilter);
                    	
                    default:
                        return false;
                }
            });
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(myDataTable.comparatorProperty());
        myDataTable.setItems(sortedData);
        Label searchLabel = new Label("Search:");
        Label searchByLabel = new Label("Search By:");
        searchLabel.setFont(Font.font("Arial", 20));
        searchLabel.setStyle("-fx-text-fill: #000000;");
        searchByLabel.setFont(Font.font("Arial", 20));
        searchByLabel.setStyle("-fx-text-fill: #000000;");
        final HBox searchBox = new HBox(30); // HBox for search field and combo box
        searchBox.setAlignment(Pos.CENTER);
        searchBox.getChildren().addAll(searchLabel,searchField,searchByLabel,columnComboBox);
        
        
        final Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("custom-button-subscene");
        deleteButton.setOnAction((ActionEvent e) -> {        	 
        	ObservableList<Book> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
        	ArrayList<Book> rows = new ArrayList<>(selectedRows);

        	Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Confirmation");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this book?");

            // Customize the buttons (OK for delete, CANCEL for cancel)
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            confirmation.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

            if (result == buttonTypeOK) {
            	rows.forEach(row -> {
            		try {
            			Book selectedItem = myDataTable.getSelectionModel().getSelectedItem();
            			if (selectedItem != null) {
            			    dataList.remove(selectedItem);
            			}
                		deleteRow(row); 
                		myDataTable.refresh();
					} catch (Exception e2) {
						SceneManager.showAlert("Erorr",e2.getMessage());
					}
            		
            	});  
            }   	 
        });
     
        final Button refreshButton = new Button("Refresh");
        refreshButton.getStyleClass().add("custom-button-subscene");
        refreshButton.setOnAction((ActionEvent e) -> {        	 
        	myDataTable.refresh();  
        });
    
        final HBox hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.setAlignment(Pos.CENTER);	
        hb1.getChildren().addAll(deleteButton,refreshButton);
        

        GridPane gridPane = new GridPane();
        gridPane.setVgap(20);
        gridPane.setHgap(10);
        gridPane.setAlignment(Pos.CENTER);

        Label addNewBookLabel = new Label("Add new book:");
        addNewBookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addNewBookLabel.setStyle("-fx-text-fill: #000000;");
        gridPane.add(addNewBookLabel, 0, 0, 6, 1); // Assuming 6 prompts

        String[] prompts = {"BookID", "Title", "Classification Number", "Copies Number", "Author", "Publication Year","ReservationCount: 0","Price per day"};
        TextField[] textFields = new TextField[prompts.length];
        

        for (int i = 0; i < prompts.length; i++) {
            TextField textField = new TextField();
            textField.setPromptText(prompts[i]);
            textField.getStyleClass().add("custom-textfield-subscene"); 
            textField.setMaxWidth(130);
            textField.setMinHeight(30);
            textFields[i] = textField;
            if(i<4)
            	gridPane.add(textField, i, 1);
            else
            	gridPane.add(textField, i%4, 2);
        }

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("custom-button-subscene");
        addButton.setOnAction(e -> {
        	try {
        		Book rc = new Book(
                        Integer.parseInt(textFields[0].getText()),
                        textFields[1].getText(),
                        Integer.parseInt(textFields[2].getText()),
                        Integer.parseInt(textFields[3].getText()),
                        textFields[4].getText(),
                        textFields[5].getText(),
                        Integer.parseInt(textFields[6].getText()),
                        Double.parseDouble(textFields[7].getText()));


        		if (insertData(rc))
                dataList.add(rc);
                

                for (TextField textField : textFields) {
                    textField.clear();
                }
			} catch (Exception e2) {
				SceneManager.showAlert("Error",e2.getMessage());
			}
        });
        GridPane.setHalignment(addButton, HPos.CENTER); 
        gridPane.add(addButton, 0, 3,4, 1);
        
        
        final VBox vbox = new VBox(25);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(searchBox,myDataTable,hb1,gridPane);
        
        return vbox;     
	}
	

	private boolean insertData(Book rc) {
	    try {

	        System.out.println("Insert into Book (BookID, Title,ClassificationNumber,Copies,Author,PublicationYear) values(" +
	                rc.getBookID() +
	                ",'" + rc.getTitle() + "'" +
	                "," + rc.getClassificationNumber() +
	                "," + rc.getCopiesNumber() +
	                ",'" + rc.getAuthor() + "'" +
	                ",'" + rc.getPublicationYear() + "')");

	        SceneManager.connectDB();
	        SceneManager.ExecuteStatement("Insert into Book (BookID, Title,ClassificationNumber,PricePerDay,Copies,Author,PublicationYear) values(" +
	                rc.getBookID() +
	                ",'" + rc.getTitle() + "'" +
	                "," + rc.getClassificationNumber() +
	                "," + rc.getPricePerDay() +
	                "," + rc.getCopiesNumber() +
	                ",'" + rc.getAuthor() + "'" +
	                ",'" + rc.getPublicationYear() + "')");
	        SceneManager.getCon().close();
	        System.out.println("Connection closed " + data.size());
	        
	        
	        return true;
	    } catch (Exception e) {
        	SceneManager.showAlert("Error",e.getMessage());
	    }
	    return false;
	}
	
	private void getData() throws SQLException, ClassNotFoundException {

		String SQL;
				
		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "SELECT BookID,Title,ClassificationNumber,Copies,Author,PublicationYear,reservation_count,PricePerDay FROM Book ORDER BY Title";
		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);


		while ( rs.next() ) 
			data.add(new Book(
					Integer.parseInt(rs.getString(1)),
					rs.getString(2),
					Integer.parseInt(rs.getString(3)),
					Integer.parseInt(rs.getString(4)),
					rs.getString(5),
					rs.getString(6),
					Integer.parseInt(rs.getString(7)),
					Double.parseDouble(rs.getString(8))));

		
		rs.close();
		stmt.close();

		SceneManager.getCon().close();
		System.out.println("Connection closed" + data.size());
	
	}
	
	
	

	
	public void updateTitle(int BookID, String Title) {
		
		try {
			System.out.println("update Book set Title = '"+Title + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Book set Title = '"+Title + "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updateClassificationNumber(int BookID, int ClassificationNumber) {
		
		try {
			System.out.println("update Book set ClassificationNumber = "+ClassificationNumber + " where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Book set ClassificationNumber = "+ClassificationNumber+ " where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}

	
	public void updateCopies(int BookID, int Copies ) {
		
		try {
			System.out.println("update  Book set Copies = '"+Copies + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update Book set Copies = '"+ Copies + "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updateAuthor(int BookID, String Author) {
		
		try {
			System.out.println("update Book set Author = '"+Author + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update Book set Author = '"+Author + "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			}
	}
	
	public void updatePublicationYear(int BookID, String PublicationYear) {
		
		try {
			System.out.println("update  Book set PublicationYear = '"+PublicationYear + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Book set PublicationYear = '"+PublicationYear+ "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	public void updatePricePerDay(int BookID, double pricePerDay) {
		
		try {
			System.out.println("update  Book set pricePerDay = '"+pricePerDay + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Book set pricePerDay = '"+pricePerDay+ "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	public void updateReservation_count(int BookID, int reservation_count) {
		
		try {
			System.out.println("update  Book set reservation_count = '"+reservation_count + "' where BookID = "+BookID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Book set reservation_count = '"+reservation_count+ "' where BookID = "+BookID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
				e.printStackTrace();
		}
	}
	
	private void deleteRow(Book row) {
		
		try {
			System.out.println("delete from Book where BookID="+row.getBookID() + ";");
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("delete from Book where BookID="+row.getBookID() + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
			SceneManager.showAlert("Error",e.getMessage());
		}
	}
	

}

