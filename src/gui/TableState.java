package gui;

import javax.swing.JOptionPane;

import types.User;

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

	}

}
