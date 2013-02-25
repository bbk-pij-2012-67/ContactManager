import org.junit.*;
import static org.junit.Assert.*;
import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

public class ContactManagerTest{

	private Set<Contact> testContacts;
	private Calendar[] calArray;
	private int testSize;
	ContactManager cm;
	private int[] futureMeetingIDs;

	@Before
	public void initialize(){
		testSize = 100;
		futureMeetingIDs = new int[testSize];
		cm = new ContactManagerImpl();
		//Create a set of contacts
		testContacts = new TreeSet<Contact>();
		for(int c = 0;c < 10;c++){
			testContacts.add(new ContactImpl(c,"" + c));
			cm.addNewContact("" + c,"" + c + c);
		}

		//Store some dates in an array.
		calArray = new Calendar[testSize];
		for(int c = 0;c<testSize;c++){

			//Create a random date
			int year = (int)(Math.random() * 5)+2013;
			int month = (int)(Math.random() * 12)+ 1;
			int day = (int)(Math.random() * 28)+ 1;
			calArray[c] = Calendar.getInstance();
			calArray[c].set(year,month,day);
		}


		//Create FutureMeeting objects
		for(int c = 0; c < testSize; c++){
			futureMeetingIDs[c] = cm.addFutureMeeting(testContacts,calArray[c]);
		}
	}

	@Test
	public void TestsGetFutureMeeting(){
		FutureMeeting fm;
		for(int c = 0; c < testSize; c++){
			fm = cm.getFutureMeeting(futureMeetingIDs[c]);

			assertEquals(fm.getContacts(),testContacts);
		}
		//test for non-existent id
		fm = cm.getFutureMeeting(-1);
		assertEquals(fm,null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestsAddFutureMeetingPastDate(){
		Calendar date = Calendar.getInstance();
		date.set(2010,1,1);
		cm.addFutureMeeting(testContacts,date);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestsAddFutureMeetingContactUnknown(){
		Calendar date = Calendar.getInstance();
		date.add(Calendar.DATE,1);
		cm.addFutureMeeting(testContacts,date);
	}
	
	@Test(expected = NullPointerException.class)
	public void TestsAddNewContactNoName(){
		cm.addNewContact(null,"a");
	}
	
	@Test(expected = NullPointerException.class)
	public void TestsAddNewContactNoName(){
		cm.addNewContact("a",null);
	}


	@After
	public void CleanUp(){
		testContacts = null;
		calArray= null;
		cm = null;
		futureMeetingIDs = null;
	}
}
