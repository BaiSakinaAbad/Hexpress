import java.util.*;

public class TSPSolver {
    private static final int[][] distanceMatrix = {
            {0, 15, 14, 9, 10}, // Koriko City
            {15, 0, 4, 4, 20},  // Emishi Village
            {14, 4, 0, 5, 17},  // Kingsbury
            {9, 4, 5, 0, 15},   // Howl's Castle
            {10, 20, 17, 15, 0} // Hotel Adriano
    };
    static final String[] cities = {"Koriko City", "Emishi Village", "Kingsbury", "Howl's Castle", "Hotel Adriano"};

    public static int runTSP(List<String> batchLocations) {
        List<Integer> locationIndices = new ArrayList<>();
        for (String loc : batchLocations) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].equals(loc)) {
                    locationIndices.add(i);
                }
            }
        }

        if (locationIndices.isEmpty()) {
            System.out.println("No valid locations for TSP.");
            return 0; // Return 0 if no valid locations
        }

        return solveTSP(locationIndices);
    }

    private static int solveTSP(List<Integer> locations) {
        int minDistance = Integer.MAX_VALUE;
        List<Integer> bestRoute = null;

        List<List<Integer>> allRoutes = new ArrayList<>();
        permute(locations, 0, allRoutes);

        for (List<Integer> route : allRoutes) {
            int distance = calculateDistance(route);
            if (distance < minDistance) {
                minDistance = distance;
                bestRoute = new ArrayList<>(route);
            }
        }

        if (bestRoute != null) {
            System.out.println("\nShortest Route: Koriko City -> " + bestRoute.stream().map(i -> cities[i]).toList() + " -> Koriko City");
            System.out.println("Distance: " + minDistance + " km\n");
            return minDistance; // Return the minimum distance
        }
        return 0; // Return 0 if no route is found
    }

    private static void permute(List<Integer> arr, int len, List<List<Integer>> results) {
        if (len == arr.size()) {
            results.add(new ArrayList<>(arr));
        } else {
            for (int i = len; i < arr.size(); i++) {
                Collections.swap(arr, i, len);
                permute(arr, len + 1, results);
                Collections.swap(arr, i, len);
            }
        }
    }

    private static int calculateDistance(List<Integer> route) {
        int distance = 0;
        int prevCity = 0; // Koriko City as starting point
        for (int city : route) {
            distance += distanceMatrix[prevCity][city];
            prevCity = city;
        }
        distance += distanceMatrix[prevCity][0]; // Return to Koriko City
        return distance;
    }
}
