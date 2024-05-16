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


public class LibrarianManagement extends StackPane {

	private ArrayList<Librarian> data;
	private ObservableList<Librarian> dataList;

	public LibrarianManagement() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
		this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
		this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		this.getStyleClass().add("background");

		Label titelLabel = new Label("Librarian Management");
		titelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 30));
		titelLabel.setStyle("-fx-text-fill: #000000;");

		data = new ArrayList<>();
		VBox vBox = null;
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
		vBox1.getChildren().addAll(titelLabel, vBox);

		this.getChildren().addAll(vBox1);
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
		this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
	}

	@SuppressWarnings("unchecked")
	private VBox tableView() {

		TableView<Librarian> myDataTable = new TableView<Librarian>();

		myDataTable.setEditable(true);
		myDataTable.setMinHeight(270);
		myDataTable.setMaxWidth(882);

		// name of column for display
		TableColumn<Librarian, Integer> librarianIDCol = new TableColumn<Librarian, Integer>("librarianID");
		librarianIDCol.setMinWidth(50);

		// to get the data from specific column
		librarianIDCol.setCellValueFactory(new PropertyValueFactory<Librarian, Integer>("librarianID"));

		TableColumn<Librarian, String> NameCol = new TableColumn<Librarian, String>("name");
		NameCol.setMinWidth(100);
		NameCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("name"));

		NameCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		NameCol.setOnEditCommit((CellEditEvent<Librarian, String> t) -> {
			((Librarian) t.getTableView().getItems().get(t.getTablePosition().getRow())).setName(t.getNewValue()); // display
																													// only
			updateName(t.getRowValue().getLibrarianID(), t.getNewValue());
		});

		TableColumn<Librarian, String> emailCol = new TableColumn<Librarian, String>("email");
		emailCol.setMinWidth(200);
		emailCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("email"));

		emailCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		emailCol.setOnEditCommit((CellEditEvent<Librarian, String> t) -> {
			((Librarian) t.getTableView().getItems().get(t.getTablePosition().getRow())).setEmail(t.getNewValue()); // display
																													// only
			updateEmail(t.getRowValue().getLibrarianID(), t.getNewValue());
		});
		TableColumn<Librarian, String> phoneCol = new TableColumn<Librarian, String>("phone");
		phoneCol.setMinWidth(145);
		phoneCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("phone"));

		phoneCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		phoneCol.setOnEditCommit((CellEditEvent<Librarian, String> t) -> {
			((Librarian) t.getTableView().getItems().get(t.getTablePosition().getRow())).setPhone(t.getNewValue()); // display
																													// only
			updatePhone(t.getRowValue().getLibrarianID(), t.getNewValue());
		});
		TableColumn<Librarian, String> addressCol = new TableColumn<Librarian, String>("address");
		addressCol.setMinWidth(100);
		addressCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("address"));

		addressCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		addressCol.setOnEditCommit((CellEditEvent<Librarian, String> t) -> {
			((Librarian) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAddress(t.getNewValue()); // display
																														// only
			updateAddress(t.getRowValue().getLibrarianID(), t.getNewValue());
		});
		TableColumn<Librarian, String> employment_dateCol = new TableColumn<Librarian, String>("employment_date");
		employment_dateCol.setMinWidth(130);
		employment_dateCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("employment_date"));

		employment_dateCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		employment_dateCol.setOnEditCommit((CellEditEvent<Librarian, String> t) -> {
			((Librarian) t.getTableView().getItems().get(t.getTablePosition().getRow()))
					.setEmployment_date(t.getNewValue()); // display only
			updatEmployment_date(t.getRowValue().getLibrarianID(), t.getNewValue());
		});
		TableColumn<Librarian, String> passwordCol = new TableColumn<Librarian, String>("password");
		passwordCol.setMinWidth(130);
		passwordCol.setCellValueFactory(new PropertyValueFactory<Librarian, String>("password"));

		passwordCol.setCellFactory(TextFieldTableCell.<Librarian>forTableColumn());

		myDataTable.setItems(dataList);
		myDataTable.getStyleClass().add("table-view");
		myDataTable.getColumns().addAll(librarianIDCol, NameCol, emailCol, phoneCol, addressCol, employment_dateCol,passwordCol);

		myDataTable.setRowFactory(tv -> {
			TableRow<Librarian> row = new TableRow<>();
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
		columnComboBox.getItems().addAll("librarianID", "Name", "Email", "Phone", "Address", "Date","Password");
		columnComboBox.setValue("librarianID"); // Set default value
		columnComboBox.getStyleClass().add("custom-ComboBox");

		TextField searchField = new TextField();
		searchField.setPromptText("Search here...");
		searchField.getStyleClass().add("custom-textfield-subscene");
		searchField.setPrefHeight(30);
		searchField.setPrefWidth(200);
		FilteredList<Librarian> filteredData = new FilteredList<>(dataList, p -> true);

		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(librarian -> {
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				String lowerCaseFilter = newValue.toLowerCase();
				String selectedColumn = columnComboBox.getValue();

				switch (selectedColumn) {
				case "librarianID":
					return String.valueOf(librarian.getLibrarianID()).contains(lowerCaseFilter);
				case "Name":
					return librarian.getName().toLowerCase().contains(lowerCaseFilter);
				case "Email":
					return librarian.getEmail().contains(lowerCaseFilter);
				case "Phone":
					return librarian.getPhone().contains(lowerCaseFilter);
				case "Address":
					return librarian.getAddress().toLowerCase().contains(lowerCaseFilter);
				case "Date":
					return librarian.getEmployment_date().toLowerCase().contains(lowerCaseFilter);
				case "Password":
					return librarian.getPassword().toLowerCase().contains(lowerCaseFilter);	
				default:
					return false;
				}
			});
		});

		SortedList<Librarian> sortedData = new SortedList<>(filteredData);
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
		searchBox.getChildren().addAll(searchLabel, searchField, searchByLabel, columnComboBox);

		final Button deleteButton = new Button("Delete");
		deleteButton.getStyleClass().add("custom-button-subscene");
		deleteButton.setOnAction((ActionEvent e) -> {
			ObservableList<Librarian> selectedRows = myDataTable.getSelectionModel().getSelectedItems();
			ArrayList<Librarian> rows = new ArrayList<>(selectedRows);

			Alert confirmation = new Alert(AlertType.CONFIRMATION);
			confirmation.setTitle("Delete Confirmation");
			confirmation.setHeaderText(null);
			confirmation.setContentText("Are you sure you want to delete this librarian?");

			// Customize the buttons (OK for delete, CANCEL for cancel)
			ButtonType buttonTypeOK = new ButtonType("OK");
			ButtonType buttonTypeCancel = new ButtonType("Cancel");
			confirmation.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
			ButtonType result = confirmation.showAndWait().orElse(ButtonType.CANCEL);

			if (result == buttonTypeOK) {
				rows.forEach(row -> {
					try {
						Librarian selectedItem = myDataTable.getSelectionModel().getSelectedItem();
						deleteRow(row);
						if (selectedItem != null) {
							dataList.remove(selectedItem);
						}
						myDataTable.refresh();
					} catch (Exception e2) {
						SceneManager.showAlert("Erorr", e2.getMessage());
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
		hb1.getChildren().addAll(deleteButton, refreshButton);

		GridPane gridPane = new GridPane();
		gridPane.setVgap(20);
		gridPane.setHgap(10);
		gridPane.setAlignment(Pos.CENTER);

		Label addNewBookLabel = new Label("Add new librarianID:");
		addNewBookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		addNewBookLabel.setStyle("-fx-text-fill: #000000;");
		gridPane.add(addNewBookLabel, 0, 0, 6, 1); // Assuming 6 prompts

		String[] prompts = {"Name", "Email", "Phone", "Address", "Date","Password" };
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
				
				String pass=HashPassword.hashPassword(textFields[5].getText());
				Librarian rc = new Librarian(0, textFields[0].getText(),
						(textFields[1].getText()), (textFields[2].getText()), textFields[3].getText(),
						textFields[4].getText(),pass);

				if (insertData(rc)) {
					rc.setLibrarianID(findMaxIDOfLibrarian());
					dataList.add(rc);
				}
					
				for (TextField textField : textFields) {
					textField.clear();
				}
			} catch (Exception e2) {
				SceneManager.showAlert("Error", e2.getMessage());
			}
		});
		GridPane.setHalignment(addButton, HPos.CENTER);
		gridPane.add(addButton, 0, 2, 6, 1);

		final VBox vbox = new VBox(25);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(searchBox, myDataTable, hb1, gridPane);

		return vbox;
	}

	private boolean insertData(Librarian rc) {

		try {
			SceneManager.connectDB();
			SceneManager.ExecuteStatement(
					"Insert into Librarian (Name,email, phone, address,employmentdate,password) values("
							+ "'" + rc.getName() + "','" + rc.getEmail() + "','" + rc.getPhone()
							+ "', '" + rc.getAddress() + "', '" + rc.getEmployment_date() +  "', '" + rc.getPassword() + "');");
			SceneManager.getCon().close();
			System.out.println("Connection closed" + data.size());
			return true;

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
			return false;
		}
	}

	public void updateName(int snum, String name) {

		try {
			System.out.println("update  Librarian set name = '" + name + "' where librarianID = " + snum);
			SceneManager.connectDB();
			SceneManager
					.ExecuteStatement("update  Librarian set name = '" + name + "' where librarianID = " + snum + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
	}

	public void updateEmail(int snum, String email) {

		try {
			System.out.println("update  Librarian set email = " + email + " where librarianID = " + snum);
			SceneManager.connectDB();
			SceneManager
					.ExecuteStatement("update  Librarian set email = " + email + " where librarianID = " + snum + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
	}

	public void updatePhone(int snum, String phone) {

		try {
			System.out.println("update  Librarian set phone = '" + phone + "' where librarianID = " + snum);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement(
					"update  Librarian set phone = '" + phone + "' where librarianID = " + snum + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
	}

	public void updateAddress(int snum, String address) {

		try {
			System.out.println("update  Librarian set address = '" + address + "' where librarianID = " + snum);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement(
					"update  Librarian set address = '" + address + "' where librarianID = " + snum + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
	}

	public void updatEmployment_date(int snum, String employment_date) {

		try {
			System.out.println(
					"update  Librarian set employmentdate = '" + employment_date + "' where librarianID = " + snum);
			SceneManager.connectDB();
			SceneManager.ExecuteStatement("update  Librarian set employmentdate = '" + employment_date
					+ "' where librarianID = " + snum + ";");
			SceneManager.getCon().close();
			System.out.println("Connection closed");

		} catch (SQLException e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
	}

	private void deleteRow(Librarian row) throws Exception {
		System.out.println("delete from  Librarian where librarianID=" + row.getLibrarianID() + ";");
		SceneManager.connectDB();
		SceneManager.ExecuteStatement("delete from  Librarian where librarianID=" + row.getLibrarianID() + ";");
		SceneManager.getCon().close();
		System.out.println("Connection closed");
	}

	public void getData() throws SQLException, ClassNotFoundException {

		String SQL;

		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "select librarianID,Name,email, phone, address,employmentdate,password from Librarian ";
		Statement stmt = SceneManager.getCon().createStatement();
		ResultSet rs = stmt.executeQuery(SQL);

		while (rs.next()) {
			data.add(new Librarian(Integer.parseInt(rs.getString(1)), rs.getString(2), (rs.getString(3)),
					rs.getString(4), rs.getString(5), (rs.getString(6)),(rs.getString(7))));
		}

		rs.close();
		stmt.close();

		SceneManager.getCon().close();
		System.out.println("Connection closed" + data.size());

	}
	
	private int findMaxIDOfLibrarian() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT MAX(LibrarianID) FROM Librarian");
		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}
}
