package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import types.UserType;

import mediator.IGUIMediator;
import mediator.Mediator;

public class MainWindow extends JFrame implements IMainWindow {

	IGUIMediator mediator;
	private UserType uType;
	private String username;
	public String password;
	private TableView tableView;
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
		this.setUType(uType);
		this.setUsername(username);
		this.password = password;
	    userView.setStateView(this);
		setVisible(true);
	}	
	
	public void changeProgresBar(Integer value, int row, int column)
	{
		DefaultTableModel model = this.tableView.getModel();
		model.setValueAt(value, row, column);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
	
	public void setTableView(TableView tableView) {
		this.tableView = tableView;
	}

	public TableView getTableView() {
		return tableView;
	}

	public void setUType(UserType uType) {
		this.uType = uType;
	}

	public UserType getUType() {
		return uType;
	}

}
