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
 * comanda "Refuse Offer"
 * @author silvia
 *
 */
public class RefuseOffer implements Command{

	public Server server;
	
	public RefuseOffer(Server server) {
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
		//oferta este refuzata
		userReqModel.setValueAt(Status.OFFER_REFUSED.getName(), tableRow, 3);
		Object[] rowData = Packet.getRowTable(userReqModel, tableRow);
		Packet toSend = new Packet(PacketType.SET_VALUE_AT, rowData, tableRow);
		clientInfo.key.interestOps(SelectionKey.OP_WRITE);
		server.writeObject(clientInfo.key, toSend);

		//anunt si seller-ul
		String sellerName = userReqModel.getValueAt(tableRow, tableCol).toString();
		for(int i = 0; i < server.getUsers().size(); i++)
		{
			ClientInformation user = server.getUsers().get(i);
			if(user.getUType().equals(UserType.SELLER)
					&& user.getUsername().equals(sellerName))
			{
				//parcurg tabela
				DefaultTableModel sellerModel = user.getModel();
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
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
				break;
			}
		}

	}

}
