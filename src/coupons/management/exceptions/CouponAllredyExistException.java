package coupons.management.exceptions;
/**
 * Coupon All ready Exist Exception
 * @author leonid S
 *
 */
public class CouponAllredyExistException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CouponAllredyExistException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
