package net.bpelunit.framework.model.test.data;

@SuppressWarnings("serial")
/**
 * This exception is used only during internal activity evaluation in order to show
 * that this activity cannot be executed successfully.
 * 
 * If it is a real execution, this exception must not be thrown but instead the status
 * needs to be set accordingly.
 * 
 * @author Daniel Lübke
 */
public class CannotEvaluateException extends RuntimeException {

	public CannotEvaluateException(Exception e) {
		super(e);
	}

}
