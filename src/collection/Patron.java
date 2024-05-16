package collection;

public class Patron {
	 private int PatronID;
	 private String Name;
	 private String address;
	 private String phone;
	 private String email;
	
	 public Patron(int PatronID, String Name, String email, String phone, String address) {
		this.PatronID = PatronID;
		this.Name = Name;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

	public int getPatronID() {
		return PatronID;
	}

	public void setPatronID(int pnum) {
		this.PatronID = pnum;
	}

	public String getName() {
		return Name;
	}

	public void setName(String pname) {
		this.Name = pname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	  

}

