package collection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class BorrowingTransaction {
	private int borrowingID;
	private String borrowDate;
	private String dueDate;
	private String returnDate;
	private boolean paymentStatus;
	private double pricePerDay;
	private int bookID;
	private int patronID;
	private int librarianID;
	private String title;
	private String patronName;
	private String librarianName;

	public BorrowingTransaction(int borrowingID, String borrowDate, String dueDate, String returnDate, boolean paymentStatus,
			double pricePerDay, int bookID, int patronID, int librarianID, String title,
			String patronName,String librarianName) {
		this.borrowingID = borrowingID;
		this.borrowDate = borrowDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.paymentStatus = paymentStatus;
		this.pricePerDay = pricePerDay;
		this.bookID = bookID;
		this.patronID = patronID;
		this.librarianID = librarianID;
		this.title = title;
		this.patronName = patronName;
		this.librarianName = librarianName;
	}

	public int getBorrowingID() {
		return borrowingID;
	}

	public void setBorrowingID(int borrowingID) {
		this.borrowingID = borrowingID;
	}

	public String getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(String borrowDate) {
		this.borrowDate = borrowDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public boolean isPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(boolean paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}
	
	public double getFinePerDay() {
		return pricePerDay/10.0;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}

	public String getPatronName() {
		return patronName;
	}

	public void setPatronName(String patronName) {
		this.patronName = patronName;
	}

	public String getLibrarianName() {
		return librarianName;
	}

	public void setLibrarianName(String librarianName) {
		this.librarianName = librarianName;
	}

	public int getBookID() {
		return bookID;
	}

	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public int getPatronID() {
		return patronID;
	}

	public void setPatronID(int patronID) {
		this.patronID = patronID;
	}

	public int getLibrarianID() {
		return librarianID;
	}

	public void setLibrarianID(int librarianID) {
		this.librarianID = librarianID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public double calculateFinancialPrice() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate borrowDate = LocalDate.parse(this.borrowDate, formatter);
        LocalDate dueLocalDate = LocalDate.parse(this.dueDate, formatter);

        long totalNumberOfDays = ChronoUnit.DAYS.between(borrowDate,dueLocalDate);
        return totalNumberOfDays * this.getPricePerDay();
    }

	public double calculateFinancialPenalty() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate dueLocalDate = LocalDate.parse(this.dueDate, formatter);

        if (dueLocalDate.isAfter(currentDate)) {
            return 0; // No penalty if returnDate is after dueDate
        }

        long totalNumberOfDays = ChronoUnit.DAYS.between(dueLocalDate, currentDate);
        return totalNumberOfDays * this.getFinePerDay();
    }
}
