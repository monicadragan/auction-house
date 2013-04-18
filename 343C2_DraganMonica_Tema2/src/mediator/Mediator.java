package mediator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import gui.IMainWindow;
import gui.MainWindow;
import gui.TableView;

import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

import network.INetClient;
import network.NetClient;
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
	
	static Logger logger = Logger.getLogger(Mediator.class);
	
	StatusManager statManager;
	IWebServiceClient wsClient;
	INetClient netClient;
	IMainWindow gui;
	boolean readyToConnect = false;
	final int CHUNK_SIZE = 256;	
	
	public Mediator(){
	
		statManager = new StatusManager();
		wsClient = new WebServiceClient();
		netClient = new NetClient(this, "127.0.0.1", Server.PORT);
		
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
	 * Metoda prin care se trimit la executat comenzile primite de la utilizator
	 */
	public void sendRequest(String msg, int tableRow, int tableCol, TableView userPanel)
	{
		Packet p = new Packet(msg, tableRow, tableCol);
		if(msg.equals("Make offer"))
			p.price = userPanel.getModel().getValueAt(tableRow, 4).toString();	
		
		netClient.writeObject(netClient.getKey(), p);
	}
	
	public void sendRequest(Packet p)
	{
		logger.info("Send packet of type " + p.pType + " to server");
		netClient.writeObject(netClient.getKey(), p);
	}
	
	public void processReplyFromServer(Packet recvPacket)
	{
		System.out.println("A primit packet " + recvPacket.pType.getName());
		switch(recvPacket.pType)
		{
			case ADD_ROW:
				logger.debug("Received message from server to add row for " + recvPacket.rowData[0]);
				gui.addRowTable(recvPacket.rowData);
				break;
				
			case REMOVE_ROW:
				logger.debug("Received message from server to remove row " + recvPacket.tableRow);
				gui.removeRowTable(recvPacket.tableRow);
				break;
				
			case SET_VALUE_AT:
				logger.debug("Received message from server to modify row " + recvPacket.tableRow);
				gui.setValueAt(recvPacket.rowData, recvPacket.tableRow);
				break;
			case TRANSFER:
				
				if(gui.getUType() == UserType.SELLER) {
					logger.info("Received request from server to transfer " + recvPacket.product.toString());
					String filename = gui.getUsername()+"."+recvPacket.product.toString();
					
					try {
						File fin = new File(filename);
						FileInputStream in = new FileInputStream(fin);
						long size = in.getChannel().size(); 	
						
						int len;
						byte buf[] = new byte[CHUNK_SIZE];
						int offset = 0;
						
						while( (len = in.read(buf, 0, CHUNK_SIZE))!=-1 ) {
						
							sendRequest(new Packet(PacketType.TRANSFER, "Transfer", recvPacket.product, 
									recvPacket.from, recvPacket.to, buf, len, size, offset, recvPacket.fromRow, recvPacket.toRow));
							offset += len;
							logger.debug("Transfering " + offset + " bytes out of " + size);
							Thread.sleep(200);
							int value = (int) (offset*100/size);
			            	gui.changeProgresBar(value, recvPacket.fromRow, 5);
						}
						logger.debug("Transfer completed");
						in.close();
					}catch(Exception e){
						logger.error("Too little information!!");
						e.printStackTrace();
					}
				}
				else if(gui.getUType() == UserType.BUYER) {
						
					//creare fisier

					String filename = recvPacket.from + "." + recvPacket.product.toString() + "_recv";
					try {
						FileOutputStream out;
						if(recvPacket.transferOffset == 0) //deschid fisierul fara append
						{
							logger.info("Starting transfer for " + recvPacket.product.toString() + " from " + recvPacket.from);
							out = new FileOutputStream(new File(filename));
						}
						else //append= true, ca sa adauge la sfarsit
							out = new FileOutputStream(new File(filename), true); 
						out.write(recvPacket.buffer, 0, recvPacket.sizeBuffer);
						logger.debug("Received the first "
								+ recvPacket.transferOffset + " bytes from " + recvPacket.transferSize +
								" for " + recvPacket.product.toString());
						
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
		BasicConfigurator.configure();
		PropertyConfigurator.configure("log4j.properties");
		Mediator mediator = new Mediator();
		mediator.makeGUI();
		logger.info("Hello ");
		//astept sa se logheze si sa-i apara tabela
		while(!mediator.readyToConnect);

		try {
			FileAppender fileAppender = new FileAppender(new SimpleLayout(),
							mediator.gui.getUsername() + ".log");
			logger.addAppender(fileAppender);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	
	public String getUsername(){
		return gui.getUsername();
	}
	
	public void logout()
	{
		Packet p = new Packet("Logout");
		netClient.writeObject(netClient.getKey(), p);

		SelectionKey key = netClient.getKey();
		SocketChannel socketChannel = (SocketChannel) key.channel();

		try {
			socketChannel.close();
		} catch (IOException e) {
			logger.error("Logout");
		}
		System.exit(1);
		
	}
}
