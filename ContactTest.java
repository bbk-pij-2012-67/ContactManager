import org.junit.*;
import static org.junit.Assert.*;

public class ContactTest{
	
	@Test
	public void TestsGetId(){
		int testSize = 100;
		Contact[] testContact = new ContactImpl[testSize];
		for(int c = 0;c<testSize;c++){
			testContact[c] = new ContactImpl(c,"" + c);
		}
		for(int c = 0;c<testSize;c++){
			assertEquals(c,testContact[c].getId());
		}
	}
	
	@Test
	public void TestsGetName(){
		int testSize = 100;
		Contact[] testContact = new ContactImpl[testSize];
		for(int c = 0;c<testSize;c++){
			testContact[c] = new ContactImpl(c,"" + c);
		}
		for(int c = 0;c<testSize;c++){
			assertTrue(("" + c).equals(testContact[c].getName()));
		}
	}
	
	@Test
	public void TestsAddGetNotes(){
		int testSize = 100;
		Contact[] testContact = new ContactImpl[testSize];
		String[] randomList = new String[100];
		for(int c = 0;c<testSize;c++){
			testContact[c] = new ContactImpl(c,"" + c);
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
}
