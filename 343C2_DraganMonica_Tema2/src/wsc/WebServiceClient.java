package wsc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import types.Product;
import types.User;
import types.UserType;

/**
 * Clasa ce simuleaza WebServiceClient-ul citind din fisiere 
 * de configurare informatiile necesare la logarea unui utilizator
 */
public class WebServiceClient implements IWebServiceClient{

	public static final String credentialsConfigFile = "credentials.config";
	
	/**
	 * Metoda folosita pentru a obtine lista de produse a unui utilizator
	 */
	public User readInfoAboutUser(String username, String passwd, UserType uType)
	{
		if(!checkUserInfo(username, passwd, uType))
			return null;
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
	
	/**
	 * Metoda ce verifica corectitudinea credentialelor
	 */
	public boolean checkUserInfo(String username, String passwd, UserType uType)
	{
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(credentialsConfigFile));
			String line = "";
			while((line = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(line, " ");
				String user = st.nextToken();
				String pass = st.nextToken();
				if(user.equals(username) && pass.equals(passwd) ||
						(user.equals(username)&&passwd.equals("")))
					return true;
			}
			br.close();
		} catch (IOException e) {
			System.err.println("This user doesn't exist!");
			return false;
		}

		return false;
	}
	
}
