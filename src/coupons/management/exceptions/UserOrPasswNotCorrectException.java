package coupons.management.exceptions;
/**
 * User Or Password Not Correct Exception
 * @author leonid S
 *
 */
public class UserOrPasswNotCorrectException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public UserOrPasswNotCorrectException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
