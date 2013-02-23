import java.util.Calendar;
import java.util.Set;

/**
* A meeting that was held in the past.
*
* It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting{
	
	/**
	* Constructor creates a new MeetingImpl object.
	*
	* @param id int value of the meeting ID.
	* @param date Calendar object storing the date of the meeting.
	* @param contacts Set<Contact> object storing a list of contacts for the meeting.
	*/
	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts){
		super(id,date,contacts);
	}
	
	/**
	* Returns the notes from the meeting.
	*
	* If there are no notes, the empty string is returned.
	*
	* @return the notes from the meeting.
	*/
	public String getNotes(){
		return null;
	}
}
