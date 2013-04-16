package control;

import gui.IMainWindow;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.ClientInformation;
import network.Server;

import mediator.IGUIMediator;
import mediator.Mediator;
import types.UserType;

/**
 * Clasa folosita pentru a face modificarile necesare cand se primeste
 * comanda "View Best Offer" - folosita de un furnizor pentru 
 * a vedea cea mai buna contraoferta pentru un anumit produs
 * @author silvia
 *
 */
public class ViewBestOffer implements Command{

	public Server server;
	
	public ViewBestOffer(Server server) {
		this.server = server;
	}
	
	/**
	 * Metoda ce extrage cea mai buna contraoferta daca exista si afiseaza
	 */
	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		DefaultTableModel userReqModel = clientInfo.getModel();		
		String prodName = clientInfo.getModel().getValueAt(tableRow, 0).toString();
		String username = clientInfo.getUsername();
		int myPrice = Integer.parseInt(userReqModel.getValueAt(tableRow, 4).toString());
		int min = Integer.MAX_VALUE;
		String bestContraSeller = "";
		
		//caut seller-ul cu o oferta mai mica
		for(int i = 0; i < server.getUsers().size(); i++)
		{
			ClientInformation user = server.getUsers().get(i);
			DefaultTableModel sellerModel = user.getModel();
			if(user.getUType().equals(UserType.SELLER))
					for(int j = 0; j < sellerModel.getRowCount(); j++)
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
								&& !user.getUsername().equals(username))
						{
							int price = Integer.parseInt(sellerModel.getValueAt(j, 4).toString());
							if(price < min)
							{
								bestContraSeller = user.getUsername();
								min = price;
							}
						}		
		}
		if(bestContraSeller != "" && min < myPrice)
			JOptionPane.showMessageDialog(null, bestContraSeller + " has the best counteroffer: " + min);
		else
			JOptionPane.showMessageDialog(null, "You have the best offer!");

				
	}

}
