package control;
import java.nio.channels.SelectionKey;

import gui.IMainWindow;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.ClientInformation;
import network.Server;

import types.Packet;
import types.PacketType;
import types.Status;
import types.UserType;

import mediator.IGUIMediator;
import mediator.Mediator;

/**
 * Clasa folosita pentru a face modificarile necesare cand se primeste
 * comanda "Launch Offer request"
 * @author silvia
 *
 */
public class LaunchRequest implements Command {

	public Server server;
	
	public LaunchRequest(Server server) {
		this.server = server;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		String prodName = clientInfo.getModel().getValueAt(tableRow, 0).toString();
		String username = clientInfo.getUsername();
		String prodStatus = clientInfo.getModel().getValueAt(tableRow, 1).toString();
		DefaultTableModel userReqModel = clientInfo.getModel();		
		Packet toSend;
		boolean receiveResponse = false;
		
		if(prodStatus.equals("Active")){
			JOptionPane.showMessageDialog(null, "Request already launched.");
			return;
		}
		
		//anunt sellerii care au acest produs si-i adaug in tabela cumparatorului cu No offer
		for(int i = 0; i < server.getUsers().size(); i++)
		{
			ClientInformation user = server.getUsers().get(i);
			if(user.getUType() == UserType.SELLER){
				DefaultTableModel sellerModel = user.getModel();
				//parcurg tabela					
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
					if(sellerModel.getValueAt(j, 0).toString().equals(prodName))
					{
						String sellerName = user.getUsername();							
						
						Object[] rowData = new Object[userReqModel.getColumnCount()];
						Object[] rowDataSeller = new Object[userReqModel.getColumnCount()];
						String price = sellerModel.getValueAt(j, 4).toString();
						if(sellerModel.getValueAt(j, 1).toString().equals("Inactive")){
							sellerModel.removeRow(j);
//							toSend = new Packet(PacketType.REMOVE_ROW, j);
////							server.addPacketToSend(user.key, toSend);
//							user.key.interestOps(SelectionKey.OP_WRITE);
//							server.writeObject(user.key, toSend);
						}
						
						rowDataSeller[0] = prodName;
						rowDataSeller[1] = "Active";
						rowDataSeller[2] = username;
						rowDataSeller[3] = Status.NO_OFFER.getName();
						rowDataSeller[4] = price;//pret
						rowDataSeller[5] = 0;//progress bar !!
						sellerModel.addRow(rowDataSeller);//adaug noi linii	
						toSend = new Packet(PacketType.ADD_ROW, rowDataSeller);
						user.key.interestOps(SelectionKey.OP_WRITE);
						server.writeObject(user.key, toSend);
						
						rowData[0] = prodName;
						rowData[1] = "Active";
						rowData[2] = sellerName;
						rowData[3] = Status.NO_OFFER.getName();
						rowData[4] = "";//pret
						rowData[5] = 0;//progress bar !!
						userReqModel.addRow(rowData);//adaug noi linii
						toSend = new Packet(PacketType.ADD_ROW, rowData);
						clientInfo.key.interestOps(SelectionKey.OP_WRITE);
						server.writeObject(clientInfo.key, toSend);
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						receiveResponse = true;
						break;
					}
				}
			}
		}
		if(receiveResponse)//exista cel putin un furnizor
		{
			userReqModel.removeRow(tableRow);//sterg linia tableRow pentru ca coloana furnizor era goala
//			toSend = new Packet(PacketType.REMOVE_ROW, tableRow);
//			clientInfo.key.interestOps(SelectionKey.OP_WRITE);
//			server.writeObject(clientInfo.key, toSend);
		}
		System.out.println("Launch Request!");
	}
	
}

