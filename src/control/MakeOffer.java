package control;
import gui.MainWindow;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import types.Status;
import types.UserType;

import mediator.Mediator;


public class MakeOffer implements Command{

	public Mediator med;
	
	public MakeOffer(Mediator med) {
		this.med = med;
	}

	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		String buyerName = userPanel.getModel().getValueAt(tableRow, 2).toString();
		String prodStatus = userPanel.getModel().getValueAt(tableRow, 1).toString();
		String prodStatusLicitatie = userPanel.getModel().getValueAt(tableRow, 3).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		

		if(prodStatus.equals("Inactive")){
			JOptionPane.showMessageDialog(null, "There was no request made.");
			return;
		}
		else if(prodStatusLicitatie.equals(Status.OFFER_MADE.getName())){
			JOptionPane.showMessageDialog(null, "An offer was already made.");
			return;
		}
		if(prodStatusLicitatie.equals(Status.OFFER_MADE.getName())
				|| prodStatusLicitatie.equals(Status.NO_OFFER.getName()))
		{
			String price = userPanel.getModel().getValueAt(tableRow, 4).toString();
			//anunt cumparatorul caruia i s-a oferit acest produs
			for(int i = 0; i < med.users.size(); i++)
			{
				MainWindow user = med.users.get(i).gui;
				if(user.uType.equals(UserType.BUYER)//numele buyer-ului din tabela celui care face oferta
						&& user.tableView.userInfo.username.equals(
								userPanel.getModel().getValueAt(tableRow, 2).toString()))
				{
					DefaultTableModel buyerModel = user.tableView.getModel();
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
				else if(user.uType.equals(UserType.SELLER))
				{
					DefaultTableModel sellerModel = user.tableView.getModel();
					for(int j = 0; j < sellerModel.getRowCount(); j++)
					{
						if(sellerModel.getValueAt(j, 0).toString().equals(prodName)
							&& sellerModel.getValueAt(j, 3).toString().equals(Status.OFFER_MADE.getName())
							&& sellerModel.getValueAt(j, 2).toString().equals(buyerName))//coloana cumparator	
						{
							int actualPrice = Integer.parseInt(sellerModel.getValueAt(j, 4).toString());
							if(actualPrice > Integer.parseInt(price))//are un pret mai mare
							{
								sellerModel.setValueAt(Status.OFFER_EXCEEDED.getName(), j, 3);
							}
						}
						
					}	
				}
			}
			
			userReqModel.setValueAt(Status.OFFER_MADE.getName(), tableRow, 3);
			userReqModel.setValueAt(price, tableRow, 4);//schimba pretul eventual
			userPanel.setModel(userReqModel); //updatez modelul
		}
	}

}
