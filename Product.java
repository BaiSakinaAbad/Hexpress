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

	static Product[] product = {
			new Product("Apple", 3, 350),
			new Product("Flower Vase", 5, 555),
			new Product("Big Stuff Toy", 7, 711),
			new Product("Arrows", 6, 600),
			new Product("Books", 4, 469)
	};

	@Override
	public String toString() {
		return product_Name + " - " + product_Weight + "kg - " + product_Value;
	}

	public static void printProductList() {
		System.out.println("Product List:");
		for (Product p : product) {
			System.out.println(p);
		}
	}
}
