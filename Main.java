import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class Vehicle {
    protected String vehicleId;
    protected String brand;
    protected String model;
    protected double basePricePerDay;
    protected boolean isAvailable;

    public Vehicle(String vehicleId, String brand, String model, double basePricePerDay) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
        this.isAvailable = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getBasePricePerDay() {
        return basePricePerDay;
    }

    public void rent() {
        isAvailable = false;
    }

    public void returnVehicle() {
        isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }
}

class Car extends Vehicle {

    public Car(String carId, String brand, String model, double basePricePerDay) {
        super(carId, brand, model, basePricePerDay);
    }

    public String getCarId() {
        return vehicleId;
    }

    @Override
    public double calculatePrice(int rentalDays) {
        if (rentalDays > 7) {
            return (basePricePerDay * rentalDays) * 0.9; // 10% discount
        }
        return super.calculatePrice(rentalDays);
    }

    public void setBasePricePerDay(double basePricePerDay) {
        if (basePricePerDay > 0) {
            this.basePricePerDay = basePricePerDay;
        } else {
            System.out.println("Base price per day must be positive.");
        }
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() {
        return car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }
}

class CarRentalSystem {
    private List<Car> cars;
    private List<Customer> customers;
    private List<Rental> rentals;

    public CarRentalSystem() {
        cars = new ArrayList<>();
        customers = new ArrayList<>();
        rentals = new ArrayList<>();
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public void rentCar(Car car, Customer customer, int days) {
        if (car.isAvailable()) {
            car.rent();
            rentals.add(new Rental(car, customer, days));
        } else {
            System.out.println("Car is not available for rent.");
        }
    }

    public void returnCar(Car car) {
        car.returnVehicle();
        Rental rentalToRemove = null;
        for (Rental rental : rentals) {
            if (rental.getCar() == car) {
                rentalToRemove = rental;
                break;
            }
        }
        if (rentalToRemove != null) {
            rentals.remove(rentalToRemove);
        } else {
            System.out.println("Car was not rented.");
        }
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("===== Car Rental System =====");
                System.out.println("1. Rent a Car");
                System.out.println("2. Return a Car");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        rentCarFlow(scanner);
                        break;
                    case 2:
                        returnCarFlow(scanner);
                        break;
                    case 3:
                        System.out.println("\nThank you for using the Car Rental System!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a valid option.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private void rentCarFlow(Scanner scanner) {
        try {
            System.out.println("\n== Rent a Car ==\n");
            System.out.print("Enter your name: ");
            String customerName = scanner.nextLine();

            System.out.println("\nAvailable Cars:");
            for (Car car : cars) {
                if (car.isAvailable()) {
                    System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
                }
            }

            System.out.print("Enter the car ID you want to rent: ");
            String carId = scanner.nextLine();

            Car selectedCar = null;
            for (Car car : cars) {
                if (car.getCarId().equalsIgnoreCase(carId) && car.isAvailable()) {
                    selectedCar = car;
                    break;
                }
            }

            if (selectedCar == null) {
                System.out.println("Invalid car selection or car not available for rent.");
                return;
            }

            System.out.print("Enter the number of days for rental: ");
            int rentalDays = Integer.parseInt(scanner.nextLine());

            if (rentalDays <= 0) {
                System.out.println("Rental days must be greater than zero.");
                return;
            }

            Customer newCustomer = new Customer("CUS" + (customers.size() + 1), customerName);
            addCustomer(newCustomer);

            double totalPrice = selectedCar.calculatePrice(rentalDays);
            System.out.println("\n== Rental Information ==\n");
            System.out.println("Customer ID: " + newCustomer.getCustomerId());
            System.out.println("Customer Name: " + newCustomer.getName());
            System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
            System.out.println("Rental Days: " + rentalDays);
            System.out.printf("Total Price: $%.2f%n", totalPrice);

            System.out.print("\nConfirm rental (Y/N): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                rentCar(selectedCar, newCustomer, rentalDays);
                System.out.println("\nCar rented successfully.");
            } else {
                System.out.println("\nRental canceled.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Please enter a valid number for rental days.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred during rental: " + e.getMessage());
        }
    }

    private void returnCarFlow(Scanner scanner) {
        try {
            System.out.println("\n== Return a Car ==\n");
            System.out.print("Enter the car ID you want to return: ");
            String carId = scanner.nextLine();

            Car carToReturn = null;
            for (Car car : cars) {
                if (car.getCarId().equalsIgnoreCase(carId) && !car.isAvailable()) {
                    carToReturn = car;
                    break;
                }
            }

            if (carToReturn != null) {
                Customer customer = null;
                for (Rental rental : rentals) {
                    if (rental.getCar() == carToReturn) {
                        customer = rental.getCustomer();
                        break;
                    }
                }

                if (customer != null) {
                    returnCar(carToReturn);
                    System.out.println("Car returned successfully by " + customer.getName());
                } else {
                    System.out.println("Car was not rented or rental information is missing.");
                }
            } else {
                System.out.println("Invalid car ID or car is not currently rented.");
            }

        } catch (Exception e) {
            System.out.println("An unexpected error occurred during return: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        CarRentalSystem rentalSystem = new CarRentalSystem();

        Car car1 = new Car("C001", "Toyota", "Camry", 60.0);
        Car car2 = new Car("C002", "Honda", "Accord", 70.0);
        Car car3 = new Car("C003", "Mahindra", "Thar", 150.0);

        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        rentalSystem.menu();
    }
}
