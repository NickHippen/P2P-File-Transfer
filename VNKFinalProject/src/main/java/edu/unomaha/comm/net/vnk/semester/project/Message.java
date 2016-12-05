package edu.unomaha.comm.net.vnk.semester.project;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -7066331159105748322L;
	
	private String message;
	
	public Message(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	@Override
	public String toString() {
		return getMessage();
	}
	
}
