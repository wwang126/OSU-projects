package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Assignment 1: No changes are needed in this file, but it is good to be aware of for future assignments.
 */

public class Card implements Serializable {
    public final int value;
    public final Suit suit;
    public final DeckType deck;
    public final String img;

    @JsonCreator
    public Card(@JsonProperty("value") int value, @JsonProperty("suit") Suit suit, @JsonProperty("deck") DeckType deck) {
        this.value = value;
        this.suit = suit;
        this.deck = deck;
        int x;
        if (value == 14){
          x = 1;
        } else {
          x = value;
        }
        this.img = "../assets/CardSets/" + deck + "/" + (x + "_of_" + suit + ".png").toLowerCase();
    }

    public Suit getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return this.value + this.suit.toString();
    }
}
