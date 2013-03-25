package mediator;
import gui.MainWindow;

public class UserThread extends Thread{
	
	Mediator mediator;
	public MainWindow gui;
	
	public UserThread(Mediator mediator){
		this.mediator = mediator;
	}

	public void run()
	{
		gui = new MainWindow(mediator);
	}
	public void cancel()
	{
		interrupt();
	}
	
}