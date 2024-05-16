package collection;

public class DeweyClassification {
	
	int classificationNumber ;
	String description;
	 
	public DeweyClassification(int classificationNumber, String description) {
		this.classificationNumber = classificationNumber;
		this.description = description;
	}
	
	
	public int getClassificationNumber() {
		return classificationNumber;
	}
	public void setClassificationNumber(int classificationNumber) {
		this.classificationNumber = classificationNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	 
	 
}
