package battleship;

public class ShipDeck {
    private boolean isSunk;
    private int rowCoordinate;
    private int columnCoordinate;
    private Ship assignedShip;
    // Needed for convenient update of cell statuses when the assigned ship is sunk.
    // Represents the cell on the field where there deck is located.
    private Cell assignedCell;

    public ShipDeck(Ship ship) {
        isSunk = false;
        assignedShip = ship;
    }

    public ShipDeck(int row, int column) {
        rowCoordinate = row;
        columnCoordinate = column;
        isSunk = false;
        assignedShip = null;
    }


    public int getRow() {
        return rowCoordinate;
    }

    public int getColumn() {
        return columnCoordinate;
    }

    public boolean getIsSunk() {
        return isSunk;
    }

    public Ship getAssignedShip() {
        return assignedShip;
    }

    public Cell getAssignedCell() {
        return assignedCell;
    }

    public void setAssignedCell(Cell cell) {
        this.rowCoordinate = cell.getRow_coordinate();
        this.columnCoordinate = cell.getColumn_coordinate();
        this.assignedCell = cell;
        cell.setAssignedShipDeck(this);
    }

    public void sink() {
        isSunk = true;
    }

    void recover() {
        isSunk = false;
    }

    public void takeTorpedoHit() {
        assignedShip.sink();
    }

    void assignToShip(Ship ship) {
        assignedShip = ship;
    }

}
