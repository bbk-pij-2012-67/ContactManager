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
	private int[] MeetingIDs;

	@Before
	public void initialize(){
		testSize = 100;
		MeetingIDs = new int[testSize];
		cm = new ContactManagerImpl();
		//Create a set of contacts
		testContacts = new TreeSet<Contact>();
		for(int c = 0;c < 10;c++){
			//Create a test list and add identical contacts
			//assume that IDs in the manager start from 1.
			testContacts.add(new ContactImpl(c + 1,"" + c));
			cm.addNewContact("" + c,"" + c + c);
		}

		//Store some dates in an array.
		calArray = new Calendar[testSize];
		for(int c = 0;c<testSize/2;c++){
			//Create a random future date
			//half the testsize amount
			calArray[c] = getRandomDate(false);
		}
		for(int c = testSize/2 + 1;c < testSize;c++){
			//Create a random past date
			//half the testsize amount
			calArray[c] = getRandomDate(true);
		}


		//Create FutureMeeting objects
		for(int c = 0; c < testSize/2; c++){
			MeetingIDs[c] = cm.addFutureMeeting(testContacts,calArray[c]);
		}
		//Create PastMeeting objects
		for(int c = testSize/2 + 1;c < testSize;c++){
			MeetingIDs[c] = cm.addNewPastMeeting(testContacts,calArray[c],"" + c);
		}
	}
	
	private Calendar getRandomDate(boolean pastDate){
		Calendar date = null;
		

			//Create a random date
			date = Calendar.getInstance();
			int yearOffset = date.get(Calendar.YEAR);
			if(pastDate){
				yearOffset = yearOffset  - 100;
			}else{
				yearOffset = yearOffset  + 100;
			}
			int year = (int)(Math.random() * 5)+yearOffset;
			int month = (int)(Math.random() * 12)+ 1;
			int day = (int)(Math.random() * 28)+ 1;
			
			date.set(year,month,day);
		
		return date;
	}
		

	@Test
	public void TestsGetFutureMeeting(){
		FutureMeeting fm;
		for(int c = 0; c < testSize/2; c++){
			fm = cm.getFutureMeeting(MeetingIDs[c]);

			assertEquals(fm.getContacts(),testContacts);
		}
		//test for non-existent id
		fm = cm.getFutureMeeting(-1);
		assertEquals(fm,null);
	}
	
	@Test
	public void TestsGetMeeting(){
		Meeting mg;
		for(int c = 0; c < testSize; c++){
			mg = cm.getMeeting(MeetingIDs[c]);

			assertEquals(fm.getContacts(),testContacts);
		}
		//test for non-existent id
		fm = cm.getMeeting(-1);
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
		Set<Contact> contacts = new TreeSet<>();
		contacts.add(new ContactImpl(9,"jjjjj"));
		cm.addFutureMeeting(contacts,date);
	}
	
	@Test(expected=NullPointerException.class)
	public void TestsAddNewContactNoName(){
		cm.addNewContact(null,"a");
	}
	
	@Test(expected=NullPointerException.class)
	public void TestsAddNewContactNoNotes(){
		cm.addNewContact("a",null);
	}
	
	@Test
	public void TestsGetContactsByIDs(){
		Set<Contact> contacts = cm.getContacts(1,2,3,4,5,6,7,8,9,10);
		assertEquals(contacts.size(),10);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void TestsGetContactsByInvalidID(){
		Set<Contact> contacts = cm.getContacts(9999);
		assertEquals(contacts.size(),1);
	}
	
	@Test
	public void TestsGetContactsByName(){
		for(int c = 0;c < 10; c++){
			Set<Contact> contacts = cm.getContacts("" + c);
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void TestsGetContactsByInvalidName(){
		for(int c = 0;c < 10; c++){
			Set<Contact> contacts = cm.getContacts("9999");

		}
	}


	@After
	public void CleanUp(){
		testContacts = null;
		calArray= null;
		cm = null;
		MeetingIDs = null;
	}
}
