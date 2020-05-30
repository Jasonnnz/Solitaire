package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;
import model.Piles;

public class AcesUpStockStrategy implements PileStrategy {
	/**
	 * When the game begins the stock pile will contain the entire deck 
	 * except those cards dealt to the tableau piles.
	 * @param deck - entire deck except those cards dealt to the tableau piles.
	 * @throws IllegalArgumentException if deck.size() != 48
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		if(deck.size() != 48) {
			throw new IllegalArgumentException();
		}
		
		ArrayList<Stack<Card>> a = new ArrayList<Stack<Card>>();
		Stack<Card> s = new Stack<Card>();
		
		ArrayList<Card> remainingCards = deck.getCards(48);
		for(int i = 0; i < 48; i++) {
			s.push(remainingCards.remove(remainingCards.size() - 1));
		}
		a.add(s);
		return a;
	}

	/**
	 * It is impossible to add a card to a Stock pile.
	 * @param card - this parameter is ignored.
	 * @param top  - this parameter is ignored.
	 * @return {@code false}.
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		return false;
	}

	/**
	 * This operation is unsupported.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(Card top) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * It is always okay to remove a card from a Stock pile if the dest is a Tableau pile.
	 * It is impossible to remove a card from an empty pile.
	 * @param top       - the top card of this pile.
	 * @param otherTops - this parameter is ignored.
	 * @param dest      - the destination pile.
	 */
	@Override
	public boolean isRemovingLegal(Card top, Card[] otherTops, int dest) {
		return top != null && dest == Piles.TABLEAU;
	}
}
