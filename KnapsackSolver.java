
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KnapsackSolver {

    private int maxWeight;
    public List<Product> remainingProducts;

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
            if (!bestBatch.isEmpty()) {
                allBatches.add(bestBatch);
                printBatchDetails(bestBatch, allBatches.size());

                remainingProducts.removeAll(bestBatch);
            } else {
                System.out.println("\nNo more valid batches can be formed.");
                break;
            }
        }
        return allBatches;
    }

    public List<List<Product>> generateCombinations(List<Product> products) {
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
                System.out.printf("%-50s %-15d %-15d %-15d\n",
                        combinationKey, totalQuantity, totalWeight, totalValue);
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

        int totalWeight = 0, totalValue = 0, totalQuantity = 0;
        List<String> productNames = new ArrayList<>();

        for (Product p : batch) {
            totalWeight += p.product_Weight;
            totalValue += p.product_Value;
            totalQuantity += p.quantity;
            productNames.add(p.product_Name);
        }

        System.out.println("\nBest Possible Combination for Batch " + batchNumber);
        System.out.printf("%-40s %-14s %-14s %-10s\n",
                "Product Names", "Total Quantity", "Total Weight", "Total Value");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-40s %-14d %-14d %-10d\n",
                String.join(", ", productNames), totalQuantity, totalWeight, totalValue);
    }

    // ✅ STATIC WRAPPER METHOD (You can call this from Main/UI)
    public static void findAndDeliverBatches(int maxWeight) {
        KnapsackSolver knapsack = new KnapsackSolver(maxWeight);
        List<List<Product>> allBatches = knapsack.knapsackBatchSelection();

        int batchNumber = 1;

        for (List<Product> batch : allBatches) {
            System.out.println("\n------------------------------------------");
            System.out.println("        Processing Batch " + batchNumber);
            System.out.println("------------------------------------------");

            if (batch.isEmpty()) {
                System.out.println("No valid batch found. Stopping deliveries.");
                break;
            }

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

            System.out.println("Best Combination Batch " + batchNumber + ":");
            System.out.println("Products: " + String.join(", ", productNames));
            System.out.println("Total Quantity: " + totalQuantity);
            System.out.println("Total Weight: " + totalWeight + " kg");
            System.out.println("Total Value: " + totalValue);
            System.out.println("Locations for Delivery: " + String.join(", ", batchLocations) + "\n");

            // Example: call TSP_Solver to optimize delivery routes
            TSPSolver.runTSP(new ArrayList<>(batchLocations));

            System.out.println("\nKiki is back in Koriko City and ready to reload for the next batch!\n");

            batchNumber++;
        }

        System.out.println("All deliveries completed!");
    }

    // ✅ STATIC CLASS TO RETURN RESULTS TO THE UI
    public static class KnapsackResult {
        public List<String> combinationsTable;

        public KnapsackResult(List<String> combinationsTable) {
            this.combinationsTable = combinationsTable;
        }
    }

    // ✅ STATIC METHOD FOR UI TO GET BATCH COMBINATIONS (NO CONSOLE OUTPUT)
    public static KnapsackResult findCombinations(int maxWeight) {
        KnapsackSolver knapsack = new KnapsackSolver(maxWeight);
        List<List<Product>> allBatches = knapsack.knapsackBatchSelection();

        List<String> table = new ArrayList<>();
        int batchNumber = 1;

        table.add(String.format("%-10s %-40s %-15s %-15s %-15s",
                "Batch", "Product Names", "Total Quantity", "Total Weight", "Total Value"));
        table.add("--------------------------------------------------------------------------------------------------------");

        for (List<Product> batch : allBatches) {
            if (batch.isEmpty()) continue;

            List<String> productNames = new ArrayList<>();
            int totalWeight = 0, totalValue = 0, totalQuantity = 0;

            for (Product p : batch) {
                productNames.add(p.product_Name);
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;
            }

            String row = String.format("%-10d %-40s %-15d %-15d %-15d",
                    batchNumber, String.join(", ", productNames), totalQuantity, totalWeight, totalValue);

            table.add(row);
            batchNumber++;
        }

        return new KnapsackResult(table);
    }

    // ✅ STATIC METHOD TO GET ALL POSSIBLE OUTCOMES (COMBINATIONS)
    public static KnapsackResult getAllPossibleOutcomes(int maxWeight) {
        KnapsackSolver knapsack = new KnapsackSolver(maxWeight);
        List<List<Product>> combinations = knapsack.generateCombinations(knapsack.remainingProducts);

        List<String> table = new ArrayList<>();
        int combinationNumber = 1;

        table.add(String.format("%-15s %-50s %-15s %-15s %-15s",
                "Combination", "Product Names", "Total Quantity", "Total Weight", "Total Value"));
        table.add("---------------------------------------------------------------------------------------------------------------");

        for (List<Product> combo : combinations) {
            if (combo.isEmpty()) continue;

            List<String> productNames = new ArrayList<>();
            int totalWeight = 0, totalValue = 0, totalQuantity = 0;

            for (Product p : combo) {
                productNames.add(p.product_Name);
                totalWeight += p.product_Weight;
                totalValue += p.product_Value;
                totalQuantity += p.quantity;
            }

            String row = String.format("%-15d %-50s %-15d %-15d %-15d",
                    combinationNumber, String.join(", ", productNames), totalQuantity, totalWeight, totalValue);

            table.add(row);
            combinationNumber++;
        }

        return new KnapsackResult(table);
    }

}
