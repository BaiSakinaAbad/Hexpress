package hexpress_algorithm;

public class Product {
	String product_Name;
	int product_Weight;
	int product_Value;
	
	public Product(String product_Name, int product_Weight, int product_Value) {
		this.product_Name = product_Name;
		this.product_Weight = product_Weight;
		this.product_Value = product_Value;
	}
	
	Product[] product = {
			new Product("Apple", 3, 350),
			new Product("flower vase", 5, 555),
	        new Product("big stuff toy", 7, 711),
	        new Product("cat food", 6, 600),
	        new Product("books", 4, 469)
	};
	
	//for printing weight with kg
	//just add "kg" using loop
	//or recursion if possible
	
}

