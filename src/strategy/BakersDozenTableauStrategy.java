package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * Instances of this class contain algorithms necessary for a Tableau pile in a Baker's Dozen game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
public class BakersDozenTableauStrategy implements PileStrategy {
	/**
	 * Returns the initial setup of Baker's Dozen's Tableau piles using the provided deck.
	 * When the game begins each Tableau pile should be dealt 4 cards.
	 * After dealing is completed,
	 * any Kings should be moved to the bottom of their tableau. 
	 * And there should be 13 Tableau piles
	 * @param deck - the deck used to initialize the setup.
	 * @return the initial setup of the Tableau piles in a Baker's Dozen game.
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> piles = new ArrayList<Stack<Card>>();
		for(int i = 0; i < 13; i++) {
			Stack<Card> pile = moveKingsDown(deck.getCards(4));
			piles.add(pile);
		}
		
		return piles;
	}

	/**
	 * A card can be added to a Tableau pile when its value is one less than the Tableau's top card
	 * (suits do not matter for this).
	 * For example, it is legal to move a Queen onto a King, a 6 onto a 7, or an Ace onto a 2,
	 * but illegal to move a 4 onto a 6 or a Jack onto a 10.
	 * Cards cannot be added to an empty Tableau pile.
	 * @param card - the card being added to the Tableau pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 * @throws IllegalArgumetException if {@code card} is {@code null}
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		if(card == null) {
			throw new IllegalArgumentException();
		}
		
		return top != null && top.getRank() - card.getRank() == 1;
	}

	/**
	 * Only the card which is currently at the top of the Tableau pile can be removed.
	 * Once a card is removed, the card following it in the pile becomes the top card and can be removed.
	 * And it is impossible to remove a card from an empty pile.
	 * @param top - the top card of this pile. {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 */
	@Override
	public boolean isRemovingLegal(Card top) {
		return top != null;
	}

	/**
	 * Moves any kings to the bottom of their piles.
	 * @param cards - the pile that needs this special handling.
	 * @return an {@code Stack} representing a pile with its kings moved to the bottom.
	 */
	private Stack<Card> moveKingsDown(ArrayList<Card> cards) {
		Stack<Card> pile = new Stack<Card>();
		int size = cards.size();
		for(int i = 0; i < size; i++) {
			Card card = cards.get(i);
			if(card.getRank() == 13) {
				pile.push(card);
				cards.remove(i);
				size -= 1;
				i -= 1;
			}
		}
		pile.addAll(cards);
		return pile;
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
