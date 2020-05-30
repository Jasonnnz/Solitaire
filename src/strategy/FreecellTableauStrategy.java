package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * Instances of this class contain algorithms necessary for a Tableau pile in a Freecell game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
public class FreecellTableauStrategy implements PileStrategy {
	/**
	 * Returns the initial setup of Freecell's Tableau piles using the provided deck.
	 * When the game begins half of the Tableau piles will be dealt 7 cards
	 * and the other half of the Tableau piles will be dealt 6 cards. No cards need special handling.
	 * And there should be 8 Tableau piles
	 * @param deck - the deck used to initialize the setup.
	 * @return the initial setup of the Tableau piles in a Freecell game.
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> piles = new ArrayList<Stack<Card>>();
		for(int i = 0; i < 8; i++) {
			Stack<Card> pile = new Stack<Card>();
			if(i % 2 == 0) {
				pile.addAll(deck.getCards(6));
			} else {
				pile.addAll(deck.getCards(7));
			}
			piles.add(pile);
		}
		return piles;
	}

	/**
	 * A card can be added to a Tableau pile when its value is one less than the tableau's top card
	 * AND its suit is the opposite of the top card's suit.
	 * For example, it is legal to move a red Queen onto a black King, a black 6 onto a red 7,
	 * or an black Ace onto a red 2,
	 * but illegal to move a black 4 onto a red 6, a black Jack onto a red 10, or a red 3 onto a red 4.
	 * The added card becomes the Tableau's new top card. 
	 * ANY card CAN be added to an empty Tableau.
	 * @param card - the card being added to the Tableau pile.
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
			return true;
		}
		
		return !card.getColor().equals(top.getColor()) && top.getRank() - card.getRank() == 1;
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
	 * This operation is not supported by this pile.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(Card top, Card[] otherTops, int dest) {
		throw new UnsupportedOperationException();
	}
}
