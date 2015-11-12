import java.util.Date;


public class Interval {
	
	private Date from;
	private Date to;
	
	public Interval(Date from, Date to) {
		this.from = from;
		this.to = to;
	}
	
	public Date from() { return from; }
	public Date to() { return to; }

}
