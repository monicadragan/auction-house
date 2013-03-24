
public class SellerTableView extends TableView{

	static Object[][] body = {{ "Aparat Foto", "Inactiv", ""/*Ionescu*/, "",  "in progress..." },
				        { "Camera Foto", "Inactiv", ""/*Georgescu*/, "", "in progress..." },
				        { "Mouse", "Inactiv","", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." },
				        { "Tastatura","Inactiv", "", "","in progress..." }};
		
	public SellerTableView(String username, UserType utype, MainWindow frame) {
		User userInfo = new User();
		userInfo.username = username;
		userInfo.uType = utype;
		userInfo.password = "dkjugecf";
		super(userInfo, "Cumparatori", frame);
	}

}
