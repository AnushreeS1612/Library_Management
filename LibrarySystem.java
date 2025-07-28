import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

class Book {
    String isbn;
    String title;
    String author;
    int quantity;
    double cost;
    int timesBorrowed = 0;

    public Book(String isbn, String title, String author, int quantity, double cost) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.cost = cost;
    }

    public String toString() {
        return title + " | ISBN: " + isbn + " | Author: " + author +
                " | Available: " + quantity + " | Cost:  " + cost +
                " | Borrowed: " + timesBorrowed + " times";
    }
}

class User {

    String name;
    String email;
    String password;
    String role;

    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}

class BorrowerData {
    double deposit = 1500;
    Map<String, LocalDate> borrowedBooks = new HashMap<>();
    int extensionCount = 0;
    double totalFines = 0;
    List<String> fineHistory = new ArrayList<>();
}

public class LibrarySystem {
    static Scanner sc = new Scanner(System.in);
    static Map<String, User> users = new HashMap<>();
    static Map<String, Book> books = new HashMap<>();
    static Map<String, BorrowerData> borrowerData = new HashMap<>();

    public static void main(String[] args) {
        preloadData();
        System.out.println("=== Welcome to Library System ===");
        loginMenu();
    }

    static void preloadData() {
        users.put("admin@example.com", new User("AdminOne", "admin@example.com", "admin123", "ADMIN"));
        users.put("user1@example.com", new User("UserOne", "user1@example.com", "user123", "BORROWER"));
        users.put("user2@example.com", new User("UserTwo", "user2@example.com", "user456", "BORROWER"));
        borrowerData.put("user1@example.com", new BorrowerData());
        borrowerData.put("user2@example.com", new BorrowerData());

        books.put("ISBN001", new Book("ISBN001", "C Programming", "Dennis Ritchie", 4, 400));
        books.put("ISBN002", new Book("ISBN002", "Java Fundamentals", "Herbert Schildt", 2, 500));
        books.put("ISBN003", new Book("ISBN003", "Python Basics", "Guido van Rossum", 3, 450));
    }

    static void loginMenu() {
        System.out.println("\n1. Login\n2. Register");
        System.out.print("Choose option: ");
        int option = Integer.parseInt(sc.nextLine());

        if (option == 1)
            login();
        else if (option == 2) {
            register();
            loginMenu();
        } else {
            System.out.println("Invalid option.");
            loginMenu();
        }
    }

    static void register() {
        System.out.println("\n--- User Registration ---");
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();

        if (users.containsKey(email)) {
            System.out.println("User already exists!");
            return;
        }

        System.out.print("Password: ");
        String password = sc.nextLine();
        System.out.print("Role (admin/borrower): ");
        String roleInput = sc.nextLine().trim().toLowerCase();
        String role = roleInput.equals("admin") ? "ADMIN" : "BORROWER";

        User user = new User(name, email, password, role);
        users.put(email, user);
        if (role.equals("BORROWER"))
            borrowerData.put(email, new BorrowerData());

        System.out.println("Registration successful. You can now login.");
    }

    static void login() {
        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        if (users.containsKey(email) && users.get(email).password.equals(password)) {
            User user = users.get(email);
            System.out.println("Login successful. Welcome " + user.name + " [" + user.getRole() + "]");
            if (user.getRole().equals("ADMIN"))
                adminMenu(user);
            else
                borrowerMenu(user);
        } else
            System.out.println("Invalid credentials.");
    }

    static void adminMenu(User user) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Add Book\n2. View All Books\n3. Search Book\n4. Add User\n5. Logout");
            System.out.print("Enter your choice: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    searchBook();
                    break;
                case 4:
                    addUser();
                    break;
                case 5:
                    return; 
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void addBook() {
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();
        System.out.print("Title: ");
        String title = sc.nextLine();
        System.out.print("Author: ");
        String author = sc.nextLine();
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(sc.nextLine());
        System.out.print("Cost: ");
        double cost = Double.parseDouble(sc.nextLine());

        books.put(isbn, new Book(isbn, title, author, qty, cost));
        System.out.println("Book added successfully!");
    }

    static void viewBooks() {
        System.out.println("\n--- Book Inventory ---");
        for (Book book : books.values())
            System.out.println(book);
    }

    static void searchBook() {
        System.out.print("Enter ISBN or Title: ");
        String input = sc.nextLine().toLowerCase();
        boolean found = false;
        for (Book book : books.values()) {
            if (book.title.toLowerCase().contains(input) || book.isbn.equalsIgnoreCase(input)) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found)
            System.out.println("No matching book found.");
    }

    static void addUser() {
        System.out.print("Enter User Role (admin/borrower): ");
        String role = sc.nextLine().trim().toLowerCase();
        if (!role.equals("admin") && !role.equals("borrower")) {
            System.out.println("Invalid role.");
            return;
        }
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        if (users.containsKey(email)) {
            System.out.println("User already exists.");
            return;
        }
        System.out.print("Password: ");
        String pass = sc.nextLine();
        users.put(email, new User(name, email, pass, role));
        if (role.equals("borrower"))
            borrowerData.put(email, new BorrowerData());
        System.out.println(role + " added successfully.");
    }

    static void borrowerMenu(User user) {
        while (true) {
            System.out.println("\n--- Borrower Menu ---");
            System.out.println(
                    "1. View Books\n2. Borrow Book\n3. Return Book\n4. Extend Book\n5. Mark Book Lost\n6. Report Card Lost\n7. View Fines\n8. Logout");
            System.out.print("Enter choice: ");
            int ch = Integer.parseInt(sc.nextLine());

            switch (ch) {
                case 1:
                    viewBooks();
                    break;
                case 2:
                    borrowBook(user);
                    break;
                case 3:
                    returnBook(user);
                    break;
                case 4:
                    extendBook(user);
                    break;
                case 5:
                    markBookLost(user);
                    break;
                case 6:
                    cardLost(user);
                    break;
                case 7:
                    viewFineHistory(user);
                    break;
                case 8:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    static void borrowBook(User user) {
        BorrowerData data = borrowerData.get(user.email);
        if (data.borrowedBooks.size() >= 3) {
            System.out.println("You can't borrow more than 3 books at a time.");
            return;
        }
        System.out.print("Enter ISBN to borrow: ");
        String isbn = sc.nextLine();
        Book book = books.get(isbn);
        if (book == null || book.quantity == 0) {
            System.out.println("Book unavailable.");
            return;
        }
        if (data.borrowedBooks.containsKey(isbn)) {
            System.out.println("You already borrowed this book.");
            return;
        }
        book.quantity--;
        book.timesBorrowed++;
        data.borrowedBooks.put(isbn, LocalDate.now());
        System.out.println("Borrowed successfully. Return within 15 days.");
    }

    static void returnBook(User user) {
        BorrowerData data = borrowerData.get(user.email);
        System.out.print("Enter ISBN to return: ");
        String isbn = sc.nextLine();
        Book book = books.get(isbn);
        if (!data.borrowedBooks.containsKey(isbn)) {
            System.out.println("This book wasn't borrowed.");
            return;
        }
        System.out.print("Enter Return Date (DD/MM/YYYY): ");
        LocalDate retDate = LocalDate.parse(sc.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate issueDate = data.borrowedBooks.get(isbn);
        long days = ChronoUnit.DAYS.between(issueDate, retDate);

        double fine = 0;
        if (days > 15) {
            long overdue = days - 15;
            fine = 2 * overdue;
            if (overdue > 10)
                fine = fine * Math.pow(2, (overdue / 10));
            fine = Math.min(fine, 0.8 * book.cost);
            data.deposit -= fine;
            data.totalFines += fine;
            data.fineHistory.add("Late return for " + book.title + ": ₹" + fine);
            System.out.println("Fine applied: ₹" + fine);
        }
        data.borrowedBooks.remove(isbn);
        book.quantity++;
        System.out.println("Book returned.");
    }

    static void extendBook(User user) {
        BorrowerData data = borrowerData.get(user.email);
        System.out.print("Enter ISBN to extend: ");
        String isbn = sc.nextLine();
        if (!data.borrowedBooks.containsKey(isbn)) {
            System.out.println("Book not borrowed.");
            return;
        }
        if (data.extensionCount >= 2) {
            System.out.println("You can't extend more than twice consecutively.");
            return;
        }
        data.borrowedBooks.put(isbn, LocalDate.now());
        data.extensionCount++;
        System.out.println("Extension granted.");
    }

    static void markBookLost(User user) {
        BorrowerData data = borrowerData.get(user.email);
        System.out.print("Enter ISBN of lost book: ");
        String isbn = sc.nextLine();
        Book book = books.get(isbn);
        if (!data.borrowedBooks.containsKey(isbn)) {
            System.out.println("Book not borrowed.");
            return;
        }
        double fine = 0.5 * book.cost;
        data.deposit -= fine;
        data.totalFines += fine;
        data.fineHistory.add("Lost book: " + book.title + " fine ₹" + fine);
        data.borrowedBooks.remove(isbn);
        System.out.println("Fine of ₹" + fine + " deducted for lost book.");
    }

    static void cardLost(User user) {
        BorrowerData data = borrowerData.get(user.email);
        data.deposit -= 10;
        data.totalFines += 10;
        data.fineHistory.add("Card lost fine ₹10");
        System.out.println("₹10 fine applied for lost card.");
    }

    static void viewFineHistory(User user) {
        BorrowerData data = borrowerData.get(user.email);
        System.out.println("\n--- Fine History ---");
        for (String entry : data.fineHistory)
        System.out.println(entry);
        System.out.println("Total Fine: ₹" + data.totalFines);
        System.out.println("Remaining Deposit: ₹" + data.deposit);
    }
}
