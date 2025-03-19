
import java.util.*;

public class DeliveryCombination {

    // Priority queue to store combinations (max-heap by totalValue)
    private PriorityQueue<Combination> deliveryQueue;

    // Constructor
    public DeliveryCombination() {
        deliveryQueue = new PriorityQueue<>((a, b) -> Integer.compare(b.totalValue, a.totalValue));
    }

    // Add a combination to the queue with a location
    public void addCombination(List<Product> products, int totalWeight, int totalValue, String location) {
        Combination combo = new Combination(products, totalWeight, totalValue, location);
        deliveryQueue.offer(combo);
    }

    // Deliver the next best combination
    public void deliverCombination() {
        if (deliveryQueue.isEmpty()) {
            System.out.println("No more combinations to deliver.\n");
            return;
        }

        Combination bestCombo = deliveryQueue.poll();

        System.out.println("\nDelivered Combination to: " + bestCombo.location);
        System.out.println("------------------------------------------------------");

        StringBuilder productNames = new StringBuilder();
        for (Product p : bestCombo.products) {
            productNames.append(p.product_Name).append(", ");
        }

        if (productNames.length() > 0) {
            productNames.setLength(productNames.length() - 2);
        } else {
            productNames.append("None");
        }

        System.out.println("Products: " + productNames);
        System.out.println("Total Weight: " + bestCombo.totalWeight + "kg");
        System.out.println("Total Value: " + bestCombo.totalValue);
        System.out.println("------------------------------------------------------");
    }

    // Peek the next combination without removing it
    public Combination peekCombination() {
        return deliveryQueue.peek();
    }

    // Inner class for Combination
    public static class Combination {
        public List<Product> products;
        public int totalWeight;
        public int totalValue;
        public String location;

        public Combination(List<Product> products, int totalWeight, int totalValue, String location) {
            this.products = products;
            this.totalWeight = totalWeight;
            this.totalValue = totalValue;
            this.location = location;
        }
    }

    // Check if the queue is empty
    public boolean isEmpty() {
        return deliveryQueue.isEmpty();
    }
}
