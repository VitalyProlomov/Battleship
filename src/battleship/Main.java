package battleship;

import java.util.Scanner;

public class Main {
    static Scanner scannerMain = new Scanner(System.in);
    static boolean isTestingModeOn = false;

    public static void main(String[] args) {
        System.out.println("Hello, User!");
        while (!Console.wasInputExit) {
            String mode = Console.selectMode();
            if (mode.equals("exit")) {
                Console.wasInputExit = true;
                break;
            }

            Field field = Console.initializeField(mode);
            if (Console.wasInputExit || field == null) {
                Console.wasInputExit = true;
                break;
            }

            field.inputShips();
            if (Console.wasInputExit)
                break;

            if (Console.tryTurningTestingModeOn()) {
                isTestingModeOn = true;
            } else {
                isTestingModeOn = false;
            }
            if (Console.wasInputExit)
                break;
            field.displayField();

            System.out.println("""
                    
                    Now you may start shooting:
                    to choose a target cell, print its coordinates
                    seperated by one space, like that: x y.
                    To shoot a torpedo, print T x y""");
            while (!Console.wasInputExit && field.areThereAnyShipsLeftAlive()) {
                int[] coordinates = Console.InputCorrectCoordinates(field);
                if (Console.wasInputExit) {
                    break;
                }
                String message;
                if (coordinates.length == 2) {
                    message = field.ShootRegular(coordinates[0], coordinates[1]);
                } else {
                    message = field.ShootTorpedo(coordinates[0], coordinates[1]);
                }
                Console.scrollConsole();
                System.out.print(String.format("Shots fired: %d", field.getShotsFired()));
                if (mode.equals("T") || mode.equals("TR")) {
                    System.out.println(String.format("    Torpedoes left: %d", field.getTorpedoesLeft()));
                } else {
                    System.out.println();
                }
                field.displayField();
                System.out.println();
                System.out.println(message);
            }
            if (!Console.wasInputExit) {
                System.out.println(String.format("Congratulations! You won!\n" +
                        "You shot %d shots.\n If you want to exit, print 'exit',\n" +
                        "print anything else otherwise", field.getShotsFired()));
                String choice = scannerMain.nextLine();
                if (choice == "exit") {
                    Console.wasInputExit = true;
                }
            }
        }

        System.out.println("Bye!");
    }
}
