package collection;

import javafx.beans.property.SimpleStringProperty;

public class BorrowingInfo {
    private final SimpleStringProperty patronName;
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty returnDate;

    public BorrowingInfo(String patronName, String bookTitle, String returnDate) {
        this.patronName = new SimpleStringProperty(patronName);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.returnDate = new SimpleStringProperty(returnDate);
    }

    public String getPatronName() {
        return patronName.get();
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public String getReturnDate() {
        return returnDate.get();
    }
}
