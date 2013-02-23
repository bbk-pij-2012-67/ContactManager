import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;



public class MeetingTest{
	
	private int testSize;
	private Meeting[] testMeeting;
	private Set<Contact> testContacts;
	private Calendar[] calArray;
	
	@Before
	public void initialise(){
		testSize = 100;
		testMeeting = new PastMeetingImpl[testSize];
		//Create a set of contacts
		testContacts = new TreeSet<Contact>();
		for(int c = 0;c < 10;c++){
			testContacts.add(new ContactImpl(c,"" + c));
		}
		//Add test dates to the meetings. Store the dates in an
		//array.
		calArray = new Calendar[testSize];
		for(int c = 0;c<testSize;c++){
			
			//Create a random date
			int year = (int)(Math.random() * 5)+2013;
			int month = (int)(Math.random() * 12)+ 1;
			int day = (int)(Math.random() * 28)+ 1;
			calArray[c] = Calendar.getInstance();
			calArray[c].set(year,month,day);
			//System.out.println(calArray[c].getTime().toString());
			//cal.set(year,month,day);
			testMeeting[c] = new MeetingImpl(c,(Calendar)(calArray[c].clone()),testContacts);
			
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
		for(int c = 0;c<testSize;c++){
			assertTrue(calArray[c].equals(testMeeting[c].getDate()));
		}
	}
	
	@Test
	public void testsGetContacts(){
		for(int c = 0;c<testSize;c++){
			assertTrue(testMeeting[c].getContacts()==testContacts);
		}
	}
	
	
	
	@After
	public void cleanUp(){
		testSize = 0;
		testMeeting = null;
		testContacts = null;
		calArray = null;
	}
}
	
	
	
