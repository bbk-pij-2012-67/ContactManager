import java.io.*;

public class ContactImpl implements Contact, Comparable<Contact>, Serializable {
	
	private int id;
	private String name;
	private String note;
	
	
	/**
	* ContactImpl constructor
	*
	* @param id int value for contact's id
	* @param name String value of contact's name
	*/
	public ContactImpl(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	/**
	* No-argument constructor
	*/
	public ContactImpl(){
	}
	
	/**
	* Setter for notes
	*
	* @param note String passing the notes.
	*/
	public void setNotes(String note){
		this.note = note;
	}
	
	/**
	* Setter for id
	*
	* @param id int passing the contact id.
	*/
	public void setId(int id){
		this.id = id;
	}
	
	/**
	* Setter for name
	*
	* @param name String passing the name.
	*/
	public void setName(String name){
		this.name = name;
	}
	
	/**
	* Returns the ID of the contact.
	*
	* @return the ID of the contact.
	*/
	public int getId(){
		return this.id;
	}
	
	/**
	* Returns the name of the contact.
	*
	* @return the name of the contact.
	*/
	public String getName(){
		return this.name;
	}
	
	/**
	* Returns our notes about the contact, if any.
	*
	* If we have not written anything about the contact, the empty
	* string is returned.
	*
	* @return a string with notes about the contact, maybe empty.
	*/
	public String getNotes(){
		if(note == null){
			note = "";
		}
		return note;
	}
	
	/**
	* Add notes about the contact.
	*
	* @param note the notes to be added
	*/
	public void addNotes(String note){
		this.note = note;
	}
	
	/**
	* Return result of comparison.
	*
	* Compares Contacts based on id
	*
	* @param cn Contact to compare.
	* @return An int value indicating the result of comparison. 
	*	If the id of the contact to compare is greater than that of this contact, the result is -1.
	*	If the id of the contact to compare is equal to that of this contact, the result is 0.
	*	If the id of the contact to compare is smaller than that of this contact, the result is 1.
	*/
	public int compareTo(Contact cn){
		int result = 0;
		if(cn.getId() == this.getId()){
			result = 0;
		}else if(this.getId()< cn.getId()){
			result = -1;
		}else if(this.getId() > cn.getId()){
			result = 1;
		}
		return result;
	}
	
	
	public String toString(){
		return name;
	}
			
}
