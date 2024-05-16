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

public class DeweyClassificationManagement extends StackPane {
	
	private ArrayList<DeweyClassification> data;
    private ObservableList<DeweyClassification> dataList;

	public DeweyClassificationManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("Dewey Classification");
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
		
	    TableView<DeweyClassification> myDataTable = new TableView<DeweyClassification>();
 
        myDataTable.setEditable(true);
        myDataTable.setMinHeight(270);
        myDataTable.setMaxWidth(752);
 
    
        // name of column for display
        TableColumn<DeweyClassification, Integer> classificationNumberCol = 
        		new TableColumn<DeweyClassification,Integer>("classificationNumber");
        classificationNumberCol.setMinWidth(200);
        classificationNumberCol.setCellValueFactory(new PropertyValueFactory<DeweyClassification, Integer>("classificationNumber"));
        
        
         
        TableColumn<DeweyClassification, String> descriptionCol = new TableColumn<DeweyClassification, String>("description");
        descriptionCol.setMinWidth(550);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<DeweyClassification, String>("description"));
        descriptionCol.setCellFactory(TextFieldTableCell.<DeweyClassification>forTableColumn());

        descriptionCol.setOnEditCommit(
        		(CellEditEvent<DeweyClassification, String> t) -> {
                       ((DeweyClassification) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setDescription(t.getNewValue()); //display only
                 updateDescription( t.getRowValue().getClassificationNumber(),t.getNewValue());
        });
     
        myDataTable.setItems(dataList);
        myDataTable.getStyleClass().add("table-view");
        myDataTable.getColumns().addAll(classificationNumberCol, descriptionCol);

        myDataTable.setRowFactory(tv -> {
            TableRow<DeweyClassification> row = new TableRow<>();
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
        columnComboBox.getItems().addAll("Classification Number","Description");
        columnComboBox.setValue("Classification Number"); // Set default value
        columnComboBox.getStyleClass().add("custom-ComboBox");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search here...");
        searchField.getStyleClass().add("custom-textfield-subscene"); 
        searchField.setPrefHeight(30);
        searchField.setPrefWidth(200);
        FilteredList<DeweyClassification> filteredData = new FilteredList<>(dataList, p -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedColumn = columnComboBox.getValue();

                switch (selectedColumn) {
                    case "Classification Number":
                        return String.valueOf(item.getClassificationNumber()).contains(lowerCaseFilter);
                    case "Description":
                        return item.getDescription().toLowerCase().contains(lowerCaseFilter);
                    default:
                        return false;
                }
            });
        });

        SortedList<DeweyClassification> sortedData = new SortedList<>(filteredData);
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
        	ObservableList<DeweyClassification> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
        	ArrayList<DeweyClassification> rows = new ArrayList<>(selectedRows);

        	Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Confirmation");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Are you sure you want to delete this Dewey Classification?");

            // Customize the buttons (OK for delete, CANCEL for cancel)
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel");
            confirmation.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

            if (result == buttonTypeOK) {
            	rows.forEach(row -> {
            		try {
            			DeweyClassification selectedItem = myDataTable.getSelectionModel().getSelectedItem();
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

        Label addNewBookLabel = new Label("      Add new Classification:");
        addNewBookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        addNewBookLabel.setStyle("-fx-text-fill: #000000;");
        gridPane.add(addNewBookLabel, 0, 0); // Assuming 6 prompts

        String[] prompts = {"Classification Number", "Description"};
        TextField[] textFields = new TextField[prompts.length];

        for (int i = 0; i < prompts.length; i++) {
            TextField textField = new TextField();
            textField.setPromptText(prompts[i]);
            textField.getStyleClass().add("custom-textfield-subscene"); 
            textField.setMinHeight(35);
            textFields[i] = textField;
            gridPane.add(textField, i+1, 0);
        }
        textFields[0].setMinWidth(130);
        textFields[1].setMinWidth(360);

        Button addButton = new Button("Add");
        addButton.getStyleClass().add("custom-button-subscene");
        addButton.setOnAction(e -> {
        	try {
        		DeweyClassification rc = new DeweyClassification(
                        Integer.parseInt(textFields[0].getText()),
                        textFields[1].getText());

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
        gridPane.add(addButton, 0, 1,6, 1);
        
        
        final VBox vbox = new VBox(35);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(searchBox,myDataTable,hb1,gridPane);
        
        return vbox;     
	}
	

	private boolean insertData(DeweyClassification rc) {
	    try {

	        System.out.println("Insert into DeweyClassification (ClassificationNumber,Description) values(" +
	                rc.getClassificationNumber() +
	                ",'" + rc.getDescription() +"')");

	        SceneManager.connectDB();
	        SceneManager.ExecuteStatement("Insert into DeweyClassification (ClassificationNumber,Description) values(" +
	                rc.getClassificationNumber() +
	                ",'" + rc.getDescription() +"')");
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

		SQL = "SELECT ClassificationNumber,Description FROM DeweyClassification ORDER BY ClassificationNumber";
		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);


		while (rs.next()) 
			data.add(new DeweyClassification(Integer.parseInt(rs.getString(1)),rs.getString(2)));
		
		rs.close();
		stmt.close();

		SceneManager.getCon().close();
		System.out.println("Connection closed" + data.size());
	
	}

	
	public void updateDescription(int classificationNumber, String description) {
		
		try {
			System.out.println("update DeweyClassification set Description = '"+description + "' where ClassificationNumber = "+classificationNumber);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update Book set Description = '"+description + "' where ClassificationNumber = "+classificationNumber+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			}
	}

	
	private void deleteRow(DeweyClassification row) {
		
		try {
			System.out.println("delete from DeweyClassification where ClassificationNumber="+row.getClassificationNumber() + ";");
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("delete from DeweyClassification where ClassificationNumber="+row.getClassificationNumber() + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
			SceneManager.showAlert("Error",e.getMessage());
		}
	}
	

}


