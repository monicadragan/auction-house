package gui;
import types.User;

/**
 * Clasa ce defineste tabela de produse a unui furnizor
 * cu denumirea coloanei user-ilor care doresc un produs modificata
 * (aici: Cumparatori)
 * @author silvia
 *
 */
public class SellerTableView extends TableView{
		
	public SellerTableView(User userInfo, MainWindow frame) {
		super(userInfo, "Cumparatori", frame);
	}

}
