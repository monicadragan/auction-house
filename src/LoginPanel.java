import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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


public class LoginPanel extends JPanel{
	
	MainWindow mainFrame;
	
	private JLabel lUsername = new JLabel("Username");		// username label
	private JTextField	tUsername = new JTextField(10);		// username field
	private JLabel lPassword = new JLabel("Password");		// password label
	private JTextField	tPassword = new JTextField(10);		// password field
	private JButton	bLogin	= new JButton("Login");		// login button
	private static final String userTypeOptions[] = { "Seller", "Buyer"};
	private JPanel userTypePanel;
	
	public LoginPanel(MainWindow frame)
	{
		this.mainFrame = frame;
		init();
	}
	
	public void init() {
		
		JPanel userPanel = new JPanel(new GridLayout(1, 2)); 
		JPanel passwdPanel = new JPanel(new GridLayout(1, 2));
		userTypePanel = createRadioButtonGrouping(userTypeOptions, "User Type");
		JPanel loginPanel = new JPanel(new GridLayout(1, 0));
		
		this.setLayout(new GridLayout(10, 3));
//				(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(userTypePanel);
		this.add(new JPanel());
		
		this.add(userPanel);
		this.add(new JPanel());
		
		this.add(passwdPanel);
		this.add(new JPanel());
		
		
		this.add(loginPanel);
		this.add(new JPanel()); this.add(new JPanel());this.add(new JPanel());
		this.add(new JPanel()); this.add(new JPanel());this.add(new JPanel());
		
		// bottom panel: name field, add button, remove button
		userPanel.add(lUsername);
		userPanel.add(tUsername);
		passwdPanel.add(lPassword);
		passwdPanel.add(tPassword);

		loginPanel.add(new JPanel());
		loginPanel.add(bLogin);

		bLogin.addActionListener(new LoginActionListener());
	}

	private class LoginActionListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String username = tUsername.getText();
			String passwd = tPassword.getText();
			String selectedType = getSelectedElement(userTypePanel);
			UserType uType;
			if(username == null)
			{
				System.err.println("Invalid username");
				JOptionPane.showMessageDialog(null, "Invalid username");
				return;
			}
			if(selectedType == null)
			{
				System.err.println("You must choose a type!");
				JOptionPane.showMessageDialog(null, "You must choose a type!");
				return;
			}
			System.out.println("Login " + username + "::" + selectedType);
			if(selectedType.equals("Seller"))
				uType = UserType.SELLER;
			else uType = UserType.BUYER;
			
			mainFrame.loginRequest(username, passwd, uType);
		}
		
	}

	public JPanel createRadioButtonGrouping(String elements[], String title) {
		    JPanel panel = new JPanel(new GridLayout(1, 2));
		    ButtonGroup group = new ButtonGroup();
		    JRadioButton aRadioButton;

		    for (int i = 0, n = elements.length; i < n; i++) {
		      aRadioButton = new JRadioButton(elements[i]);
		      panel.add(aRadioButton);
		      group.add(aRadioButton);
		    }
		    return panel;
	}
	
	 private String getSelectedElement(Container container) {
	    Component components[] = container.getComponents();
	    for (int i = 0, n = components.length; i < n; i++) {
	      if (components[i] instanceof AbstractButton) {
	        AbstractButton button = (AbstractButton) components[i];
	        if (button.isSelected()) {
	          return button.getText();
	        }
	      }
	    }
	    return null;
	}

}
