package gui;

import javax.swing.JOptionPane;

import types.User;

/**
 * Clasa defineste si seteaza starea de vizualizare (panel-ul)
 * ce apare dupa login (cu lista de servicii)  
 * @author silvia
 *
 */
public class TableState implements StateView{

	@Override
	public void setPanel(ConcreteUserView userView,
						MainWindow mainFrame)
	{
		//preluarea datelor (modelului) de la mediator
		User userInfo = mainFrame.mediator.readUserInformation(
							mainFrame.getUsername(),
							mainFrame.password,
							mainFrame.getUType());
		if(userInfo == null)
		{
			JOptionPane.showMessageDialog(null, "This user is invalid!");
			return;
		}
		switch(mainFrame.getUType())
		{			
			case BUYER:
			{
				mainFrame.setTableView(new BuyerTableView(userInfo, mainFrame));
				mainFrame.setTitle("Buyer Products/Services List");
				break;
			}
			case SELLER:
			{			
				mainFrame.setTableView(new SellerTableView(userInfo, mainFrame));
				mainFrame.setTitle("Seller Products/Services List");
				break;
			}
		}

		mainFrame.setContentPane(mainFrame.getTableView());
		
		//anunt mediatorul ca ma pot conecta la server pentru a comunica cu alti utilizatori
		mainFrame.mediator.setReadyToConnect(true);
	}

}
