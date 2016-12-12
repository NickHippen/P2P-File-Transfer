package edu.unomaha.comm.net.vnk.semester.project;

/**
 * An interface to provide an implementation to performAction(input)
 * @author Nick Hippen
 *
 * @param <RETURN_TYPE> the type to be returned by performAction(input)
 * @param <INPUT_TYPE> the type of argument to be passed into performAction(input)
 */
public interface Action<RETURN_TYPE, INPUT_TYPE> {

	/**
	 * An action to be performed
	 * @param input the input provided
	 * @return the result of the action
	 */
	public RETURN_TYPE performAction(INPUT_TYPE input);
	
}
