package edu.unomaha.comm.net.vnk.semester.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu<RETURN_TYPE, INPUT_TYPE> {

	private String[] messages;
	private Map<Integer, Action<RETURN_TYPE, INPUT_TYPE>> actionMap = new HashMap<>();
	
	public Menu(String[] messages) {
		this.messages = messages;
	}
	
	public void addAction(int choice, Action<RETURN_TYPE, INPUT_TYPE> action) {
		actionMap.put(choice, action);
	}
	
	public RETURN_TYPE prompt(Scanner input, INPUT_TYPE arg) {
		System.out.println();
		for (String message : messages) {
			System.out.println(message);
		}
		int choice = input.nextInt();
		if (!actionMap.containsKey(choice)) {
			System.out.println("Invalid choice!");
			return prompt(input, arg);
		}
		Action<RETURN_TYPE, INPUT_TYPE> action = actionMap.get(choice);
		return action.performAction(arg);
	}
	
}
