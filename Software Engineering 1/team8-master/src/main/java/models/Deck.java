package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck extends CardCollection{

    public Deck(){
        cards = new ArrayList<Card>();
        for(int i = 2; i < 15; i++){
            cards.add(new Card(i, Suit.Clubs, DeckType.Standard));
            cards.add(new Card(i, Suit.Hearts, DeckType.Standard));
            cards.add(new Card(i, Suit.Diamonds, DeckType.Standard));
            cards.add(new Card(i, Suit.Spades, DeckType.Standard));
        }
    }

    /*
    shuffles the deck
    */
    public void shuffle(){
        long seed = System.nanoTime();
        Collections.shuffle(cards, new Random(seed));
    }

}
