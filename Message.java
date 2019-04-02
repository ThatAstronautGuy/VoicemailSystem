import java.util.Date;
import java.io.*;

public class Message implements Comparable<Message>, java.io.Serializable{
	private static final long serialVersionUID = -9046610004769247786L;
	private String caller;
	private Date datetime;
	private String message;
	
	public Message(String caller, String message) {
		this.caller = caller;
		this.message = message;
		datetime = new Date();
	}
	
	public String getCaller() {
		return caller;
	}
	
	public Date getDateTime() {
		return datetime;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int compareTo(Message m) {
		return datetime.compareTo(m.getDateTime());
	}
	
	// serialize functions
	public final void writeObject(ObjectOutputStream oos) throws IOException {
		oos.defaultWriteObject();
	}
	
	public final void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException{
		ois.defaultReadObject();
	}
}
