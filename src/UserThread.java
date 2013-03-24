class UserThread extends Thread{
	
	Mediator mediator;
	MainWindow gui; 
	
	UserThread(Mediator mediator){
		this.mediator = mediator;
	}

	public void run(){
		gui = new MainWindow(mediator);
	}
	
}