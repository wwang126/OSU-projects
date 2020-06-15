package models;

import org.junit.Test;

import static org.junit.Assert.*;

//This tests out the suits.java
public class testCard {
    @Test
    public void testGetSuit(){
        Card c = new Card(5,Suit.Clubs, DeckType.Standard);
        assertEquals(Suit.Clubs,c.getSuit());
         Card h = new Card(5,Suit.Hearts, DeckType.Standard);
        assertEquals(Suit.Hearts,h.getSuit());
         Card s = new Card(5,Suit.Spades, DeckType.Standard);
        assertEquals(Suit.Spades,s.getSuit());
         Card d = new Card(5,Suit.Diamonds, DeckType.Standard);
        assertEquals(Suit.Diamonds,d.getSuit());
    }
//test tostring function
    @Test
    public void testToString(){
        Card c = new Card(5,Suit.Clubs, DeckType.Standard);
        assertEquals("5Clubs",c.toString());
        Card h = new Card(7,Suit.Hearts, DeckType.Standard);
        assertEquals("7Hearts",h.toString());
        Card s = new Card(3,Suit.Spades, DeckType.Standard);
        assertEquals("3Spades",s.toString());
        Card d = new Card(4,Suit.Diamonds, DeckType.Standard);
        assertEquals("4Diamonds",d.toString());
    }
}
