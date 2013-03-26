package control;
import gui.MainWindow;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import types.Status;
import types.UserType;

import mediator.Mediator;

public class DropRequest implements Command {

	public Mediator med;
	
	public DropRequest(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		String prodStatus = userPanel.getModel().getValueAt(tableRow, 1).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		
	
		if(prodStatus.equals("Inactive")){
			JOptionPane.showMessageDialog(null, "There was no request made.");
			return;
		}

		//anunt sellerii care ii oferisera acest produs
		for(int i = 0; i < med.users.size(); i++)
		{
			MainWindow user = med.users.get(i).gui;
			String price = "";
			if(user.uType.equals(UserType.SELLER))
			{
				int thisProdRows = 0;//numarul de linii pentru acest produs care raman in tabela
				//parcurg tabela
				DefaultTableModel sellerModel = user.tableView.getModel();
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
					//sterg linia cu produsul si cu username-ul
					if(sellerModel.getValueAt(j, 0).toString().equals(prodName) )
					{
						thisProdRows ++;
						if(sellerModel.getValueAt(j, 2).toString().equals(username))	
						{
							sellerModel.removeRow(j);
							price = sellerModel.getValueAt(j, 4).toString();
							thisProdRows--;
						}
					}
				}
				if(thisProdRows == 0)
				{
					Object[] rowData = new Object[sellerModel.getColumnCount()];
					rowData[0] = prodName;
					rowData[1] = "Inactive";
					rowData[2] = "";
					rowData[3] = Status.INACTIVE.getName();
					rowData[4] = price;//pret
					rowData[5] = 0;//progress bar 
					sellerModel.addRow(rowData);//adaug noi linii
				}
			}
				
		}
		//sterg toate liniile cu acest produs
		for(int j = 0; j < userReqModel.getRowCount(); j++)
		{
			if(userReqModel.getValueAt(j, 0).toString().equals(prodName))
			{
				userReqModel.removeRow(j);
				j--;
			}
		}
		
		Object[] rowData = new Object[userReqModel.getColumnCount()];
		rowData[0] = prodName;
		rowData[1] = "Inactive";
		rowData[2] = "";
		rowData[3] = Status.INACTIVE.getName();
		rowData[4] = "";//pret
		rowData[5] = 0;//progress bar 
		userReqModel.addRow(rowData);//adaug noi linii

		userPanel.setModel(userReqModel); //updatez modelul

	}
		
}
