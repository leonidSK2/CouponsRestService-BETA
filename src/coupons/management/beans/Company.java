package coupons.management.beans;

import java.util.ArrayList;

/**
 * 
 * @author leonid S 
 * Company bean
 */
public class Company {
	private long id;
	private String companyName, password, email;
	private ArrayList<Coupon> coupons;

	/**
	 * Constructor with Company data
	 * 
	 * @param company id
	 * @param company Name
	 * @param company password
	 * @param company email
	 */
	public Company(long id, String companyName, String password, String email) {
		this.id = id;
		this.companyName = companyName;
		this.password = password;
		this.email = email;
		coupons = new ArrayList<Coupon>();
	}

	/**
	 * get the Company Name
	 * 
	 * @return company name
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * set the company name
	 * 
	 * @param companyName
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * get the password of company
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * set the company Password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * get the company email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * set the company email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * get company id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * build string with all Parameters of the company
	 */
	@Override
	public String toString() {
		return "Company [id=" + id + ", companyName=" + companyName + ", password=" + password + ", email=" + email
				+ ", coupons=" + coupons + "]";
	}

}
