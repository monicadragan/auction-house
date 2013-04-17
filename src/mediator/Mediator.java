package mediator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.SelectionKey;
import java.util.Scanner;

import gui.IMainWindow;
import gui.MainWindow;
import gui.TableView;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import network.Client;
import network.INetwork;
import network.Network;
import network.Server;

import types.Packet;
import types.PacketType;
import types.User;
import types.UserPublicInfo;
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
	IMainWindow gui;
	boolean readyToConnect = false;
	final int CHUNK_SIZE = 1024;	
	
	public Mediator(){
	
		statManager = new StatusManager();
		networkManager = new Network(this);//???? NU mai tb folosit
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
	public void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel)
	{
		Packet p = new Packet(msg, tableRow, tableCol);
		if(msg.equals("Make offer"))
			p.price = userPanel.getModel().getValueAt(tableRow, 4).toString();	
		
		netClient.writeObject(netClient.key, p);
	}
	
	public void sendRequest(Packet p)
	{
		System.out.println("Trimit un pachet la server: " + p.pType);
		
		netClient.writeObject(netClient.key, p);
	}
	
	public void processReplyFromServer(Packet recvPacket)
	{
		switch(recvPacket.pType)
		{
			case ADD_ROW:
				System.out.println("to add: " + recvPacket.rowData);
				gui.addRowTable(recvPacket.rowData);
				break;
				
			case REMOVE_ROW:
				System.out.println("to remove: " + recvPacket.tableRow);
				gui.removeRowTable(recvPacket.tableRow);
				break;
				
			case SET_VALUE_AT:
				gui.setValueAt(recvPacket.rowData, recvPacket.tableRow);
				break;
			case TRANSFER:
				if(gui.getUType() == UserType.SELLER){
					System.out.println("Furnizorul a primit cerere de transfer");
					String filename = gui.getUsername()+"."+recvPacket.product.toString();
					System.out.println("filename = " +filename);
					
					try {
						File fin = new File(filename);
						FileInputStream in = new FileInputStream(fin);
						long size = in.getChannel().size(); 	
						
						int len;
						byte buf[] = new byte[CHUNK_SIZE];
						int offset = 0;
						//TODO: de vazut cum functioneaza
						
						while( (len = in.read(buf, 0, CHUNK_SIZE))!=-1 ) {
							System.out.println("A citit : " + len  + " din " + size );
							sendRequest(new Packet(PacketType.TRANSFER, "Transfer", recvPacket.product, 
									recvPacket.from, recvPacket.to, buf, len, size, offset, recvPacket.fromRow, recvPacket.toRow));
							offset += len;
							Thread.sleep(500);
							System.out.println(len + " " + offset);
							int value = (int) (offset*100/size);
			            	gui.changeProgresBar(value, recvPacket.fromRow, 5);
						}
						in.close();
					}catch(Exception e){
						System.err.println("Too little information!!");
						e.printStackTrace();
					}
					//SwingUtilities.invokeLater(new GUIThread(this));
				}
				else if(gui.getUType() == UserType.BUYER) {
						
					//creare fisier
					String filename = recvPacket.from + "." + recvPacket.product.toString()+"_recv";
					try {
						//append= true, ca sa adauge la sfarsit
						FileOutputStream out = new FileOutputStream(new File(filename), true); 
						out.write(recvPacket.buffer, 0, recvPacket.sizeBuffer);
						
						System.out.println("Am primit produs cumparat de la "+ recvPacket.from + " cu offset "
								+ recvPacket.transferOffset + " din " + recvPacket.transferSize);
						
						int value = (int) ( (recvPacket.transferOffset + recvPacket.sizeBuffer)*100/recvPacket.transferSize);
		            	gui.changeProgresBar(value, recvPacket.toRow, 5);
		            	
		            	out.close();
		            	
					} catch (Exception e) {
						e.printStackTrace();
					}														            		            	
				}
				
				break;
		}
	}
	
	public void makeGUI()
	{
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
		while(!mediator.readyToConnect);
//			System.out.println(mediator.readyToConnect);
//			System.out.println("Waiting...");
		
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
	
	public UserPublicInfo getClientPublicInfo()
	{
		String username = gui.getUsername();
		UserType uType = gui.getUType();
		return new UserPublicInfo(username, uType);
	}
	
}
