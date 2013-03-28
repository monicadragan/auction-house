package control;

import gui.IMainWindow;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import mediator.IGUIMediator;
import mediator.Mediator;
import types.UserType;

public class ViewBestOffer implements Command{

	public IGUIMediator med;
	
	public ViewBestOffer(Mediator med) {
		this.med = med;
	}
	
	/**
	 * Metoda ce extrage cea mai buna contraoferta daca exista si afiseaza
	 */
	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		DefaultTableModel userReqModel = userPanel.getModel();		
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		int myPrice = Integer.parseInt(userReqModel.getValueAt(tableRow, 4).toString());
		int min = Integer.MAX_VALUE;
		String bestContraSeller = "";
		
		//caut seller-ul cu o oferta mai mica
		for(int i = 0; i < med.getUsers().size(); i++)
		{
			IMainWindow user = med.getUsers().get(i).gui;
			DefaultTableModel sellerModel = user.getTableView().getModel();
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
