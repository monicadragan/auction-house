import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


public class InputParser {

	public static User readInfoAboutUser(String username, String passwd, UserType uType)
	{
		String filename = username + ".config";
		User user = new User(username, passwd, uType);
		ArrayList<Product> products = new ArrayList<Product>();
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = "";
			while((line = br.readLine()) != null)
			{
				Product p;
				StringTokenizer st = new StringTokenizer(line, ",");
				p = new Product(st.nextToken());
				if(uType.equals(UserType.SELLER))
					p.pret = Integer.parseInt(st.nextToken());
				products.add(p);
			}
			br.close();
		} catch (IOException e) {
			System.err.println("This user doesn't exist!");
			return null;
		}

		user.products = products;
		return user;
	}
	
}
