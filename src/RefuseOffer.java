import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class RefuseOffer implements Command{

	private Mediator med;
	
	public RefuseOffer(Mediator med) {
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
		//oferta este refuzata
		userReqModel.setValueAt(Status.OFFER_REFUSED.getName(), tableRow, 3);
		//anunt si seller-ul
		String sellerName = userReqModel.getValueAt(tableRow, tableCol).toString();
		for(int i = 0; i < med.users.size(); i++)
		{
			MainWindow user = med.users.get(i).gui;
			if(user.uType.equals(UserType.SELLER)
					&& user.tableView.userInfo.username.equals(sellerName))
			{
				//parcurg tabela
				DefaultTableModel sellerModel = user.tableView.getModel();
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
					if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
							&& sellerModel.getValueAt(j, 2).toString().equals(username))
					{
						sellerModel.setValueAt(Status.OFFER_REFUSED.getName(), j, 3);
						break;
					}
				}
				break;
			}
		}

	}

}
