import org.junit.*;
import static org.junit.Assert.*;

public class ContactTest{
	
	private int testSize;
	private Contact[] testContact;
	private String[] randomList;
	
	@Before
	public void initialise(){
		testSize = 100;
		testContact = new ContactImpl[testSize];
		randomList = new String[100];
		for(int c = 0;c<testSize;c++){
			testContact[c] = new ContactImpl(c,"" + c);
		}
	}
	
	@Test
	public void testsGetId(){
		
		for(int c = 0;c<testSize;c++){
			assertEquals(c,testContact[c].getId());
		}
	}
	
	@Test
	public void testsGetName(){	
		for(int c = 0;c<testSize;c++){
			assertTrue(("" + c).equals(testContact[c].getName()));
		}
	}
	
	@Test
	public void testsAddGetNotes(){
		
		for(int c = 0;c<testSize;c++){
			randomList[c] = "" + Math.random();
			testContact[c].addNotes("" + randomList[c]);
		}
		
		
		for(int c = 0;c<testSize;c++){
			assertEquals(randomList[c],testContact[c].getNotes());
		}
		// test for no notes saved.
		Contact testContact2 = new ContactImpl(99999,"a");
		assertEquals("",testContact2.getNotes());
	}
	
	@After
	public void cleanUp(){
		testSize = 0;
		testContact = null;
		randomList = null;
	}
}
