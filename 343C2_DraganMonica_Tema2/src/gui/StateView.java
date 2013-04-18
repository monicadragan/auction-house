package gui;

/**
 * Interfata ce defineste starea de vizualizare a interfetei grafice
 * @author silvia
 *
 */
public interface StateView {

	void setPanel(ConcreteUserView userView, MainWindow mainFrame);
	
}
