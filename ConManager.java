import java.io.ObjectInputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.List;
import java.text.SimpleDateFormat;

public class ConManager{
	ContactManagerImpl cm = null;
	SimpleDateFormat dfm = new SimpleDateFormat("dd-mm-yyyy");

	public static void main(String[] args){
		ConManager cnm = new ConManager();

		cnm.launch();
	}

	public void launch(){

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

	public void Menu1(){
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
				cm.flush();
			}
		}
	}

	public void contactsMenu(){

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
				cm.flush();
			}else if(str.equals("p")){
				return;
			}
		}
	}

	public void meetingsMenu(){
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
				cm.flush();
			}else if(str.equals("p")){
				return;
			}
		}
	}

	public void listContacts(){
		Set<Contact> contacts = ((ContactManagerImpl)(cm)).getContacts();
		if(contacts.size()!=0){
			for(Contact contact : contacts){
				System.out.println("ID: " + contact.getId() + " " + contact.getName());
			}
		}else{
			System.out.println("You currently have no contacts\n");
		}
	}

	public void addContact(){
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

	public void findContact(){
		
		printContacts(returnContacts);

	}
	
	
	private Set<Contact> returnContact(){
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
		Set<Contact> contacts = returnContact();
		for(Contact contact : contacts){
			return contact;
		}
	}
		

	public void printContacts( Set<Contact> contacts){

		for(Contact contact : contacts){
			System.out.println("ID: " + contact.getId() + " " + contact.getName() + " " + contact.getNotes());
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

	public void listMeetingsByDate(){
		Calendar calDate = chooseDate();
		List<Meeting> meetings = cm.getFutureMeetingList(calDate);
		for(Meeting meeting : meetings){
			System.out.println("Meeting due on: " + dfm.format(meeting.getDate().getTime()) );
			System.out.println("Contacts attending:");
			Set<Contact> contacts = meeting.getContacts();
			for(Contact contact : contacts){
				System.out.println("ID: " + contact.getId() + " " + contact.getName());
			}
		}
	}
	
	private Calendar chooseDate(){
		Calendar calDate = Calendar.getInstance();
		Date dDate;
		boolean finished = false;
		do{

			System.out.println("Enter a date in dd-mm-yyyy format");
			System.out.println("Year: ");
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



	public void addMeeting(){
		String str;
		while(true){
			do{
				System.out.println("\nSelect: [P]-Past Meeting [F]-Future Meeting [S]-Save [P]-Previous Menu [Q]-Quit");
				str = System.console().readLine();
				str = str.toLowerCase();
			}while(!str.equals("p") && !str.equals("f") && !str.equals("s") && !str.equals("p") && !str.equals("q"));
			if(str.equals("q")){
				quitProgram();
			}else if(str.equals("p")){
				addPastMeeting();
			}else if(str.equals("f")){
				addFutureMeeting();
			}else if(str.equals("s")){
				cm.flush();
			}else if(str.equals("p")){
				return;
			}
		}
	}

	public void listAllMeetings(){
		List<Meeting> meetings = cm.getMeetings();
		for(Meeting meeting : meetings){
			System.out.println("Meeting due on: " + dfm.format(meeting.getDate().getTime()) );
			System.out.println("Contacts attending:");
			Set<Contact> contacts = meeting.getContacts();
			for(Contact contact : contacts){
				System.out.println("ID: " + contact.getId() + " " + contact.getName());
			}
		}
	}

	public void addNotes(){
		System.out.println("Enter the id of a meeting for which you want to add notes");
		String id = System.console().readLine();
		System.out.println("Enter the id of a meeting for which you want to add notes");
		String notes = System.console().readLine();
		if(notes.length()==0){ notes=null;}
		try{
			cm.addMeetingNotes(id,notes);
			System.out.println("Notes have been added.");
		}catch(IllegalArgumentException ia){
			System.out.println("No meeting with that id exists");
		}catch(IllegalStateException is){
			System.out.println("Cannot add notes to a future meeting.");
		}catch(NullPointerException np){
			System.out.println("Cannot supply empty note.");
		}
			
	}
	
	private void addPastMeeting(){
		Calendar date = chooseDate();
		Set<Contact> contacts = null;
		String str = null;
		System.out.println("Add contacts to meeting:");
		do{
			contacts.add(returnSungleContact()):
			System.out.println("Enter another contact? [Y/N]");
			str = System.console().readLine();
			str = str.toLowerCase();
		}while(!str.equals("n"));
		System.out.println("Enter notes for meeting:");
		String notes = System.console().readLine();
		try{
			cm.addNewPastmeeting(contacts,date,notes);
		}catch(IllegalArgumentException ia){
			System.out.println("Contact list empty or contacts do not exist.");
		}catch(NullPointerException np){
			System.out.println("Contact list, date, or notes are null.");
		}
	}
	
	private void addFutureMeeting(){
	}

	public void quitProgram(){
		cm.flush();
		System.out.println("Goodbye.");
		System.exit(0);
	}


}

//public void
