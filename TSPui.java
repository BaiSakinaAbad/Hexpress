import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class TSPui {

    private JFrame frame;
    private JPanel mapPanel;
    private List<String> allBatchLocations;
    private List<List<String>> allRoutes;
    private List<List<Point>> allCoordinates;
    private int currentBatchIndex;
    private int currentRouteIndex;
    private javax.swing.Timer animationTimer;
    private JButton proceedToSortingButton;
    private JButton startButton;
    private JButton stopButton;
    private JButton newButton1;
    private JButton newButton2;
    private JButton skipToSortingButton;
    private static final int[][] distanceMatrix = {
            {0, 15, 14, 9, 10},
            {15, 0, 4, 4, 20},
            {14, 4, 0, 5, 17},
            {9, 4, 5, 0, 15},
            {10, 20, 17, 15, 0}
    };
    private static final String[] cities = {"Koriko City", "Emishi Village", "Kingsbury", "Howl's Castle", "Hotel Adriano"};
    private static final Point[] cityCoordinates = {
            new Point(1140, 440), // Koriko City
            new Point(830, 60), // Emishi Village
            new Point(550, 102),  // Kingsbury
            new Point(850, 180), // Howl's Castle
            new Point(1036, 705)  // Hotel Adriano
    };
    private List<Color> arrowColors;
    private ImageIcon backgroundIcon;
    private Set<Integer> visitedLocations;

    public TSPui(DeliveryCombination deliveryCombination, List<String> allBatchLocations) {
        this.allBatchLocations = allBatchLocations;
        this.currentBatchIndex = 0;
        this.currentRouteIndex = 0;
        this.allRoutes = new ArrayList<>();
        this.allCoordinates = new ArrayList<>();
        this.arrowColors = new ArrayList<>();
        this.visitedLocations = new HashSet<>();
        initializeRoutes();
        initializeUI();
    }

    private void initializeRoutes() {
        System.out.println("Initializing routes...");
        if (allBatchLocations == null || allBatchLocations.isEmpty()) {
            System.out.println("allBatchLocations is null or empty!");
            return;
        }

        int batchCount = 0;
        for (String locationString : allBatchLocations) {
            batchCount++;
            System.out.println("Batch " + batchCount + " - Location string: " + locationString);
            List<String> batchLocations = new ArrayList<>(Arrays.asList(locationString.split(", ")));
            System.out.println("Batch " + batchCount + " - Split locations: " + batchLocations);

            List<Integer> locationIndices = getLocationIndices(batchLocations);
            System.out.println("Batch " + batchCount + " - Location indices: " + locationIndices);

            List<Integer> bestRoute = solveTSP(locationIndices);
            System.out.println("Batch " + batchCount + " - Best route indices: " + bestRoute);

            List<String> route = new ArrayList<>();
            for (Integer idx : bestRoute) {
                route.add(cities[idx]);
            }
            allRoutes.add(route);
            System.out.println("Batch " + batchCount + " - Route: " + route);

            List<Point> coordinateRoute = new ArrayList<>();
            coordinateRoute.add(cityCoordinates[0]);
            for (Integer idx : bestRoute) {
                if (idx >= 0 && idx < cityCoordinates.length) {
                    coordinateRoute.add(cityCoordinates[idx]);
                } else {
                    System.err.println("Invalid city index: " + idx);
                }
            }
            coordinateRoute.add(cityCoordinates[0]);
            allCoordinates.add(coordinateRoute);
            System.out.println("Batch " + batchCount + " - Coordinate route: " + coordinateRoute);

            for (int i = 0; i < coordinateRoute.size() - 1; i++) {
                arrowColors.add(Color.BLUE);
            }
        }
    }

    private void initializeUI() {
        frame = new JFrame("Kiki's Delivery Route");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Load the GIF properly
        backgroundIcon = new ImageIcon("C:\\Users\\Sakina Abad\\IdeaProjects\\HEXPRESS2.0\\src\\tspmap.png");
        Image backgroundImage = backgroundIcon.getImage();

        mapPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                drawRoutes(g);
                drawCityMarkers(g); // Draw city markers and routes on top of the GIF
            }
        };

        mapPanel.setLayout(null);

        startButton = createStyledButton("Start Animation");
        startButton.setBounds(100, 580, 200, 60);
        startButton.addActionListener(e -> startAnimation());
        mapPanel.add(startButton);

        stopButton = createStyledButton("Stop Animation");
        stopButton.setBounds(350, 580, 200, 60);
        stopButton.setEnabled(false);
        stopButton.addActionListener(e -> stopAnimation());
        mapPanel.add(stopButton);

        proceedToSortingButton = createStyledButton("Proceed to Sorting");
        proceedToSortingButton.setBounds(600, 580, 300, 60);
        proceedToSortingButton.setEnabled(false);
        proceedToSortingButton.setVisible(false);
        proceedToSortingButton.addActionListener((ActionEvent e) -> {
            System.out.println("Proceeding to Sorting UI...");
            openSortingGui();
            frame.dispose();
        });
        mapPanel.add(proceedToSortingButton);

        skipToSortingButton = createStyledButton("Skip to Sorting");
        skipToSortingButton.setBounds(600, 580, 300, 60);
        skipToSortingButton.setEnabled(true);
        skipToSortingButton.setVisible(true);
        skipToSortingButton.addActionListener(e -> {
            System.out.println("Skipping to Sorting UI...");
            openSortingGui();
            frame.dispose();
        });
        mapPanel.add(skipToSortingButton);

        frame.setContentPane(mapPanel);
        frame.setVisible(true);
    }

    // New method to open SortingGui with combinations
    private void openSortingGui() {
        // Assume combinations are generated or available from DeliveryCombination or another source
        KnapsackSolver solver = new KnapsackSolver(20); // Example max weight (adjust as needed)
        List<List<Product>> combinations = solver.generateCombinations(solver.remainingProducts);
        SortingGui.setCombinations(combinations); // Set the combinations before opening
        SortingGui.open(); // Use the static open method
    }

    private void drawCityMarkers(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        int ovalSize = 20;
        for (int i = 0; i < cityCoordinates.length; i++) {
            Point p = cityCoordinates[i];
            if (i == 0 || visitedLocations.contains(i)) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.YELLOW);
            }
            g2.fillOval(p.x - ovalSize / 2, p.y - ovalSize / 2, ovalSize, ovalSize);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - ovalSize / 2, p.y - ovalSize / 2, ovalSize, ovalSize);
            g2.drawString(cities[i], p.x + 15, p.y + 5);
        }
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 180, 150));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        button.setForeground(Color.BLACK);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setOpaque(false);
        return button;
    }

    private void drawRoutes(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(3));

        // Only draw the current batch's routes
        if (currentBatchIndex < allCoordinates.size()) {
            List<Point> coordinates = allCoordinates.get(currentBatchIndex);
            for (int i = 0; i < coordinates.size() - 1; i++) {
                if (i < currentRouteIndex) {
                    g2.setColor(Color.RED); // Completed segments in red
                } else if (i == currentRouteIndex) {
                    g2.setColor(Color.GREEN); // Current segment in green
                    // Mark the current location as visited
                    int currentCityIndex = getCityIndexFromPoint(coordinates.get(i + 1));
                    if (currentCityIndex != -1) {
                        visitedLocations.add(currentCityIndex);
                    }
                } else {
                    g2.setColor(Color.BLUE); // Upcoming segments in blue
                }
                drawArrow(g2, coordinates.get(i), coordinates.get(i + 1));
            }
        }
    }

    private void drawArrow(Graphics2D g2, Point start, Point end) {
        int arrowSize = 35;
        double angle = Math.atan2(end.y - start.y, end.x - start.x);

        g2.drawLine(start.x, start.y, end.x, end.y);

        int arrowX1 = end.x - (int) (arrowSize * Math.cos(angle - Math.PI / 6));
        int arrowY1 = end.y - (int) (arrowSize * Math.sin(angle - Math.PI / 6));
        int arrowX2 = end.x - (int) (arrowSize * Math.cos(angle + Math.PI / 6));
        int arrowY2 = end.y - (int) (arrowSize * Math.sin(angle + Math.PI / 6));

        g2.drawLine(end.x, end.y, arrowX1, arrowY1);
        g2.drawLine(end.x, end.y, arrowX2, arrowY2);
    }

    private void startAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            return;
        }

        animationTimer = new javax.swing.Timer(1000, (ActionEvent e) -> {
            if (currentBatchIndex >= allRoutes.size()) {
                animationTimer.stop();
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
                proceedToSortingButton.setEnabled(true);
                proceedToSortingButton.setVisible(true);
                mapPanel.repaint();
                return;
            }

            List<Point> currentCoordinates = allCoordinates.get(currentBatchIndex);
            currentRouteIndex++;

            if (currentRouteIndex >= currentCoordinates.size() - 1) {
                currentBatchIndex++;
                currentRouteIndex = 0;
                visitedLocations.clear(); // Clear visited locations for the new batch
                visitedLocations.add(0); // Keep Koriko City as visited
            }

            mapPanel.repaint();
        });

        animationTimer.start();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
    }

    private void stopAnimation() {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        }
    }

    private List<Integer> getLocationIndices(List<String> batchLocations) {
        List<Integer> locationIndices = new ArrayList<>();
        for (String loc : batchLocations) {
            String normalizedLoc = normalizeLocation(loc);
            boolean found = false;
            for (int i = 0; i < cities.length; i++) {
                if (normalizeLocation(cities[i]).equals(normalizedLoc)) {
                    if (i != 0) {
                        locationIndices.add(i);
                    }
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.err.println("Location not found: " + loc);
            }
        }
        return locationIndices;
    }

    private String normalizeLocation(String location) {
        return location.trim().toLowerCase();
    }

    private List<Integer> solveTSP(List<Integer> locations) {
        if (locations.isEmpty()) {
            return new ArrayList<>();
        }

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
        return bestRoute != null ? bestRoute : new ArrayList<>();
    }

    private void permute(List<Integer> arr, int len, List<List<Integer>> results) {
        if (len == arr.size()) {
            results.add(new ArrayList<>(arr));
            return;
        }
        for (int i = len; i < arr.size(); i++) {
            Collections.swap(arr, len, i);
            permute(arr, len + 1, results);
            Collections.swap(arr, len, i);
        }
    }

    private int calculateDistance(List<Integer> route) {
        int distance = 0;
        int prevCity = 0; // Start at Koriko City (index 0)
        for (int city : route) {
            distance += distanceMatrix[prevCity][city];
            prevCity = city;
        }
        distance += distanceMatrix[prevCity][0]; // Return to Koriko City
        return distance;
    }

    private int getCityIndexFromPoint(Point p) {
        for (int i = 0; i < cityCoordinates.length; i++) {
            if (cityCoordinates[i].equals(p)) {
                return i;
            }
        }
        return -1;
    }
}