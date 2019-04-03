import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VoicemailSystem {
	private static ArrayList<Box> boxes;
	private static String adminPassword;
	private static transient boolean adminLoginStatus = false;
	private static transient Scanner input = new Scanner(System.in);
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		adminPassword = "1234";
		try {
			File datfile = new File("voicemailbox.dat");
			if(datfile.exists()) {
				FileInputStream fis = new FileInputStream(datfile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				boxes = (ArrayList<Box>) ois.readObject();
				//System.out.println(boxes.size());
				//System.out.println("read");
				ois.close();
				fis.close();
			}
			else {
				 boxes = new ArrayList<Box>();
			}
		}
		catch(Exception e) {System.out.println(e);}
		
		
		System.out.println("Please enter 0 to login to your voicemail, 1 to dial a number, and 2 to login to the administrative panel: ");
		String result = readInput();
		
		if(result.equals("0")){
			System.out.println("Please enter a boxID: ");
			int boxID = Integer.parseInt(readInput());
			System.out.println("Please enter your passcode: ");
			String passcode = readInput();
			login(boxID, passcode);
		}
		else if(result.equals("1")) {
			System.out.println("Please enter a boxID: ");
			int boxID = Integer.parseInt(readInput());
			call(boxID);
		}
		else if(result.equals("2")) {
			System.out.println("Please enter admin passcode: ");
			String passcode = readInput();
			try{
				administrate(passcode);
			}
			catch(Exception e) {
				System.out.println(e);
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
		}
		else {
			System.out.println("Please enter a valid options.");
		}
		try {hangup();}
		catch(Exception ex) {System.out.println(ex);}
		finally {System.exit(0);}
	}
	
	public static void call(int boxID) {
		int index = Box.findBoxIdIndex(boxes, boxID);
		if(index >= 0) {
			System.out.println(boxes.get(index).getGreeting());
			String result = readVoice("Please leave your message, followed by the # key to signify you are finished.");
			if(result.equalsIgnoreCase("H")) {
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
			else {
				boxes.get(index).receiveMessage(new Message(result));
			}
		}
		else {
			System.out.println("Invalid box ID. Please call back.");
			try {hangup();}
			catch(Exception ex) {System.out.println(ex);}
			finally {System.exit(0);}
		}
	}
	
	public static void login(int boxID, String passcode) {
		int index = Box.findBoxIdIndex(boxes, boxID);
		if(index >= 0) {
			try{
				boxes.get(index).login(passcode);
				System.out.println("Enter 1 to retrieve messages, enter 2 to change greeting, enter 3 change the passcode, enter 4 to log out:");
				String result = readInput();
				if(result.equals("1")) {
					System.out.println("You have " + boxes.get(index).getNumMessages() + " messages. Enter 1 to play them all: ");
					result = readInput();
					if(result.equals("1")) {
						for(int i = 0; i < boxes.get(index).getNumMessages(); i++) {
							Message message = boxes.get(index).getMessage(i);
							System.out.println("Message " + i + ": " + message.getMessage());
						}
						try {hangup();}
						catch(Exception ex) {System.out.println(ex);}
						finally {System.exit(0);}
					}
				}
				else if(result.equals("2")) {
					result = readVoice("Please state your new greeting followed by the #");
					if(result.equalsIgnoreCase("H")) {
						try {hangup();}
						catch(Exception ex) {System.out.println(ex);}
						finally {System.exit(0);}
					}
					else {
						boxes.get(index).changeGreeting(result);
					}
					try {hangup();}
					catch(Exception ex) {System.out.println(ex);}
					finally {System.exit(0);}
					
				}
				else if(result.equals("3")) {
					System.out.println("Please enter your new passcode: ");
					String newPasscode = readInput();
					boxes.get(index).changePasscode(newPasscode);
					try {hangup();}
					catch(Exception ex) {System.out.println(ex);}
					finally {System.exit(0);}
				}
				else if(result.equals("4")) {
					boxes.get(index).logout();
					try {hangup();}
					catch(Exception ex) {System.out.println(ex);}
					finally {System.exit(0);}
				}
			}
			catch(Exception e) {
				System.out.println(e);
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
		}
		else {
			System.out.println("Box not found: please call back");
		}
	}
	
	public static void administrate(String adminPass) throws InvalidPin {
		if(adminPassword.equals(adminPass)) {
			adminLoginStatus = true;
			System.out.println("Please dial 1 to create a box.");
			String result = readInput();
			if(result.equals("1")) {
				try{
					createBox();
					System.out.println(boxes.size());
					try {hangup();}
					catch(Exception ex) {System.out.println(ex);}
					finally {System.exit(0);}
				}
				// safe to ignore since you don't have to be logged in to create a box
				catch(NotLoggedIn e) {}
			}
		}
		else {
			adminLoginStatus = false;
			throw new InvalidPin("Invalid pin: please try again");
		}
	}
	
	public static String readInput() {
		String result = "";
		String str = input.nextLine();
		while(!str.equals("#")) {
			if(str.equalsIgnoreCase("H")) {
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
			if(str.length() == 1) {
				if(Character.isDigit(str.charAt(0))) {
					result += str.charAt(0);
				}
			}
			str = input.nextLine();
		}
		return result;
	}
	
	public static String readVoice(String message) {
		System.out.println(message);
		String result = "";
		String intext = input.nextLine();
		while(!intext.equals("#")){
			if(intext.equalsIgnoreCase("H")) {
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
			else {
				result += intext;
			}
			intext = input.nextLine();
		}
		return result;
	}
	
	public static void hangup() throws IOException, ClassNotFoundException{
		System.out.println("Hanging up.");
		FileOutputStream fos = new FileOutputStream("voicemailbox.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(boxes);
		oos.flush();
		oos.close();
		fos.close();
		input.close();
		System.exit(0);
	}
	
	private static void createBox() throws NotLoggedIn {
		if(adminLoginStatus == true) {
			System.out.println("Please enter the boxID: ");
			int boxID = Integer.parseInt(readInput());
			System.out.println("Please enter the passcode: ");
			String passcode = readInput();
			try{
				boxes.add(new Box(boxID, passcode));
			}
			catch (Exception e){
				System.out.println(e);
				try {hangup();}
				catch(Exception ex) {System.out.println(ex);}
				finally {System.exit(0);}
			}
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied");
		}
	}
	
	// exceptions
	public static class InvalidPin extends Exception {
		private static final long serialVersionUID = -5885999723778000275L;

		public InvalidPin(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public static class AlreadyLoggedIn extends Exception{
		private static final long serialVersionUID = 447120984257737498L;

		public AlreadyLoggedIn(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public static class NotLoggedIn extends Exception{
		private static final long serialVersionUID = -8573973957631968187L;

		public NotLoggedIn(String errorMessage) {
			super(errorMessage);
		}
	}
}
