import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;


public class MainWindow extends JFrame{

	MainWindow(){
 	    super("Zanko Auction House");

	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - getHeight()) / 2);
	    setLocation(x, y);

   	    setLayout(new BorderLayout());
   	    setDefaultCloseOperation(EXIT_ON_CLOSE);
   	    
   	    //la click pe butonul login
   	    this.add(new TableView("Username", UserType.BUYER), BorderLayout.CENTER);
  	    setSize(600, 200);
   	    setVisible(true);

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
