package utility;

/**
 * An exception thrown when a player breaks a rule
 */
public class BrokeRuleException extends Exception {
	public BrokeRuleException(String violation) {
		super(violation);
	}
}
