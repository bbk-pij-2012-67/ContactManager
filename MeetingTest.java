import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;

public class MeetingTest{
	
	private int testSize;
	private Meeting[] testMeeting;
	private Set<Contact> testContact;
	private CalendarData calData;
	
	@Before
	public void initialise(){
		testSize = 100;
		testMeeting = new MeetingImpl[testSize];
		testContact = new ContactImpl[10];
		for(int c = 0;c < 10;c++){
			testContact.add(new ContactImpl(c,"" + c));
		}
		//randomList = new String[100];
		//Add test dates to the meetings. Store the dates in an
		//array.
		for(int c = 0;c<testSize;c++){
			Calendar cal = Calendar.getInstance();
			calData = new CalendarData[testSize];
			int year = (int)(Math.random * 5)+2013;
			int month = (int)(Math.random * 12)+ 1;
			int day = (int)(Math.random * 28)+ 1;
			calData[c] = new CalendarData(year,month,day);
			cal.set(year,month,day)
			testMeeting[c] = new MeetingImpl(c,cal,testContact);
		}
	}
	
	@Test
	public void testsGetId(){
		for(int c = 0;c<testSize;c++){
			assertEquals(c,testMeeting[c].getId());
		}
	}
	
	@Test
	public void testsGetDate(){
	}
	
	private class CalendarData{
		public int _year;
		public int _month;
		public int _day;
	
		public CalendarDate(int year, int month, int, day){
			this._year=year;
			this._month=month;
			this._day=day;
		}
	}
	
