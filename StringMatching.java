package DAA;
import java.util.HashMap;
import java.util.Scanner;

public class StringMatching {
    private static HashMap<String, String[]> customerData = new HashMap<>();

    public static void main(String[] args) {
        // Initialize customer data
        customerData.put("tombo", new String[]{"Koriko City", "Big stuffed toy", "7kg", "711"});
        customerData.put("howl", new String[]{"Howl's Castle", "Books", "4kg", "469"});
        customerData.put("ashitaka", new String[]{"Emishi Village", "Arrows", "6kg", "600"});
        customerData.put("madame sulliman", new String[]{"Kingsbury", "Apple pie", "3kg", "350"});
        customerData.put("madame gina", new String[]{"Hotel Adriano", "Flower Vase", "5kg", "555"});

        Scanner scanner = new Scanner(System.in);

        // Display available customer names ONCE
        System.out.println("\nAvailable Customers:");
        customerData.keySet().stream()
                .sorted()
                .map(StringMatching::capitalize)
                .forEach(name -> System.out.println(" - " + name));

        while (true) {
            // Ask user to enter a name
            System.out.print("\nEnter a customer name: ");
            String customerName = scanner.nextLine().trim().toLowerCase();

            // Check if customer exists
            if (customerData.containsKey(customerName)) {
                String[] details = customerData.get(customerName);
                System.out.println("\nCustomer Details:");
                System.out.println("Customer: " + capitalize(customerName));
                System.out.println("Location: " + details[0]);
                System.out.println("Product: " + details[1]);
                System.out.println("Weight: " + details[2]);
                System.out.println("Value: " + details[3]);
            } else {
                System.out.println("\nCustomer doesn't exist. Please try again.");
            }
        }
    }

    // Capitalizes first letter of each word
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
