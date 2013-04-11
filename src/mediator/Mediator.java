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

/**
 * Clasa ce defineste modulul mediator care face legatura intre 
 * componentele aplicatiei
 *
 */
public class Mediator implements IGUIMediator, INetMediator, IWSCMediator{
	
	StatusManager statManager;
	public ArrayList<UserThread> users; 
	INetwork networkManager;
	IWebServiceClient wsClient;
	
	public Mediator(){
		statManager = new StatusManager();
		users = new ArrayList<UserThread>();//gui
		networkManager = new Network(this);
		wsClient = new WebServiceClient();
	}
	
	/**
	 * Metoda ce trimite modulului de network informatii despre transferul unui produs
	 */
	public void sendFile(MainWindow source, MainWindow destination, int sourceRow, int destRow){
		networkManager.transferFile(source, destination, sourceRow, destRow);
	}

	/**
	 * Metoda ce verifica daca un user exista deja logat in sistem
	 */
	public boolean findUser(String name)
	{
		for(int i = 0; i < users.size(); ++i)
			if(name.equals(users.get(i).gui.getUsername()))
				return true;
		return false;
	}
	
	/**
	 * Metoda ce asigura comunicarea cu interfata grafica
	 * pentru a efectua modificarile din timpul unui transfer
	 */
	public void changeTransferProgress(Integer val, MainWindow srcFrame,
			MainWindow destFrame, int srcRow, int dstRow)
	{
		//anunt interfata grafica sa modifice progress-barul
		srcFrame.changeProgresBar(val, srcRow, 5);
		if(!findUser(destFrame.getUsername()))
		{
			this.sendRequest(-1 + "", srcRow, 3, srcFrame.getTableView());
			return;
		}
		this.sendRequest(val + "", srcRow, 3, srcFrame.getTableView());

		destFrame.changeProgresBar(val, dstRow, 5);
		this.sendRequest(val + "", dstRow, 3, destFrame.getTableView());
	}
	
	/**
	 * Metoda prin care se trimit la executat comenzile primite de la utilizator
	 */
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
		else if(msg.equals("View Best Offer"))
			cmd = new ViewBestOffer(this);
		else // este un transfer
		{
			cmd = new TransferProgress(this);
			((TransferProgress)cmd).value = Integer.parseInt(msg);
		}
		
		statManager.processRequest(cmd, tableRow, tableCol, userPanel);
		
	}

	/**
	 * Metoda ce comunica cu web-service-client pentru a verifica/obtine
	 * informatii despre un utilizator care vrea sa se logheze
	 */
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
				UserThread t = new UserThread();
			    t.start();
			    mediator.users.add(t);
			}
		}
	}

}
