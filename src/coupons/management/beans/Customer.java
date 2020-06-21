package coupons.management.beans;

import java.util.ArrayList;

/**
 * 
 * @author leonid S Customer bean
 */
public class Customer {
	private long id;
	private String custName;
	private String password;
	private ArrayList<Coupon> coupons;

	/**
	 * 
	 * @param customer id
	 * @param custName
	 * @param customer password
	 */
	public Customer(long id, String custName, String password) {
		super();
		this.id = id;
		this.custName = custName;
		this.password = password;
		coupons = new ArrayList<Coupon>();
	}

	/**
	 * get the name of the customer
	 * 
	 * @return custName
	 */
	public String getCustName() {
		return custName;
	}

	/**
	 * set name for the customer
	 * 
	 * @param custName
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * get id for the customer
	 * 
	 * @return customer id
	 */
	public long getId() {
		return id;
	}

	/**
	 * get the customer password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * list of all customer coupons
	 * 
	 * @return coupons
	 */
	public ArrayList<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * set customer password
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *  build string with all Parameters of customer
	 * 
	 */
	@Override
	public String toString() {
		return "Customer [id=" + id + ", custName=" + custName + ", password=" + password + ", coupons=" + coupons
				+ "]";
	}

}
