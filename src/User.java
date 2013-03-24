import java.util.ArrayList;


public class User {
	ArrayList<Product> products;
	String username;
	String password;
	UserType uType;
	
	User(){
		products = new ArrayList<Product>();
	}

}
