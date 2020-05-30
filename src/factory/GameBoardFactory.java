package factory;

import java.awt.event.MouseListener;
import java.util.ArrayList;

import abstraction.Card;
import handler.MouseEventHandler;
import model.Game;

/**
 * A GameBoardFactory can generate different game boards.
 * @author Feng Mao Tsai (Frank)
 */
public class GameBoardFactory {
	/**
	 * Creates a game board with a supplied instance of {@code Game}. If the argument
	 * is {@code null}. An IllegalArgumentException is thrown.
	 * @param game - an instance of {@code Game}.
	 * @return a game board.
	 * @throws IllegalArgumentException if the argument is {@code null}.
	 */
	public static ArrayList<ArrayList<String>> createBakersDozenGameBoard(Game game) {
		if(game == null) {
			throw new IllegalArgumentException("There must be an instance of Game to generate a game board.");
		}
		return createGameBoard(game, BakersDozenGameBoardProperties.ROW, BakersDozenGameBoardProperties.COL,
				BakersDozenGameBoardProperties.LAYOUT);
	}
	
	/**
	 * Creates a collection of {@code MouseListener}s that will be used in Baker's Dozen's game board.
	 * @param game - an instance of {@code Game}.
	 * @return a collection of {@code MouseListener}s.
	 * @throws IllegalArgumentException if the argument is {@code null}.
	 */
	public static ArrayList<ArrayList<MouseListener>> createBakersDozenTileListeners(Game game) {
		if(game == null) {
			throw new IllegalArgumentException("There must be an instance of Game to generate a game board.");
		}
		return createTileListeners(game, BakersDozenGameBoardProperties.ROW, BakersDozenGameBoardProperties.COL,
				BakersDozenGameBoardProperties.LAYOUT);
	}
	/**
	 * Creates a game board with a supplied instance of {@code Game}. If the argument
	 * is {@code null}. An IllegalArgumentException is thrown.
	 * @param game - an instance of {@code Game}.
	 * @return a game board.
	 * @throws IllegalArgumentException if the argument is {@code null}.
	 */
	public static ArrayList<ArrayList<String>> createFreecellGameBoard(Game game) {
		if(game == null) {
			throw new IllegalArgumentException("There must be an instance of Game to generate a game board.");
		}
		return createGameBoard(game, FreecellGameBoardProperties.ROW, FreecellGameBoardProperties.COL,
				FreecellGameBoardProperties.LAYOUT);
	}
	
	/**
	 * Creates a collection of {@code MouseListener}s that will be used in Baker's Dozen's game board.
	 * @param game - an instance of {@code Game}.
	 * @return a collection of {@code MouseListener}s.
	 * @throws IllegalArgumentException if the argument is {@code null}.
	 */
	public static ArrayList<ArrayList<MouseListener>> createFreecellTileListeners(Game game) {
		if(game == null) {
			throw new IllegalArgumentException("There must be an instance of Game to generate a game board.");
		}
		return createTileListeners(game, FreecellGameBoardProperties.ROW, FreecellGameBoardProperties.COL,
				FreecellGameBoardProperties.LAYOUT);
	}
	
	/**
	 * Creates a game board with a supplied instance of {@code Game}. If the argument
	 * is {@code null}. An IllegalArgumentException is thrown.
	 * @param game - an instance of {@code Game}.
	 * @return a game board.
	 * @throws IllegalArgumentException if the argument is {@code null}.
	 */
	public static ArrayList<ArrayList<String>> createAcesUpGameBoard(Game game) {
		if(game == null) {
			throw new IllegalArgumentException();
		}
		return createGameBoard(game, AcesUpGameBoardProperties.ROW, AcesUpGameBoardProperties.COL,
				AcesUpGameBoardProperties.LAYOUT);
	}
	
	public static ArrayList<ArrayList<MouseListener>> createAcesUpTileListeners(Game game) {
		if(game == null) {
			throw new IllegalArgumentException();
		}
		return createTileListeners(game, AcesUpGameBoardProperties.ROW, AcesUpGameBoardProperties.COL,
				AcesUpGameBoardProperties.LAYOUT);
	}
	
	private static ArrayList<ArrayList<String>> createGameBoard(Game game, int numRow, int numCol, int [][][] layout) {
		ArrayList<ArrayList<String>> retVal = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < numRow; i++) {
			ArrayList<String> row = new ArrayList<String>();
			for(int j = 0; j < numCol; j++) {
				int type = layout[i][j][0];
				if(type == -1) {
					row.add("green");
				} else {
					int pos = layout[i][j][1];
					Card topCard = game.getTopCard(type, pos);
					if(topCard == null) {
						row.add("gold");
					} else {
						row.add(topCard.toString());
					}
				}
			}
			retVal.add(row);
		}
		return retVal;
	}
	
	private static ArrayList<ArrayList<MouseListener>> createTileListeners(Game game, int numRow, int numCol, int [][][] layout) {
		ArrayList<ArrayList<MouseListener>> retVal = new ArrayList<ArrayList<MouseListener>>();
		for(int i = 0; i < numRow; i++) {
			ArrayList<MouseListener> row = new ArrayList<MouseListener>();
			for(int j = 0; j < numCol; j++) {
				int type = layout[i][j][0];
				
				if(type == -1) {
					row.add(null);
				} else {
					int pos = layout[i][j][1];
					row.add(new MouseEventHandler(i, j, type, pos));
				}
			}
			retVal.add(row);
		}
		return retVal;
	}
}
