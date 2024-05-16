package application;


import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;


import java.sql.*;
import java.util.*;

import collection.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.*;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;

public class  BorrowingTransactionManagement extends StackPane {
	
	private ArrayList<BorrowingTransaction> data;
    private ObservableList<BorrowingTransaction> dataList;
    
    public class TotalPriceColumn extends TableColumn<BorrowingTransaction, Double> {

        public TotalPriceColumn() {
            super("TotalPrice");
            setMinWidth(90);
            setCellValueFactory(param -> {
                BorrowingTransaction borrowingTransaction = param.getValue();
                if (borrowingTransaction != null) {
                    double totalPrice = borrowingTransaction.calculateFinancialPrice();
                    return new SimpleDoubleProperty(totalPrice).asObject();
                } else {
                    return new SimpleDoubleProperty().asObject();
                }
            });

            setCellFactory(column -> {
                TableCell<BorrowingTransaction, Double> cell = new TableCell<BorrowingTransaction, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };

                return cell;
            });
        }
    }
    
    public class TotalFineColumn extends TableColumn<BorrowingTransaction, Double> {

        public TotalFineColumn() {
            super("TotalFine");
            setMinWidth(90);
            setCellValueFactory(param -> {
                BorrowingTransaction borrowingTransaction = param.getValue();
                if (borrowingTransaction != null) {
                    double totalfine = borrowingTransaction.calculateFinancialPenalty();
                    return new SimpleDoubleProperty(totalfine).asObject();
                } else {
                    return new SimpleDoubleProperty().asObject();
                }
            });

            setCellFactory(column -> {
                TableCell<BorrowingTransaction, Double> cell = new TableCell<BorrowingTransaction, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("");
                        } else {
                            setText(String.valueOf(item));
                        }
                    }
                };

                return cell;
            });
        }
    }

	public  BorrowingTransactionManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
	    this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	    this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	    this.getStyleClass().add("background");

        Label titelLabel = new Label("Borrowing Transaction Management");
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
		
	    TableView<BorrowingTransaction> myDataTable = new TableView<BorrowingTransaction>();
 
        myDataTable.setEditable(true);
        myDataTable.setMinHeight(270);
        myDataTable.setMaxWidth(1032);
 
    
        // name of column for display
        TableColumn<BorrowingTransaction, Integer> BorrowingIDCol = new TableColumn<BorrowingTransaction, Integer>("borrowingID");
        BorrowingIDCol.setMinWidth(90);
        BorrowingIDCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Integer>("borrowingID"));
        
        
         
        TableColumn<BorrowingTransaction, String> BorrowDateCol = new TableColumn<BorrowingTransaction, String>("borrowDate");
        BorrowDateCol.setMinWidth(100);
        BorrowDateCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, String>("borrowDate"));


        TableColumn<BorrowingTransaction, String> DueDateCol = new TableColumn<BorrowingTransaction, String>("dueDate");
        DueDateCol.setMinWidth(100);
        DueDateCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, String>("dueDate"));
        DueDateCol.setCellFactory(TextFieldTableCell.<BorrowingTransaction>forTableColumn());
        DueDateCol.setOnEditCommit(
        		(CellEditEvent<BorrowingTransaction, String> t) -> {
                       ((BorrowingTransaction) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setBorrowDate(t.getNewValue()); //display only
                 updateDueDate( t.getRowValue().getBorrowingID(),t.getNewValue());
        });
        
        TableColumn<BorrowingTransaction, String> ReturnDateCol = new TableColumn<BorrowingTransaction, String>("returnDate");
        ReturnDateCol.setMinWidth(100);
        ReturnDateCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, String>("returnDate"));
        
        TableColumn<BorrowingTransaction, Boolean> paymentStatusCol = new TableColumn<>("paymentStatus");
        paymentStatusCol.setMinWidth(100);
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Boolean>("paymentStatus"));
        paymentStatusCol.setCellFactory(new Callback<TableColumn<BorrowingTransaction, Boolean>, TableCell<BorrowingTransaction, Boolean>>() {
            @Override
            public TableCell<BorrowingTransaction, Boolean> call(TableColumn<BorrowingTransaction, Boolean> param) {
                return new CheckBoxTableCell<BorrowingTransaction, Boolean>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            CheckBox checkBox = new CheckBox();
                            checkBox.setSelected(item);
                            setGraphic(checkBox);
                            setAlignment(getAlignment());
                            checkBox.setDisable(true);
                        }
                    }
                };
            }
        });
        
        TableColumn<BorrowingTransaction, Double> priceperdayCol = new TableColumn<BorrowingTransaction, Double>("PricePerDay");
        priceperdayCol.setMinWidth(90);
        priceperdayCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Double>("pricePerDay"));       
        priceperdayCol.setCellFactory(TextFieldTableCell.<BorrowingTransaction,Double> forTableColumn(new DoubleStringConverter()));
        priceperdayCol.setOnEditCommit(        
        		(CellEditEvent<BorrowingTransaction, Double> t) -> {
                       ((BorrowingTransaction) t.getTableView().getItems().get(
        	                        t.getTablePosition().getRow())
        	                        ).setPricePerDay(t.getNewValue());
                  updatePrice( t.getRowValue().getBorrowingID(),t.getNewValue());
        });
        
        

        
        TableColumn<BorrowingTransaction, Integer> BookIDCol = new TableColumn<BorrowingTransaction, Integer>("bookID");
        BookIDCol.setMinWidth(90);
        BookIDCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Integer>("bookID"));       
       
        TableColumn<BorrowingTransaction, Integer> PatronIDCol = new TableColumn<BorrowingTransaction, Integer>("patronID");
        PatronIDCol.setMinWidth(90);
        PatronIDCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Integer>("patronID"));       

        TableColumn<BorrowingTransaction, Integer> LibrarianIDCol = new TableColumn<BorrowingTransaction, Integer>("librarianID");
        LibrarianIDCol.setMinWidth(90);
        LibrarianIDCol.setCellValueFactory(new PropertyValueFactory<BorrowingTransaction, Integer>("librarianID"));       
       
        TotalPriceColumn totalPriceCol = new TotalPriceColumn();
        TotalFineColumn totalFineCol = new TotalFineColumn();
        
        myDataTable.setItems(dataList);
        myDataTable.getStyleClass().add("table-view");
        myDataTable.getColumns().addAll(BorrowingIDCol,BorrowDateCol,DueDateCol,ReturnDateCol,paymentStatusCol,priceperdayCol,totalPriceCol,totalFineCol,BookIDCol,PatronIDCol,LibrarianIDCol);

        myDataTable.setRowFactory(tv -> {
            TableRow<BorrowingTransaction> row = new TableRow<>();
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
        columnComboBox.getItems().addAll("BorrowingID","BorrowDate","DueDate","ReturnDate","PaymentStatus","Price","PaidPrice","FinePerDay","BookID","PatronID","LibrarianID");
        columnComboBox.setValue("BorrowingID"); // Set default value
        columnComboBox.getStyleClass().add("custom-ComboBox");
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search here...");
        searchField.getStyleClass().add("custom-textfield-subscene"); 
        searchField.setPrefHeight(30);
        searchField.setPrefWidth(200);
        FilteredList<BorrowingTransaction> filteredData = new FilteredList<>(dataList, p -> true);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Borrowing -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                String selectedColumn = columnComboBox.getValue();

                switch (selectedColumn) {
                case "BorrowingID":
                    return String.valueOf(Borrowing.getBorrowingID()).contains(lowerCaseFilter);
                case "BorrowDate":
                    return Borrowing.getBorrowDate().toString().toLowerCase().contains(lowerCaseFilter); // Assuming BorrowDate is of type LocalDate
                case "DueDate":
                    return Borrowing.getDueDate().toString().toLowerCase().contains(lowerCaseFilter); // Assuming DueDate is of type LocalDate
                case "ReturnDate":
                    return Borrowing.getReturnDate() != null && Borrowing.getReturnDate().toString().toLowerCase().contains(lowerCaseFilter); // Assuming ReturnDate is of type LocalDate
                case "PaymentStatus":
                    return String.valueOf(Borrowing.isPaymentStatus()).toLowerCase().contains(lowerCaseFilter);
                case "PricePerDay":
                    return String.valueOf(Borrowing.getPricePerDay()).contains(lowerCaseFilter);
                case "FinePerDay":
                    return String.valueOf(Borrowing.getFinePerDay()).contains(lowerCaseFilter);
                case "TotalPrice":
                	return String.valueOf(Borrowing.calculateFinancialPenalty() + Borrowing.calculateFinancialPrice()).contains(lowerCaseFilter);
                case "BookID":
                    return String.valueOf(Borrowing.getBookID()).contains(lowerCaseFilter);
                case "PatronID":
                    return String.valueOf(Borrowing.getPatronID()).contains(lowerCaseFilter);
                case "LibrarianID":
                    return String.valueOf(Borrowing.getLibrarianID()).contains(lowerCaseFilter);
                default:
                    return false;
            }
            });
        });

        SortedList<BorrowingTransaction> sortedData = new SortedList<>(filteredData);
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
        	ObservableList<BorrowingTransaction> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
        	ArrayList<BorrowingTransaction> rows = new ArrayList<>(selectedRows);

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
            			BorrowingTransaction selectedItem = myDataTable.getSelectionModel().getSelectedItem();
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
        

        
        final VBox vbox = new VBox(25);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(searchBox,myDataTable,hb1);
        
        return vbox;     
	}
	

		
	public void updateReturnDateCol(int BorrowingID, String ReturnDate) {
		
		try {
			System.out.println("update BorrowingTransaction set ReturnDate = '"+ReturnDate + "' where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update BorrowingTransaction set ReturnDate = '"+ReturnDate + "' where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updateDueDate(int BorrowingID, String DueDate) {
		
		try {
			System.out.println("update BorrowingTransaction set DueDate = '"+DueDate + "' where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  BorrowingTransaction set DueDate = '"+DueDate + "' where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updateBorrowDate(int BorrowingID, String ReturnDate) {		
		try {
			System.out.println("update BorrowingTransaction set ReturnDate = '"+ReturnDate + "' where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update BorrowingTransaction set ReturnDate = '"+ReturnDate + "' where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updateFinePerDay(int BorrowingID,double finePerDay) {
		
		try {
			System.out.println("update BorrowingTransaction set FinePerDay = "+finePerDay + " where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update BorrowingTransaction set FinePerDay = "+finePerDay+ " where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}

	
	public void updatePrice(int BorrowingID,double price ) {
		
		try {
			System.out.println("update BorrowingTransaction set Price = '"+price + "' where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update BorrowingTransaction set Price = '"+ price + "' where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	public void updatePaidPrice(int BorrowingID,double paidPrice ) {
		
		try {
			System.out.println("update BorrowingTransaction set paidPrice = '"+paidPrice + "' where BorrowingID = "+BorrowingID);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update BorrowingTransaction set paidPrice = '"+paidPrice+ "' where BorrowingID = "+BorrowingID+";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
			} catch (SQLException e) {
				SceneManager.showAlert("Error",e.getMessage());
			} 
	}
	
	private void getData() throws SQLException, ClassNotFoundException {
        String SQL;
        SceneManager.connectDB();
        System.out.println("Connection established");
        
        SQL = "SELECT B.BorrowingID, B.BorrowDate, B.DueDate, B.ReturnDate, B.PaymentStatus, K.PricePerDay, B.BookID, B.PatronID, B.LibrarianID, K.Title, P.Name,L.Name "
                + "FROM BorrowingTransaction B, Book K, Patron P ,Librarian L "
                + "WHERE B.LibrarianID = L.LibrarianID AND B.BookID = K.BookID AND B.PatronID = P.PatronID";

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
	private void deleteRow(BorrowingTransaction row) {
		
		try {
			System.out.println("delete from BorrowingTransaction where BorrowingID="+row.getBorrowingID() + ";");
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("delete from BorrowingTransaction where BorrowingID="+row.getBorrowingID() + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");
			
		} catch (SQLException e) {
			SceneManager.showAlert("Error",e.getMessage());
		}
	}
	

}

