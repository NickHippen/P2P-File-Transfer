package edu.unomaha.comm.net.vnk.semester.project;

public interface Action<RETURN_TYPE, INPUT_TYPE> {

	public RETURN_TYPE performAction(INPUT_TYPE input);
	
}
