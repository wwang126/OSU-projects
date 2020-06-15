package models;

import org.junit.Test;

import static org.junit.Assert.*;

public class testCardCollection{

    @Test
    public void testAdd(){
        CardCollection col = new CardCollection(0);
        Card c = new Card(5,Suit.Clubs, DeckType.Standard);
        col.add(c);
        assertEquals(col.size(), 1);
    }

    @Test
    public void testRemove(){
        CardCollection col = new CardCollection(1);
        Card c = new Card(5,Suit.Clubs, DeckType.Standard);
        col.add(c);
        assertEquals(col.size(), 1);
        col.remove(0);
        assertEquals(col.size(), 0);
    }

    @Test
    public void testPop(){
        CardCollection c = new CardCollection();
        for(int i = 0; i < 6; i++){
            Card cIn = new Card(i,Suit.Clubs,DeckType.Standard);
            c.add(cIn);
        }
        assertEquals(6,c.size());
        c.pop();
        assertEquals(5,c.size());

    }
    @Test
    public void testCopyConstructor(){
        CardCollection c1 = new CardCollection();
        for(int i = 0; i < 6; i++){
            Card card = new Card(i,Suit.Clubs,DeckType.Standard);
            c1.add(card);
        }
        CardCollection c2 = new CardCollection(c1);
        for(int j = 0; j < 6; j++){
            assertEquals(c1.get(j),c2.get(j));
        }
    }


}