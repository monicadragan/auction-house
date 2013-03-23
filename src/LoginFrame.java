import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;


public class LoginFrame extends JPanel{
	
	private JLabel lUsername = new JLabel("Username");		// username label
	private JTextField	tUsername = new JTextField(10);		// username field
	private JLabel lPassword = new JLabel("Password");		// password label
	private JTextField	tPassword = new JTextField(10);		// password field
	private JButton	bLogin	= new JButton("Login");		// login button
	private static final String userTypeOptions[] = { "Seller", "Buyer"};
	
	public LoginFrame()
	{
		init();
	}
	
	public void init() {
		
		// main panel: top panel, bottom panel
		JPanel userPanel = new JPanel(new GridLayout(1, 2)); // 1 row, any number of columns
		JPanel passwdPanel = new JPanel(new GridLayout(1, 2));
		JPanel userTypePanel = createRadioButtonGrouping(userTypeOptions, "User Type");
		JPanel loginPanel = new JPanel(new GridLayout(1, 0));
//		this.setLayout(new BorderLayout());
		this.add(userTypePanel);
		this.add(userPanel);
		this.add(passwdPanel);
		this.add(loginPanel);
		
		// bottom panel: name field, add button, remove button
		userPanel.add(lUsername);
		userPanel.add(tUsername);
		passwdPanel.add(lPassword);
		passwdPanel.add(tPassword);
                
		loginPanel.add(bLogin);
		bLogin.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String username = tUsername.getText();
				String passwd = tPassword.getText();
				if(username == null)
				{
					System.err.println("Invalid username");
					return;
				}
				if(passwd == null)
				{
					System.err.println("Invalid password");
					return;
				}
				
				System.out.println("Login " + username);
				
			}
		});
	}

	public JPanel createRadioButtonGrouping(String elements[], String title) {
		    JPanel panel = new JPanel(new GridLayout(1, 2));
		    //   Create group
		    ButtonGroup group = new ButtonGroup();
		    JRadioButton aRadioButton;
		    //   For each String passed in:
		    //   Create button, add to panel, and add to group
		    for (int i = 0, n = elements.length; i < n; i++) {
		      aRadioButton = new JRadioButton(elements[i]);
		      panel.add(aRadioButton);
		      group.add(aRadioButton);
		      //TODO - action listener!!!!
//		      if (actionListener != null) {
//		        aRadioButton.addActionListener(actionListener);
//		      }
//		      if (itemListener != null) {
//		        aRadioButton.addItemListener(itemListener);
//		      }
//		      if (changeListener != null) {
//		        aRadioButton.addChangeListener(changeListener);
//		      }
		    }
		    return panel;
		  }

	
	public static void buildGUI() {
		JFrame frame = new JFrame("Login"); // title
		frame.setContentPane(new LoginFrame()); // content: the JPanel above
        frame.setLocationRelativeTo(null);
		frame.setSize(300, 300); // width / height
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit application when window is closed
		frame.setVisible(true); // show it!
	}

	public static void main(String[] args) {
		// run on EDT (event-dispatching thread), not on main thread!
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				buildGUI();
			}
		});

	}	

}
