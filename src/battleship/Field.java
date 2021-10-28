package battleship;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Field {

    // Keeps track of amount of shots taken by user.
    private int shotsFired = 0;
    private int torpedoesLeft = 0;

    // Contains all of cells that field contains.
    private final Cell[][] allCellsMatrix;
    // Possible values: "T" - torpedo mode, "R" - recovery mode,
    // "TR" - torpedo recovery, any other value - normal mode.
    private final String mode;
    private ArrayList<Ship> allShips = new ArrayList<Ship>();


    public Field(int rows, int columns) {
        this(rows, columns, 0);
    }

    public Field(int rows, int columns, int torpedoesAmount) {
        this(rows, columns, torpedoesAmount, "normal");
    }

    /**
     *
     * @param rows amount of rows
     * @param columns amount of columns
     * @param torpedoesAmount amount of torpedoes that user is allowed to shoot.
     * @param mode 'T' for torpedo mode, other strings - for normal mode.
     */
    public Field(int rows, int columns, int torpedoesAmount, String mode) {
        allCellsMatrix = new Cell[rows][columns];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                allCellsMatrix[i][j] = new Cell(i, j);
            }
        }
        torpedoesLeft = torpedoesAmount;
        shotsFired = 0;
        this.mode = mode;
    }

    public int getShotsFired() {
        return shotsFired;
    }

    public int getTorpedoesLeft() {
        return torpedoesLeft;
    }

    public int getRowsAmount() {
        return allCellsMatrix.length;
    }

    public int getColumnsAmount() {
        return allCellsMatrix[0].length;
    }


    /**
     * clears all the cells and their assignments to shipDecks and replaces them with new cells.
     */
    private void clear() {
        for (int i = 0; i < allCellsMatrix.length; ++i) {
            for (int j = 0; j < allCellsMatrix[0].length; ++j) {
                allCellsMatrix[i][j] = new Cell(i, j);
            }
        }
    }

    public Cell.CellStatus getCellStatus(int row_index, int column_index) {
        return allCellsMatrix[row_index][column_index].getStatus();
    }

    /**
     * Checks if there are any ships assigned to the field.
     * @return whether there are any ships assigned to the field.
     */
    public boolean areThereAnyShipsLeftAlive() {
        for (Ship ship : allShips) {
            if (!ship.isSunk) {
                return true;
            }
        }
        return false;
    }


    /**
     * Gets coordinates from user and performs a shot to the conforming cell.
     * @param row_coordinate row with 0-base index
     * @param column_index column with 0-base index
     * @return message for user about success of the shot.
     */
    public String ShootRegular(int row_coordinate, int column_index) {
        if (row_coordinate >= getRowsAmount() || row_coordinate < 0 ||
                column_index >= getColumnsAmount() || column_index < 0) {
            return "Indexes out of range, try again";
        }
        Cell currentCell = allCellsMatrix[row_coordinate][column_index];
        Cell.CellStatus currentStatus = currentCell.getStatus();
        if (currentStatus == Cell.CellStatus.NOT_SHOT) {
            shotsFired++;
            if (currentCell.getAssignedShipDeck() == null) {
                currentCell.setStatus(Cell.CellStatus.MISSED_SHOT);
                return "You missed";
            } else {
                ShipDeck shotDeck = currentCell.getAssignedShipDeck();
                shotDeck.getAssignedShip().handleShot(shotDeck, false);
                if (shotDeck.getAssignedShip().getIsSunk()) {
                    shotDeck.getAssignedShip().updateSunkStatus();
                    return String.format("Wonderful, you`ve just eliminated enemy`s" +
                            " %s ", shotDeck.getAssignedShip().toString());
                }
                shotDeck.getAssignedCell().setStatus(Cell.CellStatus.WOUND_SHOT);
                return "Great, you damaged one of the ships";
            }
        } else {
            return "You`ve already shot here, try another cell (shot is not counted).";
        }

    }

    /**
     * Perorms a torpedo shot to the cell with given coordinates.
     * @param row_coordinate row with 0-base index
     * @param column_index column with 0-base index
     * @return message for user about success of the shot.
     */
    public String ShootTorpedo(int row_coordinate, int column_index) {
        if (row_coordinate >= getRowsAmount() || row_coordinate < 0 ||
                column_index >= getColumnsAmount() || column_index < 0) {
            return "Indexes out of range, try again";
        }
        if (torpedoesLeft <= 0) {
            return "You are out of torpedoes, use regular bombs";
        }
        Cell currentCell = allCellsMatrix[row_coordinate][column_index];
        if (currentCell.getStatus() == Cell.CellStatus.NOT_SHOT) {
            shotsFired++;
            torpedoesLeft--;
            if (currentCell.getAssignedShipDeck() == null) {
                currentCell.setStatus(Cell.CellStatus.MISSED_SHOT);
                return "You missed";
            }
            // shotDeck.getAssignedShip().handleShot(shotDeck, false);
            Ship currentShip = currentCell.getAssignedShipDeck().getAssignedShip();
            currentShip.sink();
            currentShip.updateSunkStatus();
            return String.format("Great job, you've eliminated enemy`s %s",
                    currentShip.toString());
        } else {
            return "You`ve already shot there, shot is not counted";
        }
    }

    /**
     * Gets string from user containing amounts of each type of ship
     * and adds them to the field.
     */
    public void inputShips() {
        System.out.println("Now enter how much ships of each kind you would" +
                "like to battle against\n" +
                "You should enter a row of 5 positive integers seperated by 1 space each,\n" +
                "where n-th number in a row represents amount of n-deck ships generated." +
                "Example: \n" +
                "If you want 2 single-deck ships and 1 4-deck ship, enter \n" +
                "2 0 0 1 0\n" +
                "!!Constraints!! - each number must be in range [0;50]");
        boolean wasInputWrong = true;
        while (wasInputWrong) {
            int parameters[];
            parameters = inputCorrectShipAmounts();
            if (Console.wasInputExit) {
                return;
            }
            int spaceOccupied = addShipsAndFindSpace(parameters);
            int amountOfShips = 0;
            for (int j = 0; j < parameters.length; ++j) {
                amountOfShips += parameters[j];
            }
            wasInputWrong = spaceOccupied > (getColumnsAmount()) * (getRowsAmount());

            if (wasInputWrong || !tryInsertShipsRandomly(parameters)) {
                clear();
                allShips = new ArrayList<Ship>();
                System.out.println("""

                        Computer was unable to insert to generate game with such parameters.
                        That could`ve happened for 2 reasons:
                        1) Such configuration is simply impossible
                        2) There is a very limited amount of ways
                        it could place the ships on the field.
                        So, the computer could not find any possible ones.""");
                System.out.println("Try another set of numbers:");
                wasInputWrong = true;
            }
        }
    }


    /**
     * gets correct string, containing 5 numbers - amounts of each
     * ship type from user
     * @return int with conforming amounts or null, if user printed 'exit'.
     */
    private int[] inputCorrectShipAmounts() {
        Scanner scanner1 = new Scanner(System.in);
        String input = scanner1.nextLine();
        while (true) {
            boolean wasInputCorrect = true;
            int[] shipsAmounts = new int[1];
            if (input == "exit") {
                Console.wasInputExit = true;
                return null;
            }
            if (input.split(" ").length == 5) {
                shipsAmounts = new int[input.split(" ").length];
                for (int i = 0; i < shipsAmounts.length; ++i) {
                    String inputAmount = input.split(" ")[i];
                    try {
                        int number = Integer.parseInt(inputAmount);
                        shipsAmounts[i] = number;
                    } catch (Exception ex) {
                        wasInputCorrect = false;
                        break;
                    }
                }
            } else {
                wasInputCorrect = false;
            }
            if (wasInputCorrect) {
                return shipsAmounts;
            } else {
                System.out.println("Incorrect input, please enter " +
                        "a row of 5 positive integers\n seperated by 1" +
                        "space each, where n-th number in a row represents \n" +
                        "amount of n-deck ships generated");
                input = scanner1.nextLine();
            }

        }

    }


    /**
     * Adding all entered ships in Field.allShips List.
     * @param shipsAmounts 5 positive integers seperated by space - amounts of each type of ship.
     */
    private int addShipsAndFindSpace(int[] shipsAmounts) {
        int neededSpaceSum = 0;
        for (int decksAmount = 1; decksAmount <= 5; ++decksAmount) {
            for (int i = 0; i < shipsAmounts[decksAmount - 1]; ++i) {
                switch (decksAmount) {
                    case 1:
                        allShips.add((new Submarine()));
                        break;
                    case 2:
                        allShips.add((new Destroyer()));
                        break;
                    case 3:
                        allShips.add((new Cruiser()));
                        break;
                    case 4:
                        allShips.add((new BattleShip()));
                        break;
                    case 5:
                        allShips.add((new Carrier()));
                        break;
                }
                // On every iteration we add amount of cells that the ship is going
                // to occupy on the field if it does not touch the borders of the
                // field and does not neighbour with any other ships.
                neededSpaceSum += (allShips.get(i).getLength() * 3) + 6;
            }
        }
        return neededSpaceSum;
    }

    Random rnd = new Random();
    final int INSERT_SHIP_ATTEMPTS = 20;
    final int FIELD_RESET_ATTEMPTS = 200;

    /**
     * Here I am going to try to insert ships to the field randomly
     * by choosing a cell for its head and then inserting the rest
     * of the cells either upwards or to the left of the head.
     * I will also only choose the position for the head X cells
     * down from the top border and X cells right left borders of the field,
     * where X = length of ship. That way indexes never get out of bounds.
     *
     * @param parameters amounts of each ship type
     * @return flag, showing was insertion successful or not.
     */
    private boolean tryInsertShipsRandomly(int[] parameters) {
        boolean wasWholeInsertionSuccessful = false;
        int fieldResetsLeft = FIELD_RESET_ATTEMPTS;
        while (!wasWholeInsertionSuccessful && fieldResetsLeft > 0) {

            for (int currentShipIndex = 0; currentShipIndex < allShips.size(); ++currentShipIndex) {
                boolean wasCurrentShipInserted = false;
                int shipIsertionAttemptsLeft = INSERT_SHIP_ATTEMPTS;

                // Shows if current ship can be inserted into the randomly
                // chosen coordinates with chosen direction or not.
                boolean canBeInserted = false;
                while (!canBeInserted && shipIsertionAttemptsLeft > 0) {
                    canBeInserted = tryInsertingCurrentShip(currentShipIndex);
                    if (!canBeInserted) {
                        shipIsertionAttemptsLeft--;
                    }
                }
                wasWholeInsertionSuccessful = shipIsertionAttemptsLeft > 0;
                // Resetting the field, clearing it from the
                // inserted ships from previous successful iterations.
                if (!wasWholeInsertionSuccessful) {
                    this.clear();
                    break;
                }
            }
            fieldResetsLeft--;
        }
        // Checking if all decks of all ships are actually assigned to some cell.
        // Needed to prevent mistakes (in case some ship was not actually assigned anywhere).
        for (Ship ship : allShips) {
            for (int j = 0; j < ship.allDecks.length; ++j) {
                if (ship.allDecks[j].getAssignedCell() == null) {
                    return false;
                }
            }
        }
        return fieldResetsLeft > 0;
    }

    /**
     * Trying to insert one ship to the field
     * @param currentShipIndex index of the ship in list of all ships
     * assigned to current field.
     * @return boolean showing if insertion was successful or not/
     */
    private boolean tryInsertingCurrentShip(int currentShipIndex) {
        boolean canBeInserted = true;
        int lowerBorder = allShips.get(currentShipIndex).getLength() - 1;
        // Index of the last row
        int upperBorder = allCellsMatrix.length - 1;
        int rowCoord = (int) (rnd.nextDouble() * (upperBorder - lowerBorder) + lowerBorder);

        lowerBorder = allShips.get(currentShipIndex).getLength() - 1;
        upperBorder = allCellsMatrix[0].length - 1;
        int columnCoord = (int) (rnd.nextDouble() * (upperBorder - lowerBorder) + lowerBorder);
        boolean isDirectionUp = ((int) (rnd.nextDouble() * 10) % 2 == 0);

        // Checking if ship can be inserted at current coordinates.
        for (int i = 0; i < allShips.get(currentShipIndex).getLength(); ++i) {
            Cell currentInsertionCell;
            if (isDirectionUp) {
                currentInsertionCell = allCellsMatrix[rowCoord - i][columnCoord];
            } else {
                currentInsertionCell = allCellsMatrix[rowCoord][columnCoord - i];
            }
            if (areThereAnyNeighbours(currentInsertionCell)) {
                canBeInserted = false;
                break;
            }
        }
        Ship currentShip = allShips.get(currentShipIndex);
        // Actually inserting the ship if possible.
        if (canBeInserted) {
            for (int i = 0; i < currentShip.getLength(); ++i) {
                Cell currentInsertionCell;
                if (isDirectionUp) {
                    currentInsertionCell = allCellsMatrix[rowCoord - i][columnCoord];
                } else { // Direction is left.
                    currentInsertionCell = allCellsMatrix[rowCoord][columnCoord - i];
                }
                currentInsertionCell.setAssignedShipDeck(currentShip.allDecks[i]);
                currentInsertionCell.setStatus(Cell.CellStatus.NOT_SHOT);
                currentShip.allDecks[i].setAssignedCell(currentInsertionCell);
            }
        }
        return canBeInserted;
    }


    /**
     * checks 9 cells around (including the cell itself) if there are already shipDecks
     * assigned to it.
     * @param cell given cell.
     * @return true if assigned cells are found, false otherwise.
     */
    boolean areThereAnyNeighbours(Cell cell) {
        int row = cell.getRow_coordinate();
        int column = cell.getColumn_coordinate();
        for (int i = row - 1; i <= row + 1; ++i) {
            for (int j = column - 1; j <= column + 1; ++j) {
                if (i < getRowsAmount() && i >= 0 &&
                        j < getColumnsAmount() && j >= 0) {
                    if (allCellsMatrix[i][j].getAssignedShipDeck() != null){
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Prints all the cell statuses in user-friendly format (as a table).
     */
    public void displayField() {
        System.out.print("\n   ");
        for (int i = 0; i < getColumnsAmount(); ++i) {
            if ((i < 10)) {
                System.out.print(String.format("%d  ", i));
            } else {
                System.out.print(String.format("%d ", i));
            }
        }
        for (int i = 0; i < getRowsAmount(); ++i) {
            System.out.println();
            if (i < 10) {
                System.out.print(String.format("%d  ", i));
            } else {
                System.out.print(String.format("%d ", i));
            }
            for (int j = 0; j < getColumnsAmount(); ++j) {
                if (Main.isTestingModeOn &&
                        getCellStatus(i, j) == Cell.CellStatus.NOT_SHOT &&
                        allCellsMatrix[i][j].getAssignedShipDeck() != null) {
                    System.out.print("#");
                } else {
                    System.out.print(getCellStatus(i, j));
                }
                System.out.print("  ");
            }
        }
    }

}
