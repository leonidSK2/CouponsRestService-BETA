package coupons.management.beans;

import java.util.Date;

/**
 * 
 * @author leonid S Coupon bean
 */
public class Coupon {
	private long id;
	private String title;
	private Date startDate, endDate;
	private double price;
	private int amount;
	private CouponType type;
	private String message;
	private String image;

	/**
	 * all Parameters of coupon
	 * 
	 * @param coupon id
	 * @param coupon title
	 * @param coupon startDate
	 * @param coupon endDate
	 * @param coupon price
	 * @param coupon amount
	 * @param coupon type
	 * @param coupon message
	 * @param coupon image
	 */
	public Coupon(long id, String title, Date startDate, Date endDate, double price, int amount, CouponType type,
			String message, String image) {
		super();
		this.id = id;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.price = price;
		this.amount = amount;
		this.type = type;
		this.message = message;
		this.image = image;
	}

	/**
	 * get the title of coupon
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * set title for the coupon
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * get the start date of the coupon
	 * 
	 * @return start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * get the start date for the coupon
	 * 
	 * @param start Date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * get the end date of the coupon
	 * 
	 * @return endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * set the end date for the coupon
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * get the price of the coupon
	 * 
	 * @return price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * set the price for the coupon
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * get the amount of the coupon
	 * 
	 * @return amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * set the amount for the coupon
	 * 
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/*
	 * get the type of the coupon
	 */
	public CouponType getType() {
		return type;
	}

	/**
	 * set me type for coupon
	 * 
	 * @param type
	 */
	public void setType(CouponType type) {
		this.type = type;
	}

	/**
	 * get the coupon id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * get massage of the coupon
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * set a message for the coupon
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * get image of the coupon
	 * 
	 * @return image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * set image for the coupon
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * build string with all Parameters of coupon
	 */
	@Override
	public String toString() {
		return "Coupon [id=" + id + ", title=" + title + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", price=" + price + ", amount=" + amount + ", type=" + type + ", message=" + message + ", image="
				+ image + "]";
	}

}
