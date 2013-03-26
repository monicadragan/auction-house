import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTable;


public class Mediator {
	
	StatusManager statManager = new StatusManager();
	ArrayList<UserThread> users = new ArrayList<UserThread>(); 
	Network networkManager;

	
	Mediator(){
		networkManager = new Network();
	}
	
	void sendFile(TableView source, TableView destination, int sourceRow, int destRow){
		networkManager.transferFile(source, destination, sourceRow, destRow);
	}
	
	void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel){
		Command cmd = new LaunchRequest(this);
		
		if(msg.equals("Launch Offer request"))
			cmd = new LaunchRequest(this);
		else if(msg.equals("Drop Offer request"))
			cmd = new DropRequest(this);
		else if(msg.equals("Make offer"))
			cmd = new MakeOffer(this);
		else if(msg.equals("Drop auction"))
			cmd = new DropAuction(this);
		else if(msg.equals("Accept Offer"))
			cmd = new AcceptOffer(this);
		else if(msg.equals("Refuse Offer"))
			cmd = new RefuseOffer(this);

		statManager.processRequest(cmd, tableRow, tableCol, userPanel);
		
	}
	
	
	public static void main(String[] args)
	{		
		
		Mediator mediator = new Mediator();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.print("Type command (launch/exit): ");
			String input = sc.nextLine();
			if(input.equals("exit")){
				System.exit(1);
			}
			if(input.equals("l")){
				UserThread t = new UserThread(mediator);
			    t.start();
			    mediator.users.add(t);
			}
		}
	}

	public User readUserInformation(String username, String password, UserType uType)
	{	
		return InputParser.readInfoAboutUser(username, password, uType);
	}

}
