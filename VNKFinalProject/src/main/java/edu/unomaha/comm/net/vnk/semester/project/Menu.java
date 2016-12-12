package edu.unomaha.comm.net.vnk.semester.project;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A menu object for prompting the user options
 * @author Nick Hippen
 *
 * @param <RETURN_TYPE> the type to be returned by an action & upon prompting
 * @param <INPUT_TYPE> the type to be taken in as an argument for an action & upon prompting
 */
public class Menu<RETURN_TYPE, INPUT_TYPE> {

	private final String[] messages;
	private Map<Integer, Action<RETURN_TYPE, INPUT_TYPE>> actionMap = new HashMap<>();
	
	/**
	 * Constructs a new menu
	 * @param messages the messages to be displayed when prompting the menu
	 */
	public Menu(String[] messages) {
		this.messages = messages;
	}
	
	/**
	 * Adds an action to the action map
	 * @param choice the key to place the action under
	 * @param action the action to place under the key
	 */
	public void addAction(int choice, Action<RETURN_TYPE, INPUT_TYPE> action) {
		actionMap.put(choice, action);
	}
	
	/**
	 * Prompts the user for a set of options to perform an action
	 * @param input the scanner to be used to gather user input
	 * @param arg an argument to be passed to an action
	 * @return the result of the prompt
	 */
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
