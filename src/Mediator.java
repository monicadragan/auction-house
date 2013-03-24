import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTable;


public class Mediator {
	
	StateManager stateManager;
	ArrayList<UserThread> users = new ArrayList<UserThread>(); 
	
	Mediator(){
		stateManager = new StateManager( this);
	}
	
	void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel){
		
		stateManager.processRequest(msg, tableRow, tableCol, userPanel);
		
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
