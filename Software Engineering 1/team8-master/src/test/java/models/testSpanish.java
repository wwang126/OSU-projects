package models;

import org.junit.Test;

import static org.junit.Assert.*;

//This tests out the spanish deck related fucntion
public class testSpanish{
   
    @Test
    public void testGetSuit(){
        Card c = new Card(5,Suit.Clubs,DeckType.Spanish);
        assertEquals(Suit.Clubs,c.getSuit());
        Card h = new Card(5,Suit.Swords, DeckType.Spanish);
        assertEquals(Suit.Swords,h.getSuit());
         Card s = new Card(5,Suit.Coins, DeckType.Spanish);
        assertEquals(Suit.Coins,s.getSuit());
         Card d = new Card(5,Suit.Cups, DeckType.Spanish);
        assertEquals(Suit.Cups,d.getSuit());
    }

    @Test
    public void testDeckSwap(){
        Game g = new Game();
        g.buildDeck();
        g.swapDeck(DeckType.Spanish);
        assertEquals(50,g.deck.size());
        g.swapDeck(DeckType.Standard);
        assertEquals(52,g.deck.size());
    }

    @Test
    public void testJokers(){
        Game g = new Game();
        g.buildDeck();
        g.swapDeck(DeckType.Spanish);
        assertEquals(Suit.Joker,g.deck.get(49).getSuit());
        assertEquals(Suit.Joker,g.deck.get(48).getSuit());
        assertFalse(Suit.Joker.equals(g.deck.get(47).getSuit()));
    }

    @Test
    public void testSpanishDeckSuit(){
        Deck d = new SpanishDeck();
        assertEquals(50,d.size());
        assertEquals(Suit.Clubs,d.get(0).getSuit());
        assertEquals(Suit.Cups,d.get(1).getSuit());
        assertEquals(Suit.Coins,d.get(2).getSuit());
        assertEquals(Suit.Swords,d.get(3).getSuit());
        assertEquals(Suit.Clubs,d.get(44).getSuit());
        assertEquals(Suit.Cups,d.get(45).getSuit());
        assertEquals(Suit.Coins,d.get(46).getSuit());
        assertEquals(Suit.Swords,d.get(47).getSuit());
        assertEquals(Suit.Joker,d.get(48).getSuit());
        assertEquals(Suit.Joker,d.get(49).getSuit());
    }
}
    
