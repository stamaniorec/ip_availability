import java.text.SimpleDateFormat;
import java.util.Date;


public class Interval {
	
	private Date from;
	private Date to;
	private static String FORMAT = "yyyy-­MM-dd'T'HH'_'mm'_'ss.SSSZ";
	
	public Interval(Date from) {
		this.from = from;
		to = null;
	}
	
	public void setTo(Date to) { this.to = to; }
	
	private String formatDate(Date date) {
		return date == null ? null : new SimpleDateFormat(FORMAT).format(date);
	}
	
	public String from() { return formatDate(from); }
	public String to() { return formatDate(to); }

}
