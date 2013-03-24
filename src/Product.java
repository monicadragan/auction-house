import java.util.ArrayList;

import javax.swing.JProgressBar;

public class Product {

	String name; //nume produs/serviciu	
	String status; //statusul produsului: Active/Inactive
	Status statusLicitatie = Status.INACTIVE;
	int pret = 0;
	JProgressBar progressBar;//progresul transferului
	
	public Product(String name) {
		this.name = name;
		this.status = "Inactive";
		progressBar= new JProgressBar();
	}
	
}