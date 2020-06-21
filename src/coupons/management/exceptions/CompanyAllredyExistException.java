package coupons.management.exceptions;
/**
 * Company All ready Exist Exception
 * @author leonid S
 *
 */
public class CompanyAllredyExistException extends Exception {
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CompanyAllredyExistException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
