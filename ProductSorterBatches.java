import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProductSorterBatches {

    public static void sortAndSearchMenu(List<List<Product>> batches, Scanner scanner, StringMatching stringMatching) {
        boolean running = true;

        while (running) {
            System.out.println("\nChoose an Option:");
            System.out.println("1. Sort by Product Name (Alphabetically)");
            System.out.println("2. Sort by Total Quantity");
            System.out.println("3. Sort by Total Weight");
            System.out.println("4. Sort by Total Value");
            System.out.println("5. Search for Customer Invoice");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    sortByProductName(batches);
                    displayBatches(batches);
                    break;
                case 2:
                    sortByTotalQuantity(batches);
                    displayBatches(batches);
                    break;
                case 3:
                    sortByTotalWeight(batches);
                    displayBatches(batches);
                    break;
                case 4:
                    sortByTotalValue(batches);
                    displayBatches(batches);
                    break;
                case 5:
                    System.out.print("\nEnter Customer Name: ");
                    String customerName = scanner.nextLine();
                    stringMatching.findCustomer(customerName); // Call existing StringMatching
                    break;
                case 6:
                    System.out.println("\nExiting program.");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please try again.");
            }
        }
    }

    public static void sortByProductName(List<List<Product>> batches) {
        Collections.sort(batches, (batch1, batch2) -> {
            String name1 = batch1.get(0).product_Name;
            String name2 = batch2.get(0).product_Name;
            return name1.compareToIgnoreCase(name2);
        });
    }

    public static void sortByTotalQuantity(List<List<Product>> batches) {
        Collections.sort(batches, Comparator.comparingInt(batch ->
                batch.stream().mapToInt(p -> p.quantity).sum()));
    }

    public static void sortByTotalWeight(List<List<Product>> batches) {
        Collections.sort(batches, Comparator.comparingInt(batch ->
                batch.stream().mapToInt(p -> p.product_Weight).sum()));
    }

    public static void sortByTotalValue(List<List<Product>> batches) {
        batches.sort((batch1, batch2) -> {
            int value1 = batch1.stream().mapToInt(p -> p.product_Value).sum();
            int value2 = batch2.stream().mapToInt(p -> p.product_Value).sum();
            return Integer.compare(value2, value1); // Descending order
        });
    }

    public static void displayBatches(List<List<Product>> batches) {
        System.out.println("\n--------------------------------------");
        System.out.println("           Sorted Batches");
        System.out.println("--------------------------------------");

        int batchNumber = 1;

        for (List<Product> batch : batches) {
            System.out.println("\nBatch " + batchNumber);
            int totalWeight = 0, totalValue = 0, totalQuantity = 0;

            for (Product p : batch) {
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;

                System.out.printf(" - %-20s | Qty: %-4d | Weight: %-4d kg | Value: %-4d\n",
                        p.product_Name, p.quantity, p.product_Weight, p.product_Value);
            }

            System.out.println("------------------------------------------------");
            System.out.printf("Total Quantity: %-5d | Total Weight: %-5d kg | Total Value: %-5d\n",
                    totalQuantity, totalWeight, totalValue);

            batchNumber++;
        }
    }
}
