package coupons.management.exceptions;
/**
 * Company Not Found Exception
 * @author leonid S
 *
 */
public class CompanyNotFoundException extends Exception {
	
	private static final long serialVersionUID = 1L;
	private String source;
	
	public CompanyNotFoundException(String message, String source) {
		super(message);
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
