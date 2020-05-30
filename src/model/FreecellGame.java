package model;

import abstraction.Card;
import strategy.FreecellFreecellStrategy;
import strategy.FreecellTableauStrategy;
import strategy.HomecellStrategy;

/**
 * An instance of this class represents a Freecell game.
 */
public class FreecellGame extends Game {
	/**
	 * Constant
	 * 
	 * GAME_ID - this game's unique id
	 */
	public static final int GAME_ID = 0x00000001;
	
	/**
	 * Creates an instance of {@code FreecellGame} and initialize its piles to the initial setup.
	 */
	public FreecellGame() {
		super(new Deck(), new FreecellTableauStrategy(), new HomecellStrategy(), new FreecellFreecellStrategy());
	}
	
	/**
	 * Freecell game does not allow any card to be added without legality check.
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void forceAddCard(Card card, int pile, int pos) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * This game does not support this method.
	 * @throws UnsupportedOperationException
	 */
	public boolean isRemovingLegal(int pile, int pos, int dest) {
		throw new UnsupportedOperationException("This game does not support this method.");
	}
	
	/**
	 * This game does not support this method.
	 * @throws UnsupportedOperationException
	 */
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
