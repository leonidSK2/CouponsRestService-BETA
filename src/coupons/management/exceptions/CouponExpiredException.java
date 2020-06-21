package coupons.management.exceptions;
/**
 * Coupon Expired Exception
 * @author leonid S
 *
 */
public class CouponExpiredException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CouponExpiredException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
