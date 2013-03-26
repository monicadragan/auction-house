import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class AcceptOffer implements Command{

	private Mediator med;
	
	public AcceptOffer(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		String prodStatus = userPanel.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		

		if(!prodStatus.equals(Status.OFFER_MADE.getName())){
			JOptionPane.showMessageDialog(null, "A seller should make an offer first!");
			return;
		}
		//accepta aceasta oferta
		userReqModel.setValueAt(Status.OFFER_ACCEPTED.getName(), tableRow, 3);
		//refuza restul ofertelor
		for(int j = 0; j < userReqModel.getRowCount(); j++)
			if(userReqModel.getValueAt(j, 0).toString().equals(prodName) && j != tableRow)
				userReqModel.setValueAt(Status.OFFER_REFUSED.getName(), j, 3);
		String sellerName = userReqModel.getValueAt(tableRow, tableCol).toString();
		for(int i = 0; i < med.users.size(); i++)
		{
			MainWindow user = med.users.get(i).gui;
			DefaultTableModel sellerModel = user.tableView.getModel();
			if(user.uType.equals(UserType.SELLER))
				if (user.tableView.userInfo.username.equals(sellerName))
				{
					for(int j = 0; j < sellerModel.getRowCount(); j++)
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
								&& sellerModel.getValueAt(j, 2).toString().equals(username))
						{
							sellerModel.setValueAt(Status.OFFER_ACCEPTED.getName(), j, 3);
							med.sendFile(user.tableView, userPanel,j,tableRow);
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
							break;
						}
				}
		
		}


	}

}
