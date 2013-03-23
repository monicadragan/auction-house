import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;


public class MainWindow extends JFrame{
	ActionsController actionCtrl;
	
	MainWindow(){
 	    super("Zanko Auction House");
 	    setLayout(new BorderLayout());
  	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setTitle("Login");
  	    
  	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);
	    
	    actionCtrl = new ActionsController(this);
	    setContentPane(new LoginPanel(actionCtrl));
	    //la click pe butonul login
//	    changeContentPane(new TableView("Username", UserType.BUYER));
//   	    this.add(new TableView("Username", UserType.BUYER), BorderLayout.CENTER);
  	    setSize(300, 300);
   	    setVisible(true);

	}
	
	public void changeContentPane(Container content)
	{
		this.setContentPane(content);
	}
		
	public static void main(String[] args)
	{
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MainWindow();
			}
		});
	}
}
