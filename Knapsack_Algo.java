package hexpress_algorithm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Knapsack_Algorithm {

    public static int capacity = 15;

    // Holds the result of the knapsack 
    public static class KnapsackResult {
        public List<Product> bestCombination;
        public int bestWeight;
        public int maxProfit;
        public List<String> combinationsTable;

        public KnapsackResult(List<Product> bestCombination, int bestWeight, int maxProfit, List<String> combinationsTable) {
            this.bestCombination = bestCombination;
            this.bestWeight = bestWeight;
            this.maxProfit = maxProfit;
            this.combinationsTable = combinationsTable;
        }
    }

    // Combination delivery queue instance (clean separation!)
    public static DeliveryCombination deliveryQueue = new DeliveryCombination();

    // Track delivered products
    public static Set<Product> deliveredProducts = new HashSet<>();

    // Runs the solver and prints outputs
    public static void runKnapsackSolver(int capacity) {

        System.out.println("\nBasket capacity: " + capacity + "kg:\n");

        int deliveries = 3; // Number of delivery batches

        for (int i = 0; i < deliveries; i++) {

            // FIND BEST COMBO FOR REMAINING PRODUCTS
            KnapsackResult result = findCombinations(capacity);

            if (result.bestCombination.isEmpty()) {
                System.out.println("\nNo more valid combinations left to deliver.");
                break;
            }

            System.out.println("\nDelivery #" + (i + 1));
            System.out.println("------------------------");
            System.out.println("\nProduct Lists Combination \n");

            for (String row : result.combinationsTable) {
                System.out.println(row);
            }

            // 👉 Simulate or input delivery location per batch
            String deliveryLocation = "Location-" + (i + 1); // Replace with user input if needed
            System.out.println("Assigning delivery location: " + deliveryLocation);

            // Add the batch with its location to the delivery queue
            deliveryQueue.addCombination(result.bestCombination, result.bestWeight, result.maxProfit, deliveryLocation);

            // Deliver highest profit combination
            deliveryQueue.deliverCombination();

            // Mark delivered products so they don't appear again
            deliveredProducts.addAll(result.bestCombination);
        }

        System.out.println("\nAll deliveries completed.");
    }

    // Finds all valid combinations, returns best result
    public static KnapsackResult findCombinations(int capacity) {

        Product[] allProducts = Product.product; // Assuming Product.product[] exists elsewhere

        // Filter out already delivered products
        List<Product> availableProducts = new ArrayList<>();
        for (Product p : allProducts) {
            if (!deliveredProducts.contains(p)) {
                availableProducts.add(p);
            }
        }

        int n = availableProducts.size();

        if (n == 0) {
            return new KnapsackResult(new ArrayList<>(), 0, 0, List.of("No products available for delivery."));
        }

        int maxProfit = 0;
        int bestWeight = 0;
        List<Product> bestCombination = new ArrayList<>();
        List<String> combinationsTable = new ArrayList<>();

        int totalCombinations = 1 << n;

        combinationsTable.add(String.format("%-50s %-15s %-15s", "Products", "Total Weight", "Total Value"));
        combinationsTable.add("----------------------------------------------------------------------------------------");

        for (int i = 0; i < totalCombinations; i++) {

            List<Product> currentCombination = new ArrayList<>();
            int totalWeight = 0;
            int totalValue = 0;

            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    Product p = availableProducts.get(j);
                    totalWeight += p.product_Weight;
                    totalValue += p.product_Value;
                    currentCombination.add(p);
                }
            }

            if (totalWeight <= capacity) {

                StringBuilder productNames = new StringBuilder();
                for (Product p : currentCombination) {
                    productNames.append(p.product_Name).append(", ");
                }

                if (productNames.length() > 0) {
                    productNames.setLength(productNames.length() - 2);
                } else {
                    productNames.append("None");
                }

                combinationsTable.add(String.format("%-50s %-15s %-15s",
                        productNames, totalWeight + "kg", totalValue));

                // Update best combo found
                if (totalValue > maxProfit) {
                    maxProfit = totalValue;
                    bestWeight = totalWeight;
                    bestCombination = new ArrayList<>(currentCombination);
                }
            }
        }

        return new KnapsackResult(bestCombination, bestWeight, maxProfit, combinationsTable);
    }
}
