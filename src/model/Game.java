package model;

import java.util.ArrayList;

import abstraction.Card;
import strategy.PileStrategy;

/**
 * A {@code Game} defines all funtionalities of a game.
 */
public abstract class Game {
	/**
	 * A collection of {@code Piles}. It contains all the piles in a game. The first
	 * element must represent Tableau piles, the second element must represent
	 * Homecell piles, and the third element must represent Freecell piles.
	 */
	private ArrayList<Piles> _piles;
	private Deck             _deck;

	public Game(Deck deck, PileStrategy... strategies) {
		_deck  = deck;
		_piles = new ArrayList<Piles>();
		for(int i = 0; i < strategies.length; i++) {
			_piles.add(new Piles(strategies[i], _deck));
		}
	}
	
	/**
	 * Determines whether this operation is legal.
	 * @param card - card to be added to the pile.
	 * @param pile - the type of the pile.
	 * @param pos - the position of the pile.
	 * @return whether this operation is legal.
	 */
	public boolean isAddingLegal(Card card, int pile, int pos) {
		return _piles.get(pile).isAddingLegal(card, pos);
	}
	
	/**
	 * Adds a card to the top of the specified pile with 0 representing Tableau, 1
	 * representing Homecell, and 2 representing Freecell. And returns whether this
	 * operation is successful.
	 * 
	 * @param card
	 *            - card to be added.
	 * @param pile
	 *            - which pile. (0-Tableau, 1-Homecell, 2-Freecell)
	 * @param pos
	 *            - position of the pile.
	 * @return whether this operation is successful.
	 */
	public boolean addCard(Card card, int pile, int pos) {
		return _piles.get(pile).addCard(card, pos);
	}
	
	/**
	 * Adds a card to the top of the specified pile with 0 representing Tableau, 1
	 * representing Homecell, and 2 representing Freecell bypassing any legality check.
	 * @param card - the card to be added.
	 * @param pile - which pile. (0-Tableau, 1-Homecell, 2-Freecell)
	 * @param pos  - the position of the pile.
	 */
	public void forceAddCard(Card card, int pile, int pos) {
		_piles.get(pile).forceAddCard(card, pos);
	}
	
	/**
	 * Default special pile action.
	 */
	public void invokeDefaultSpecialAction() {
		for(int i = 0; i < 4; i++) {
			Card stockTopCard = getTopCard(Piles.STOCK, 0);
			forceAddCard(stockTopCard, Piles.TABLEAU, i);
			removeCard(Piles.STOCK, 0, Piles.TABLEAU);
		}
	}
	
	/**
	 * Determines whether this operation is legal.
	 * @param pile - the type of the pile.
	 * @param pos - the position of the pile.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(int pile, int pos) {
		return _piles.get(pile).isRemovingLegal(pos);
	}
	
	/**
	 * Determines whether this operation is legal.
	 * @param pile - the type of the pile.
	 * @param pos  - the position of the pile.
	 * @param dest - the destination of the removed card.
	 * @return whether this operation is legal.
	 */
	public boolean isRemovingLegal(int pile, int pos, int dest) {
		return _piles.get(pile).isRemovingLegal(pos, dest);
	}

	/**
	 * Removes a card from the top of the specified pile with 0 representing
	 * Tableau, 1 representing Homecell, and 2 representing Freecell. And returns
	 * whether this operation is successful.
	 * 
	 * @param pile
	 *            - which pile. (0-Tableau, 1-Homecell, 2-Freecell)
	 * @param pos
	 *            - position of the pile.
	 * @return whether this operation is successful.
	 */
	public boolean removeCard(int pile, int pos) {
		return _piles.get(pile).removeCard(pos);
	}
	
	/**
	 * Removes a card from the top of the specified pile with 0 representing
	 * Tableau, 1 representing Homecell, and 2 representing Freecell. And returns
	 * whether this operation if successful.
	 * @param pile - the pile type which the card is removed.
	 * @param pos  - the position of the pile.
	 * @param dest - the destination pile.
	 * @return whether this operation is successful.
	 */
	public boolean removeCard(int pile, int pos, int dest) {
		return _piles.get(pile).removeCard(pos, dest);
	}

	/**
	 * Returns the top card of the specified pile with 0 representing Tableau, 1
	 * representing Homecell, and 2 representing Freecell. Nothing will be modified
	 * after this call.
	 * 
	 * @param pile
	 *            - which pile. (0-Tableau, 1-Homecell, 2-Freecell)
	 * @param pos
	 *            - position of the pile.
	 * @return the top card of the specified pile.
	 */
	public Card getTopCard(int pile, int pos) {
		return _piles.get(pile).getTopCard(pos);
	}
	
	/**
	 * Returns the top cards of piles other than the specified pile.
	 * @param pile - the pile type.
	 * @param pos  - position of the pile.
	 * @return the top cards of other piles.
	 */
	public Card[] getOtherTopCards(int pile, int pos) {
		return _piles.get(pile).getOtherTopCards(pos);
	}

	/**
	 * Returns the size of the specified pile with 0 representing Tableau, 1
	 * representing Homecell, and 2 representing Freecell. Nothing will be modified
	 * after this call.
	 * 
	 * @param pile
	 *            - which pile. (0-Tableau, 1-Homecell, 2-Freecell)
	 * @param pos
	 *            - position of the pile.
	 * @return the size of the pile.
	 */
	public int size(int pile, int pos) {
		return _piles.get(pile).size(pos);
	}
	
	/**
	 * Test helper.
	 * DO NOT USE IT
	 */
	@Deprecated
	public ArrayList<Piles> getPiles() {
		return _piles;
	}
}
