package gui;

/**
 * Clasa ce defineste contextul unei stari
 * avand drept membru starea curenta
 * @author silvia
 *
 */
public class ConcreteUserView {

	public StateView stateView;
	
	public ConcreteUserView()
	{
		stateView = new LoginState();
	}
	
	public void setStateView(MainWindow mainFrame)
	{
		stateView.setPanel(this, mainFrame);
	}
	
	
}
