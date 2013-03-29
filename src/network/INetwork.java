package network;

import gui.MainWindow;

/**
 * Interfata modulului Network
 */
public interface INetwork {

	public void transferFile(MainWindow source, MainWindow destination,
							int srcRow, int dstRow);

}
