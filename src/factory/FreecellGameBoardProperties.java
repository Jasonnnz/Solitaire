package factory;

/**
 * This class contains methods that generate a game board of a Freecell game.
 * @author Feng Mao Tsai (Frank)
 */
class FreecellGameBoardProperties {
	/**
	 * Constants
	 * 
	 * ROW    - the number of rows in the game board.
	 * COL    - the number of cols in the game board.
	 * LAYOUT - the layout of the game board. {x, y}: x indicates the type of the pile.
	 *                                                y indicates the position of the pile.
	 *                                                If x is -1, y is not important.
	 */
	protected static final int ROW = 4;
	protected static final int COL = 8;
	protected static final int [][][] LAYOUT = { 
			{ {0, 0},  {0, 1},  {0, 2},  {0, 3},  {0, 4},  {0, 5},  {0, 6},  {0, 7},  },
			{ {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, },
			{ {1, 0},  {1, 1},  {1, 2},  {1, 3},  {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, },
			{ {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {2, 0},  {2, 1},  {2, 2},  {2, 3}   }
	};
}
