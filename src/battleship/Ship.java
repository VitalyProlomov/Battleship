package battleship;


public abstract class Ship {
    protected ShipDeck[] allDecks;
    protected boolean isSunk;

    public Ship() {

    }


    public Ship(ShipDeck[] decks) {
        if (decks == null) {
            throw new IllegalArgumentException("Null array can not be used as a parameter.");
        }
        isSunk = false;
    }

    //Handles taking a shot in a shipDeck, entered as a parameter
    // and updates information whether the ship is sunk or not.
    public void handleShot(ShipDeck shipDeck, boolean isTorpedoShot) {
        boolean doesShipContainDeck = false;
        for (ShipDeck deck : allDecks) {
            if (shipDeck == deck) {
                doesShipContainDeck = true;
            }
        }
        if (!doesShipContainDeck) {
            throw new IllegalArgumentException("This ship does not " +
                    "contain entered shipDeck.");
        }
        if (isTorpedoShot) {
            this.sink();
            updateSunkStatus();
            return;
        }
        shipDeck.sink();
        for (int i = 0; i < allDecks.length; ++i) {
            if (!allDecks[i].getIsSunk()) {
                isSunk = false;
                shipDeck.getAssignedCell().setStatus(Cell.CellStatus.WOUND_SHOT);
                return;
            }
        }
        isSunk = true;
        updateSunkStatus();
    }

    /**
     * If the ship is sunk => all cells have status WOUND_SHOT,
     * so they must all be changed to KILL_SHOT, since the ship is now sunk.
     */
    protected void updateWoundCellStatuses() {
        if (this.isSunk) {
            for (ShipDeck deck : allDecks) {
                deck.getAssignedCell().setStatus(Cell.CellStatus.KILL_SHOT);
            }
        }
    }

    public boolean getIsSunk() {
        for (ShipDeck alldeck : allDecks) {
            if (!alldeck.getIsSunk()) {
                isSunk = false;
                return false;
            }
        }
        isSunk = true;
        return true;
    }

    public int getLength() {
        return allDecks.length;
    }

    public void sink() {
        for (int i = 0; i < allDecks.length; ++i) {
            allDecks[i].sink();
        }
        isSunk = true;
    }

    /**
     * Checks all the shipDecks if they are sunk or not, updates
     * sunk status of the ship itself.
     */
    public void updateSunkStatus() {
        for (int i = 0; i < allDecks.length; ++i) {
            if (!allDecks[i].getIsSunk()) {
                isSunk = false;
                return;
            }
        }
        isSunk = true;
        updateWoundCellStatuses();
    }

    public void recover() {
        for (int i = 0; i < allDecks.length; ++i) {
            allDecks[i].recover();
        }
        isSunk = false;
    }

    abstract public String toString();
}
