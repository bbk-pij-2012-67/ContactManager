import java.util.Calendar;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.io.FileOutputStream;

import java.beans.XMLEncoder;

import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.*;
/**
* A class to manage your contacts and meetings.
*/
public class ContactManagerImpl implements ContactManager, Serializable {

	private List<Meeting> meetings = new LinkedList<>();
	private Set<Contact> contacts = new TreeSet<>();
	private int nextContactId;
	private int nextMeetingId;

	public ContactManagerImpl(){
		
		//contacts = new TreeSet<>();
		//nextContactId = 0;
		//nextMeetingId = 0;
		//meetings = new LinkedList<>();
	}

	//public ContactManagerImpl(List<Meeting> meetings,Set<Contact> contacts, int nextContactId, int nextMeetingId){
	//	this.contacts = contacts;
	//	this.nextContactId = nextContactId;
	//	this.nextMeetingId = nextMeetingId;
	//	this.meetings = meetings;
	//}

	public List<Meeting> getMeetings(){
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings){
		this.meetings = meetings;
	}
	public void setContacts(Set<Contact> contacts){
		this.contacts = contacts;
	}

	public Set<Contact> getContacts(){
		return contacts;
	}

	public int getNextContactId(){
		return nextContactId;
	}

	public void setNextContactId(int nextContactId){
		this.nextContactId = nextContactId;
	}

	public void setNextMeetingId(int nextMeetingId){
		this.nextMeetingId = nextMeetingId;
	}

	public int getNextMeetingId(){
		return nextMeetingId;
	}




	/**
	* Add a new meeting to be held in the future.
	*
	* @param contacts a list of contacts that will participate in the meeting
	* @param date the date on which the meeting will take place
	* @return the ID for the meeting
	* @throws IllegalArgumentException if the meeting is set for a time in the past,
	* of if any contact is unknown / non-existent
	*/
	public int addFutureMeeting(Set<Contact> contacts, Calendar date){
		//Check if date is in the past.
		date.set(date.get(Calendar.YEAR),date.get(Calendar.MONTH),date.get(Calendar.DATE));
		if(date.before(Calendar.getInstance())){
			throw new IllegalArgumentException("Date is past");
		}else if(!contactsAreValid(contacts)){
			throw new IllegalArgumentException("Contact unknown");
		}
		nextMeetingId++;
		Meeting meeting = new FutureMeetingImpl(nextMeetingId,date,contacts);
		meetings.add(meeting);

		return nextMeetingId;
	}


	private boolean contactsAreValid(Set<Contact> contacts){

		boolean contactNotFound = true;
		//boolean result = false;

		//Check if each contact is valid
		for(Contact contact : contacts){
			contactNotFound = true;
			//int id = contact.getId();
			//String name = contact.getName();
			for(Contact mContact : this.contacts){
				if(mContact == contact){
					contactNotFound = false;
					//result = true;
				}
			}
			if(contactNotFound){
				return false;
			}

		}
		return true;
	}



	/**
	* Returns the PAST meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the future
	*
	*/
	public PastMeeting getPastMeeting(int id){
		//find meeting with id
		Meeting returnedMeeting = null;
		boolean meetingFound = false;
		for(Meeting meeting : meetings){
			if(id == meeting.getId()){
				meetingFound = true;

				returnedMeeting = meeting;
			}
		}
		if(!meetingFound){
			//meeting not found
			return null;
		}
		//Check if the date of the meeting is in the future
		if((Calendar.getInstance()).before(returnedMeeting.getDate())){
			throw new IllegalArgumentException();
		}else{
			//if it is not in the future, but it is a futureMeeting,
			//then create a pastMeeting in its place.
			if(returnedMeeting instanceof PastMeetingImpl){
				return (PastMeetingImpl)returnedMeeting;
			}else if(returnedMeeting instanceof FutureMeetingImpl){
				Calendar mtDate = returnedMeeting.getDate();
				Set<Contact> mtContacts = returnedMeeting.getContacts();
				meetings.remove(returnedMeeting);
				PastMeetingImpl updatedMeeting = new PastMeetingImpl(id,mtDate,mtContacts,null);
				meetings.add(updatedMeeting);
				return updatedMeeting;
			}
		}


		return null;
	}


	/**
	* Returns the FUTURE meeting with the requested ID, or null if there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	* @throws IllegalArgumentException if there is a meeting with that ID happening in the past
	*/
	public FutureMeeting getFutureMeeting(int id){
		Meeting returnedMeeting = null;
		boolean meetingFound = false;
		for(Meeting meeting : meetings){
			if(id == meeting.getId()){
				meetingFound = true;

				returnedMeeting = meeting;
			}
		}
		if(!meetingFound){
			//meeting not found
			return null;
		}
		if((Calendar.getInstance()).after(returnedMeeting.getDate())){
			throw new IllegalArgumentException("Meeting found has a past date.");
		}else{
			//if it is not in the future, but it is a futureMeeting,
			//then create a pastMeeting in its place.
			if(returnedMeeting instanceof FutureMeetingImpl){
				return (FutureMeetingImpl)returnedMeeting;
			}
			return null;
		}
	}




	/**
	* Returns the meeting with the requested ID, or null if it there is none.
	*
	* @param id the ID for the meeting
	* @return the meeting with the requested ID, or null if it there is none.
	*/
	public Meeting getMeeting(int id){
		for(Meeting meeting : meetings){
			if(meeting.getId() == id){
				return meeting;
			}
		}
		return null;
	}


	/**
	* Returns the list of future meetings scheduled with this contact.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param contact one of the user's contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<Meeting> getFutureMeetingList(Contact contact){
		Set<Contact> myContacts = new TreeSet<>();
		myContacts.add(contact);
		if(!contactsAreValid(myContacts)){
			throw new IllegalArgumentException();
		}
		List<Meeting> fMeetings = new LinkedList<Meeting>();

		for(Meeting meeting : meetings){
			if(meeting instanceof FutureMeetingImpl){
				Set<Contact> rtContacts = meeting.getContacts();
				for(Contact rtContact : rtContacts){
					if(rtContact == contact){
						fMeetings.add(meeting);
					}
				}
			}
		}
		
			return fMeetings;
		
	}


	/**
	* Returns the list of meetings that are scheduled for, or that took
	* place on, the specified date
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param date the date
	* @return the list of meetings
	*/
	public List<Meeting> getFutureMeetingList(Calendar date){
		List<Meeting> fMeetings = new LinkedList<>();
		for(Meeting meeting : meetings){
			if(meeting instanceof FutureMeetingImpl){
				if((meeting.getDate()).equals(date)){
					fMeetings.add(meeting);
				}
			}
		}
		if (fMeetings.isEmpty()){
			return null;
		}else{
			return fMeetings;
		}
	}


	/**
	* Returns the list of past meetings in which this contact has participated.
	*
	* If there are none, the returned list will be empty. Otherwise,
	* the list will be chronologically sorted and will not contain any
	* duplicates.
	*
	* @param contact one of the user's contacts
	* @return the list of future meeting(s) scheduled with this contact (maybe empty).
	* @throws IllegalArgumentException if the contact does not exist
	*/
	public List<PastMeeting> getPastMeetingList(Contact contact){
		Set<Contact> myContacts = new TreeSet<>();
		myContacts.add(contact);
		if(!contactsAreValid(myContacts)){
			throw new IllegalArgumentException();
		}
		List<PastMeeting> pMeetings = new LinkedList<>();

		for(Meeting meeting : meetings){
			if(meeting instanceof PastMeetingImpl){
				Set<Contact> rtContacts = meeting.getContacts();
				for(Contact rtContact : rtContacts){
					if(rtContact == contact){
						pMeetings.add((PastMeetingImpl)meeting);
					}
				}
			}
		}
		if (pMeetings.isEmpty()){
			return null;
		}else{
			return pMeetings;
		}

	}



	/**
	* Create a new record for a meeting that took place in the past.
	*
	* @param contacts a list of participants
	* @param date the date on which the meeting took place
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the list of contacts is
	* empty, or any of the contacts does not exist
	* @throws NullPointerException if any of the arguments is null
	*/
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){
		if(contacts.isEmpty()){
			throw new IllegalArgumentException();
		}
		if(!contactsAreValid(contacts)){
			throw new IllegalArgumentException();
		}
		if(contacts == null || date == null || text == null){
			throw new NullPointerException();
		}
		nextMeetingId++;
		PastMeeting pm = new PastMeetingImpl(nextMeetingId,date,contacts,text);
		meetings.add(pm);
	}


	/**
	* Add notes to a meeting.
	*
	* This method is used when a future meeting takes place, and is
	* then converted to a past meeting (with notes).
	*
	* It can be also used to add notes to a past meeting at a later date.
	*
	* @param id the ID of the meeting
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the meeting does not exist
	* @throws IllegalStateException if the meeting is set for a date in the future
	* @throws NullPointerException if the notes are null
	*/
	public void addMeetingNotes(int id, String text){
		Meeting mt = getMeeting(id);
		if(mt == null){
			throw new IllegalArgumentException("Meeting does not exist.");
		}
		if((mt.getDate()).after(Calendar.getInstance())){
			throw new IllegalStateException("Notes cannot be added to future meeting.");
		}
		if(text == null){
			throw new NullPointerException("Attempted to pass notes of null value");
		}

		if(mt instanceof FutureMeeting){
			Calendar mtDate = mt.getDate();
			Set<Contact> mtContacts = mt.getContacts();
			meetings.remove(mt);
			PastMeetingImpl updatedMeeting = new PastMeetingImpl(id,mtDate,mtContacts,text);
			meetings.add(updatedMeeting);

		}else{
			((PastMeetingImpl)(mt)).addNotes(text);
		}

	}


	/**
	* Create a new contact with the specified name and notes.
	*
	* @param name the name of the contact.
	* @param notes notes to be added about the contact.
	* @throws NullPointerException if the name or the notes are null
	*/
	public void addNewContact(String name, String notes){
		Contact cn;
		if(notes == null || name == null){
			throw new NullPointerException("Contact not created. Either name or notes is null");
		}else{
			nextContactId++;
			cn = new ContactImpl(nextContactId,name);
			cn.addNotes(notes);
			contacts.add(cn);
		}
		

	}


	/**
	* Returns a list containing the contacts that correspond to the IDs.
	*
	* @param ids an arbitrary number of contact IDs
	* @return a list containing the contacts that correspond to the IDs.
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	*/
	public Set<Contact> getContacts(int... ids){
		boolean contactNotFound = false;
		Set<Contact> cns = new TreeSet<>();
		//search for incidence of each parameter id in contacts list
		for(int c = 0;c < ids.length;c++){
			contactNotFound = true;
			for(Contact contact : contacts){
				if(contact.getId() == ids[c]){
					cns.add(contact);
					contactNotFound = false;
				}
			}
			if(contactNotFound){
				throw new IllegalArgumentException();

			}
		}
		return cns;
	}


	/**
	* Returns a list with the contacts whose name contains that string.
	*
	* @param name the string to search for
	* @return a list with the contacts whose name contains that string.
	* @throws NullPointerException if the parameter is null
	*/
	public Set<Contact> getContacts(String name){
		boolean contactNotFound = false;
		Set<Contact> cns = new TreeSet<>();
		//search for incidence of name in contacts list

		contactNotFound = true;
		for(Contact contact : contacts){
			if((contact.getName()).equals(name)){
				cns.add(contact);
				contactNotFound = false;
			}
		}
		if(contactNotFound){
			throw new NullPointerException("Contact not found");

		}

		return cns;
	}

	public List<Integer> getPastMeetingIDs(){
		List<Integer> ids = new LinkedList<>();
		for(Meeting meeting : meetings){
			if(meeting instanceof PastMeetingImpl){
				ids.add(meeting.getId());
				//System.out.println(meeting.getId());
			}
		}
		return ids;
	}


	/**
	* Save all data to disk.
	*
	* This method must be executed when the program is
	* closed and when/if the user requests it.
	*/
	public void flush(){
		//ObjectSaver.save(this);
		XMLEncoder xml = null;
		ObjectOutputStream bos = null;
		try{
			FileOutputStream fos = new FileOutputStream("contacts.txt");
			bos = new ObjectOutputStream(fos);
			xml = new XMLEncoder(bos);
			xml.writeObject(this);

		}catch(FileNotFoundException fnfe){
			}catch(IOException ie){
		}finally{

			xml.close();

		}

	}


}


