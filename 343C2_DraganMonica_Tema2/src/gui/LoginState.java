package gui;

/**
 * Clasa defineste si seteaza starea de vizualizare (panel-ul)
 * ce apare la login  
 * @author silvia
 *
 */
public class LoginState implements StateView{

	@Override
	public void setPanel(ConcreteUserView userView,
						MainWindow mainFrame)
	{
	    LoginPanel loginPan = new LoginPanel(mainFrame);
	    mainFrame.setContentPane(loginPan);
	    mainFrame.setTitle("Login");
	    userView.stateView = new TableState();//setez noua stare
	}

}
