package obj_class;

// template attributes
public class Car {
	//attributes
	// global variable (in blue)
	String make;
	String color;
	String type;
	int model; //year
	int displacement;
	double fuelBurnRate; // liter per hour
	double fuelCapacity;
	double distance;
	
	double calculateDistance() {
		// local variable
		double distance;
		// this.distance = distance;
		
		distance = fuelCapacity * fuelBurnRate;
//		System.out.println("The distance: " + distance);
//		System.out.println("The distance for " + this.make + " " + distance);
		// this.attribute - used if have the same instance / attribute variable
		
		return distance;
	}
	
	double fuelNeeded(double time) {
		return time * fuelBurnRate;
	}
	// instance variable (global variables)
	
	double Distance() {
		// local variable
		double distance = 10.5;
		return this.distance = distance;
	}
}
