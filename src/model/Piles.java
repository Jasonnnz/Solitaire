package model;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import strategy.PileStrategy;

/**
 * A collection of piles that can be initialized and controlled using different algorithms.
 */
public class Piles {
	/**
	 * public constants
	 */
	public static final int TABLEAU  = 0;
	public static final int HOMECELL = 1;
	public static final int FREECELL = 2;
	public static final int STOCK    = 2; // bad idea but whatever
	
	/**
	 * _piles - collection of piles.
	 * _pileStrategy - the algorithms to use for this pile.
	 */
	private ArrayList<Stack<Card>> _piles;
	private PileStrategy _pileStrategy;

	/**
	 * Creates a {@code Piles} instance and initialize it using the given algorithm.
	 * @param strategy - algorithms which will be used to initialize and control this {@code Piles} instance.
	 */
	public Piles(PileStrategy strategy, Deck deck) {
		_pileStrategy = strategy;
		_piles = strategy.getInitialSetUp(deck);
	}
	
	/**
	 * Determines whether this operation is legal.
	 * @param card - card to be added to the pile.
	 * @param pos - the position of the pile.
	 * @return whether this operation is legal.
	 */
	public boolean isAddingLegal(Card card, int pos) {
		return _pileStrategy.isAddingLegal(card, getTopCard(pos));
	}

	/**
	 * Determines whether this operation is legal. If it is legal {@code card} is added to the top of the specified pile.
	 * @param card - card to be added to the pile.
	 * @param pos - the position of the pile.
	 * @return whether this operation is legal.
	 */
	public boolean addCard(Card card, int pos) {
		if(isAddingLegal(card, pos)) {
			_piles.get(pos).push(card);
			return true;
		}
		return false;
	}
	
	/**
	 * Addes a card to the specified pile bypassing any legality check.
	 * @param card - the card to be added.
	 * @param pos  - the position of the pile.
	 */
	public void forceAddCard(Card card, int pos) {
		_piles.get(pos).push(card);
	}
	
	/**
	 * Determines whether this operation is legal.
	 * @param pos - the position of the pile.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(int pos) {
		return _pileStrategy.isRemovingLegal(getTopCard(pos));
	}
	
	/**
	 * Determines whether removing a card is legal.
	 * @param pos  - the position of the pile.
	 * @param dest - the destination pile of the removed card.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(int pos, int dest) {
		return _pileStrategy.isRemovingLegal(getTopCard(pos), getOtherTopCards(pos), dest);
	}

	/**
	 * Determines whether this operation is legal, If it is, remove the top card from the specified pile.
	 * @param pos - the position of the pile
	 * @return whether this operation is legal
	 */
	public boolean removeCard(int pos) {
		if(isRemovingLegal(pos)) {
			_piles.get(pos).pop();
			return true;
		}
		return false;
	}
	
	/**
	 * Determines whether this operation is legal, If it is, remove the top card from the specified pile.
	 * @param pos  - the position of the pile.
	 * @param dest - the destination of the removed card.
	 * @return whether this operation is legal.
	 */
	public boolean removeCard(int pos, int dest) {
		if (isRemovingLegal(pos, dest)) {
			_piles.get(pos).pop();
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns the top card of the specified pile. Nothing will be modified after this method call.
	 * @param pos - position of the pile.
	 * @return the top card of the specified pile.
	 */
	public Card getTopCard(int pos) {
		Stack<Card> pile = _piles.get(pos);
		if(pile.isEmpty()) {
			return null;
		}
		return pile.peek();
	}
	
	/**
	 * Returns the top cards of piles other than the specified one.
	 * @param pos - position of the pile.
	 * @return the top cards of other piles.
	 */
	public Card [] getOtherTopCards(int pos) {
		Card [] otherTops = new Card[_piles.size() - 1];
		int pileCounter = 0;
		for(int i = 0; i < _piles.size(); i++) {
			if(i != pos) {
				otherTops[pileCounter] = getTopCard(i);
				pileCounter += 1;
			}
		}
		return otherTops;
	}

	/**
	 * Return the size of the specified pile
	 * @param pos position of the pile
	 * @return the size of the pile
	 */
	public int size(int pos) {
		return _piles.get(pos).size();
	}
	
	/**
	 * Returns the reference to _piles.
	 * DO NOT USE IT
	 */
	@Deprecated
	public ArrayList<Stack<Card>> getPiles() {
		return _piles;
	}
	
	/**
	 * Sets the values of _piles
	 * DO NOT USE IT
	 */
	@Deprecated
	public void setPiles(Card [] cards) {
		ArrayList<Stack<Card>> piles = new ArrayList<Stack<Card>>();
		for(int i = 0; i < 4; i++) {
			Stack<Card> pile = new Stack<Card>();
			pile.push(cards[i]);
			piles.add(pile);
		}
		_piles = piles;
	}
	
	/**
	 * Test helper.
	 * DO NOT USE IT
	 */
	@Deprecated
	public PileStrategy getPileStrategy() {
		return _pileStrategy;
	}
}
