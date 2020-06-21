package coupons.management.exceptions;
/**
 * Coupons DB Exception
 * @author leonid S
 *
 */
public class CouponsDBException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CouponsDBException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
	
}
