package types;

import javax.swing.JProgressBar;

/**
 * Clasa ce defineste un produs/serviciu
 */
public class Product {

	public String name; //nume produs/serviciu	
	public String status; //statusul produsului: Active/Inactive
	public Status statusLicitatie = Status.INACTIVE;
	String user = "";
	public int pret = 0;
	JProgressBar progressBar;//progresul transferului
	
	public Product(String name) {
		this.name = name;
		this.status = "Inactive";
		progressBar= new JProgressBar();
	}	
}