import java.util.Calendar;
import java.util.Set;

/**
* A meeting to be held in the future
*/
public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting{
	// No methods here, this is just a naming interface

	// (i.e. only necessary for type checking and/or downcasting)
	/**
	* Constructor.
	* 
	* @param id int value of the new future meeting id.
	* @param date Calender object signifying the date that the meeting is to take place.
	* @param contacts Set<Contact> object containing a set of contacts attending the meeting.
	*/
	public FutureMeetingImpl(int id, Calendar date, Set<Contact> contacts){
		super(id,date,contacts);
	}
	
	/**
	* No-argument constructor
	*/
	public FutureMeetingImpl(){
		
	}
	

}
