import java.util.ArrayList;


public class User {

	String username;
	String password;
	UserType uType;
	ArrayList<Product> products;
	
	User() {
		products = new ArrayList<Product>();
	}
	User(String username, String passwd, UserType uType) {
		this.username = username;
		this.password = passwd;
		this.uType = uType;
		products = new ArrayList<Product>();
	}
}
