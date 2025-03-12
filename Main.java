import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        StringMatching stringMatching = new StringMatching();
        exitClass exitClass = new exitClass();
        stringMatching.displayCustomers();

        while (true) {
            System.out.print("\nDo you want to check the delivery invoice? [yes/no]: ");
            String choiceInvoice = scanner.nextLine().trim().toLowerCase();

            if (choiceInvoice.equals("yes")) {
                System.out.print("\nEnter a customer name: ");
                String customerName = scanner.nextLine();
                stringMatching.findCustomer(customerName);
            }
            else if (choiceInvoice.equals("no")) {
                System.out.print("\nDo you want to proceed to the TSP Solver? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if (response.equals("yes")) {
                    TSPSolver solver = new TSPSolver();
                    solver.findShortestRoute();
                }

                break; // Exit after TSP decision
            }
            else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        // Print exiting message
        exitClass.printExit();

        scanner.close();
    }
}
