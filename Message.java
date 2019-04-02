import java.util.Date;

public class Message implements Comparable<Message>{
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
}
