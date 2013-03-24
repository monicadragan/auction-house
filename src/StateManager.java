import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory.Default;


public class StateManager {
	private Statelike currentState;
	private Mediator med;
	
	//user-ul + product pentru care se verifica starea

	public StateManager(Mediator med) {
		this.med = med;
		currentState = new StateInactive(med);
	}

	public void processRequest(String msg, int tableRow, int tableCol, TableView userPanel)
	{
		String prodName = userPanel.getModel().getValueAt(tableRow, tableCol).toString();
		
		if(msg.equals("Launch Offer request"))
		{
			DefaultTableModel userReqModel = userPanel.getModel();
			//anunt sellerii care au acest produs si-i adaug in tabela cumparatorului cu No offer
			for(int i = 0; i < med.users.size(); i++)
			{
				MainWindow user = med.users.get(i).gui; 
				if(user.uType == UserType.SELLER){
					//parcurg tabela
					TableModel model = user.tableView.table.getModel();
					for(int j = 0; j < model.getRowCount(); j++)
					{
						if(model.getValueAt(j, 0).toString().equals(prodName))
						{
							Object[] rowData = new Object[userReqModel.getColumnCount()];

							model.setValueAt(userPanel.userInfo.username, j, 2);//!! hardcodat _2_ ?
							model.setValueAt("Active", j, 1);
							model.setValueAt(Status.NO_OFFER.getName(), j, 3);
							
							rowData[0] = prodName;
							rowData[1] = "Active";// ar trebui sa folosesc clasele state?
							rowData[2] = user.tableView.userInfo.username;//!!!!! NU e bine -> sunt prea multe nivele??!!!!!!!!!!
							rowData[3] = Status.NO_OFFER.getName();
							rowData[4] = 0;//pret
							rowData[5] = "";//progress bar !!
							userReqModel.addRow(rowData);//adaug noi linii
						}
					}
				}
			}
			userReqModel.removeRow(tableRow);//sterg linia pentru ca coloana furnizor era goala
			userPanel.setModel(userReqModel); //updatez modelul
		}
		else if(msg.equals("Drop Offer request"))//TODO
		{
			
		}

	}
	
}
