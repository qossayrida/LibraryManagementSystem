package collection;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ReturnBorrowing {
	private int borrowingID;
	private String borrowDate;
	private String dueDate;
	private String returnDate;
	private boolean paymentStatus;
	private double price;
	private double paidPrice;
	private double finePerDay;
	private int bookID;
	private int patronID;
	private int librarianID;
	private String title;
	private String name;

	

	public ReturnBorrowing(int borrowingID, String borrowDate, String dueDate, String returnDate, boolean paymentStatus,
			double price, double paidPrice, double finePerDay, int bookID, int patronID, int librarianID, String title,
			String name) {
		this.borrowingID = borrowingID;
		this.borrowDate = borrowDate;
		this.dueDate = dueDate;
		this.returnDate = returnDate;
		this.paymentStatus = paymentStatus;
		this.price = price;
		this.paidPrice = paidPrice;
		this.finePerDay = finePerDay;
		this.bookID = bookID;
		this.patronID = patronID;
		this.librarianID = librarianID;
		this.title = title;
		this.name = name;
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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getFinePerDay() {
		return finePerDay;
	}

	public void setFinePerDay(double finePerDay) {
		this.finePerDay = finePerDay;
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

	public double getPaidPrice() {
		return paidPrice;
	}

	public void setPaidPrice(double paidPrice) {
		this.paidPrice = paidPrice;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public double calculateFinancialPenalty() {
        if (dueDate == null) {
            return 0; // Handle the case where returnDate or dueDate is null
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate currentDate = LocalDate.now();
        LocalDate dueLocalDate = LocalDate.parse(dueDate, formatter);

        if (dueLocalDate.isAfter(currentDate)) {
            return 0; // No penalty if returnDate is after dueDate
        }

        long totalNumberOfDays = ChronoUnit.DAYS.between(dueLocalDate, currentDate);
        return totalNumberOfDays * finePerDay;
    }
}
