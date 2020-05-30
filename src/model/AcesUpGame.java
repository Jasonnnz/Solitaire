package model;

import abstraction.Card;
import strategy.AcesUpHomecellStrategy;
import strategy.AcesUpStockStrategy;
import strategy.AcesUpTableauStrategy;

public class AcesUpGame extends Game {
	/**
	 * Constant
	 * 
	 * GAME_ID - this game's unique id
	 */
	public static final int GAME_ID = 0x00000002;
	
	/**
	 * Creates an instance of {@code AcesUpGame} and initializes its piles to the initial setup.
	 */
	public AcesUpGame() {
		super(new Deck(), new AcesUpTableauStrategy(), new AcesUpHomecellStrategy(), new AcesUpStockStrategy());
	}

	/**
	 * Adds a card to a specified Tableau pile without legality check. If the client attempts
	 * to add a card to other types of piles, an IllegalArgumentException will be thrown.
	 * It card is null, nothing will happen.
	 * @param card - the card to be added.
	 * @param pile - this parameter must be 0.
	 * @param pos  - the position of the Tableau pile.
	 * @throws IllegalArgumentException if pile != 0.
	 */
	@Override
	public void forceAddCard(Card card, int pile, int pos) {
		if(pile != 0) {
			throw new IllegalArgumentException("Ace's Up game does not allow cards to be added "
					+ "without legality check except for Tableau piles.");
		}
		if(card != null) {
			super.forceAddCard(card, pile, pos);
		}
	}
	
	/**
	 * This game does not support this method.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(int pile, int pos) {
		throw new UnsupportedOperationException("More info is needed to determine the result, "
				+ "this game does not support this method.");
	}
	
	/**
	 * This game does not support this method
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean removeCard(int pile, int pos) {
		throw new UnsupportedOperationException("More info is needed to determine the result, "
				+ "this game does not support this method.");
	}
}
