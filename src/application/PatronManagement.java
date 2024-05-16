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

public class PatronManagement extends StackPane {
	
	private ArrayList<Patron> data;
    private ObservableList<Patron> dataList;

	public PatronManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("Patron Management");
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
		
	    TableView<Patron> myDataTable = new TableView<Patron>();
 
        myDataTable.setEditable(true);
        myDataTable.setMinHeight(270);
        myDataTable.setMaxWidth(752);
 
    
        // name of column for display
        TableColumn<Patron, Integer> patronIDCol = 
        		new TableColumn<Patron, Integer>("PatronID");
        patronIDCol.setMinWidth(90);
        patronIDCol.setCellValueFactory(new PropertyValueFactory<Patron, Integer>("PatronID"));
        
        
         
        TableColumn<Patron, String> phoneCol = new TableColumn<Patron, String>("Phone");
        phoneCol.setMinWidth(150);
        phoneCol.setCellValueFactory(new PropertyValueFactory<Patron, String>("Phone"));
        phoneCol.setCellFactory(TextFieldTableCell.<Patron>forTableColumn());


        phoneCol.setOnEditCommit(
        		(CellEditEvent<Patron, String> t) -> {
                       ((Patron) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setPhone(t.getNewValue()); //display only
                 updatePhone( t.getRowValue().getPatronID(),t.getNewValue());
        		});

        
        
        
        TableColumn<Patron, String> NameCol = new TableColumn<Patron, String>("Name");
        NameCol.setMinWidth(150);
        NameCol.setCellValueFactory(new PropertyValueFactory<Patron, String>("Name"));       
        NameCol.setCellFactory(TextFieldTableCell.<Patron>forTableColumn());
       
        NameCol.setOnEditCommit(        
        		(CellEditEvent<Patron, String> t) -> {
                       ((Patron) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setName(t.getNewValue());
                  updateName( t.getRowValue().getPatronID(),t.getNewValue());
        		});
        
        
        TableColumn<Patron, String> addCol = new TableColumn<>("Address");
        addCol.setMinWidth(180);
        addCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        addCol.setId("h3");
        addCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addCol.setOnEditCommit(
            (CellEditEvent<Patron, String> t) -> {
                ((Patron) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue());
                updateAdd(t.getRowValue().getPatronID(), t.getNewValue());
            }
        );  
       

        
        TableColumn<Patron, String> emailCol = new TableColumn<>("Email");
        emailCol.setMinWidth(180);
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));
        emailCol.setId("h4");
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setOnEditCommit(
            (CellEditEvent<Patron, String> t) -> {
                ((Patron) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue());
                updateEmail(t.getRowValue().getPatronID(), t.getNewValue());
            }
       
        ); 
   
        myDataTable.setItems(dataList);
        myDataTable.getStyleClass().add("table-view");
        myDataTable.getColumns().addAll(patronIDCol, NameCol,emailCol,phoneCol, addCol);

        myDataTable.setRowFactory(tv -> {
            TableRow<Patron> row = new TableRow<>();
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
        columnComboBox.getItems().addAll("PatronID", "Name", "Email", "Phone", "Address");
        columnComboBox.setValue("Name"); // Set default value
        columnComboBox.getStyleClass().add("custom-ComboBox");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search here...");
        searchField.getStyleClass().add("custom-textfield-subscene"); 
        searchField.setPrefHeight(30);
        searchField.setPrefWidth(200);
        FilteredList<Patron> filteredData = new FilteredList<>(dataList, p -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(patron -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedColumn = columnComboBox.getValue();

                switch (selectedColumn) {
                    case "PatronID":
                        return String.valueOf(patron.getPatronID()).contains(lowerCaseFilter);
                    case "Name":
                        return patron.getName().toLowerCase().contains(lowerCaseFilter);
                    case "Phone":
                        return String.valueOf(patron.getPhone()).contains(lowerCaseFilter);
                    case "Address":
                        return String.valueOf(patron.getAddress()).contains(lowerCaseFilter);
                    case "Email":
                        return patron.getEmail().toLowerCase().contains(lowerCaseFilter);

                    default:
                        return false;
                }
            });
        });

        SortedList<Patron> sortedData = new SortedList<>(filteredData);
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
        	ObservableList<Patron> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
        	ArrayList<Patron> rows = new ArrayList<>(selectedRows);

        	Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Confirmation");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this patron?");

            // Customize the buttons (OK for delete, CANCEL for cancel)
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            confirmation.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

            if (result == buttonTypeOK) {
            	rows.forEach(row -> {
            		try {
            			Patron selectedItem = myDataTable.getSelectionModel().getSelectedItem();		
                		deleteRow(row); 
                		if (selectedItem != null) {
            			    dataList.remove(selectedItem);
            			}
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

        Label addNewPatronLabel = new Label("Add new Patron:");
        addNewPatronLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addNewPatronLabel.setStyle("-fx-text-fill: #000000;");
        gridPane.add(addNewPatronLabel, 0, 0, 5, 1); // Assuming 6 prompts

        String[] prompts = {"Name", "Email", "Phone", "Address"};
        TextField[] textFields = new TextField[prompts.length];
        for (int i = 0; i < prompts.length; i++) {
            TextField textField = new TextField();
            textField.setPromptText(prompts[i]);
            textField.getStyleClass().add("custom-textfield-subscene"); 
            textField.setMaxWidth(125);
            textField.setMinHeight(30);
            textFields[i] = textField;
            gridPane.add(textField, i, 1);
        }
        

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("custom-button-subscene");
        addButton.setOnAction(e -> {
        	try {
        		for (TextField textField : textFields) {
                    if(textField.getText()=="")
                    	throw new Exception("Some field is null");
                }
        		Patron rc = new Patron(
                        0,
                        textFields[0].getText(),
                        textFields[1].getText(),
                        textFields[2].getText(),
                        textFields[3].getText()
                );

        		if (insertData(rc)) {
        			rc.setPatronID(findMaxIDOfPatron());
        			dataList.add(rc);
        		}
                
                for (TextField textField : textFields) {
                    textField.clear();
                    
                }
			} catch (Exception e2) {
				SceneManager.showAlert("Error",e2.getMessage());
			}
        });
        GridPane.setHalignment(addButton, HPos.CENTER); 
        gridPane.add(addButton, 0, 2,6, 1);
        
        
        final VBox vbox = new VBox(25);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(searchBox,myDataTable,hb1,gridPane);
        
        return vbox;     
	}
	

	private boolean insertData(Patron rc) {
	    try {

	        System.out.println("Insert into Patron (Name,Email,Phone,Address) values(" +
	                "'" + rc.getName() + "'" +
	                "," + rc.getEmail() +
	                "," + rc.getPhone() +
	                ",'"  + rc.getAddress() + "')");

	        SceneManager.connectDB();
	        SceneManager.ExecuteStatement("Insert into Patron (Name,Email,Phone,Address) values(" +
	                "'" + rc.getName() + "'" +
	                ",'" + rc.getEmail() +"'" +
	                ",'"+ rc.getPhone() +"'" +
	                ",'" +rc.getAddress() + "')");
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

		SQL = "SELECT PatronID,Name,Email,Phone,Address FROM Patron ORDER BY Name";
		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);


		while ( rs.next() ) 
			data.add(new Patron(
					Integer.parseInt(rs.getString(1)),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4),
					rs.getString(5)));
		
		rs.close();
		stmt.close();

		SceneManager.getCon().close();
		System.out.println("Connection closed" + data.size());
	
	}
	
	public void updateTitle(int BookID, String Title) {
		
		try {
			System.out.println("update  Book set Title = '"+Title + "' where BookID = "+BookID);
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


	
	private void deleteRow(Patron row) throws Exception{
		System.out.println("delete from  patron where PatronID="+row.getPatronID() + ";");
		SceneManager.connectDB();
		SceneManager.ExecuteStatement("delete from  patron where PatronID="+row.getPatronID() + ";");
		SceneManager.getCon().close();
		System.out.println("Connection closed");
	}
	
	
	public void updateName(int pid, String name) {
		
		try {
			System.out.println("update  patron set pname = '"+name + "' where PatronID = "+pid);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  patron set pname = '"+name + "' where PatronID = "+pid+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	public void updatePhone(int pid, String phone) {
		
		try {
			System.out.println("update  patron set phone = '"+phone + "' where PatronID = "+pid);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  patron set phone = '"+phone + "' where PatronID = "+pid+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	public void updateAdd(int pid, String address) {
	
		try {
			System.out.println("update  patron set address = '"+address + "' where PatronID = "+pid);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  patron set address = '"+address + "' where PatronID = "+pid+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
		
		} catch (SQLException e) {
			SceneManager.showAlert("Error",e.getMessage());
		} 
	}
	
	public void updateEmail(int pid, String email) {
	
		try {
			System.out.println("update  patron set email = '"+email + "' where PatronID = "+pid);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  patron set email = '"+email + "' where PatronID = "+pid+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
		
		} catch (SQLException e) {
			SceneManager.showAlert("Error",e.getMessage());
		} 

	}


	private int findMaxIDOfPatron() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT MAX(PatronID) FROM Patron");
		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}
	

}

