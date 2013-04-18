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
 * comanda "Drop Request"
 * @author silvia
 *
 */
public class DropRequest implements Command {

	public Server server;
	
	public DropRequest(Server server) {
		this.server = server;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		String prodName = clientInfo.getModel().getValueAt(tableRow, 0).toString();
		String username = clientInfo.getUsername();
		String prodStatus = clientInfo.getModel().getValueAt(tableRow, 1).toString();
		DefaultTableModel userReqModel = clientInfo.getModel();		
	
		if(prodStatus.equals("Inactive")){
			JOptionPane.showMessageDialog(null, "There was no request made.");
			return;
		}

		//anunt sellerii care ii oferisera acest produs
		for(int i = 0; i < server.getUsers().size(); i++)
		{
			ClientInformation user = server.getUsers().get(i);
			String price = "";
			if(user.getUType().equals(UserType.SELLER))
			{
				int thisProdRows = 0;//numarul de linii pentru acest produs care raman in tabela
				//parcurg tabela
				DefaultTableModel sellerModel = user.getModel();
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
					//sterg linia cu produsul si cu username-ul
					if(sellerModel.getValueAt(j, 0).toString().equals(prodName) )
					{
						thisProdRows ++;
						if(sellerModel.getValueAt(j, 2).toString().equals(username))	
						{
							sellerModel.removeRow(j);
							//trimit mesaj sa-si stearga linia corespunzatoare acestui cumparator din tabela
							Packet toSend = new Packet(PacketType.REMOVE_ROW, j);
							user.key.interestOps(SelectionKey.OP_WRITE);
							server.writeObject(user.key, toSend);
							j--;
							price = sellerModel.getValueAt(j, 4).toString();
							thisProdRows--;
						}
					}
				}
				if(thisProdRows == 0)
				{
					Object[] rowData = new Object[sellerModel.getColumnCount()];
					rowData[0] = prodName;
					rowData[1] = "Inactive";
					rowData[2] = "";
					rowData[3] = Status.INACTIVE.getName();
					rowData[4] = price;//pret
					rowData[5] = 0;//progress bar 
					sellerModel.addRow(rowData);//adaug noi linii
				}
			}
				
		}
		//sterg toate liniile cu acest produs
		for(int j = 0; j < userReqModel.getRowCount(); j++)
		{
			if(userReqModel.getValueAt(j, 0).toString().equals(prodName))
			{
				userReqModel.removeRow(j);
				j--;
			}
		}
		
		Object[] rowData = new Object[userReqModel.getColumnCount()];
		rowData[0] = prodName;
		rowData[1] = "Inactive";
		rowData[2] = "";
		rowData[3] = Status.INACTIVE.getName();
		rowData[4] = "";//pret
		rowData[5] = 0;//progress bar 
		userReqModel.addRow(rowData);//adaug noi linii
		Packet toSend = new Packet(PacketType.ADD_ROW, rowData);
		clientInfo.key.interestOps(SelectionKey.OP_WRITE);
		server.writeObject(clientInfo.key, toSend);

		System.out.println("Drop Request!");

	}
		
}
