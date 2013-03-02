import java.util.Calendar;
import java.util.Set;

/**
* A meeting that was held in the past.
*
* It includes your notes about what happened and what was agreed.
*/
public class PastMeetingImpl extends MeetingImpl implements PastMeeting{
	
	private String notes;
	
	/**
	* Constructor creates a new PastMeetingImpl object.
	*
	* @param id int value of the meeting ID.
	* @param date Calendar object storing the date of the meeting.
	* @param contacts Set<Contact> object storing a list of contacts for the meeting.
	* @param notes String object containing the notes for the meeting.
	*/
	public PastMeetingImpl(int id, Calendar date, Set<Contact> contacts,String notes){
		super(id,date,contacts);
		this.notes = notes;
	}
	
	public PastMeetingImpl(){
	}
		
	
	/**
	* Returns the notes from the meeting.
	*
	* If there are no notes, the empty string is returned.
	*
	* @return the notes from the meeting.
	*/
	public String getNotes(){
		return notes;
	}
	
	public void addNotes(String notes){
		this.notes = notes;
	}
}
