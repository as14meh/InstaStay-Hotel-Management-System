import java.util.*;

class HotelManagementSystem {
    private HashMap<String, Customer> customerDatabase;
    private ArrayList<Room> availableRooms;
    private HashMap<String, Booking> bookings;
    private int bookingIdCounter;

    public HotelManagementSystem() {
        customerDatabase = new HashMap<>();
        availableRooms = new ArrayList<>();
        bookings = new HashMap<>();
        bookingIdCounter = 1;
        initializeRooms();
    }

    private void initializeRooms() {
        availableRooms.add(new Room(101, "Single", 1000));
        availableRooms.add(new Room(102, "Double", 1500));
        availableRooms.add(new Room(103, "Suite", 3000));
    }

    public boolean authenticateAdmin(String username, String password) {
        // Simple hardcoded authentication check for the admin
        return username.equals("Astha") && password.equals("Miss Mehra");
    }

    public void bookRoom(String name, String address, String phone, int roomNumber, int days) {
        Customer customer;
        String customerId = phone; // Use phone number as unique identifier for the customer

        if (!customerDatabase.containsKey(customerId)) {
            customer = new Customer(name, customerId, address, phone);
            customerDatabase.put(customerId, customer);
            System.out.println("Customer added: " + name);
        } else {
            customer = customerDatabase.get(customerId);
        }

        Room room = findRoom(roomNumber);
        if (room != null && room.isAvailable()) {
            String bookingId = generateBookingId(name);
            Booking booking = new Booking(bookingId, customer, room, days);
            bookings.put(bookingId, booking);
            room.setAvailable(false);
            System.out.println("Room booked for " + customer.getName() + " for " + days + " days. Booking ID: " + bookingId);
        } else {
            System.out.println("Room is not available or doesn't exist.");
        }
    }

    private String generateBookingId(String customerName) {
        // Generate unique booking ID using first 3 characters of the customer's name and bookingIdCounter
        String bookingId = customerName.substring(0, 3).toUpperCase() + bookingIdCounter++;
        return bookingId;
    }

    public void extendStay(String bookingId, int extraDays) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.extendStay(extraDays);
            System.out.println("Stay extended by " + extraDays + " days.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void setDiscount(String bookingId, double discount) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.setDiscount(discount);
            System.out.println("Discount of " + discount + "% applied to Booking ID " + bookingId);
        } else {
            System.out.println("Booking not found.");
        }
    }

    // Generate invoice only at checkout
    private void generateInvoice(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            double totalAmount = booking.calculateTotalAmount();
            System.out.println("\n--- Invoice for Booking ID: " + bookingId + " ---");
            System.out.println("Customer: " + booking.getCustomer().getName());
            System.out.println("Room: " + booking.getRoom().getRoomType());
            System.out.println("Number of Days: " + booking.getDays());
            System.out.println("Discount: " + booking.getDiscount() + "%");
            System.out.println("Total Amount after Discount: \u20B9" + totalAmount + " INR");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void checkoutCustomer(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            // Generate invoice automatically at checkout
            generateInvoice(bookingId);
            booking.getRoom().setAvailable(true); // Mark the room as available after checkout
            bookings.remove(bookingId); // Remove the booking from the system
            System.out.println("Customer checked out. Room is now available.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking != null) {
            booking.getRoom().setAvailable(true); // Make the room available
            bookings.remove(bookingId); // Remove the booking
            System.out.println("Booking ID " + bookingId + " has been canceled. Room is now available.");
        } else {
            System.out.println("Booking not found.");
        }
    }

    public void showAvailableRooms() {
        System.out.println("\n--- Available Rooms ---");
        for (Room room : availableRooms) {
            if (room.isAvailable()) {
                System.out.println("Room Number: " + room.getRoomNumber() + " | Type: " + room.getRoomType() + " | Price per day: \u20B9" + room.getPricePerDay());
            }
        }
    }

    private Room findRoom(int roomNumber) {
        for (Room room : availableRooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        HotelManagementSystem system = new HotelManagementSystem();
        Scanner scanner = new Scanner(System.in);
        
        // Admin Login
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();

        // Authenticate Admin
        if (system.authenticateAdmin(username, password)) {
            System.out.println("Login Successful! Welcome, " + username);
            boolean exit = false;

            while (!exit) {
                System.out.println("\n--- Hotel Management System ---");
                System.out.println("1. Book Room");
                System.out.println("2. Check Room Availability");
                System.out.println("3. Extend Stay");
                System.out.println("4. Apply Discount");
                System.out.println("5. Checkout Customer");
                System.out.println("6. Cancel Booking");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Customer Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Customer Address: ");
                        String address = scanner.nextLine();
                        System.out.print("Enter Phone Number: ");
                        String phone = scanner.nextLine();
                        System.out.print("Enter Room Number: ");
                        int roomNumber = scanner.nextInt();
                        System.out.print("Enter Number of Days: ");
                        int days = scanner.nextInt();
                        system.bookRoom(name, address, phone, roomNumber, days);
                        break;

                    case 2:
                        system.showAvailableRooms();
                        break;

                    case 3:
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Booking ID: ");
                        String bookingId = scanner.nextLine();
                        System.out.print("Enter Extra Days: ");
                        int extraDays = scanner.nextInt();
                        system.extendStay(bookingId, extraDays);
                        break;

                    case 4:
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Booking ID: ");
                        bookingId = scanner.nextLine();
                        System.out.print("Enter Discount Percentage: ");
                        double discount = scanner.nextDouble();
                        system.setDiscount(bookingId, discount);
                        break;

                    case 5:
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Booking ID: ");
                        bookingId = scanner.nextLine();
                        system.checkoutCustomer(bookingId);
                        break;

                    case 6:
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter Booking ID: ");
                        bookingId = scanner.nextLine();
                        system.cancelBooking(bookingId);
                        break;

                    case 7:
                        exit = true;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid credentials. Access denied.");
        }

        scanner.close();
    }
}

// Customer class
class Customer {
    private String name;
    private String id;
    private String address;
    private String phone;

    public Customer(String name, String id, String address, String phone) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}

// Room class
class Room {
    private int roomNumber;
    private String roomType;
    private double pricePerDay;
    private boolean available;

    public Room(int roomNumber, String roomType, double pricePerDay) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerDay = pricePerDay;
        this.available = true;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

// Booking class with discount
class Booking {
    private String bookingId;
    private Customer customer;
    private Room room;
    private int days;
    private double discount; // Percentage discount

    public Booking(String bookingId, Customer customer, Room room, int days) {
        this.bookingId = bookingId;
        this.customer = customer;
        this.room = room;
        this.days = days;
        this.discount = 0; // Default discount is 0%
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Room getRoom() {
        return room;
    }

    public int getDays() {
        return days;
    }

    public void extendStay(int extraDays) {
        this.days += extraDays;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double calculateTotalAmount() {
        double totalAmount = days * room.getPricePerDay();
        totalAmount -= totalAmount * discount / 100; // Apply discount
        return totalAmount;
    }
}
