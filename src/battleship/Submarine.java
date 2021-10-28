package battleship;

public class Submarine extends Ship{

    public Submarine() {
        allDecks = new ShipDeck[1];
        allDecks[0] = new ShipDeck(this);
    }

    public Submarine(ShipDeck[] decks) {
        super(decks);
        if (decks.length != 1) {
            throw new IllegalArgumentException("Amount of decks must be in 1 for Cruiser");
        }
        allDecks = new ShipDeck[1];
        for (int i = 0; i < decks.length; ++i) {
            allDecks[i] = decks[i];
        }
    }

    public String toString() {
        return "Submarine";
    }
}
