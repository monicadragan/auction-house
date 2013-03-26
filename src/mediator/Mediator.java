package mediator;
import gui.MainWindow;
import gui.TableView;

import java.util.ArrayList;
import java.util.Scanner;

import network.INetwork;
import network.Network;

import types.User;
import types.UserType;
import wsc.IWebServiceClient;
import wsc.WebServiceClient;

import control.*;

public class Mediator implements IGUIMediator, INetMediator, IWSCMediator{
	
	StatusManager statManager;
	public ArrayList<UserThread> users; 
	INetwork networkManager;
	IWebServiceClient wsClient;
	
	public Mediator(){
		statManager = new StatusManager();
		users = new ArrayList<UserThread>();
		networkManager = new Network(this);
		wsClient = new WebServiceClient();
	}
	
	public void sendFile(MainWindow source, MainWindow destination, int sourceRow, int destRow){
		networkManager.transferFile(source, destination, sourceRow, destRow);
	}

	public boolean findUser(String name)
	{
		for(int i = 0; i < users.size(); ++i)
			if(name.equals(users.get(i).gui.getUsername()))
				return true;
		return false;
	}
	
	public void changeTransferProgress(Integer val)
	{
		//anunt interfata grafica sa modifice progress-barul
		networkManager.getSource().changeProgresBar(val, networkManager.getSourceRow(), 5);
		//trebuie modificat si statusul!
		if(!findUser(networkManager.getDest().getUsername()))
		{
			this.sendRequest(-1 + "", networkManager.getSourceRow(), 3, networkManager.getSource().getTableView());
			return;
		}
		this.sendRequest(val + "", networkManager.getSourceRow(), 3, networkManager.getSource().getTableView());

		networkManager.getDest().changeProgresBar(val, networkManager.getDestRow(), 5);
		this.sendRequest(val + "", networkManager.getDestRow(), 3, networkManager.getDest().getTableView());
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
		else // este un transfer
		{
			cmd = new TransferProgress(this);
			((TransferProgress)cmd).value = Integer.parseInt(msg);
		}
		
		statManager.processRequest(cmd, tableRow, tableCol, userPanel);
		
	}

	public User readUserInformation(String username, String password, UserType uType)
	{	
		return wsClient.readInfoAboutUser(username, password, uType);
	}
	
	public ArrayList<UserThread> getUsers()
	{
		return users;
	}
	
	public void setUsers(ArrayList<UserThread> users)
	{
		this.users = users;
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
