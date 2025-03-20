package hexpress_algorithm;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.function.Consumer;

public class Main_ {

    public static void main(String[] args) {
    	    SwingUtilities.invokeLater(() -> {
    	        new Product_UI(); // Start with Product_UI only!
    	    });
    	}

    // ✅ Process knapsack operations after getting weight from Bag_UI
    public static void proceedWithKnapsack(int maxWeight) {
        System.out.println("\nWeight received from Bag_UI: " + maxWeight + " kg");

        // ✅ Get all possible outcomes (combinations), print or display
        Knapsack_Algorithm.KnapsackResult outcomes = Knapsack_Algorithm.getAllPossibleOutcomes(maxWeight);

        SwingUtilities.invokeLater(() -> new Knapsack_UI(maxWeight));

        // ✅ Proceed with deliveries using best batches if needed
        System.out.println("\n\n========= DELIVERING BATCHES =========");
        Knapsack_Algorithm.findAndDeliverBatches(maxWeight);

        System.out.println("\nHexpress Delivery Service has finished all operations. Goodbye!");
    }
}
