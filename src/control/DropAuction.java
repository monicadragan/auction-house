package control;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import types.Status;


import mediator.IGUIMediator;
import mediator.Mediator;


public class DropAuction implements Command{

	public IGUIMediator med;
	
	public DropAuction(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodStatusLicitatie = userPanel.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		
		
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
			userReqModel.setValueAt(0, tableRow, 5);//progress bar
		}
		
		
	}

}
