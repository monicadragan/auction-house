package control;
import gui.MainWindow;
import gui.TableView;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import types.Status;
import types.UserType;

import mediator.Mediator;

public class LaunchRequest implements Command {

	public Mediator med;
	
	public LaunchRequest(Mediator med) {
		this.med = med;
	}
	
	@Override
	public void execute(int tableRow, int tableCol, TableView userPanel)
	{
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		String prodStatus = userPanel.getModel().getValueAt(tableRow, 1).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		
		
		boolean receiveResponse = false;
		if(prodStatus.equals("Active")){
			JOptionPane.showMessageDialog(null, "Request already launched.");
			return;
		}
		
		//anunt sellerii care au acest produs si-i adaug in tabela cumparatorului cu No offer
		for(int i = 0; i < med.users.size(); i++)
		{
			MainWindow user = med.users.get(i).gui; 
			if(user.uType == UserType.SELLER){
				DefaultTableModel sellerModel = user.tableView.getModel();
				//parcurg tabela					
				for(int j = 0; j < sellerModel.getRowCount(); j++)
				{
					if(sellerModel.getValueAt(j, 0).toString().equals(prodName))
					{
						String sellerName = user.tableView.userInfo.username;							
						
						Object[] rowData = new Object[userReqModel.getColumnCount()];
						Object[] rowDataSeller = new Object[userReqModel.getColumnCount()];
						String price = sellerModel.getValueAt(j, 4).toString();
						if(sellerModel.getValueAt(j, 1).toString().equals("Inactive")){
							sellerModel.removeRow(j);
						}
						
						rowDataSeller[0] = prodName;
						rowDataSeller[1] = "Active";
						rowDataSeller[2] = username;
						rowDataSeller[3] = Status.NO_OFFER.getName();
						rowDataSeller[4] = price;//pret
						rowDataSeller[5] = 0;//progress bar !!
						sellerModel.addRow(rowDataSeller);//adaug noi linii	
						
						rowData[0] = prodName;
						rowData[1] = "Active";
						rowData[2] = sellerName;
						rowData[3] = Status.NO_OFFER.getName();
						rowData[4] = "";//pret
						rowData[5] = 0;//progress bar !!
						userReqModel.addRow(rowData);//adaug noi linii
						receiveResponse = true;
						break;
					}
				}
			}
		}
		if(receiveResponse)//exista cel putin un furnizor
			userReqModel.removeRow(tableRow);//sterg linia pentru ca coloana furnizor era goala

	}
	
}

