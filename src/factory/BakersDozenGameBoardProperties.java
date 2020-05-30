package factory;

/**
 * This class contains methods that generate a game board of a Baker's Dozen game.
 * @author Feng Mao Tsai (Frank)
 */
class BakersDozenGameBoardProperties {
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
			{ {-1, 0},  {0, 0},  {0, 1},  {0, 2},  {0, 3},  {0, 4},  {-1, 0},  {1, 0}, },
			{ {-1, 0},  {0, 5},  {0, 6},  {0, 7},  {0, 8},  {0, 9},  {-1, 0},  {1, 1}, },
			{ {-1, 0},  {-1, 0}, {0, 10}, {0, 11}, {0, 12}, {-1, 0}, {-1, 0},  {1, 2}, },
			{ {-1, 0},  {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0},  {1, 3}  }
	};
}
