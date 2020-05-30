package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * Instances of this class contain algorithms necessary for a Homecell pile in both Baker's Dozen and Freecell game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
public class HomecellStrategy implements PileStrategy {
	/**
	 * Returns the initial setup of Homecell piles.
	 * Since it does not require a deck to initialize, {@code deck} is ignored and may be {@code null}.
	 * When the game begins each Homecell pile should empty, and there should be 4 Homecell piles.
	 * @param deck the deck used to initialize
	 * @return the initial setup of the Homecell piles.
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> piles = new ArrayList<Stack<Card>>();
		for(int i = 0; i < 4; i++) {
			Stack<Card> pile = new Stack<Card>();
			piles.add(pile);
		}
		
		return piles;
	}

	/**
	 * A card can be added to a Homecell pile if it has the identical suit
	 * and a value one more than the Homecell's top card.
	 * For example, the Queen of Spades can only be added to a homecell with the Jack of Spades as its top card.
	 * The added card becomes the homecell's new top card. Only the Aces can be added to an empty homecell.
	 * @param card - the card being added to the Homecell pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 * @throws IllegalArgumentException if {@code card} is {@code null}
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		if(card == null) {
			throw new IllegalArgumentException();
		}
		
		if(top == null) {
			if(card.getRank() == 1) {
				return true;
			} else {
				return false;
			}
		}
		
		return card.getSuit().equals(top.getSuit()) && card.getRank() - top.getRank() == 1;
	}
	
	/**
	 * Cards cannot be removed from a Homecell pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 */
	@Override
	public boolean isRemovingLegal(Card top) {
		return false;
	}
	
	/**
	 * This operation is not supported by this pile.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(Card top, Card[] otherTops, int dest) {
		throw new UnsupportedOperationException();
	}
}
