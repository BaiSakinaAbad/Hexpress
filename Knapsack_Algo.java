package hexpress_algorithm;

import java.util.ArrayList;
import java.util.List;

public class Knapsack_Algo {

    public static void findCombinations(int capacity) {
        Product[] products = Product.product;
        int n = products.length;

        int maxProfit = 0;
        List<Product> bestCombination = new ArrayList<>();

        int totalCombinations = 1 << n; // 2^n possible combinations

        System.out.println("\nAll possible combinations within capacity " + capacity + "kg:\n");

        // Print table headers
        System.out.printf("%-40s %-15s %-15s\n", "Products", "Total Weight", "Total Value");
        System.out.println("----------------------------------------------------------------------------------------");

        for (int i = 0; i < totalCombinations; i++) {
            List<Product> currentCombination = new ArrayList<>();
            int totalWeight = 0;
            int totalValue = 0;

            // Check each bit of i to decide whether to include product[j]
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    Product p = products[j];
                    totalWeight += p.product_Weight;
                    totalValue += p.product_Value;
                    currentCombination.add(p);
                }
            }

            // Only consider combinations within the capacity
            if (totalWeight <= capacity) {
                // Build product names into a single string
                StringBuilder productNames = new StringBuilder();
                for (Product p : currentCombination) {
                    productNames.append(p.product_Name).append(", ");
                }

                // Remove trailing comma and space if not empty
                if (productNames.length() > 0) {
                    productNames.setLength(productNames.length() - 2);
                } else {
                    productNames.append("None");
                }

                // Print the row in the table
                System.out.printf("%-40s %-15s %-15s\n", productNames, totalWeight + "kg", totalValue);

                // Update best combination if current has higher value
                if (totalValue > maxProfit) {
                    maxProfit = totalValue;
                    bestCombination = new ArrayList<>(currentCombination);
                }
            }
        }

        // Print the best combination
        System.out.println("\nBest combination based on highest profit:");
        System.out.println("------------------------------------------------------");
        StringBuilder bestProducts = new StringBuilder();
        int bestWeight = 0;
        for (Product p : bestCombination) {
            bestProducts.append(p.product_Name).append(", ");
            bestWeight += p.product_Weight;
        }
        if (bestProducts.length() > 0) {
            bestProducts.setLength(bestProducts.length() - 2);
        } else {
            bestProducts.append("None");
        }

        System.out.printf("Products: %s\n", bestProducts);
        System.out.printf("Total Weight: %dkg\n", bestWeight);
        System.out.printf("Total Value: %d\n", maxProfit);
    }

    public static void main(String[] args) {
        int capacity = 15; // Set the max capacity of the knapsack (in kg)

        Product.printProductList(); // Show the products first

        findCombinations(capacity); // Find and print all combinations
    }
}
