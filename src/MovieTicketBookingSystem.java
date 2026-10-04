import java.util.*;

public class MovieTicketBookingSystem {

    // =========================
    // Movie
    // =========================
    static class Movie {
        String name;
        double rating;
        int ratingCount;

        Movie(String name) {
            this.name = name;
            this.rating = 0.0;
            this.ratingCount = 0;
        }

        void addRating(int newRating) {
            rating = ((rating * ratingCount) + newRating) / (ratingCount + 1);
            ratingCount++;
        }

        void displayMovie() {
            System.out.println("Movie   : " + name);
            System.out.printf("Rating  : %.2f/5%n", rating);
        }
    }

    // =========================
    // Seat
    // =========================
    static class Seat {
        String seatNumber;
        boolean booked;

        Seat(String seatNumber) {
            this.seatNumber = seatNumber;
            this.booked = false;
        }
    }

    // =========================
    // Booking
    // =========================
    static class Booking {
        int bookingId;
        String movieName;
        String seatNumber;
        double amount;
        boolean cancelled;

        Booking(int bookingId, String movieName,
                String seatNumber, double amount) {

            this.bookingId = bookingId;
            this.movieName = movieName;
            this.seatNumber = seatNumber;
            this.amount = amount;
            this.cancelled = false;
        }

        void displayBooking() {
            System.out.println("--------------------------------");
            System.out.println("Booking ID : " + bookingId);
            System.out.println("Movie      : " + movieName);
            System.out.println("Seat       : " + seatNumber);
            System.out.println("Amount     : ₹" + amount);
            System.out.println("Status     : " +
                    (cancelled ? "CANCELLED" : "CONFIRMED"));
            System.out.println("--------------------------------");
        }
    }

    // =========================
    // Global Data
    // =========================

    static Scanner scanner = new Scanner(System.in);

    static Movie movie = new Movie("Avengers: Endgame");

    static ArrayList<Seat> seats = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static int bookingCounter = 1001;

    static final double BASE_PRICE = 200;
    static final double WEEKEND_PRICE = 250;

    // =========================
    // Main
    // =========================

    public static void main(String[] args) {

        initializeSeats();

        while (true) {

            System.out.println("\n====================================");
            System.out.println("     MOVIE TICKET BOOKING SYSTEM");
            System.out.println("====================================");

            System.out.println("1. Show Available Seats");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Apply Discount Coupon");
            System.out.println("5. Weekend Pricing");
            System.out.println("6. Rate Movie");
            System.out.println("7. Show Movie Details");
            System.out.println("8. Show All Bookings");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {

                case 1:
                    showAvailableSeats();
                    break;

                case 2:
                    bookTicket();
                    break;

                case 3:
                    cancelTicket();
                    break;

                case 4:
                    demonstrateCoupon();
                    break;

                case 5:
                    showWeekendPricing();
                    break;

                case 6:
                    rateMovie();
                    break;

                case 7:
                    movie.displayMovie();
                    break;

                case 8:
                    showAllBookings();
                    break;

                case 9:
                    System.out.println("Thank you for using the system!");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // =========================
    // Initialize Seats
    // =========================

    static void initializeSeats() {

        String[] rows = {"A", "B", "C", "D"};

        for (String row : rows) {

            for (int i = 1; i <= 5; i++) {

                seats.add(new Seat(row + i));
            }
        }
    }

    // =========================
    // Show Available Seats
    // =========================

    static void showAvailableSeats() {

        System.out.println("\n========== SEATS ==========");

        for (int i = 0; i < seats.size(); i++) {

            Seat seat = seats.get(i);

            if (seat.booked) {
                System.out.print("[X] ");
            } else {
                System.out.print("[" + seat.seatNumber + "] ");
            }

            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }

        System.out.println("\n[X] = Booked");
    }

    // =========================
    // Book Ticket
    // =========================

    static void bookTicket() {

        System.out.println("\n========== BOOK TICKET ==========");

        showAvailableSeats();

        System.out.print("\nEnter seat number: ");

        String seatNumber = scanner.nextLine().toUpperCase();

        Seat selectedSeat = findSeat(seatNumber);

        if (selectedSeat == null) {

            System.out.println("Seat does not exist!");
            return;
        }

        if (selectedSeat.booked) {

            System.out.println("Seat is already booked!");
            return;
        }

        // Determine ticket price
        double price = calculateTicketPrice();

        System.out.println("\nBase Ticket Price: ₹" + price);

        // Coupon
        System.out.print("Do you have a coupon? (yes/no): ");

        String answer = scanner.nextLine();

        double discount = 0;

        if (answer.equalsIgnoreCase("yes")) {

            System.out.print("Enter coupon code: ");

            String coupon = scanner.nextLine().toUpperCase();

            discount = calculateDiscount(coupon, price);

            if (discount > 0) {
                System.out.println("Discount: ₹" + discount);
            }
        }

        double finalAmount = price - discount;

        selectedSeat.booked = true;

        Booking booking = new Booking(
                bookingCounter++,
                movie.name,
                seatNumber,
                finalAmount
        );

        bookings.add(booking);

        System.out.println("\n========== BOOKING SUCCESSFUL ==========");

        booking.displayBooking();
    }

    // =========================
    // Find Seat
    // =========================

    static Seat findSeat(String seatNumber) {

        for (Seat seat : seats) {

            if (seat.seatNumber.equalsIgnoreCase(seatNumber)) {
                return seat;
            }
        }

        return null;
    }

    // =========================
    // Calculate Ticket Price
    // =========================

    static double calculateTicketPrice() {

        System.out.println("\nSelect Day:");

        System.out.println("1. Monday");
        System.out.println("2. Tuesday");
        System.out.println("3. Wednesday");
        System.out.println("4. Thursday");
        System.out.println("5. Friday");
        System.out.println("6. Saturday");
        System.out.println("7. Sunday");

        System.out.print("Enter day: ");

        int day = readInt();

        if (day == 6 || day == 7) {

            return WEEKEND_PRICE;
        }

        return BASE_PRICE;
    }

    // =========================
    // Weekend Pricing
    // =========================

    static void showWeekendPricing() {

        System.out.println("\n========== WEEKEND PRICING ==========");

        System.out.println("Weekday Ticket : ₹" + BASE_PRICE);
        System.out.println("Weekend Ticket : ₹" + WEEKEND_PRICE);

        System.out.println("\nSaturday and Sunday have higher pricing.");
    }

    // =========================
    // Coupon
    // =========================

    static double calculateDiscount(String coupon, double price) {

        switch (coupon) {

            case "MOVIE20":

                System.out.println("MOVIE20 applied - 20% discount");

                return price * 0.20;

            case "WELCOME10":

                System.out.println("WELCOME10 applied - 10% discount");

                return price * 0.10;

            case "FLAT50":

                System.out.println("FLAT50 applied - ₹50 discount");

                return Math.min(50, price);

            default:

                System.out.println("Invalid coupon!");

                return 0;
        }
    }

    // =========================
    // Demonstrate Coupon
    // =========================

    static void demonstrateCoupon() {

        System.out.println("\n========== AVAILABLE COUPONS ==========");

        System.out.println("MOVIE20  -> 20% Discount");
        System.out.println("WELCOME10 -> 10% Discount");
        System.out.println("FLAT50 -> ₹50 Discount");

        System.out.print("\nEnter ticket price to test coupon: ₹");

        double price = readDouble();

        System.out.print("Enter coupon code: ");

        String coupon = scanner.nextLine().toUpperCase();

        double discount = calculateDiscount(coupon, price);

        double finalPrice = price - discount;

        System.out.println("Original Price : ₹" + price);
        System.out.println("Discount       : ₹" + discount);
        System.out.println("Final Price    : ₹" + finalPrice);
    }

    // =========================
    // Cancel Ticket
    // =========================

    static void cancelTicket() {

        System.out.println("\n========== CANCEL TICKET ==========");

        System.out.print("Enter Booking ID: ");

        int bookingId = readInt();

        Booking selectedBooking = null;

        for (Booking booking : bookings) {

            if (booking.bookingId == bookingId) {

                selectedBooking = booking;
                break;
            }
        }

        if (selectedBooking == null) {

            System.out.println("Booking not found!");
            return;
        }

        if (selectedBooking.cancelled) {

            System.out.println("Booking is already cancelled!");
            return;
        }

        // Cancel booking
        selectedBooking.cancelled = true;

        // Make seat available again
        Seat seat = findSeat(selectedBooking.seatNumber);

        if (seat != null) {
            seat.booked = false;
        }

        System.out.println("Ticket cancelled successfully!");

        System.out.println("Booking ID: " + bookingId);
        System.out.println("Seat " +
                selectedBooking.seatNumber +
                " is now available.");
    }

    // =========================
    // Rate Movie
    // =========================

    static void rateMovie() {

        System.out.println("\n========== RATE MOVIE ==========");

        System.out.println("Movie: " + movie.name);

        System.out.println("1. ⭐");
        System.out.println("2. ⭐⭐");
        System.out.println("3. ⭐⭐⭐");
        System.out.println("4. ⭐⭐⭐⭐");
        System.out.println("5. ⭐⭐⭐⭐⭐");

        System.out.print("Enter rating (1-5): ");

        int rating = readInt();

        if (rating < 1 || rating > 5) {

            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        movie.addRating(rating);

        System.out.println("Thank you for rating the movie!");

        System.out.printf(
                "Current Rating: %.2f/5%n",
                movie.rating
        );
    }

    // =========================
    // Show Movie Details
    // =========================

    static void showMovieDetails() {

        System.out.println("\n========== MOVIE DETAILS ==========");

        movie.displayMovie();

        System.out.println("Available Seats: " + getAvailableSeatCount());
        System.out.println("Booked Seats   : " +
                (seats.size() - getAvailableSeatCount()));
    }

    // =========================
    // Show All Bookings
    // =========================

    static void showAllBookings() {

        System.out.println("\n========== ALL BOOKINGS ==========");

        if (bookings.isEmpty()) {

            System.out.println("No bookings found.");
            return;
        }

        for (Booking booking : bookings) {

            booking.displayBooking();
        }
    }

    // =========================
    // Available Seat Count
    // =========================

    static int getAvailableSeatCount() {

        int count = 0;

        for (Seat seat : seats) {

            if (!seat.booked) {
                count++;
            }
        }

        return count;
    }

    // =========================
    // Read Integer Safely
    // =========================

    static int readInt() {

        while (true) {

            try {

                int value = Integer.parseInt(scanner.nextLine());

                return value;

            } catch (NumberFormatException e) {

                System.out.print("Enter a valid number: ");
            }
        }
    }

    // =========================
    // Read Double Safely
    // =========================

    static double readDouble() {

        while (true) {

            try {

                double value = Double.parseDouble(scanner.nextLine());

                return value;

            } catch (NumberFormatException e) {

                System.out.print("Enter a valid amount: ");
            }
        }
    }
}