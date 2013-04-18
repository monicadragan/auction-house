package gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import types.UserType;

/**
 * Clasa ce defineste interfata grafica pentru fereastra de login
 * @author silvia
 *
 */
public class LoginPanel extends JPanel{
	
	MainWindow mainFrame;
	
	protected JLabel lUsername = new JLabel("Username");		// username label
	protected JTextField	tUsername = new JTextField(10);		// username field
	protected JLabel lPassword = new JLabel("Parola");		// password label
	protected JTextField	tPassword = new JTextField(10);		// password field
	protected JButton	bLogin	= new JButton("Login");		// login button
	protected static final String userTypeOptions[] = { "Furnizor", "Cumparator"};
	protected JPanel userTypePanel;
	
	public LoginPanel(MainWindow frame)
	{
		this.mainFrame = frame;
		init();
	}
	
	/**
	 * Constructor folosit in simulare
	 */
	public LoginPanel(MainWindow frame, String user, String passwd, String type)
	{
		this.mainFrame = frame;
		tUsername.setText(user);
		tPassword.setText(passwd);
		init();
	}
	
	
	public void init() {
		
		JPanel userPanel = new JPanel(new GridLayout(1, 2)); 
		JPanel passwdPanel = new JPanel(new GridLayout(1, 2));
		userTypePanel = createRadioButtonGrouping(userTypeOptions, "User Type");
		JPanel loginPanel = new JPanel(new GridLayout(1, 0));
		
		this.setLayout(new GridLayout(10, 3));
		this.add(userTypePanel);
		this.add(new JPanel());
		
		this.add(userPanel);
		this.add(new JPanel());
		
		this.add(passwdPanel);
		this.add(new JPanel());
				
		this.add(loginPanel);
		//dummy panels
		this.add(new JPanel()); this.add(new JPanel());this.add(new JPanel());
		this.add(new JPanel()); this.add(new JPanel());this.add(new JPanel());
		
		userPanel.add(lUsername);
		userPanel.add(tUsername);
		passwdPanel.add(lPassword);
		passwdPanel.add(tPassword);

		loginPanel.add(new JPanel());
		loginPanel.add(bLogin);

		bLogin.addActionListener(new LoginActionListener());
	}

	/**
	 *	ActionListener pentru butonul de Login
	 */
	protected class LoginActionListener implements ActionListener
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
			if(selectedType.equals(userTypeOptions[0]))
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
	
	 public String getSelectedElement(Container container) {
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
