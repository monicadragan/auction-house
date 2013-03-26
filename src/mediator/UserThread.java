package mediator;
import gui.*;

public class UserThread extends Thread{
	
	Mediator mediator;
	public IMainWindow gui;
	
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