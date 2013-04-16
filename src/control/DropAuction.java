package control;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import network.ClientInformation;
import network.Server;

import types.Status;


import mediator.IGUIMediator;
import mediator.Mediator;

/**
 * Clasa folosita pentru a face modificarile necesare cand se primeste
 * comanda "Drop Auction"
 * @author silvia
 *
 */
public class DropAuction implements Command{

	public Server server;
	
	public DropAuction(Server server) {
		this.server = server;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, ClientInformation clientInfo)
	{
		String prodStatusLicitatie = clientInfo.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = clientInfo.getModel();		
		
		if(prodStatusLicitatie.equals(Status.OFFER_MADE.getName())
				|| prodStatusLicitatie.equals(Status.OFFER_ACCEPTED.getName()))
		{
			JOptionPane.showMessageDialog(null, "You can't drop the auction!");
			return;
		}
		//seller-ul poate renunta la licitatie daca e exceeded, refused, transfer failed
		if(prodStatusLicitatie.equals(Status.OFFER_EXCEEDED.getName())
				|| prodStatusLicitatie.equals(Status.OFFER_REFUSED.getName())
				|| prodStatusLicitatie.equals(Status.TRANSFER_FAILED.getName()))
		{
			userReqModel.setValueAt(Status.NO_OFFER.getName(), tableRow, 3);
			userReqModel.setValueAt(0, tableRow, 5);//resetare progress bar
		}
		
		
	}

}
