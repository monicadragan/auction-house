package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.StyledEditorKit.BoldAction;

import types.User;
import types.UserType;

import mediator.Mediator;

public class MainWindow extends JFrame{

	Mediator mediator;
	public UserType uType;
	public TableView tableView;
	
	public MainWindow(Mediator mediator){
		
 	    super("Zanko Auction House");
 	    		
		this.mediator = mediator;
		
 	    setLayout(new BorderLayout());
  	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setTitle("Login");
	    
  	    setSize(500, 300);
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((dim.width-getWidth())/2, (dim.height-getHeight())/2);
	    
	    LoginPanel login = new LoginPanel(this);
	    setContentPane(login);
   	    setVisible(true);

	}
	
	public void loginRequest(String username, String password, UserType uType)
	{
		System.out.println(username);
		this.uType = uType;
		User userInfo = mediator.readUserInformation(username, password, uType);
		if(userInfo == null)
		{
			JOptionPane.showMessageDialog(null, "This user is invalid");
			return;
		}
		switch(uType)
		{			
			case BUYER:
			{
				tableView = new BuyerTableView(userInfo, this);
				setTitle("Buyer Products/Services List");
				break;
			}
			case SELLER:
			{			
				tableView = new SellerTableView(userInfo, this);
				setTitle("Seller Products/Services List");
				break;
			}
		}
		setContentPane(tableView);
		setVisible(true);

	}	
		
	public static void main(String[] args)
	{
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow(null);
			}
		});
	}
}
