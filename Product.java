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
        new Product("Books", 4, 469),
        new Product("Magic Amulet", 2, 450),
        new Product("Tea Pots", 9, 393),
        new Product("Music Box", 7, 568)
    };

    @Override
    public String toString() {
        return product_Name + " - " + product_Weight + "kg - " + product_Value;
    }

    public static void printProductList() {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s\n", "No.", "Product Name", "Weight (kg)", "Value");
        System.out.println("---------------------------------------------------------------------------------");

        int index = 1;
        for (Product p : product) {
            System.out.printf("%-5d %-25s %-15d %-15d\n", index++, p.product_Name, p.product_Weight, p.product_Value);
        }

        System.out.println("---------------------------------------------------------------------------------");
    }
}
