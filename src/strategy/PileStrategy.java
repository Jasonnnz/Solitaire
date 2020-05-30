package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * A pile strategy represents a collection of algorithms used to control a game.
 */
public interface PileStrategy {
	/**
	 * Returns the initial setup of a collection of piles using the provided
	 * deck. If initialization does not require a deck, it is ignored and may be {@code null}.
	 * @param deck - the deck used to initialize the setup.
	 * @return the initial setup of the collection of piles.
	 */
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck);
	
	/**
	 * Determines whether it is legal to move this card onto the top card of this pile.
	 * If this pile is empty, the top card should be {@code null}.
	 * @param card - the card being added to the pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 */
	public boolean isAddingLegal(Card card, Card top);
	
	/**
	 * Determines whether it is legal to remove the top card from this pile.
	 * If this pile is empty, the top card should be {@code null}.
	 * And it is impossible to remove a card from an empty pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(Card top);
	
	/**
	 * Determines whether it is legal to remove the top card from this pile.
	 * If this pile is empty, the top card should be {@code null}.
	 * And it is impossible to remove a card from an empty pile.
	 * @param top       - the top card of this pile. {@code null} represents an empty pile.
	 * @param otherTops - the top cards of other piles. An {@code null} entry indicates an
	 * empty pile.
	 * @param dest      - the destination pile of the top card.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(Card top, Card[] otherTops, int dest);
}