import javax.swing.JPanel;


public class BuyerTableView extends TableView{
	static Object[][] body = {{ "Aparat Foto", "Inactiv", "", "",  "in progress..." },
		        { "Camera Foto", "Inactiv", "", "", "in progress..." },
		        { "Mouse", "Inactiv","", "","in progress..." },
		        { "Tastatura","Inactiv", "", "","in progress..." },
		        { "Tastatura","Inactiv", "", "","in progress..." }};
	
	public BuyerTableView(String username, UserType type) {
		super(username, type, "Furnizori", body);
	}
	

}
