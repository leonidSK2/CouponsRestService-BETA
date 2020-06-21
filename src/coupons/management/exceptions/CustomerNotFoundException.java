package coupons.management.exceptions;
/**
 * Customer Not Found Exception
 * @author leonid S
 *
 */
public class CustomerNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CustomerNotFoundException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
