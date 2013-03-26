import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import types.UserType;
import mediator.Mediator;
import mediator.UserThread;


public class SimulateAuctionHouse {
	
	Mediator mediator;
	
	public SimulateAuctionHouse() throws InterruptedException {

		mediator = new Mediator();
//	    un utilizator face log in 
		UserThread seller1 = loginUser("user1", "pass1", UserType.SELLER);
	    Thread.sleep(2000);
		UserThread seller2 = loginUser("user5", "pass5", UserType.SELLER);
	    Thread.sleep(2000);

		UserThread buyer1 = loginUser("user3", "pass3", UserType.BUYER);
	    Thread.sleep(2000);
		UserThread buyer2 = loginUser("user4", "pass4", UserType.BUYER);
	    Thread.sleep(2000);
	    
//	    se lanseaza o cerere de oferta,
		JOptionPane.showMessageDialog(null, "user3 Launch Offer request for "
					+buyer1.gui.tableView.getModel().getValueAt(1, 0));
	    mediator.sendRequest("Launch Offer request", 1, 0, buyer1.gui.tableView);

//	    se anuleaza o cerere de oferta,
		JOptionPane.showMessageDialog(null, "user3  Drop Offer request for "
					+buyer1.gui.tableView.getModel().getValueAt(1, 0));	    
	    mediator.sendRequest("Drop Offer request", 1, 0, buyer1.gui.tableView);

//	    se face o oferta,
	    String prodName = buyer1.gui.tableView.getModel().getValueAt(0, 0).toString();	    
	    JOptionPane.showMessageDialog(null, "user3 Launch Offer request for "+prodName);
	    int buyerProductRow1 = buyer1.gui.tableView.getModel().getRowCount()-1;
	    mediator.sendRequest("Launch Offer request", 0, 0, buyer1.gui.tableView);
	    
	    String prodName2 = buyer2.gui.tableView.getModel().getValueAt(0, 0).toString();
	    JOptionPane.showMessageDialog(null, "user4 Launch Offer request for "+prodName2);
	    int buyerProductRow2 = buyer2.gui.tableView.getModel().getRowCount()-1;
	    mediator.sendRequest("Launch Offer request", 0, 0, buyer2.gui.tableView);
	    
	    DefaultTableModel sellerModel = seller1.gui.tableView.getModel();
		for(int j = 0; j < sellerModel.getRowCount(); j++)
		{
			if(sellerModel.getValueAt(j, 0).equals(prodName)
					&& sellerModel.getValueAt(j, 2).equals(buyer1.gui.tableView.userInfo.username))
			{
				JOptionPane.showMessageDialog(null, "user1 Make offer to user3");
				mediator.sendRequest("Make offer", j, 2, seller1.gui.tableView);				
				break;
			}
			if(sellerModel.getValueAt(j, 0).equals(prodName2)
					&& sellerModel.getValueAt(j, 2).equals(buyer2.gui.tableView.userInfo.username))
			{
				JOptionPane.showMessageDialog(null, "user3 Make offer to user4");
				mediator.sendRequest("Make offer", j, 2, seller1.gui.tableView);				
				break;
			}
		}
	    DefaultTableModel sellerModel2 = seller2.gui.tableView.getModel();
		for(int j = 0; j < sellerModel2.getRowCount(); j++)
		{
			if(sellerModel2.getValueAt(j, 0).equals(prodName)
				&& sellerModel.getValueAt(j, 2).equals(buyer1.gui.tableView.userInfo.username))
			{
				JOptionPane.showMessageDialog(null, "user5 Make offer to user3");
				mediator.sendRequest("Make offer", j, 2, seller2.gui.tableView);				
				break;
			}
			if(sellerModel2.getValueAt(j, 0).equals(prodName2) 
					&& sellerModel.getValueAt(j, 2).equals(buyer2.gui.tableView.userInfo.username))
			{
				JOptionPane.showMessageDialog(null, "user5 Make offer to user4");
				mediator.sendRequest("Make offer", j, 2, seller2.gui.tableView);				
				break;
			}
		}

//	    oferta este acceptata/depasita/refuzata,
		JOptionPane.showMessageDialog(null, "user3 Offer accepted");
		mediator.sendRequest("Accept Offer", buyerProductRow1, 2, buyer1.gui.tableView);

		JOptionPane.showMessageDialog(null, "user4 Offer refused");
		mediator.sendRequest("Refuse Offer", buyerProductRow2, 2, buyer2.gui.tableView);

//		logout
		JOptionPane.showMessageDialog(null, "Logout");
	    seller1.gui.tableView.logout();
		seller2.gui.tableView.logout();
	    buyer1.gui.tableView.logout();
	    buyer2.gui.tableView.logout();
	}

	public UserThread loginUser(String user, String passwd, UserType uType) throws InterruptedException{
		UserThread t = new UserThread(mediator);
	    t.start();
	    Thread.sleep(1000);
	    t.gui.loginRequest(user, passwd, uType);
	    mediator.users.add(t);
	    return t;
	}
	
	public static void main(String s[]) throws InterruptedException{
		SimulateAuctionHouse sim = new SimulateAuctionHouse();
	}
	
}
