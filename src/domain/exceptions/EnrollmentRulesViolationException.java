package domain.exceptions;

public class EnrollmentRulesViolationException extends Exception {

	public EnrollmentRulesViolationException(String msg) {
		super(msg);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		EnrollmentRulesViolationException other = (EnrollmentRulesViolationException)obj;
		return this.getMessage().equals(other.getMessage());
	}
}
