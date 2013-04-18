package control;
import java.nio.channels.SelectionKey;

import gui.*;

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
 * comanda "Accept Offer"
 * @author silvia
 *
 */
public class AcceptOffer implements Command{

	public Server server;
	
	public AcceptOffer(Server server) {
		this.server = server;
	}

	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		String prodName = clientInfo.getModel().getValueAt(tableRow, 0).toString();
		String username = clientInfo.getUsername();
		String prodStatus = clientInfo.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = clientInfo.getModel();		

		if(!prodStatus.equals(Status.OFFER_MADE.getName())){
			JOptionPane.showMessageDialog(null, "A seller should make an offer first!");
			return;
		}
		//accepta aceasta oferta
		userReqModel.setValueAt(Status.OFFER_ACCEPTED.getName(), tableRow, 3);
		Object[] rowData = Packet.getRowTable(userReqModel, tableRow);
		Packet toSend = new Packet(PacketType.SET_VALUE_AT, rowData, tableRow);
		clientInfo.key.interestOps(SelectionKey.OP_WRITE);
		server.writeObject(clientInfo.key, toSend);

		//refuza restul ofertelor
		for(int j = 0; j < userReqModel.getRowCount(); j++)
			if(userReqModel.getValueAt(j, 0).toString().equals(prodName) && j != tableRow)
				userReqModel.setValueAt(Status.OFFER_REFUSED.getName(), j, 3);
		
		String sellerName = userReqModel.getValueAt(tableRow, tableCol).toString();
		for(int i = 0; i < server.getUsers().size(); i++)
		{
			ClientInformation user = server.getUsers().get(i);
			DefaultTableModel sellerModel = user.getModel();
			if(user.getUType().equals(UserType.SELLER))
				if (user.getUsername().equals(sellerName))
				{
					for(int j = 0; j < sellerModel.getRowCount(); j++)
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
								&& sellerModel.getValueAt(j, 2).toString().equals(username))
						{
							sellerModel.setValueAt(Status.OFFER_ACCEPTED.getName(), j, 3);
							rowData = Packet.getRowTable(sellerModel, j);
							toSend = new Packet(PacketType.SET_VALUE_AT, rowData, j);
							user.key.interestOps(SelectionKey.OP_WRITE);
							server.writeObject(user.key, toSend);

							//TODO: Transfer FILE
							System.out.println("trimit de la "+ user.getUsername() + " la "+ clientInfo.getUsername()+" produsul "+ rowData[0]);
							server.sendFileRequest(user.getUsername(), clientInfo.getUsername(), rowData[0], j, tableRow);
							break;
						}
				}
				else //este un alt seller verifica daca ii oferise acest produs si il refuz
				{
					for(int j = 0; j < sellerModel.getRowCount(); j++)
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
								&& sellerModel.getValueAt(j, 2).toString().equals(username))
						{
							sellerModel.setValueAt(Status.OFFER_REFUSED.getName(), j, 3);
							rowData = Packet.getRowTable(sellerModel, j);
							toSend = new Packet(PacketType.SET_VALUE_AT, rowData, j);
							user.key.interestOps(SelectionKey.OP_WRITE);
							server.writeObject(user.key, toSend);
							break;
						}
				}
		
		}
	}

}
