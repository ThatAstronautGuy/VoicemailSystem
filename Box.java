import java.util.ArrayList;
import java.io.*;

public class Box implements java.io.Serializable {
	private static final long serialVersionUID = -8961537260269159115L;
	private int boxID;
	private String greeting;
	private transient boolean loginStatus;
	private ArrayList<Message> messages = new ArrayList<Message>();
	private String passcode;
	
	public Box(int boxID) {
		this.boxID = boxID;
		greeting = "User has not set a greeting.";
	}
	
	public void changeGreeting(String greeting) throws NotLoggedIn {
		if(loginStatus == true) {
			if(greeting.length() == 0) {
				this.greeting = "User has not set a greeting.";
			}
			else {
				this.greeting = greeting;
			}
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied");
		}
	}
	
	public void changePasscode(String passcode) throws InvalidPin, NotLoggedIn {
		if(loginStatus == true) {
			try {
				int num = Integer.parseInt(passcode);
				if(num < 0) {
					throw new InvalidPin("Invalid pin: pin is negative");
				}
				else {
					this.passcode = passcode;
				}
			}
			catch (NumberFormatException e){
				throw new InvalidPin("Invalid pin: not a number");
			}
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied");
		}
	}
	
	public void deleteMessage(int index) throws NotLoggedIn, InvalidIndex {
		if(loginStatus == true) {	
			if(index < getNumMessages() && index >= 0) {
				messages.remove(index);
			}
			else {
				throw new InvalidIndex("Invalid index: index out of bounds");
			}
			
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied");
		}
	}
	
	public void deleteAllMessages() throws NotLoggedIn {
		if(loginStatus == true) {
			messages = new ArrayList<Message>();
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied"); 
		}
	}
	
	public Message getMessage(int index) throws NotLoggedIn, InvalidIndex {
		if(loginStatus == true) {	
			if(index < getNumMessages() && index >= 0) {
				return messages.get(index);
			}
			else {
				throw new InvalidIndex("Invalid index: index out of bounds");
			}
			
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied");
		}
	}

	public int getBoxID() {
		return boxID;
	}
	
	public String getGreeting() {
		return greeting;
	}
	
	public boolean login(String passcode) throws AlreadyLoggedIn, IncorrectPin {
		if(loginStatus == true) {
			throw new AlreadyLoggedIn("Already logged in: please logout first");
		}
		else {
			if(this.passcode.contentEquals(passcode)) {
				loginStatus = true;
				return true;
			}
			else {
				throw new IncorrectPin("Incorrect pin: please try again");
			}
		}
	}
	
	public void logout() throws NotLoggedIn {
		if(loginStatus == true) {
			loginStatus = false;
		}
		else {
			throw new NotLoggedIn("Not logged in: please login to log out");
		}
	}
	
	public void receiveMessage(Message message) {
		messages.add(message);
	}
	
	public int getNumMessages() throws NotLoggedIn {
		if(loginStatus == true) {
			return messages.size();
		}
		else {
			throw new NotLoggedIn("Not logged in: access denied"); 
		}
	}
	
	// serialize functions
	public final void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}
	
	public final void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
		ois.defaultReadObject();
	}
	
	
	// exception classes
	public class InvalidPin extends Exception{
		private static final long serialVersionUID = -4095214552816804474L;

		public InvalidPin(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public class NotLoggedIn extends Exception{
		private static final long serialVersionUID = -5472123155936872795L;

		public NotLoggedIn(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public class AlreadyLoggedIn extends Exception{
		private static final long serialVersionUID = 447120984257737498L;

		public AlreadyLoggedIn(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public class IncorrectPin extends Exception {
		private static final long serialVersionUID = 1772054241922437429L;

		public IncorrectPin(String errorMessage) {
			super(errorMessage);
		}
	}
	
	public class InvalidIndex extends Exception {
		private static final long serialVersionUID = -2238378927268688766L;

		public InvalidIndex(String errorMessage) {
			super(errorMessage);
		}
	}
}
