package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import abstraction.Rank;
import model.Deck;
import model.Piles;

/**
 * Instances of this class contain necessary algorithms for a Tableau pile in a Ace's Up game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
public class AcesUpTableauStrategy implements PileStrategy {
	/**
	 * Returns the initial setup of Ace's Up's a Tableau pile using the provided deck.
	 * When the game begins each Tableau will be dealt 1 card. No cards need special
	 * handling. And there should be 4 Tableau piles.
	 * @param deck - the deck used to initialize the setup.
	 * @return the initial setup of a Tableau pile in an Ace's Up game.
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> piles = new ArrayList<Stack<Card>>();
		
		for(int i = 0; i < 4; i++) {
			Stack<Card> pile = new Stack<Card>();
			pile.push(deck.getCards(1).get(0));
			piles.add(pile);
		}
		return piles;
	}

	/**
	 * A card can be added to an empty Tableau pile at any time.
	 * @param card - the card to be added to the pile.
	 * @param top  - the top card of this pile, {@code null} represents an empty pile.
	 * @return whether this operation is legal.
	 * @throws IllegalArgumentException if card is {@code null}.
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		if(card == null) {
			throw new IllegalArgumentException();
		}
		
		return top == null;
	}

	/**
	 * This operation is not supported by this pile.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(Card top) {
		throw new UnsupportedOperationException();
	}

	/**
	 * A top card can be removed whenever another Tableau's top card has the same suit but
	 * with a higher rank. Ace is the highest valued card in each suit.
	 * A card cannot be removed from an empty pile.
	 * @param top       - the top card of this pile.
	 * @param otherTops - the top cards of other Tableau piles.
	 * @param dest      - the destination pile of the top card.
	 * @throws IllegalArgumentException if otherTops.length != 3 || dest is doesn't correspond to a legal pile.
	 */
	@Override
	public boolean isRemovingLegal(Card top, Card[] otherTops, int dest) {
		if(otherTops.length != 3 || (dest != Piles.TABLEAU && dest != Piles.HOMECELL)) {
			throw new IllegalArgumentException();
		}
		
		if(top == null) {
			return false;
		}
		
		if(dest == Piles.TABLEAU) {
			for(int i = 0; i < 3; i++) {
				if(otherTops[i] == null) {
					return true;
				}
			}
		} else {
			for(int i = 0; i < 3; i++) {
				if(otherTops[i] != null) {
					if(otherTops[i].getSuit().equals(top.getSuit())) {
						if(top.getRank() == Rank.ACE.rank()) {
							return false;
						}
						if(otherTops[i].getRank() == Rank.ACE.rank()) {
							return true;
						} else if(otherTops[i].getRank() > top.getRank()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
