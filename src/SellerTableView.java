
public class SellerTableView extends TableView{

	static Object[][] body = {{ "Aparat Foto", "Inactiv", ""/*Ionescu*/, "",  "in progress..." },
				        { "Camera Foto", "Inactiv", ""/*Georgescu*/, "", "in progress..." },
				        { "Mouse", "Inactiv","", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." }};
		
	public SellerTableView(User userInfo, MainWindow frame) {
		super(userInfo, "Cumparatori", frame);
	}

}
