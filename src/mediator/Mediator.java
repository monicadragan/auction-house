package mediator;
import gui.LoginState;
import gui.MainWindow;
import gui.TableView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import network.Client;
import network.INetwork;
import network.Network;
import network.Server;

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
	INetwork networkManager;
	IWebServiceClient wsClient;
	Client netClient;
	MainWindow gui;
	boolean readyToConnect = false;
	
	public Mediator(){
	
		statManager = new StatusManager();
		networkManager = new Network(this);
		wsClient = new WebServiceClient();
		netClient = new Client(this, "127.0.0.1", Server.PORT);
		
	}
	
	/**
	 * Metoda ce trimite modulului de network informatii despre transferul unui produs
	 */
	public void sendFile(MainWindow source, MainWindow destination, int sourceRow, int destRow){
		networkManager.transferFile(source, destination, sourceRow, destRow);
	}

	/**
	 * TODO: Tema3 - wsc
	 * Metoda ce verifica daca un user exista deja logat in sistem
	 */
	public boolean findUser(String name)
	{
//		for(int i = 0; i < users.size(); ++i)
//			if(name.equals(users.get(i).gui.getUsername()))
//				return true;
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
//		Command cmd = new LaunchRequest(this);
//		
//		if(msg.equals("Launch Offer request"))
//			cmd = new LaunchRequest(this);
//		else if(msg.equals("Drop Offer request"))
//			cmd = new DropRequest(this);
//		else if(msg.equals("Make offer"))
//			cmd = new MakeOffer(this);
//		else if(msg.equals("Drop auction"))
//			cmd = new DropAuction(this);
//		else if(msg.equals("Accept Offer"))
//			cmd = new AcceptOffer(this);
//		else if(msg.equals("Refuse Offer"))
//			cmd = new RefuseOffer(this);
//		else if(msg.equals("View Best Offer"))
//			cmd = new ViewBestOffer(this);
//		else // este un transfer
//		{
//			cmd = new TransferProgress(this);
//			((TransferProgress)cmd).value = Integer.parseInt(msg);
//		}
//		
//		statManager.processRequest(cmd, tableRow, tableCol, userPanel);
		netClient.write(netClient.key, msg);
	}
	public void makeGUI()
	{
//		gui = new MainWindow(this);
		SwingUtilities.invokeLater(new GUIThread(this));
	}
	
	private class GUIThread implements Runnable{
		
		Mediator med;
		
		public GUIThread(Mediator med) {
			this.med = med;
		}
		
		public void run() {
			gui = new MainWindow(med);
		}
	}

	/**
	 * Metoda ce comunica cu web-service-client pentru a verifica/obtine
	 * informatii despre un utilizator care vrea sa se logheze
	 */
	public User readUserInformation(String username, String password, UserType uType)
	{
		return wsClient.readInfoAboutUser(username, password, uType);
	}
		
	public static void main(String[] args)
	{
		Mediator mediator = new Mediator();
		mediator.makeGUI();
//		if(mediator.gui == null)
//			System.out.println("is null...");
//		else System.out.println("is OK...");
//		while(mediator.gui.userView.stateView instanceof LoginState)
		while(!mediator.readyToConnect)
			System.out.println("Waiting...");
		mediator.netClient.makeConnection();		

	}

	@Override
	public DefaultTableModel getTableModel() {
		return gui.getTableView().getModel();		
	}

	public void setReadyToConnect(boolean readyToConnect) {
		this.readyToConnect = readyToConnect;
	}

	public boolean isReadyToConnect() {
		return readyToConnect;
	}
	
}
