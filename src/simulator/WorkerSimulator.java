package simulator;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import types.UserType;

import mediator.Mediator;
import mediator.UserThread;

public class WorkerSimulator {

	Mediator med;
	
	public WorkerSimulator()
	{
		med = new Mediator();
	}
	
	public void startWorking(){
	
        SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>() {

            protected int sleepDummy = new Random().nextInt(10) + 15;
            protected int lengthOfTask = 13;
            UserThread seller1, seller2;
            UserThread buyer1, buyer2;
            String prodName, prodName2;
            int buyerProductRow1, buyerProductRow2;
            
            @Override
            protected Integer doInBackground() {
                int current = 0;
                while (current < lengthOfTask) {

                    current++;
                    try {
                        Thread.sleep(200*sleepDummy);
                    } catch (InterruptedException ie) {
                        break;
                    }
                    publish(current);
                    
                }
                return sleepDummy * lengthOfTask;
            }
            @Override
            protected void process(List<Integer> c) {
            	Integer nr = c.get(c.size() - 1);
            	switch(nr)
            	{
//	testare login            	
            	case 1:
					seller1 = loginUser("user1", "pass1", UserType.SELLER);
					break;
            	case 2:	
					seller2 = loginUser("user5", "pass5", UserType.SELLER);
					break;					
            	case 3:
            		buyer1 = loginUser("user3", "pass3", UserType.BUYER);
            		break;
            	case 4:
            	    buyer2 = loginUser("user4", "pass4", UserType.BUYER);
            	    break;
//            	    se lanseaza o cerere de oferta,
            	case 5:
            	    JOptionPane.showMessageDialog(null, "user3 Launch Offer request for "
            					+buyer1.gui.getTableView().getModel().getValueAt(1, 0));
            	    med.sendRequest("Launch Offer request", 1, 0, buyer1.gui.getTableView());
            	    break;
//            	    se anuleaza o cerere de oferta,
            	case 6:
            		JOptionPane.showMessageDialog(null, "user3  Drop Offer request for "
            					+buyer1.gui.getTableView().getModel().getValueAt(1, 0));	    
            	    med.sendRequest("Drop Offer request", 1, 0, buyer1.gui.getTableView());
            	    break;
//            	    se face o oferta,
            	case 7:
            	    prodName = buyer1.gui.getTableView().getModel().getValueAt(0, 0).toString();	    
            	    JOptionPane.showMessageDialog(null, "user3 Launch Offer request for "+prodName);
            	    buyerProductRow1 = buyer1.gui.getTableView().getModel().getRowCount()-1;
            	    med.sendRequest("Launch Offer request", 0, 0, buyer1.gui.getTableView());
            	    break;
            	case 8:   
            	    prodName2 = buyer2.gui.getTableView().getModel().getValueAt(0, 0).toString();
            	    JOptionPane.showMessageDialog(null, "user4 Launch Offer request for "+prodName2);
            	    buyerProductRow2 = buyer2.gui.getTableView().getModel().getRowCount()-1;
            	    med.sendRequest("Launch Offer request", 0, 0, buyer2.gui.getTableView());
            	    break;
            	case 9:    
            	    DefaultTableModel sellerModel = seller1.gui.getTableView().getModel();
            		for(int j = 0; j < sellerModel.getRowCount(); j++)
            		{
            			if(sellerModel.getValueAt(j, 0).equals(prodName)
            					&& sellerModel.getValueAt(j, 2).equals(buyer1.gui.getTableView().userInfo.username))
            			{
            				JOptionPane.showMessageDialog(null, "user1 Make offer to user3");
            				med.sendRequest("Make offer", j, 2, seller1.gui.getTableView());				
            			}
            			if(sellerModel.getValueAt(j, 0).equals(prodName2)
            					&& sellerModel.getValueAt(j, 2).equals(buyer2.gui.getTableView().userInfo.username))
            			{
            				JOptionPane.showMessageDialog(null, "user3 Make offer to user4");
            				med.sendRequest("Make offer", j, 2, seller1.gui.getTableView());				
            			}
            		}
            		break;
            	case 10:
            	    DefaultTableModel sellerModel2 = seller2.gui.getTableView().getModel();
            		for(int j = 0; j < sellerModel2.getRowCount(); j++)
            		{
            			if(sellerModel2.getValueAt(j, 0).equals(prodName)
            				&& sellerModel2.getValueAt(j, 2).equals(buyer1.gui.getTableView().userInfo.username))
            			{
            				JOptionPane.showMessageDialog(null, "user5 Make offer to user3");
            				med.sendRequest("Make offer", j, 2, seller2.gui.getTableView());				
            			}
            			if(sellerModel2.getValueAt(j, 0).equals(prodName2) 
            					&& sellerModel2.getValueAt(j, 2).equals(buyer2.gui.getTableView().userInfo.username))
            			{
            				JOptionPane.showMessageDialog(null, "user5 Make offer to user4");
            				med.sendRequest("Make offer", j, 2, seller2.gui.getTableView());				
            			}
            		}
            		break;
            	case 11:
//            	    oferta este acceptata/depasita/refuzata,
            		JOptionPane.showMessageDialog(null, "user3 Accept Offer  from user1");
            		med.sendRequest("Accept Offer", buyerProductRow1, 2, buyer1.gui.getTableView());
            		break;
            	case 12:
            		JOptionPane.showMessageDialog(null, "user4 Refuse Offer from user1");
            		med.sendRequest("Refuse Offer", buyerProductRow2, 2, buyer2.gui.getTableView());
            		break;
//            		logout
            	case 13:
            		JOptionPane.showMessageDialog(null, "Logout");
            	    seller1.gui.getTableView().logout();
            	    break;
            	}
            }

            @Override
            protected void done() {
                String text;
                int i = -1;
                if (isCancelled()) {
                    text = "Cancelled";
                } else {
                    try {
                        i = get();
                        text = (i >= 0) ? "Done" : "Disposed";
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                        text = ignore.getMessage();
                    }
                }
                System.out.println(text + "(" + i + "ms)");
            }  
        };
       
        
        worker.execute();
	}
	

	public UserThread loginUser(String user, String passwd, UserType uType){
		UserThread t = new UserThread(med);
	    t.start();
	    try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    t.gui.loginRequest(user, passwd, uType);
	    med.users.add(t);
	    return t;
	}

	public static void main(String[] args)
	{
		WorkerSimulator ws = new WorkerSimulator();
		ws.startWorking();
		Scanner sc = new Scanner(System.in);
		while(true){
			System.out.print("Type command (launch/exit): ");
			String input = sc.nextLine();
			if(input.equals("exit")){
				System.exit(1);
			}
			if(input.equals("launch")){
				UserThread t = new UserThread(ws.med);
			    t.start();
			    ws.med.users.add(t);
			}
		}
	}
}
