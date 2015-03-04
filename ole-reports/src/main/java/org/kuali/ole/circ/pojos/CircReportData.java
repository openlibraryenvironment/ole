package org.kuali.ole.circ.pojos;

public class CircReportData {
	
	private String firstName;
	private String lastName;
	private String patronType;
	private String checkoutDate;
	private String itemLocation;
	private String itemBarcode;
	private String itemId;
	private String itemType;
	
	public String getPatronType() {
		return patronType;
	}
	public void setPatronType(String patronType) {
		this.patronType = patronType;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getCheckoutDate() {
		return checkoutDate;
	}
	public String getItemLocation() {
		return itemLocation;
	}
	public String getItemBarcode() {
		return itemBarcode;
	}
	public String getItemId() {
		return itemId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
		
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
		
	}
	public void setCheckoutDate(String checkoutDate) {
		this.checkoutDate = checkoutDate;
		
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
		
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
		
	}
	public void setItemLocation(String itemLocation) {
		this.itemLocation = itemLocation;
		
	}
	public void setItemBarcode(String itemBarcode) {
		this.itemBarcode = itemBarcode;
		
	}

}
