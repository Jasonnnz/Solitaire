package model;

import java.util.ArrayList;
import java.util.Collections;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;

/**
 * Instances of the class represent decks of cards.
 */
public class Deck {
	/**
	 * _deck - cards in this deck.
	 */
	private ArrayList<Card> _deck;
	
	/**
	 * Creates a new deck with 52 unique cards in random order.
	 */
	public Deck() {
		_deck = new ArrayList<Card>();
		for (Suit s : Suit.values()) {
			for (Rank r : Rank.values()) {
				_deck.add(new Card(s, r));
			}
		}
		Collections.shuffle(_deck);
	}

	/**
	 * Remove n cards in the deck and return an {@code ArrayList} that contains those cards.
	 * @param n the number of cards to remove
	 * @return an {@code ArrayList} that contains those removed n cards
	 * @throws <code>IllegalArgumentException</code> if argument is less than or equal to 0
	 * @throws <code>IndexOutOfBoundsException</code> if n is greater than the number of cards that this deck owns
	 */
	public ArrayList<Card> getCards(int n) {
		if(n <= 0){
			throw new IllegalArgumentException();
		}
		
		ArrayList<Card> ac = new ArrayList<Card>();
		for (int i = 0; i < n; i++) {
			ac.add(_deck.remove(0));
		}
		return ac;
	}
	
	/**
	 * Returns the size of this deck.
	 * @return the size of this deck.
	 */
	public int size() {
		return _deck.size();
	}
}