import java.util.*;

public class KnapsackSolver {
    private int maxWeight;
    private List<Product> remainingProducts;

    // Constructor to initialize max weight
    public KnapsackSolver(int maxWeight) {
        this.maxWeight = maxWeight;
        this.remainingProducts = new ArrayList<>(Arrays.asList(Product.product));
    }

    public List<List<Product>> knapsackBatchSelection() {
        List<List<Product>> allBatches = new ArrayList<>();

        while (!remainingProducts.isEmpty()) {
            List<List<Product>> combinations = generateCombinations(remainingProducts);
            printCombinations(combinations);

            List<Product> bestBatch = selectBestCombination(combinations);
            allBatches.add(bestBatch);
            printBatchDetails(bestBatch, allBatches.size());

            remainingProducts.removeAll(bestBatch);
        }
        return allBatches;
    }

    private List<List<Product>> generateCombinations(List<Product> products) {
        List<List<Product>> combinations = new ArrayList<>();
        int n = products.size();

        for (int i = 0; i < (1 << n); i++) {
            Set<Product> uniqueProducts = new HashSet<>();
            List<Product> subset = new ArrayList<>();
            int totalWeight = 0;

            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    Product p = products.get(j);
                    if (totalWeight + p.product_Weight > maxWeight) break;

                    // Avoid duplicate product selections
                    if (!uniqueProducts.contains(p)) {
                        subset.add(p);
                        uniqueProducts.add(p);
                        totalWeight += p.product_Weight;
                    }
                }
            }
            if (!subset.isEmpty()) {
                combinations.add(subset);
            }
        }
        return combinations;
    }

    private void printCombinations(List<List<Product>> combinations) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("\nPOSSIBLE COMBINATIONS\n");
        System.out.printf("%-50s %-15s %-15s %-15s\n", "Product Names", "Total Quantity", "Total Weight", "Total Value");
        System.out.println("-------------------------------------------------------------");

        Set<String> printedCombinations = new HashSet<>();

        for (List<Product> batch : combinations) {
            if (batch.isEmpty()) continue;

            int totalWeight = 0, totalValue = 0, totalQuantity = 0;
            List<String> productNames = new ArrayList<>();

            for (Product p : batch) {
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;
                productNames.add(p.product_Name);
            }

            String combinationKey = String.join(", ", productNames);
            if (!printedCombinations.contains(combinationKey)) {
                printedCombinations.add(combinationKey);
                System.out.printf("%-50s %-15d %-15d %-15d\n", combinationKey, totalQuantity, totalWeight, totalValue);
            }
        }
    }

    private List<Product> selectBestCombination(List<List<Product>> combinations) {
        List<Product> bestBatch = new ArrayList<>();
        int maxValue = 0;

        for (List<Product> batch : combinations) {
            int totalValue = batch.stream().mapToInt(p -> p.product_Value).sum();
            if (totalValue > maxValue) {
                maxValue = totalValue;
                bestBatch = batch;
            }
        }
        return bestBatch;
    }

    private void printBatchDetails(List<Product> batch, int batchNumber) {
        if (batch.isEmpty()) {
            System.out.println("\nNo valid batch found for Batch " + batchNumber);
            return;
        }

        // Gather batch summary details
        int totalWeight = 0, totalValue = 0, totalQuantity = 0;
        List<String> productNames = new ArrayList<>();

        for (Product p : batch) {
            totalWeight += p.product_Weight;
            totalValue += p.product_Value;
            totalQuantity += p.quantity;
            productNames.add(p.product_Name);
        }

        System.out.print("\nBest Possible Combinations");
        System.out.printf("%-40s %-14s %-14s %-10s\n", "\nProduct Names", "Total Quantity", "Total Weight", "Total Value");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-40s %-14d %-14d %-10d\n",
                String.join(", ", productNames), totalQuantity, totalWeight, totalValue);
    }
}
