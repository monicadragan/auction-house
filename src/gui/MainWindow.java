package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import types.UserType;

import mediator.Mediator;

public class MainWindow extends JFrame implements IMainWindow {

	Mediator mediator;
	public UserType uType;
	public String username;
	public String password;
	public TableView tableView;
	public ConcreteUserView userView;
	
	public MainWindow(Mediator mediator){
		
 	    super("Zanko Auction House");
 	    		
		this.mediator = mediator;
		userView = new ConcreteUserView();
		initWindow();
		
	}

	public void initWindow()
	{
 	    setLayout(new BorderLayout());
  	    setDefaultCloseOperation(EXIT_ON_CLOSE);
  	    setSize(500, 300);
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    setLocation((dim.width-getWidth())/2, (dim.height-getHeight())/2);
	    
	    userView.setStateView(this);
   	    setVisible(true);
   	    
	}
	
	public void loginRequest(String username, String password, UserType uType)
	{
		this.uType = uType;
		this.username = username;
		this.password = password;
	    userView.setStateView(this);
		setVisible(true);
	}	
	
	public void changeProgresBar(Integer value, int row, int column)
	{
		DefaultTableModel model = this.tableView.getModel();
		model.setValueAt(value, row, column);
	}
}
