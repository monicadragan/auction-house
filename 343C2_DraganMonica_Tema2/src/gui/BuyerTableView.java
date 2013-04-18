package gui;
import types.User;

/**
 * Clasa ce defineste tabela de produse a unui cumparator
 * cu denumirea coloanei user-ilor care ofera un produs modificata
 * ("Furnizori")
 * @author silvia
 *
 */
public class BuyerTableView extends TableView {
	
	public BuyerTableView(User userInfo, MainWindow frame) {
		super(userInfo, "Furnizori", frame);
	}

}
