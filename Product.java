
public class Product {
	String product_Name;
	int product_Weight;
	int product_Value;
	String customer_name;
	String location;
	int quantity;

	public Product(String customer_name,  String location, int quantity,String product_Name, int product_Weight, int product_Value) {
		this.customer_name = customer_name;
		this.location = location;
		this.quantity = quantity;
		this.product_Name = product_Name;
		this.product_Weight = product_Weight;
		this.product_Value = product_Value;
	}
// customer_name, location, quantity, product, weight, value
	static Product[] product = {
			new Product("Madam Sulliman", "Kingsbury", 2, "Apple Pie", 3, 180),
			new Product("Madam Gina", "Hotel Adriano", 1, "Flower Vase", 2, 150),
			new Product("King", "Kingsbury", 1, "Music Box", 3, 300),
			new Product("Porco Rosso", "Hotel Adriano", 1, "Big Stuff Toy", 7, 700),
			new Product("Ashitaka", "Emishi Village", 15, "Arrows", 8, 1500),
			new Product("Howl", "Howl's Castle", 3, "Books", 5, 225),
			new Product("San", "Emishi Village", 5, "Magic Amulet", 4, 2500),
			new Product("Kacho", "Kingsbury", 6, "Tea Pot", 9, 3445)
};

	@Override
	public String toString() {
		return customer_name + " - " + location + " - " + quantity + " - " + product_Name + " - " + product_Weight + "kg - " + product_Value;
	}

	public static void printProductList() {
		System.out.println("-------------------------------------------------------------------------------------");
		System.out.printf("%-5s %-25s %-15s %-15s\n", "No.", "Product Name", "Weight (kg)", "Value");
		System.out.println("-------------------------------------------------------------------------------------");

		int index = 1;
		for (Product p : product) {
			System.out.printf("%-5d %-25s %-15d %-15d\n", index++, p.product_Name, p.product_Weight, p.product_Value);
		}

		System.out.println("-------------------------------------------------------------------------------------");
	}
}
