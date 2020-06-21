package coupons.management.exceptions;
/**
 * Coupon Already Purchase Exception
 * @author leonid S
 *
 */
public class CouponAllredyPurchaseException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CouponAllredyPurchaseException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
