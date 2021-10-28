package battleship;

public class Carrier extends Ship {

    public Carrier() {
        allDecks = new ShipDeck[5];
        for (int i = 0; i <allDecks.length; ++i) {
            allDecks[i] = new ShipDeck(this);
        }
    }

    public Carrier(ShipDeck[] decks) {
        super(decks);
        if (decks.length != 5) {
            throw new IllegalArgumentException("Amount of decks must be in 5 for Cruiser");
        }
        allDecks = new ShipDeck[5];
        for (int i = 0; i < decks.length; ++i) {
            allDecks[i] = decks[i];
        }
    }

    @Override
    public String toString() {
        return "Carrier";
    }
}
