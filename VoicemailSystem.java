import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class VoicemailSystem {
	private static ArrayList<Box> boxes = new ArrayList<Box>();
	private String adminPassword = "1234";
	private transient boolean adminLoginStatus = false;
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		try {
			File datfile = new File("voicemailbox.dat");
			if(datfile.exists()) {
				FileInputStream fis = new FileInputStream(datfile);
				ObjectInputStream ois = new ObjectInputStream(fis);
				boxes = (ArrayList<Box>) ois.readObject();
				ois.close();
				fis.close();
			}
		}
		catch(Exception e) {
			
		}
		
		
		System.out.println("Please enter an extension: ");

		
		input.close();
	}
	
	public void call(int boxID) {
		
	}
	
	public void login(int boxID, String passcode) {
		
	}
	
	public void administrate(String adminPassword) throws IncorrectPin {
		if(this.adminPassword.equals(adminPassword)) {
			adminLoginStatus = true;
		}
		else {
			throw new IncorrectPin("Incorrect pin: please try again");
		}
	}
	
	public void hangup() throws IOException, ClassNotFoundException{
		FileOutputStream fos = new FileOutputStream("voicemailbox.dat");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(boxes);
		oos.flush();
		oos.close();
		fos.close();
		System.exit(0);
	}
	
	// exceptions
	public class IncorrectPin extends Exception {
		private static final long serialVersionUID = -5885999723778000275L;

		public IncorrectPin(String errorMessage) {
			super(errorMessage);
		}
	}
}
