import java.io.ObjectInputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

/**
* Program providing an interface to the ContactManagerImpl class.
*/
public class ConManager{
	ContactManagerImpl cm = null;
	SimpleDateFormat dfm = new SimpleDateFormat("dd-MM-yyyy");

	public static void main(String[] args){
		ConManager cnm = new ConManager();

		cnm.launch();
	}

	public void launch(){
		//Start Contact Manager and load contacts.txt if it is available
		System.out.println("Starting Contact Manager...");
		boolean result = readFile();
		if (!result){
			System.out.println("Data file was not found. A new one can be saved at any time or when the application quits");
			cm = new ContactManagerImpl();
		}
		Menu1();
	}

	private boolean readFile(){
		boolean result = false;

		File file = new File("contacts.txt");
		if (file.exists()){
			XMLDecoder xml = null;
			try{
				//File read from xml doc
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream bis = new ObjectInputStream(fis);
				xml = new XMLDecoder(bis);

				cm = (ContactManagerImpl) (xml.readObject());
				System.out.println("Contact manager has been loaded.");
				//Display stats
				Set<Contact> contacts = cm.getContacts();
				System.out.println("Contacts loaded: " + contacts.size());
				List<Meeting> meetings = cm.getMeetings();
				System.out.println("Meetings loaded: " + meetings.size());
				result = true;
			}catch(FileNotFoundException fnfe){
			}catch(IOException ie){
			}finally{

				xml.close();

			}
		}
		return result;
	}

	private void Menu1(){
		String str;
		while(true){

			do{
				System.out.println("\nSelect: [C]-Manage Contacts [M]-Manage Meetings [S]-Save [Q]-Quit");
				str = System.console().readLine();
				str = str.toLowerCase();
			}while(!str.equals("c") && !str.equals("m") && !str.equals("s") && !str.equals("q"));
			if(str.equals("q")){
				quitProgram();
			}else if(str.equals("c")){
				contactsMenu();
			}else if(str.equals("m")){
				meetingsMenu();
			}else if(str.equals("s")){
				save();

			}
		}
	}

	private void contactsMenu(){

		String str;
		while(true){
			do{
				System.out.println("\nSelect: [L]-List Contacts [A]-Add Contacts [F]-Find Contact [S]-Save [P]-Previous Menu [Q]-Quit");
				str = System.console().readLine();
				str = str.toLowerCase();
			}while(!str.equals("l") && !str.equals("a") && !str.equals("f") && !str.equals("s") && !str.equals("p") && !str.equals("q"));
			if(str.equals("q")){
				quitProgram();
			}else if(str.equals("l")){
				listContacts();
			}else if(str.equals("a")){
				addContact();
			}else if(str.equals("f")){
				findContact();
			}else if(str.equals("s")){
				save();
			}else if(str.equals("p")){
				return;
			}
		}
	}

	private void meetingsMenu(){
		String str;
		while(true){
			do{
				System.out.println("\nSelect: [L]-List Future Meetings by Date [A]-Add Meeting [M]-List All Meetings [N]-Add Notes [S]-Save [P]-Previous Menu [Q]-Quit");
				str = System.console().readLine();
				str = str.toLowerCase();
			}while(!str.equals("l") && !str.equals("a") && !str.equals("m") && !str.equals("n") && !str.equals("s") && !str.equals("p") && !str.equals("q"));
			if(str.equals("q")){
				quitProgram();
			}else if(str.equals("l")){
				listMeetingsByDate();
			}else if(str.equals("a")){
				addMeeting();
			}else if(str.equals("m")){
				listAllMeetings();
			}else if(str.equals("n")){
				addNotes();
			}else if(str.equals("s")){
				save();
			}else if(str.equals("p")){
				return;
			}
		}
	}

	private void listContacts(){
		Set<Contact> contacts = ((ContactManagerImpl)(cm)).getContacts();
		if(contacts.size()!=0){
			for(Contact contact : contacts){
				System.out.println("ID: " + contact.getId() + " " + contact.getName());
			}
		}else{
			System.out.println("You currently have no contacts\n");
		}
	}

	private void addContact(){
		try{
			System.out.println("Enter the name of the contact:");
			String name = System.console().readLine();
			if(name.length()==0) {name = null;}
			System.out.println("Enter notes for the contact:");
			String notes = System.console().readLine();
			if(notes.length()==0) {notes = null;}
			cm.addNewContact(name,notes);
		}catch(NullPointerException np){
			System.out.println("You must enter a name and notes");
		}
	}

	private void findContact(){
		try{
			printContacts(returnContacts());
		}catch(NullPointerException np){
			System.out.println(np.getMessage());
		}

	}
	
	private Set<Contact> returnContacts(){
		//search for contacts based on id or name
		                           System.out.println("Enter the name or contact id number of the contact you are looking for:");
		                           String str = System.console().readLine();
		                           int id = 0;
		                           //test if response is an integer. if so, search by id
		                           try{
			                           id = Integer.parseInt(str);
		                           }catch(NumberFormatException nf){
			                           //not a number
		                           }
		                           Set<Contact> contacts;
		                           if(id != 0){
			                           contacts = cm.getContacts(id);

		                           }else{
			                           contacts = cm.getContacts(str);

		                           }
		                           return contacts;
	                           }

	                           private Contact returnSingleContact(){
		                                                 Set<Contact> contacts = returnContacts();
		                                                 for(Contact contact : contacts){
			                                                 return contact;
		                                                 }
		                                                 System.out.println("Contact not found");
		                                                 return null;
	                                                 }


	                                                 private void printContacts( Set<Contact> contacts){
		                                                 for(Contact contact : contacts){
			                                                 System.out.println("ID: " + contact.getId() + " " + contact.getName() + "\nNotes: " + contact.getNotes());
			                                                 List<Meeting> fMeetings = cm.getFutureMeetingList(contact);
			                                                 if(fMeetings.size()==0){
				                                                 System.out.println("No meetings");
			                                                 }else{
				                                                 for(Meeting meeting : fMeetings){
					                                                 System.out.println("Meeting due on: " + dfm.format(meeting.getDate().getTime()) );
				                                                 }
			                                                 }
		                                                 }



	                                                 }

	                                                 private void listMeetingsByDate(){
		                                                 Calendar calDate = chooseDate();
		                                                 List<Meeting> meetings = cm.getFutureMeetingList(calDate);
		                                                 if(meetings != null){
			                                                 for(Meeting meeting : meetings){
				                                                 System.out.println("Meeting due on: " + dfm.format(meeting.getDate().getTime()) );
				                                                 System.out.println("Contacts attending:");
				                                                 Set<Contact> contacts = meeting.getContacts();
				                                                 for(Contact contact : contacts){
					                                                 System.out.println("ID: " + contact.getId() + " " + contact.getName());
				                                                 }
				                                                 System.out.println("--------------");
			                                                 }
		                                                 }else{
			                                                 System.out.println("No meetings found.");
		                                                 }
	                                                 }

	                                                 private Calendar chooseDate(){
		                                                 Calendar calDate = Calendar.getInstance();
		                                                 Date dDate;
		                                                 boolean finished = false;
		                                                 do{

			                                                 System.out.println("Enter a date in dd-mm-yyyy format");
			                                                 String date = System.console().readLine();
			                                                 try{
				                                                 dDate = dfm.parse(date);
				                                                 calDate.setTime(dDate);
				                                                 finished = true;
			                                                 }catch(ParseException pe){
				                                                 System.out.println("Date is invalid");
			                                                 }
		                                                 }while(!finished);
		                                                 return calDate;
	                                                 }



	                                                 private void addMeeting(){
		                                                 String str;
		                                                 while(true){
			                                                 do{
				                                                 System.out.println("\nSelect: [A]-Past Meeting [F]-Future Meeting [S]-Save [P]-Previous Menu [Q]-Quit");
				                                                 str = System.console().readLine();
				                                                 str = str.toLowerCase();
			                                                 }while(!str.equals("a") && !str.equals("f") && !str.equals("s") && !str.equals("p") && !str.equals("q"));
			                                                 if(str.equals("q")){
				                                                 quitProgram();
			                                                 }else if(str.equals("a")){
				                                                 addPastMeeting();
			                                                 }else if(str.equals("f")){
				                                                 addFutureMeeting();
			                                                 }else if(str.equals("s")){
				                                                 save();
			                                                 }else if(str.equals("p")){
				                                                 return;
			                                                 }
		                                                 }
	                                                 }

	                                                 private void listAllMeetings(){
		                                                 List<Meeting> meetings = cm.getMeetings();
		                                                 for(Meeting meeting : meetings){
			                                                 if(meeting instanceof FutureMeetingImpl){
			                                                 	 //print out Future Meeting
				                                                 System.out.println("Meeting (ID " + meeting.getId() + ") due on: " + dfm.format(meeting.getDate().getTime()) );
				                                                 System.out.println("Contacts attending:");
			                                                 }else{
			                                                 	 //Print out past meeting
				                                                 System.out.println("Meeting (ID " + meeting.getId() + ") took place on: " + dfm.format(meeting.getDate().getTime()) );
				                                                 System.out.println("Contacts that attended:");
				                                                 System.out.println("Notes: " + ((PastMeetingImpl)(meeting)).getNotes());
			                                                 }
			                                                 Set<Contact> contacts = meeting.getContacts();
			                                                 for(Contact contact : contacts){
				                                                 System.out.println("ID: " + contact.getId() + " " + contact.getName());
			                                                 }
			                                                 //Print separator
			                                                 System.out.println("--------------");
		                                                 }
	                                                 }

	                                                 private void addNotes(){
		                                                 try{
			                                                 System.out.println("Enter the id of a meeting for which you want to add notes");
			                                                 String id = System.console().readLine();
			                                                 int idInt = (int)(Integer.parseInt(id));
			                                                 System.out.println("Enter the notes for the meeting.");
			                                                 String notes = System.console().readLine();
			                                                 if(notes.length()==0){ notes=null;}
			                                                 try{
				                                                 cm.addMeetingNotes(idInt,notes);
				                                                 System.out.println("Notes have been added.");
			                                                 }catch(IllegalArgumentException ia){
				                                                 System.out.println("No meeting with that id exists");
			                                                 }catch(IllegalStateException is){
				                                                 System.out.println("Cannot add notes to a future meeting.");
			                                                 }catch(NullPointerException np){
				                                                 System.out.println("Cannot supply empty note.");
			                                                 }
		                                                 }catch(NumberFormatException nf){
			                                                 System.out.println("Invalid data entered as a number");
		                                                 }
	                                                 }

	                                                 private void addPastMeeting(){
		                                                 Calendar date = chooseDate();
		                                                 Set<Contact> contacts = new TreeSet<>();
		                                                 String str = null;
		                                                 System.out.println("Add contacts to meeting:");
		                                                 
		                                                 //Read in contacts
		                                                 do{
			                                                 contacts.add(returnSingleContact());
			                                                 System.out.println("Enter another contact? [Y/N]");
			                                                 str = System.console().readLine();
			                                                 str = str.toLowerCase();
		                                                 }while(!str.equals("n"));
		                                                 
		                                                 System.out.println("Enter notes for meeting:");
		                                                 String notes = System.console().readLine();
		                                                 try{
			                                                 cm.addNewPastMeeting(contacts,date,notes);
		                                                 }catch(IllegalArgumentException ia){
			                                                 System.out.println("Contact list empty or contacts do not exist.");
		                                                 }catch(NullPointerException np){
			                                                 System.out.println("Contact list, date, or notes are null.");
		                                                 }
	                                                 }

	                                                 private void addFutureMeeting(){
		                                                 //Add date
		                                                 Calendar date = chooseDate();
		                                                 //Add contacts
		                                                 Set<Contact> contacts = new TreeSet<>();
		                                                 String str = null;
		                                                 System.out.println("Add contacts to meeting:");
		                                                 
		                                                 //Read in contacts
		                                                 do{
			                                                 contacts.add(returnSingleContact());
			                                                 System.out.println("Enter another contact? [Y/N]");
			                                                 str = System.console().readLine();
			                                                 str = str.toLowerCase();
		                                                 }while(!str.equals("n"));
		                                                 
		                                                 try{
			                                                 cm.addFutureMeeting(contacts,date);
		                                                 }catch(IllegalArgumentException ia){
			                                                 System.out.println(ia.getMessage());
		                                                 }
	                                                 }

	                                                 private void quitProgram(){
		                                                 save();
		                                                 System.out.println("Quitting...\nGoodbye.");
		                                                 System.exit(0);
	                                                 }

	                                                 private void save(){
		                                                 cm.flush();
		                                                 System.out.println("Saving to file...");
	                                                 }


                                                 }

                                                 //public void
