package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Assignment 1: Each of the blank methods below require implementation to get AcesUp to build/run
 */
public class Game {

    public Deck deck;

    public java.util.List<CardCollection> cols = new ArrayList<>(4);


    public Game() {
        // initialize a new game such that each column can store cards
        for (int i = 0; i < 4; i++) {
            this.cols.add(i, new CardCollection());
        }
    }
    /*
     * Swaps between game types
     */
    public void swapDeck(DeckType deckType){
        if(deckType == DeckType.Standard){
            deck = new Deck();
        }
        if(deckType == DeckType.Spanish){
            deck = new SpanishDeck();
        }
        this.cols = new ArrayList<>(4);
        // initialize a new game such that each column can store cards
        for (int i = 0; i < 4; i++) {
            this.cols.add(i, new CardCollection());
        }
    }

    /*
    buildDeck creates a new deck object
     */
    public void buildDeck(){
        deck = new Deck();
    }

    /*
    shuffle uses the deck class to shuffle the deck
     */
    public void shuffle(){
        deck.shuffle();
    }

	/*
	Deal Four does the equivalent of drawing four cards and then setting them on each holding column.
	*/
    public void dealFour() {
        Card tempCard; //creates variable as object type card
        //loop to place a card in each column
        if(deck.size() < 4){
            for(int i=0; i<2; i++) {
                //Draw card from deck
                tempCard = deck.get(0);
                deck.remove(0);
                addCardToCol(i,tempCard); //places card in column
            }
            return;
        }
        for(int i=0; i<4; i++) {
            //Draw card from deck
            tempCard = deck.get(0);
            deck.remove(0);
            addCardToCol(i,tempCard); //places card in column
        }
    }
	
	
	//customDeal to setup game for testing purposes (i.e. shuffled cards are random and hard to test)
    public void customDeal(int c1, int c2, int c3, int c4) {
        Card tempCard0 = new Card(c1,Suit.Clubs, DeckType.Standard);
        addCardToCol(0,tempCard0);
        //deck.remove(c1);
        Card tempCard1 = new Card(c2,Suit.Clubs, DeckType.Standard);
        addCardToCol(1,tempCard1);
        //deck.remove(c2);
        Card tempCard2 = new Card(c3,Suit.Clubs, DeckType.Standard);
        addCardToCol(2,tempCard2);
        //deck.remove(c3);
        Card tempCard3 = new Card(c4,Suit.Clubs, DeckType.Standard);
        addCardToCol(3,tempCard3);
        //deck.remove(c4);
    }

    //This removes the card based on the rules of aces high
    public void remove(int columnNumber) {
        Card c = getTopCard(columnNumber);
        boolean removeCard = false;
        boolean joker = false;
        int jokerIdx = -1;
        //Go thru columns to see if there is a larger card of same suit
        for (int i = 0; i < 4; i++) {
            if (i != columnNumber) {
                if (columnHasCards(i)) {
                    Card compare = getTopCard(i);
                    if (compare.getSuit() == c.getSuit()) {
                        if (compare.getValue() > c.getValue()) {
                            removeCard = true;
                        }
                    }
                    //If joker is found, remove Joker and set remove card to true
                    if (Suit.Joker.equals(compare.getSuit())){
                        if (compare.getValue() >= c.getValue()) {
                            joker = true;
                            jokerIdx = i;
                        }
                    }
                }
            }
        }
        //If there is, remove the card
        if (removeCard) {
            this.cols.get(columnNumber).remove(this.cols.get(columnNumber).size() - 1);
        }
        //If there isn't a larger card but there is a joker, use joker rule
        else if (joker){
            this.cols.get(columnNumber).remove(this.cols.get(columnNumber).size() - 1);
            this.cols.get(jokerIdx).remove(this.cols.get(jokerIdx).size() - 1);
        }
    }


    //If column has cards, return true, otherwise false
    protected boolean columnHasCards(int columnNumber) {
        // if empty return true, else false
        if(this.cols.get(columnNumber).size()>0){
            return true;
        }
        return false;
    }

    protected Card getTopCard(int columnNumber) {
        return this.cols.get(columnNumber).get(this.cols.get(columnNumber).size()-1);
    }

    //Moves a card from one column to another
    public void move(int columnFrom, int columnTo) {
        // remove the top card from the columnFrom column, add it to the columnTo column
        // save top card
        Card topCard = getTopCard(columnFrom);
        // remove top card
        removeCardFromCol(columnFrom);
        //Add card to new column
        addCardToCol(columnTo , topCard);
    }

    protected void addCardToCol(int columnTo, Card cardToMove) {
        cols.get(columnTo).add(cardToMove);
    }

    private void removeCardFromCol(int colFrom) {
        this.cols.get(colFrom).remove(this.cols.get(colFrom).size()-1);
    }
}
