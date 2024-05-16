package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class HomeScene extends StackPane {

	PieChart pieChart;
	BarChart<String, Number> barChart;
	VBox borrowingTableVBox ;

	public HomeScene() {
		this.setPrefWidth(Screen.getPrimary().getVisualBounds().getWidth());
		this.setPrefHeight(Screen.getPrimary().getVisualBounds().getHeight());
		this.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		this.getStyleClass().add("background");

		StackPane bookBox = null;
		StackPane classificationBox = null;
		StackPane patronBox = null;
		StackPane librarianBox = null;

		createBorrowingStatepieChart();
		createpieDeweyClassificationChart();

		try {
			bookBox = createInfoBox("Total Books: " + findNumberOfBook(), "/bookicon.png");
			classificationBox = createInfoBox("Total Classification: " + findNumberOfClassification(),
					"/deweyicon.png");
			patronBox = createInfoBox("Total Patron: " + findNumberOfPatron(), "/patronicon.png");
			librarianBox = createInfoBox("Total Librarian: " + findNumberOfLibrarian(), "/librarianicon.png");

		} catch (Exception e) {
			SceneManager.showAlert("Error", "We can't connect to the DataBase");
		}

		HBox infoboxHBox = new HBox(40); // Adjust spacing
		infoboxHBox.setAlignment(Pos.TOP_CENTER);
		infoboxHBox.getChildren().addAll(bookBox, classificationBox, patronBox, librarianBox);

	

		try {
			populateclosereturnDateTable();
		} catch (Exception e) {
			SceneManager.showAlert("Error", "We can't connect to the DataBase");
		}
		HBox finalHBox = new HBox(30);
		finalHBox.setAlignment(Pos.CENTER);

		StackPane mostReservedBookBox = createMostReservedBookBox();
		mostReservedBookBox.setAlignment(Pos.CENTER);
		mostReservedBookBox.setMinHeight(50);
		mostReservedBookBox.setMinWidth(50);
		finalHBox.getChildren().addAll(borrowingTableVBox,pieChart,mostReservedBookBox);
		
		VBox vBox1 = new VBox(50); // Adjust spacing
		vBox1.setPadding(new Insets(60));
		vBox1.setAlignment(Pos.TOP_LEFT);
		vBox1.setPrefWidth(700);
		vBox1.getChildren().addAll(infoboxHBox,month_information(),finalHBox);

		this.getChildren().addAll(vBox1);
	}

	private int findNumberOfBook() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT COUNT(*) FROM Book");

		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	private int findNumberOfLibrarian() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT COUNT(*) FROM Librarian");

		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	private int findNumberOfPatron() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT COUNT(*) FROM Patron");

		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	private int findNumberOfClassification() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		// Replace this query with the actual query needed for your database
		ResultSet rs = SceneManager.ExecuteStatement("SELECT COUNT(*) FROM DeweyClassification");

		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	public void createBorrowingStatepieChart() {
		try {
			int numberOfPaidTransactions = hasPaidTransactions();
			int numberOfUnpaidTransactions = hasUnpaidTransactions();
			ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
					new PieChart.Data("Paid Transactions:" + numberOfPaidTransactions, numberOfPaidTransactions),
					new PieChart.Data("Unpaid Transactions:" + numberOfUnpaidTransactions, numberOfUnpaidTransactions));

			pieChart = new PieChart(pieChartData);
			pieChart.setTitle("Borrowing Transactions");
			pieChart.setLegendVisible(false);
			pieChart.setLegendSide(null);

			// Get the chart title
			Label chartTitle = (Label) pieChart.lookup(".chart-title");

			// Set padding to move the title down (adjust the values as needed)
			chartTitle.setPadding(new Insets(20));

		} catch (Exception e) {
			SceneManager.showAlert("Error", "We can't connect to the DataBase");
		}
	}

	private StackPane createMostReservedBookBox() {
		try {
			String query = "SELECT B.BookID, B.Title, COUNT(*) AS TotalReservations " + "FROM BorrowingTransaction BT "
					+ "JOIN Book B ON BT.BookID = B.BookID " + "GROUP BY B.BookID, B.Title "
					+ "ORDER BY TotalReservations DESC " + "LIMIT 1";

			SceneManager.connectDB();
			ResultSet rs = SceneManager.ExecuteStatement(query);

			String mostReservedBookTitle = "";
			int mostReservedBookReservations = 0;

			if (rs.next()) {
				mostReservedBookTitle = rs.getString("Title");
				mostReservedBookReservations = rs.getInt("TotalReservations");
			}

			rs.close();

			StackPane infoBox = new StackPane();
			infoBox.setStyle("-fx-background-color: transparent;");
			Rectangle roundedRect = new Rectangle(200, 200);
			roundedRect.setArcWidth(20);
			roundedRect.setArcHeight(20);
			roundedRect.setFill(Color.web("#add8e6"));
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(Color.GRAY);
			dropShadow.setRadius(5);
			dropShadow.setOffsetX(3);
			dropShadow.setOffsetY(3);
			roundedRect.setEffect(dropShadow);
			VBox mostReservedBookInfoBox = new VBox();
			Image numberOneIcon = new Image(getClass().getResourceAsStream("/one.png"));
			ImageView numberOneImageView = new ImageView(numberOneIcon);
			numberOneImageView.setFitWidth(100);
			numberOneImageView.setFitHeight(100);

			// Create labels to display the information
			Label titleLabel = new Label("Most Reserved Book:");
			Label bookTitleLabel = new Label(mostReservedBookTitle);
			Label reservationsLabel = new Label("Total Reservations: " + mostReservedBookReservations);

			// Set font and text color
			titleLabel.setFont(Font.font("Arial", 10));
			bookTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			reservationsLabel.setFont(Font.font("Arial", 12));

			titleLabel.setTextFill(Color.web("#000080"));
			bookTitleLabel.setTextFill(Color.web("#CCB400"));
			reservationsLabel.setTextFill(Color.web("#000080"));

			// Add labels and the icon to the StackPane
			mostReservedBookInfoBox.getChildren().addAll(numberOneImageView, titleLabel, bookTitleLabel,
					reservationsLabel);
			mostReservedBookInfoBox.setAlignment(Pos.CENTER);
			infoBox.getChildren().addAll(roundedRect, mostReservedBookInfoBox);

			return infoBox;

		} catch (SQLException e) {
			e.printStackTrace(); // Handle or log the exception appropriately
			return new StackPane(); // Return an empty HBox in case of an error
		}
	}

	private void populateclosereturnDateTable() throws SQLException, ClassNotFoundException {
		LocalDate currentdate = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateString = currentdate.format(formatter);
		ObservableList<BorrowingInfo> data = FXCollections.observableArrayList();

		String query = "select p.Name,b.Title,BT.DueDate " + "from Patron p,Book b,BorrowingTransaction BT "
				+ "where p.PatronID=BT.PatronID AND b.BookID=BT.BookID and BT.paymentstatus = 0 and BT.DueDate<=" + "'"
				+ dateString + "'";

		SceneManager.connectDB();
		ResultSet rs = SceneManager.ExecuteStatement(query);
		while (rs.next()) {
			String patronName = rs.getString(1);
			String bookTitle = rs.getString(2);
			String returnDate = rs.getString(3);

			System.out.println(patronName);
			data.add(new BorrowingInfo(patronName, bookTitle, returnDate));
		}

		rs.close();
		
		borrowingTableVBox = new VBox(20); // Adjust spacing
		borrowingTableVBox.setAlignment(Pos.CENTER);

		Label titelLabel = new Label("Should be returned today");
		titelLabel.setFont(Font.font(17));
		titelLabel.setStyle("-fx-text-fill: #000000;");

		TableView<BorrowingInfo> borrowingTable = new TableView<>();
		borrowingTable.setMaxHeight(300);
		borrowingTable.setMinHeight(300);
		borrowingTable.setPrefWidth(452);

		TableColumn<BorrowingInfo, String> patronColumn = new TableColumn<>("Patron Name");
		patronColumn.setPrefWidth(150);
		patronColumn.setCellValueFactory(new PropertyValueFactory<>("patronName"));

		TableColumn<BorrowingInfo, String> bookTitleColumn = new TableColumn<>("Book Title");
		bookTitleColumn.setPrefWidth(150);
		bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

		TableColumn<BorrowingInfo, String> returnDateColumn = new TableColumn<>("Return Date");
		returnDateColumn.setPrefWidth(150);
		returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
		borrowingTable.setItems(data);
		borrowingTable.getColumns().addAll(patronColumn, bookTitleColumn, returnDateColumn);

		borrowingTableVBox.getChildren().addAll(titelLabel, borrowingTable);
	}

	private int hasUnpaidTransactions() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		ResultSet rs = SceneManager
				.ExecuteStatement("SELECT COUNT(*) FROM BorrowingTransaction " + "WHERE PaymentStatus=false");
		if (rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	private int hasPaidTransactions() throws SQLException, ClassNotFoundException {
		SceneManager.connectDB();
		ResultSet rs = SceneManager
				.ExecuteStatement("SELECT COUNT(*) FROM BorrowingTransaction " + "WHERE PaymentStatus=true");
		if (rs.next()) {
			return rs.getInt(1);
		}
		return 0;
	}

	public void createpieDeweyClassificationChart() {
		try {
			int numberOfPaidTransactions = hasPaidTransactions();
			int numberOfUnpaidTransactions = hasUnpaidTransactions();

			// Creating X and Y axes
			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis();

			barChart = new BarChart<>(xAxis, yAxis);
			barChart.setTitle("Borrowing Transactions by State");

			// Creating data series
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			series.getData().add(new XYChart.Data<>("Paid Transactions", numberOfPaidTransactions));
			series.getData().add(new XYChart.Data<>("Unpaid Transactions", numberOfUnpaidTransactions));

			// Adding the data series to the chart
			barChart.getData().add(series);
		} catch (SQLException e) {
			e.printStackTrace(); // Handle or log the exception appropriately
		} catch (ClassNotFoundException e) {
			e.printStackTrace(); // Handle or log the exception appropriately
		}
	}

	private StackPane createInfoBox(String labelText, String imagePath) {
		StackPane infoBox = new StackPane();
		infoBox.setStyle("-fx-background-color: transparent;");

		// Add a rounded rectangle with a border to the info box
		Rectangle roundedRect = new Rectangle(200, 80);
		roundedRect.setArcWidth(20);
		roundedRect.setArcHeight(20);
		roundedRect.setFill(Color.web("#add8e6"));
		DropShadow dropShadow = new DropShadow();
		dropShadow.setColor(Color.GRAY);
		dropShadow.setRadius(5);
		dropShadow.setOffsetX(3);
		dropShadow.setOffsetY(3);
		roundedRect.setEffect(dropShadow);
		Label label = new Label(labelText);
		label.setFont(Font.font("Arial", 14));
		label.setTextFill(Color.web("#000080"));
		// Load and set the logo image
		ImageView logoImageView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
		logoImageView.setFitWidth(40);
		logoImageView.setFitHeight(40);

		// Create an HBox to contain the label and logo
		HBox contentBox = new HBox(5); // Adjust the spacing as needed
		contentBox.setAlignment(Pos.CENTER);
		contentBox.getChildren().addAll(logoImageView, label);

		// Add the contentBox to the infoBox
		infoBox.getChildren().addAll(roundedRect, contentBox);

		return infoBox;
	}

	public VBox month_information() {
		ComboBox<String> month = new ComboBox<>();
		month.getStyleClass().add("custom-ComboBox");
		month.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
		month.setValue("1");

		TextField year = new TextField();
		year.getStyleClass().add("custom-textfield-subscene");
		year.setPromptText("Enter your text here");
		Label l1 = new Label("Year");
		Label l2 = new Label("Month");
		HBox contentBox = new HBox(20);
		contentBox.getChildren().addAll(l1, year, l2, month);
		contentBox.setAlignment(Pos.CENTER);

		VBox verticalbox = new VBox(30);
		verticalbox.setAlignment(Pos.CENTER);
		verticalbox.getChildren().addAll(contentBox);
		LocalDate today = LocalDate.now();

		int nowyear = today.getYear();
		int nowmonth = today.getMonthValue();
		month.setValue("" + nowmonth);
		year.setText("" + nowyear);

		StackPane v1i = createInfoBox("Top Book: " + findmostbook(nowyear, nowmonth), "/bookicon.png");
		StackPane v2i = createInfoBox("Top Librarian: " + findmostlibrarian(nowyear, nowmonth), "/librarianicon.png");
		StackPane v3i = createInfoBox("Top Patron: " + findmostpatron(nowyear, nowmonth), "/patronicon.png");
		HBox datai = new HBox(40);
		datai.setAlignment(Pos.CENTER);
		datai.getChildren().addAll(v1i, v2i, v3i);
		verticalbox.getChildren().addAll(datai);

		year.setOnKeyPressed(e -> {
			try {
				if (e.getCode() == KeyCode.ENTER) {
					int mont = Integer.parseInt(month.getValue());
					int yea = Integer.parseInt(year.getText());

					StackPane v1 = createInfoBox("Top Book: " + findmostbook(yea, mont), "/bookicon.png");
					StackPane v2 = createInfoBox("Top Librarian: " + findmostlibrarian(yea, mont),
							"/librarianicon.png");
					StackPane v3 = createInfoBox("Top Patron: " + findmostpatron(yea, mont), "/patronicon.png");
					HBox data = new HBox(40);
					data.setAlignment(Pos.CENTER);
					data.getChildren().addAll(v1, v2, v3);
					verticalbox.getChildren().addAll(data);
					verticalbox.getChildren().remove(1);
				}
			} catch (Exception e2) {

				SceneManager.showAlert("Error", e2.getMessage());
			}
		});

		return verticalbox;
	}

	public String findmostbook(int year, int month) {

		String ans = "\n";

		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();

		String SQL;

		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "select B.BookID, B.Title, COUNT(*) AS TotalReservations\r\n" + "	   FROM BorrowingTransaction BT,\r\n"
				+ "	    Book B where BT.BookID = B.BookID and Bt.BorrowDate>='" + year + "-" + month
				+ "-1'and Bt.BorrowDate <='" + year + "-" + month + "-" + daysInMonth + "'  \r\n"
				+ "	   GROUP BY B.BookID, B.Title\r\n" + "	   ORDER BY TotalReservations DESC ";
		try {
			Statement stmt = SceneManager.getCon().createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			boolean par = true;
			int max = 0;
			while (rs.next()) {

				if (par || max == Integer.parseInt(rs.getString(3))) {
					String n = (rs.getString(2) + "   " + rs.getString(3));

					ans += n + "\n";

					par = false;

				} else {
					break;
				}
				max = Integer.parseInt(rs.getString(3));

			}

			rs.close();
			stmt.close();

			SceneManager.getCon().close();
		} catch (Exception e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
		return ans;

	}

	public String findmostlibrarian(int year, int month) {

		String ans = "\n";

		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();

		String SQL;

		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "select l.LibrarianID, l.name, COUNT(*) AS TotalReservations\r\n"
				+ "	   FROM BorrowingTransaction BT,\r\n"
				+ "	    Librarian l where BT.LibrarianID = l.LibrarianID and Bt.BorrowDate>='" + year + "-" + month
				+ "-1'and Bt.BorrowDate <='" + year + "-" + month + "-" + daysInMonth + "'  \r\n"
				+ "	   GROUP BY l.LibrarianID, l.name\r\n" + "	   ORDER BY TotalReservations DESC ";
		try {
			Statement stmt = SceneManager.getCon().createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			boolean par = true;
			int max = 0;
			while (rs.next()) {

				if (par || max == Integer.parseInt(rs.getString(3))) {
					String n = (rs.getString(2) + "   " + rs.getString(3));
					ans += n + "\n";

					par = false;

				} else {
					break;
				}
				max = Integer.parseInt(rs.getString(3));

			}

			rs.close();
			stmt.close();

			SceneManager.getCon().close();
		} catch (Exception e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
		return ans;

	}

	public String findmostpatron(int year, int month) {

		String ans = "\n";

		YearMonth yearMonth = YearMonth.of(year, month);
		int daysInMonth = yearMonth.lengthOfMonth();

		String SQL;

		SceneManager.connectDB();
		System.out.println("Connection established");

		SQL = "select p.PatronID, p.name, COUNT(*) AS TotalReservations\r\n"
				+ "	   FROM BorrowingTransaction BT,\r\n"
				+ "	    Patron p where BT.PatronID = p.PatronID and Bt.BorrowDate>='" + year + "-" + month
				+ "-1'and Bt.BorrowDate <='" + year + "-" + month + "-" + daysInMonth + "'  \r\n"
				+ "	   GROUP BY p.PatronID, p.name\r\n" + "	   ORDER BY TotalReservations DESC ";
		try {
			Statement stmt = SceneManager.getCon().createStatement();
			ResultSet rs = stmt.executeQuery(SQL);
			boolean par = true;
			int max = 0;
			while (rs.next()) {

				if (par || max == Integer.parseInt(rs.getString(3))) {
					String n = (rs.getString(2) + "   " + rs.getString(3));
					ans += n + "\n";

					par = false;

				} else {
					break;
				}
				max = Integer.parseInt(rs.getString(3));

			}

			rs.close();
			stmt.close();

			SceneManager.getCon().close();
		} catch (Exception e) {
			SceneManager.showAlert("Error", e.getMessage());
		}
		return ans;

	}

}
