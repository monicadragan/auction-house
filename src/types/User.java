package types;
import java.util.ArrayList;



public class User {

	public String username;
	String password;
	public UserType uType;
	public ArrayList<Product> products;
	
	User() {
		products = new ArrayList<Product>();
	}
	public User(String username, String passwd, UserType uType) {
		this.username = username;
		this.password = passwd;
		this.uType = uType;
		products = new ArrayList<Product>();
	}
}
