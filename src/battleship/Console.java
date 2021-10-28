package battleship;

import javax.crypto.Mac;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Contains functions to work with user`s input and write useful information
 * to the console.
 */
public class Console {

    // Constants, defying limits of the field.
    static final int MIN_FIELD_SIZE = 6;
    static final int MAX_FIELD_SIZE = 30;
    // This flag tells if user`s input was 'exit' or not.
    // As soon as its value becomes true, the program shall end.
    static boolean wasInputExit = false;

    static Scanner scanner = new Scanner(System.in);

    /**
     *
     * @param input any string
     * @return boolean value whether the input parameter can be parsed into int or not.
     */
    public static boolean isInputInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * gets correct int input from user
     *
     * @param minValue minimum allowed value
     * @param maxValue maximum allowed value
     * @return Integer.MIN_VALUE, if user entered 'exit', user`s input otherwise.
     */
    public static int getNumberInFixedRange(int minValue, int maxValue) {
        String input = scanner.nextLine();
        while ((!isInputInt(input) || Integer.parseInt((input)) < minValue
                || Integer.parseInt(input) > maxValue) && !Objects.equals(input, "exit")) {
            System.out.print(String.format("Please enter an integer in range [%d;%d] " +
                    "or type 'exit' if you want to finish the game.\n", minValue, maxValue));
            input = scanner.nextLine();
        }
        if (input.equals("exit")) {
            wasInputExit = true;
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(input);
    }

    /**
     * Gets all necessary information to initialize the field from user.
     * @param mode "T" - enables torpedo mode, any other string - normal mode.
     * @return field with user`s parameters
     */
    static Field initializeField(String mode) {
        int torpedoesAmount = 0;
        if (Objects.equals(mode, "T") ) {
            System.out.print("Now enter amount of torpedoes" +
                    " you would like to start with(int in range [0;500]): ");
            torpedoesAmount = getNumberInFixedRange(0, 500);
            System.out.println();
        }
        if (wasInputExit) {
            return null;
        }
        System.out.print(String.format("Now choose the parameters of the battlefield " +
                "(the ocean size)\nEnter amount of rows(integer in range [%d;%d]): ",
                MIN_FIELD_SIZE, MAX_FIELD_SIZE ));
        int rows = getNumberInFixedRange(MIN_FIELD_SIZE, MAX_FIELD_SIZE);
        // It means that user entered 'exit'.
        if (rows == Integer.MIN_VALUE || wasInputExit) {
            wasInputExit = true;
            return null;
        }
        System.out.print(String.format("Now enter amount of columns ([%d;%d]):",
                MIN_FIELD_SIZE, MAX_FIELD_SIZE));
        int columns = getNumberInFixedRange(MIN_FIELD_SIZE, MAX_FIELD_SIZE);
        if (columns == Integer.MIN_VALUE || wasInputExit) {
            wasInputExit = true;
            return null;
        }

        if (torpedoesAmount == Integer.MIN_VALUE) {
            wasInputExit = true;
            return null;
        }
        return new Field(rows, columns, torpedoesAmount, mode);
    }

    /**
     * Scrolls console
      */
    static void scrollConsole() {
        for (int i = 0; i < 40; ++i) {
            System.out.println("\n");
        }
    }

    static String GetCorrectInput(String[] correctOptions, Scanner scanner) {
        String input = "";
        while (!Arrays.asList(correctOptions).contains(input)) {
            input = scanner.nextLine();
        }
        return input;
    }

    /**
     *
     * @return user`s choice of mode in a string - 'T' stays for torpedo mode,
     * any other string - normal mode.
     */
    static String selectMode() {
        System.out.println("""
                Choose mode to play:
                If you want to enable Torpedo Mode, type 'T'
                To play by normal rules, type anything else""");
        return scanner.nextLine();
    }

    /**
     * Turns test mode (user can see all the ships), if user prints "test".
     * @return boolean, representing if user turned on test mode or not.
     */
    static boolean tryTurningTestingModeOn() {
        System.out.println("""
                If you want to turn testing mode (you will
                be able to see all the ships locations), print 'test', otherwise
                print anything else.""");
        scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (Objects.equals(input, "exit")) {
            wasInputExit = true;
        }
        return (Objects.equals(input, "test"));
    }

    /**
     * Gets valid coordinartes from user
     * @param field
     * @return int array with 2 coordinates - row coordinate and column coordinate.
     */
    public static int[] InputCorrectCoordinates(Field field) {
        int[] coordinates = new int[2];
        String input;
        boolean wasInputCorrect = true;
        do {
            input = scanner.nextLine();
            if (Objects.equals(input, "exit")) {
                wasInputExit = true;
                return coordinates;
            }
            if (Objects.equals(input.split(" ")[0], "T")) {
                coordinates = new int[3];
            }
            if (input.split(" ").length == 2 ) {
                try {
                    coordinates[0] = Integer.parseInt(input.split(" ")[0]);
                    coordinates[1] = Integer.parseInt(input.split(" ")[1]);
                    wasInputCorrect = true;
                } catch (Exception ex) {
                    wasInputCorrect = false;
                }
            } else if (input.split(" ").length == 3) { // First parameter is
                // 'T' - stays for torpedo shot.
                try {
                    coordinates[0] = Integer.parseInt(input.split(" ")[1]);
                    coordinates[1] = Integer.parseInt(input.split(" ")[2]);
                    wasInputCorrect = true;
                } catch (Exception ex) {
                    wasInputCorrect = false;
                }
            } else {
                wasInputCorrect = false;
            }
            if (!wasInputCorrect) {
                System.out.println("Incorrect input");
            }
        }while (!wasInputCorrect);
        return coordinates;
    }

}

