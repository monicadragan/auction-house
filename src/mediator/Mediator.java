package mediator;
import gui.TableView;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTable;

import network.Network;

import types.User;
import types.UserType;
import wsc.WebServiceClient;

import control.AcceptOffer;
import control.Command;
import control.DropAuction;
import control.DropRequest;
import control.LaunchRequest;
import control.MakeOffer;
import control.RefuseOffer;
import control.StatusManager;


public class Mediator {
	
	StatusManager statManager;
	public ArrayList<UserThread> users; 
	Network networkManager;
	WebServiceClient wsClient;
	
	public Mediator(){
		statManager = new StatusManager();
		users = new ArrayList<UserThread>();
		networkManager = new Network();
		wsClient = new WebServiceClient();
	}
	
	public void sendFile(TableView source, TableView destination, int sourceRow, int destRow){
		networkManager.transferFile(source, destination, sourceRow, destRow);
	}
	
	public void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel){
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

	public User readUserInformation(String username, String password, UserType uType)
	{	
		return wsClient.readInfoAboutUser(username, password, uType);
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
			if(input.equals("launch")){
				UserThread t = new UserThread(mediator);
			    t.start();
			    mediator.users.add(t);
			}
		}
	}

}
