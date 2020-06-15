package models;

import java.util.ArrayList;

public class SpanishDeck extends Deck {
    public SpanishDeck(){
        cards = new ArrayList<Card>();
        for(int i = 2; i < 15; i++){
            if(i != 13){
                cards.add(new Card(i, Suit.Clubs, DeckType.Spanish));
                cards.add(new Card(i, Suit.Cups, DeckType.Spanish));
                cards.add(new Card(i, Suit.Coins, DeckType.Spanish));
                cards.add(new Card(i, Suit.Swords, DeckType.Spanish));
            }
        }
        //Remove 10th card and add the jokers
        cards.add(new Card(13,Suit.Joker, DeckType.Spanish));
        cards.add(new Card(13,Suit.Joker, DeckType.Spanish));
    }
}
