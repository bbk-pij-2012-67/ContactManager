import java.io.ObjectInputStream;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.*;
import java.io.FileNotFoundException;
import java.util.Set;
import java.util.List;

public class ConManager{
	ContactManagerImpl cm = null;

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
				System.out.println("Select: [C]-Manage Contacts [M]-Manage Meetings [S]-Save [Q]-Quit");
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
				System.out.println("Select: [L]-List Contacts [A]-Add Contacts [F]-Find Contact [S]-Save [P]-Previous Menu [Q]-Quit");
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
				System.out.println("Select: [L]-List Meetings by Date [A]-Add Meeting [M]-List All Meetings [N]-Add Notes [S]-Save [P]-Previous Menu [Q]-Quit");
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
			System.out.println("Enter notes for the contact:");
			String notes = System.console().readLine();
			cm.addNewContact(name,notes);
		}catch(NullPointerException np){
			System.out.println("You must enter a name and notes");
		}
	}

	public void findContact(){}

	public void listMeetingsByDate(){}

	public void addMeeting(){}

	public void listAllMeetings(){}

	public void addNotes(){}

	public void quitProgram(){
		cm.flush();
		System.out.println("Goodbye.");
		System.exit(0);
	}


}

//public void
