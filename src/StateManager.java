import javax.swing.JOptionPane;
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
		String prodName = userPanel.getModel().getValueAt(tableRow, 0).toString();
		String username = userPanel.userInfo.username;
		String prodStatus = userPanel.getModel().getValueAt(tableRow, 1).toString();
		DefaultTableModel userReqModel = userPanel.getModel();		
		
		if(msg.equals("Launch Offer request"))
		{
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
							rowDataSeller[1] = "Active";// ar trebui sa folosesc clasele state?
							rowDataSeller[2] = username;
							rowDataSeller[3] = Status.NO_OFFER.getName();
							rowDataSeller[4] = price;//pret
							rowDataSeller[5] = "";//progress bar !!
							sellerModel.addRow(rowDataSeller);//adaug noi linii	
							
							rowData[0] = prodName;
							rowData[1] = "Active";// ar trebui sa folosesc clasele state?
							rowData[2] = sellerName;
							rowData[3] = Status.NO_OFFER.getName();
							rowData[4] = "";//pret
							rowData[5] = "";//progress bar !!
							userReqModel.addRow(rowData);//adaug noi linii
							receiveResponse = true;
							break;
						}
					}
				}
			}
			if(receiveResponse)//exista cel putin un furnizor
				userReqModel.removeRow(tableRow);//sterg linia pentru ca coloana furnizor era goala
//			userPanel.setModel(userReqModel); //updatez modelul
		}
		else if(msg.equals("Drop Offer request"))
		{
			if(prodStatus.equals("Inactive")){
				JOptionPane.showMessageDialog(null, "There was no request made.");
				return;
			}

			//anunt sellerii care ii oferisera acest produs
			for(int i = 0; i < med.users.size(); i++)
			{
				MainWindow user = med.users.get(i).gui;
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
								thisProdRows--;
							}
						}
					}
					if(thisProdRows == 0)
					{
						Object[] rowData = new Object[sellerModel.getColumnCount()];
						rowData[0] = prodName;
						rowData[1] = "Inactive";// ar trebui sa folosesc clasele state?
						rowData[2] = "";
						rowData[3] = Status.INACTIVE.getName();
						rowData[4] = "";//pret
						rowData[5] = "";//progress bar 
						sellerModel.addRow(rowData);//adaug noi linii
					}
				}
					
			}
			//sterg toate liniile cu acest produs
			for(int j = 0; j < userReqModel.getRowCount(); j++)
				if(userReqModel.getValueAt(j, 0).toString().equals(prodName))
						userReqModel.removeRow(j);
			
			Object[] rowData = new Object[userReqModel.getColumnCount()];
			rowData[0] = prodName;
			rowData[1] = "Inactive";// ar trebui sa folosesc clasele state?
			rowData[2] = "";
			rowData[3] = Status.INACTIVE.getName();
			rowData[4] = "";//pret
			rowData[5] = "";//progress bar 
			userReqModel.addRow(rowData);//adaug noi linii

			userPanel.setModel(userReqModel); //updatez modelul
	
		}
		else if(msg.equals("Make offer"))//!!! se schimba statusul in tabelele seller-ilor care au pret mai mare
		{
			if(prodStatus.equals("Inactive")){
				JOptionPane.showMessageDialog(null, "There was no request made.");
				return;
			}
			else if(prodStatus.equals(Status.OFFER_MADE.getName())){
				JOptionPane.showMessageDialog(null, "An offer was already made.");
				return;
			}
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
					break;
				}
			}
			
			userReqModel.setValueAt(Status.OFFER_MADE.getName(), tableRow, 3);
			userReqModel.setValueAt(price, tableRow, 4);//schimba pretul eventual
			userPanel.setModel(userReqModel); //updatez modelul
		}
		else if(msg.equals("Drop auction"))
		{
			//seller-ul poate renunta la licitatie doar daca e exceeded
			if(prodStatus.equals(Status.OFFER_EXCEEDED.getName()))//??cand devine exceeded------------------------------
			{
				userReqModel.setValueAt(Status.NO_OFFER.getName(), tableRow, 3);
			}
			
		}
		else if(msg.equals("Accept Offer"))
		{
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
		else if(msg.equals("Refuse Offer"))
		{
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
	
}
