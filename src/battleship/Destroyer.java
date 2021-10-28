package battleship;

public class Destroyer extends Ship{

    public Destroyer() {
        allDecks = new ShipDeck[2];
        allDecks[0] = new ShipDeck(this);
        allDecks[1] = new ShipDeck(this);
    }

    public Destroyer(ShipDeck[] decks) {
        super(decks);
        if (decks.length != 2) {
            throw new IllegalArgumentException("Amount of decks must be in 2 for Destroyer");
        }
        allDecks = new ShipDeck[2];
        for (int i = 0; i < decks.length; ++i) {
            allDecks[i] = decks[i];
        }
    }


    @Override
    public String toString() {
        return "Destroyer";
    }
}
