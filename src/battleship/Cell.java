package battleship;

/**
 * Represents a single cell of a battle field. It might contain a
 * ship deck (which means that it is assigned to some cell).
 */
public class Cell {
    private int row_coordinate;
    private int column_coordinate;
    private ShipDeck assignedShipDeck;
    // Indicates whether user shot the cell and the result of the shot.
    private CellStatus status;


    public Cell(int row, int column) {
        row_coordinate = row;
        column_coordinate = column;
        assignedShipDeck = null;
        status = CellStatus.NOT_SHOT;
    }
    // Contains all possible statuses of each cell and
    // their string interpretations in the field matrix.
    public static enum CellStatus {
        NOT_SHOT("."),
        MISSED_SHOT("o"),
        WOUND_SHOT("+"),
        KILL_SHOT("X");

        private final String stringValue;

        CellStatus(final String s) {
            stringValue = s;
        }

        public String toString() {
            return stringValue;
        }
    }

    public CellStatus getStatus() {
        return status;
    }

    public void setStatus(CellStatus status) {
        this.status = status;
    }

    public void setAssignedShipDeck(ShipDeck assignedShipDeck) {
        this.assignedShipDeck = assignedShipDeck;
    }

    public ShipDeck getAssignedShipDeck() {
        return assignedShipDeck;
    }

    public int getRow_coordinate() {
        return row_coordinate;
    }

    public int getColumn_coordinate() {
        return column_coordinate;
    }
}
