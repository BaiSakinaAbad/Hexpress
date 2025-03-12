import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TSPSolver {
    private static final int[][] distanceMatrix = {
            {0, 15, 14, 9, 10}, // Koriko City
            {15, 0, 4, 4, 20},  // Emishi Village
            {14, 4, 0, 5, 17},  // Kingsbury
            {9, 4, 5, 0, 15},   // Howl's Castle
            {10, 20, 17, 15, 0} // Hotel Adriano
    };

    private static final String[] cities = {"Koriko City", "Emishi Village", "Kingsbury", "Howl's Castle", "Hotel Adriano"};
    private ArrayList<ArrayList<Integer>> shortestPaths = new ArrayList<>();
    private int minDistance = Integer.MAX_VALUE;

    public void findShortestRoute() {
        ArrayList<Integer> cityIndexes = new ArrayList<>();
        for (int i = 1; i < cities.length; i++) {
            cityIndexes.add(i); // Exclude Koriko City (starting point)
        }

        ArrayList<ArrayList<Integer>> allRoutes = new ArrayList<>();
        permute(cityIndexes, 0, allRoutes);

        // Display all routes

        System.out.println("\n\n"+ Arrays.toString(cities) + "\n\nAll Possible Routes:");
        for (ArrayList<Integer> route : allRoutes) {
            int totalDistance = calculateTotalDistance(route);
            System.out.print("Koriko City -> ");
            for (int city : route) {
                System.out.print(cities[city] + " -> ");
            }
            System.out.print("Koriko City | Distance: " + totalDistance + " km");

            if (totalDistance == minDistance) {
                System.out.println("  <-- Shortest Route ✅");
            } else {
                System.out.println();
            }
        }

        // Display all shortest paths
        System.out.println("\nWow! There are " + shortestPaths.size() + " possible shortest routes. These are:");
        for (ArrayList<Integer> shortestPath : shortestPaths) {
            System.out.print("Koriko City -> ");
            for (int city : shortestPath) {
                System.out.print(cities[city] + " -> ");
            }
            System.out.println("Koriko City | Distance: " + minDistance + " km");
        }
    }

    private void permute(ArrayList<Integer> arr, int l, ArrayList<ArrayList<Integer>> allRoutes) {
        if (l == arr.size()) {
            ArrayList<Integer> newRoute = new ArrayList<>(arr);
            allRoutes.add(newRoute);
            int currentDistance = calculateTotalDistance(newRoute);

            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                shortestPaths.clear();
                shortestPaths.add(new ArrayList<>(newRoute));
            } else if (currentDistance == minDistance) {
                shortestPaths.add(new ArrayList<>(newRoute));
            }
        } else {
            for (int i = l; i < arr.size(); i++) {
                Collections.swap(arr, i, l);
                permute(arr, l + 1, allRoutes);
                Collections.swap(arr, i, l); // Backtrack
            }
        }
    }

    private int calculateTotalDistance(ArrayList<Integer> route) {
        int total = 0;
        int prevCity = 0; // Starting from Koriko City
        for (int city : route) {
            total += distanceMatrix[prevCity][city];
            prevCity = city;
        }
        total += distanceMatrix[prevCity][0]; // Return to Koriko City
        return total;
    }
}
