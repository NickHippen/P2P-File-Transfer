package edu.unomaha.comm.net.vnk.semester.project;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * The base runner class of VNKP2P
 * @author Nick hippen
 *
 */
public class P2PRunner {

	private static Scanner input;
	
	private static final Menu<Boolean, DNSUser> MAIN_MENU = new Menu<>(new String[] {
		"Welcome to VNKP2P!",
		"Please enter the option you would like to do: ",
		"[1] Add File",
		"[2] Receive File",
		"[3] Disconnect & Exit"
	});
	
	private static final Menu<String, Void> STORE_MENU = new Menu<>(new String[] {
		"What would you like the key to be?",
		"[1] Enter custom key name",
		"[2] Generate random key"
	});
	
	/**
	 * Sets up the menu options
	 */
	static {
		// Main Menu
		// Store File
		MAIN_MENU.addAction(1, new Action<Boolean, DNSUser>() {
			@Override
			public Boolean performAction(DNSUser dnsUser) {
				System.out.print("Enter the file that you would like to store: ");
				input.nextLine();
				String filePath = input.nextLine();
				String keyName = STORE_MENU.prompt(input, null);
				try {
					System.out.println("Storing file on DHT under key '" + keyName + "'...");
					dnsUser.store(keyName, filePath);
					System.out.println("The file has been stored.");
				} catch (IOException e) {
					e.printStackTrace();
					dnsUser.shutdown();
					System.exit(1);
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});
		// Receive File
		MAIN_MENU.addAction(2, new Action<Boolean, DNSUser>() {
			@Override
			public Boolean performAction(DNSUser dnsUser) {
				System.out.print("Enter the key name of the file you would like to receive: ");
				input.nextLine();
				String keyName = input.nextLine();
				try {
					System.out.println("Retrieving file from DHT...");
					dnsUser.get(keyName);
					System.out.println("The file has been received.");
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
					dnsUser.shutdown();
					System.exit(1);
					return Boolean.FALSE;
				}
				return Boolean.TRUE;
			}
		});
		// Exit
		MAIN_MENU.addAction(3, new Action<Boolean, DNSUser>() {
			@Override
			public Boolean performAction(DNSUser dnsUser) {
				System.out.println("Informing peers of shutdown & leaving netowrk...");
				dnsUser.shutdown();
				return Boolean.FALSE;
			}
		});
		
		// Store Menu
		// Custom Key Name
		STORE_MENU.addAction(1, new Action<String, Void>() {
			@Override
			public String performAction(Void v) {
				System.out.print("What would you like the key to be? ");
				input.nextLine();
				return input.nextLine();
			}
		});
		// Random Key Name
		STORE_MENU.addAction(2, new Action<String, Void>() {
			@Override
			public String performAction(Void input) {
				String keyName = Integer.toString(new Random().nextInt(Integer.MAX_VALUE));
				System.out.println("Your generated key name is '" + keyName + "'.");
				return keyName;
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		input = new Scanner(System.in);
		System.out.print("Enter the port you would like to open on: ");
		int port = input.nextInt();
		System.out.print("Enter the port you would like to connect to or enter -1 to be the first peer in the network: ");
		int connectionPort = input.nextInt();
		if (connectionPort == -1) {
			// "Connect" to itself
			connectionPort = port;
		}
		System.out.println((port == connectionPort ? "Creating" : "Joining") + " network...");
		DNSUser dnsUser = new DNSUser(port, connectionPort);
		System.out.println();
		while (MAIN_MENU.prompt(input, dnsUser)) {}
		System.out.println("Goodbye.");
		input.close();
	}
	
}
