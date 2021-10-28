package battleship;

public class Cruiser extends Ship {

    public Cruiser() {
        allDecks = new ShipDeck[3];
        for (int i = 0; i <allDecks.length; ++i) {
            allDecks[i] = new ShipDeck(this);
        }
    }

    public Cruiser(ShipDeck[] decks) {
        super(decks);
        if (decks.length != 3) {
            throw new IllegalArgumentException("Amount of decks must be in 3 for Cruiser");
        }
        allDecks = new ShipDeck[2];
        for (int i = 0; i < decks.length; ++i) {
            allDecks[i] = decks[i];
        }
    }
    @Override
    public String toString() {
        return "Cruiser";
    }
}
