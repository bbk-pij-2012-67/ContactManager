import java.util.Calendar;
import java.util.Set;
import java.io.*;

/**
* A class to represent meetings
*
* Meetings have unique IDs, scheduled date and a list of participating contacts
*/
public abstract class MeetingImpl implements Meeting, Comparable<Meeting>, Serializable {
	
	private int id;
	private Calendar date;
	private Set<Contact> contacts;
	
	/**
	* Constructor creates a new MeetingImpl object.
	*
	* @param id int value of the meeting ID.
	* @param date Calendar object storing the date of the meeting.
	* @param contacts Set<Contact> object storing a list of contacts for the meeting.
	*/
	public MeetingImpl(int id, Calendar date, Set<Contact> contacts){
		this.id = id;
		this.date = date;
		this.contacts = contacts;
	}
	
	/**
	* No-argument constructor. Required for serialization.
	*/
	public MeetingImpl(){
	}
	
	/**
	* id setter.
	*
	* @param id Value to set to id field.
	*/
	public void setId(int id){
		this.id = id;
	}
	
	/**
	* date setter.
	*
	* @param date Value to set to date field.
	*/
	public void setDate(Calendar date){
		this.date = date;
	}
	
	/**
	* contacts setter.
	*
	* @param contacts Value to set to contacts field.
	*/
	public void setContacts(Set<Contact> contacts){
		this.contacts = contacts;
	}
	
	/**
	* Returns the id of the meeting.
	*
	* @return the id of the meeting.
	*/
	public int getId(){
		return id;
	}
		
	
	/**
	* Return the date of the meeting.
	*
	* @return the date of the meeting.
	*/
	public Calendar getDate(){
		return date;
	}
	
	/**
	* Return the details of people that attended the meeting.
	*
	* The list contains a minimum of one contact (if there were
	* just two people: the user and the contact) and may contain an
	* arbitrary number of them.
	*
	* @return the details of people that attended the meeting.
	*/
	public Set<Contact> getContacts(){
		return contacts;
	}
	
	/**
	* Return result of comparison.
	*
	* Compares meetings based on date of meeting
	*
	* @param m Meeting to compare.
	* @return An int value indicating the result of comparison. 
	*	If the meeting to compare is later than this meeting, the result is -1.
	*	If the meeting to compare is at the same time as this meeting, the result is 0.
	*	If the meeting to compare is earlier than this meeting, the result is 1.
	*/
	@Override
	public int compareTo(Meeting m){
		if((this.getDate()).before(m.getDate())){
				return -1;
		}
		if ((this.getDate()).after(m.getDate())){
				return 1;
		}else{
			return 0;
		}
	}
}
