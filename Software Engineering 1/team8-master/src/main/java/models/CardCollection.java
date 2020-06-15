package models;

import java.util.ArrayList;

/**
 * This is the card collection class that implements a collection of cards
 */
public class CardCollection {

    //Internal array list for storing cards
    public java.util.List<Card> cards;

    //Int that points to the top of the stack
    private int topIndex;

    /**
     * Standard empty constructor that creates an empty arraylist.
     */
    public CardCollection(){
        cards = new ArrayList<Card>();
        topIndex = 0;
    }

    /**
     * Create a constructor that creates a card collection with a certain size
     *
     * @param int size of the card collection
     */
    public CardCollection (int size){
        cards = new ArrayList<Card>(size);
        topIndex = 0;
    }

    /**
     * Copy constructor for CardCollection that creates a copy of another CardConstructor object
     *
     * @param CardCollection the card collection to copy in
     */
    public CardCollection (CardCollection collectionIn){
        topIndex = 0;
        int size = collectionIn.size();
        topIndex = size - 1;
        cards = new ArrayList<Card>();
        for(int i = 0;i < size; i++){
            this.add(collectionIn.get(i));
        }
    }


    /**
     * Adds a card to the card collection
     *
     * @param Card card to add
     */
    public void add(Card cardIn){
        cards.add(cardIn);
        topIndex++;
    }

    /**
     * Removes top card if successful return a 1 else 0
     */
    public int pop(){
        cards.remove(topIndex-1);
        topIndex--;
        return 1;
    }

    /**
     * Returns the number of cards in CardCollection
     */
    public int size (){
        return cards.size();
    }

    /**
     * Returns the card at the given index, if index is out of bounds, return null
     *
     * @param int index of card to return
     */
    public Card get(int index){
        return cards.get(index);
    }

    /**
     * Removes a card at the given index
     */
    public void remove (int index){
        cards.remove(index);
    }

    /**
     * Copy of the cards.tostring
     * @return
     */
    public String toString(){
        return cards.toString();
    }
}
