package hexpress_algorithm;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ProductSorter {

    // ✅ Main method to prompt user and sort/display combinations
    public static void askUserAndSortCombinations(List<Object[]> combinations) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // ✅ User menu
            System.out.println("\n\nHow would you like to sort the combinations?");
            System.out.println("1 - Sort by Product Names (Alphabetical Order)");
            System.out.println("2 - Sort by Total Weight (Ascending)");
            System.out.println("3 - Sort by Total Value (Ascending)");
            System.out.println("4 - Exit\n");
            
            System.out.print("Enter your choice (1, 2, 3, or 4): ");

            int choice = scanner.nextInt();

            // ✅ Perform sorting based on choice
            switch (choice) {
                case 1:
                    System.out.println("\nSorted Combinations by Product Names (Alphabetical Order):");
                    sortByProductNames(combinations);
                    displayCombinations(combinations);
                    break;
                case 2:
                    System.out.println("\nSorted Combinations by Total Weight (Ascending):");
                    sortByTotalWeight(combinations);
                    displayCombinations(combinations);
                    break;
                case 3:
                    System.out.println("\nSorted Combinations by Total Value (Ascending):");
                    sortByTotalValue(combinations);
                    displayCombinations(combinations);
                    break;
                case 4:
                    exit = true; // Set exit to true to break the loop
                    exitClass exitObj = new exitClass(); // Create an instance of exitClass
                    exitObj.printExit(); // Call the printExit method
                    exitObj.printCongratulations(); // Call the printCongratulations method
                    break; // Exit the switch
                default:
                    System.out.println("\nInvalid choice. Please select 1 (Names), 2 (Weight), 3 (Value), or 4 (Exit).");
                    break; // Continue to the next iteration of the loop
            }
        }

        scanner.close(); // Close the scanner when done
    }

    // ✅ Sort combinations by Product Names (Alphabetically)
    private static void sortByProductNames(List<Object[]> combinations) {
        Collections.sort(combinations, new Comparator<Object[]>() {
        	
            @Override
            public int compare(Object[] combo1, Object[] combo2) {
                
            @SuppressWarnings("unchecked")
				List<Product> list = (List<Product>) combo1[0];
				List<Product> products1 = list;
				
            @SuppressWarnings("unchecked")
				List<Product> products2 = (List<Product>) combo2[0];

                // Sort the product lists alphabetically
                Collections.sort(products1, Comparator.comparing(p -> p.product_Name));
                Collections.sort(products2, Comparator.comparing(p -> p.product_Name));

                // Compare the first product names after sorting
                String name1 = products1.isEmpty() ? "" : products1.get(0).product_Name;
                String name2 = products2.isEmpty() ? "" : products2.get(0).product_Name;

                return name1.compareTo(name2);
            }
        });
    }

    // ✅ Sort combinations by Total Weight (Ascending)
    private static void sortByTotalWeight(List<Object[]> combinations) {
        Collections.sort(combinations, Comparator.comparingInt(combo -> (Integer) combo[1]));
    }

    // ✅ Sort combinations by Total Value (Ascending)
    private static void sortByTotalValue(List<Object[]> combinations) {
        Collections.sort(combinations, Comparator.comparingInt(combo -> (Integer) combo[2]));
    }

    // ✅ Display all combinations in table format
    private static void displayCombinations(List<Object[]> combinations) {
        System.out.printf("\n%-40s %-15s %-15s\n", "Products", "Total Weight", "Total Value");
        System.out.println("----------------------------------------------------------------------------------------");

        for (Object[] combo : combinations) {
            @SuppressWarnings("unchecked")
			List<Product> list = (List<Product>) combo[0];
			List<Product> products = list;
            int totalWeight = (Integer) combo[1];
            int totalValue = (Integer) combo[2];

            String productNames = formatProductNames(products);
            System.out.printf("%-40s %-15s %-15s\n", productNames, totalWeight + "kg", totalValue);
        }
    }

    // ✅ Helper to format product names into a readable string
    private static String formatProductNames(List<Product> products) {
        if (products.isEmpty()) return "None";

        StringBuilder names = new StringBuilder();
        for (Product p : products) {
            names.append(p.product_Name).append(", ");
        }
        names.setLength(names.length() - 2); // Remove last comma and space
        return names.toString();
    }
}