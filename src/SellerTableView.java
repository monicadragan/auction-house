
public class SellerTableView extends TableView{

	static Object[][] body = {{ "Aparat Foto", "Inactiv", ""/*Ionescu*/, "",  "in progress..." },
				        { "Camera Foto", "Inactiv", ""/*Georgescu*/, "", "in progress..." },
				        { "Mouse", "Inactiv","", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." }};
		
	public SellerTableView(String username, UserType type) {
		super(username, type, "Cumparatori", body);
	}

}
