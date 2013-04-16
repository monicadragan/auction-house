package control;
import gui.IMainWindow;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.ClientInformation;
import network.Server;

import types.Status;
import types.UserType;

import mediator.IGUIMediator;
import mediator.Mediator;

/**
 * Clasa folosita pentru a face modificarile necesare cand se primeste
 * comanda "Make Offer"
 * @author silvia
 *
 */
public class MakeOffer implements Command{

	public Server server;
	
	public MakeOffer(Server server) {
		this.server = server;
	}

	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		String prodName = clientInfo.getModel().getValueAt(tableRow, 0).toString();
		String username = clientInfo.getUsername();
		String buyerName = clientInfo.getModel().getValueAt(tableRow, 2).toString();
		String prodStatus = clientInfo.getModel().getValueAt(tableRow, 1).toString();
		String prodStatusLicitatie = clientInfo.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = clientInfo.getModel();		

		if(prodStatus.equals("Inactive")){
			JOptionPane.showMessageDialog(null, "There was no request made.");
			return;
		}
		else if(prodStatusLicitatie.equals(Status.OFFER_MADE.getName())){
			JOptionPane.showMessageDialog(null, "An offer was already made.");
			return;
		}
		if(prodStatusLicitatie.equals(Status.OFFER_EXCEEDED.getName())
				|| prodStatusLicitatie.equals(Status.NO_OFFER.getName())
				|| prodStatusLicitatie.equals(Status.OFFER_REFUSED.getName()))
		{
			String price = clientInfo.getModel().getValueAt(tableRow, 4).toString();
			boolean hasBiggerPrice = false;
			//anunt cumparatorul caruia i s-a oferit acest produs
			for(int i = 0; i < server.getUsers().size(); i++)
			{
				ClientInformation user = server.getUsers().get(i);
				if(user.getUType().equals(UserType.BUYER)//numele buyer-ului din tabela celui care face oferta
						&& user.getUsername().equals(clientInfo.getModel().getValueAt(tableRow, 2).toString()))
				{
					DefaultTableModel buyerModel = user.getModel();
					for(int j = 0; j < buyerModel.getRowCount(); j++)
					{
						if(buyerModel.getValueAt(j, 0).toString().equals(prodName) 
							&& buyerModel.getValueAt(j, 2).toString().equals(username))//coloana furnizor	
						{
							buyerModel.setValueAt(Status.OFFER_MADE.getName(), j, 3);
							buyerModel.setValueAt(price, j, 4);
						}						
					}
				}
				//se schimba statusul in tabelele seller-ilor care au pret mai mare
				else if(user.getUType().equals(UserType.SELLER))
				{
					DefaultTableModel sellerModel = user.getModel();
					for(int j = 0; j < sellerModel.getRowCount(); j++)
					{
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
							&& sellerModel.getValueAt(j, 3).toString().equals(Status.OFFER_MADE.getName())
							&& sellerModel.getValueAt(j, 2).toString().equals(buyerName))//coloana cumparator	
						{
							int actualPrice = Integer.parseInt(sellerModel.getValueAt(j, 4).toString());
							if(actualPrice > Integer.parseInt(price))//acest seller are un pret mai mare
							{
								sellerModel.setValueAt(Status.OFFER_EXCEEDED.getName(), j, 3);
							}
							if(actualPrice < Integer.parseInt(price))//user-ul curent ofera un pret mai mare
								hasBiggerPrice = true;
						}
						
					}	
				}
			}
			
			Status newStatus = Status.OFFER_MADE;
			if(hasBiggerPrice)
				newStatus = Status.OFFER_EXCEEDED;
			userReqModel.setValueAt(newStatus.getName(), tableRow, 3);
			userReqModel.setValueAt(price, tableRow, 4);//schimba pretul eventual
//			clientInfo.setModel(userReqModel); //updatez modelul
		}
	}

}
