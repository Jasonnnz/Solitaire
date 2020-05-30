package model;

import abstraction.Card;
import strategy.BakersDozenTableauStrategy;
import strategy.HomecellStrategy;

/**
 * An instance of this class represents a Baker's Dozen game.
 */
public class BakersDozenGame extends Game {
	/**
	 * Constant
	 * 
	 * GAME_ID - this game's unique id
	 */
	public static final int GAME_ID = 0x00000000;
	
	/**
	 * Creates an instance of {@code BakersDozenGame} and initialize its piles to the initial setup.
	 */
	public BakersDozenGame() {
		super(new Deck(), new BakersDozenTableauStrategy(), new HomecellStrategy());
	}

	/**
	 * Baker's Dozen game does not allow any card to be added without legality check.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void forceAddCard(Card card, int pile, int pos) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This game does not support this operation.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean isRemovingLegal(int pile, int pos, int dest) {
		throw new UnsupportedOperationException("This game does not support this method.");
	}
	
	/**
	 * This game does not support this operation.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public boolean removeCard(int pile, int pos, int dest) {
		throw new UnsupportedOperationException("This game does not support this method.");
	}
	
	/**
	 * This game does not support this operation.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void invokeDefaultSpecialAction() {
		throw new UnsupportedOperationException();
	}
}
