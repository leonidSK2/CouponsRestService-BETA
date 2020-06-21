package coupons.management.exceptions;
/**
 * Coupon Not Found Exception
 * @author leonid S
 *
 */
public class CouponNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CouponNotFoundException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
