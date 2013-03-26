package gui;

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
