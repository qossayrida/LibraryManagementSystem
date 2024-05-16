package collection;

public class Librarian {
	 private int librarianID;
	 private String name;
	 private String email;
	 private String phone;
	 private String address;
	 private String employment_date;
	 private String password;
	public Librarian(int librarianID, String name, String email, String phone, String address, String employment_date,
			String password) {
		super();
		this.librarianID = librarianID;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.employment_date = employment_date;
		this.password = password;
	}
	public int getLibrarianID() {
		return librarianID;
	}
	public void setLibrarianID(int librarianID) {
		this.librarianID = librarianID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmployment_date() {
		return employment_date;
	}
	public void setEmployment_date(String employment_date) {
		this.employment_date = employment_date;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	 

}
