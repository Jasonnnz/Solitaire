package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * Instances of this class contain algorithms necessary for a Freecell pile in a Freecell game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
public class FreecellFreecellStrategy implements PileStrategy {

	/**
	 * Returns the initial setup of Freecell's Freecell piles.
	 * Since it does not require a deck to initialize, {@code deck} is ignored and may be {@code null}.
	 * When the game begins each Free pile should be empty. And there should be 4 Freecell piles.
	 * @param deck - the deck used to initialize the setup. It may be {@code null}.
	 * @return the initial setup of the Freecell piles in a Freecell game.
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
	 * Any card can be added to an EMPTY Freecell pile.
	 * A card cannot be added to a Freecell pile that already has a card.
	 * @param card - the card being added to the Freecell pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 * @throws IllegalArgumentException is {@code card} is {@code null}
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		if(card == null) {
			throw new IllegalArgumentException();
		}
		return top == null;
	}

	/**
	 * Cards can always be remed from a Freecell pile.
	 * But it is impossible to remove a card from an empty pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 */
	@Override
	public boolean isRemovingLegal(Card top) {
		return top != null;
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
