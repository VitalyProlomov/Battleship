package battleship;

public class BattleShip extends Ship {

    public BattleShip() {
        allDecks = new ShipDeck[4];
        for (int i = 0; i <allDecks.length; ++i) {
            allDecks[i] = new ShipDeck(this);
        }
    }

    public BattleShip(ShipDeck[] decks) {
        super(decks);
        if (decks.length != 1) {
            throw new IllegalArgumentException("Amount of decks must be in 1 for BattleShip");
        }
        allDecks = new ShipDeck[4];
        for (int i = 0; i < decks.length; ++i) {
            allDecks[i] = decks[i];
        }
    }

    @Override
    public String toString() {
        return "Battleship";
    }
}
