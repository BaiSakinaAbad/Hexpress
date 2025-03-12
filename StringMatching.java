
import java.util.HashMap;
import java.util.Scanner;

public class StringMatching {
    private HashMap<String, String[]> customerData;

    public StringMatching() {
        customerData = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        customerData.put("tombo", new String[]{"Koriko City", "Big stuffed toy", "7kg", "711"});
        customerData.put("howl", new String[]{"Howl's Castle", "Books", "4kg", "469"});
        customerData.put("ashitaka", new String[]{"Emishi Village", "Arrows", "6kg", "600"});
        customerData.put("madame sulliman", new String[]{"Kingsbury", "Apple pie", "3kg", "350"});
        customerData.put("madame gina", new String[]{"Hotel Adriano", "Flower Vase", "5kg", "555"});
    }

    public void displayCustomers() {
        System.out.println("\nAvailable Customers:");
        customerData.keySet().stream()
                .sorted()
                .map(StringMatching::capitalize)
                .forEach(name -> System.out.println(" - " + name));
    }

    public void findCustomer(String customerName) {
        customerName = customerName.trim().toLowerCase();
        if (customerData.containsKey(customerName)) {
            String[] details = customerData.get(customerName);
            System.out.println("\nCustomer Details:");
            System.out.println("Customer: " + capitalize(customerName));
            System.out.println("Location: " + details[0]);
            System.out.println("Product: " + details[1]);
            System.out.println("Weight: " + details[2]);
            System.out.println("Value: " + details[3]);
        } else {
            System.out.println("\nCustomer doesn't exist. Please try again.\n");
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringMatching stringMatching = new StringMatching();

        stringMatching.displayCustomers();

        while (true) {
            System.out.print("\nEnter a customer name: ");
            String customerName = scanner.nextLine();
            stringMatching.findCustomer(customerName);
        }
    }
}
