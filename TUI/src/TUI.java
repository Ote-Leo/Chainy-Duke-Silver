import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class TUI {
    public static void main(String[] args) throws Exception {
        // create a hashmap to store choices
        Hashtable<String, ArrayList<String>> states = new Hashtable<String, ArrayList<String>>();

        ArrayList<String> initialChoices = new ArrayList<String>();
        initialChoices.add("1. Help");
        initialChoices.add("2. Register Doctor");
        initialChoices.add("3. Login Doctor");
        initialChoices.add("4. Quit");
        states.put("initial", initialChoices);

        ArrayList<String> helpChoices = new ArrayList<String>();
        helpChoices.add("1. Register Doctor");
        helpChoices.add("2. Login Doctor");
        helpChoices.add("3. Quit");
        states.put("help", helpChoices);

        ArrayList<String> registerChoices = new ArrayList<String>();
        registerChoices.add("1. Enter a Doctor Name");
        registerChoices.add("2. Enter a Doctor Specialty");
        registerChoices.add("3. Enter a Doctor Address");
        registerChoices.add("4. Enter a Doctor Phone Number");
        registerChoices.add("5. Enter a Doctor Email");
        registerChoices.add("6. Enter Password");
        registerChoices.add("7. Quit");
        states.put("register", registerChoices);

        ArrayList<String> loginChoices = new ArrayList<String>();
        loginChoices.add("1. Enter a Doctor Email");
        loginChoices.add("2. Enter Password");
        loginChoices.add("3. Quit");
        states.put("login", loginChoices);

        Scanner scanner = new Scanner(System.in);
        String state = "initial";
        System.out.println("MENU: ");
        System.out.println("Enter your choice: ");
        for (int i = 0; i < initialChoices.size(); i++) {
            System.out.println(initialChoices.get(i));
        }
        int choice = scanner.nextInt();

        if (choice == 1) {
            state = "help";
        } else if (choice == 2) {
            state = "register";
        } else if (choice == 3) {
            state = "login";
        } else if (choice == 4) {
            System.exit(0);
        } else {
            System.out.println("Invalid choice");
        }
        boolean flag = true;
        boolean flag2 = true;
        boolean flag3 = true;
        while (true) {

            if (state.equals("register")) {
                if (flag) {
                    System.out.print("\033[H\033[2J");
                    System.out.println("Registration TUI \n Enter your choice: ");
                    for (int i = 0; i < registerChoices.size(); i++) {
                        System.out.println(registerChoices.get(i));
                    }
                    flag = false;
                }
                int choice2 = Integer.parseInt(scanner.next());
                if (choice2 == 1) {
                    System.out.println("Enter a Doctor Name: ");
                    String name = scanner.next();
                } else if (choice2 == 2) {
                    System.out.println("Enter a Doctor Specialty: ");
                    String specialty = scanner.next();

                } else if (choice2 == 3) {
                    System.out.println("Enter a Doctor Address: ");
                    String address = scanner.next();
                } else if (choice2 == 4) {
                    System.out.println("Enter a Doctor Phone Number: ");
                    String phone = scanner.next();
                } else if (choice2 == 5) {
                    System.out.println("Enter a Doctor Email: ");
                    String email = scanner.next();
                } else if (choice2 == 6) {
                    System.out.println("Enter Password: ");
                    String password = scanner.next();
                    System.out.println("Doctor registered successfully");
                } else if (choice2 == 7) {
                    state = "initial";
                } else {
                    System.out.println("Invalid choice");
                }

            }

            if (state.equals("login")) {
                if (flag2) {
                    System.out.print("\033[H\033[2J");
                    System.out.println("Login TUI \n Enter your choice: ");
                    for (int i = 0; i < loginChoices.size(); i++) {
                        System.out.println(loginChoices.get(i));
                    }
                    flag2 = false;
                }

                int choice2 = scanner.nextInt();
                if (choice2 == 1) {
                    System.out.println("Enter a Doctor Email: ");
                    String email = scanner.next();
                } else if (choice2 == 2) {
                    System.out.println("Enter Password: ");
                    String password = scanner.next();
                    System.out.println("Login successful");
                } else if (choice2 == 3) {
                    state = "initial";
                } else {
                    System.out.println("Invalid choice");
                }
            }

            if (state.equals("help") || state.equals("initial")) {
                System.out.print("\033[H\033[2J");
                if (state.equals("help")) {
                    System.out.println("HELP TUI \n Enter your choice: ");
                    for (int i = 0; i < initialChoices.size(); i++) {
                        System.out.println(initialChoices.get(i));
                    }
                    int choice2 = scanner.nextInt();
                    if (choice2 == 1) {
                        state = "help";
                    } else if (choice2 == 2) {
                        state = "register";
                    } else if (choice2 == 3) {
                        state = "login";
                    } else if (choice2 == 4) {
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice");
                    }

                } else if (state.equals("initial")) {
                    System.out.println("MENU: ");
                    System.out.println("Enter your choice: ");
                    for (int i = 0; i < initialChoices.size(); i++) {
                        System.out.println(initialChoices.get(i));
                    }
                    int choice2 = scanner.nextInt();
                    if (choice2 == 1) {
                        state = "help";
                    } else if (choice2 == 2) {
                        state = "register";
                    } else if (choice2 == 3) {
                        state = "login";
                    } else if (choice2 == 4) {
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice");
                    }
                }

            }

        }
    }
}
