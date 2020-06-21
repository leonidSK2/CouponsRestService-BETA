package coupons.management.exceptions;
/**
 *Customer Already Exist Exception 
 * @author leonid S
 *
 */
public class CustomerAllredyExistException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CustomerAllredyExistException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
