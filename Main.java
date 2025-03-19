import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int maxWeight;

         do {
            System.out.print("Enter the maximum weight capacity (8-15 kg): ");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter a number between 8 and 15.");
                scanner.next();
            }
            maxWeight = scanner.nextInt();
        } while (maxWeight < 8 || maxWeight > 15);

        scanner.close();

        // Pass maxWeight to KnapsackSolver
        KnapsackSolver knapsackSolver = new KnapsackSolver(maxWeight);
        List<List<Product>> allBatches = knapsackSolver.knapsackBatchSelection();

        int batchNumber = 1;
        for (List<Product> batch : allBatches) {
            System.out.println("\n------------------------------------------");
            System.out.println("        Processing Batch " + batchNumber);
            System.out.println("------------------------------------------");

            if (batch.isEmpty()) {
                System.out.println("No valid batch found. Stopping deliveries.");
                break;
            }

            // Gather batch details
            int totalWeight = 0, totalValue = 0, totalQuantity = 0;
            Set<String> batchLocations = new HashSet<>();
            List<String> productNames = new ArrayList<>();

            for (Product p : batch) {
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;
                batchLocations.add(p.location);
                productNames.add(p.product_Name);
            }

            // Print batch summary
            System.out.println("Best Combination Batch " + batchNumber + ":");
            System.out.println("Products: " + String.join(", ", productNames));
            System.out.println("Total Quantity: " + totalQuantity);
            System.out.println("Total Weight: " + totalWeight + " kg");
            System.out.println("Total Value: " + totalValue);
            System.out.println("Locations for Delivery: " + String.join(", ", batchLocations) + "\n");

            // Run TSP for batch locations
            TSPSolver.runTSP(new ArrayList<>(batchLocations));

            // Indicate batch completion
            System.out.println("\nKiki is back in Koriko City and ready to reload for the next batch!\n");
            batchNumber++;
        }

        System.out.println("All deliveries completed!");
    }
}
