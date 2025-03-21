
import java.util.HashMap;
import java.util.LinkedList;

public class StringMatching {
    private HashMap<String, LinkedList<Product>> customerProducts;

    public StringMatching() {
        customerProducts = new HashMap<>();
        initializeData();
    }

    public LinkedList<String> getAllCustomerNames() {
        return new LinkedList<>(customerProducts.keySet());
    }

    private void initializeData() {
        for (Product p : Product.product) {
            customerProducts.computeIfAbsent(p.customer_name.toLowerCase(), k -> new LinkedList<>()).add(p);
        }
    }

    public LinkedList<Product> getCustomerProducts(String customerName) {
        return customerProducts.getOrDefault(customerName.toLowerCase(), new LinkedList<>());
    }

    public void displayCustomers() {
        System.out.println("\nAvailable Customers:");
        customerProducts.keySet().stream()
                .sorted()
                .map(StringMatching::capitalize)
                .forEach(name -> System.out.println(" - " + name));
    }

    public void findCustomer(String customerName) {
        customerName = customerName.trim().toLowerCase();
        if (customerProducts.containsKey(customerName)) {
            LinkedList<Product> products = customerProducts.get(customerName);
            System.out.println("\nCustomer Details:");
            for (Product product : products) {
                System.out.println("Customer Name: " + product.customer_name);
                System.out.println("Location: " + product.location);
                System.out.println("Quantity: " + product.quantity);
                System.out.println("Product: " + product.product_Name);
                System.out.println("Weight: " + product.product_Weight + "kg");
                System.out.println("Value: " + product.product_Value);
                System.out.println();
            }
        } else {
            System.out.println("\nCustomer not found. Please ensure the name is spelled correctly or try using the full name.\n" + "Please Try Again.");
        }
    }

    private static String capitalize(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            capitalized.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }
        return capitalized.toString().trim();
    }
}
