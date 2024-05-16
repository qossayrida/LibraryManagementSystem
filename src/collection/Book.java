package collection;

public class Book {
	
	private int bookID;
	private int copiesNumber;
	private int classificationNumber;
	private double pricePerDay;
	private String title;
	private String author;
	private String publicationYear;
	private int reservation_count;
	
	

	public Book(int bookID,String title, int classificationNumber,int copiesNumber, String author,
			String publicationYear, int reservation_count,double pricePerDay) {
		super();
		this.bookID = bookID;
		this.copiesNumber = copiesNumber;
		this.classificationNumber = classificationNumber;
		this.title = title;
		this.author = author;
		this.publicationYear = publicationYear;
		this.reservation_count = reservation_count;
		this.pricePerDay=pricePerDay;
	}

	public int getBookID() {
		return bookID;
	}
	
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}

	public int getCopiesNumber() {
		return copiesNumber;
	}

	public void setCopiesNumber(int copiesNumber) {
		this.copiesNumber = copiesNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public int getClassificationNumber() {
		return classificationNumber;
	}

	public void setClassificationNumber(int classificationNumber) {
		this.classificationNumber = classificationNumber;
	}

	public int getReservation_count() {
		return reservation_count;
	}

	public void setReservation_count(int reservation_count) {
		this.reservation_count = reservation_count;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
		
}
