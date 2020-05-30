package controller;

import java.awt.event.MouseListener;
import java.util.ArrayList;

import abstraction.Card;
import factory.GameBoardFactory;
import model.AcesUpGame;
import model.BakersDozenGame;
import model.FreecellGame;
import model.Game;
import model.Piles;
import ui.UI;

/**
 * A controller is an interface between model and UI. Users can use it to
 * manipulate data stored in model.
 * @author Feng Mao Tsai (Frank)
 * @author Edwin Chiu    (Edwin)
 * @author Mehmet Ozel   (Mehmet)
 */
/*
 * Good design thrown out of the windows. It is what you get for starting the project late.
 * Controller is a good example for strategy pattern FYI
 */
public class Controller {
	/**
	 * Fields
	 * 
	 * game             - a reference to a Game object. If no game is being played, this value must be null.
	 * observer         - a reference to an observer object. If no observer is registered, this value must be null.
	 * selectedCard     - the currently selected card. If no card is currently selected, this value must be null.
	 * selectedRowPos   - the row position of the currently selected card. If no card is currently selected,
	 *                    this value must be -1.
	 * selectedColPos   - the col position of the currently selected card. If no card is currently selected,
	 *                    this value must be -1.
	 * selectedPileType - the type of the pile in which the currently selected card is. If no card is currently
	 *                    selected, this value must be -1.
	 * selectedPilePos  - the position of the pile in which the currently selected card is. If no card is currently
	 *                    selected, this value must be -1.
	 */
	private static Game game             = null;
	private static UI   observer         = null;
	private static Card selectedCard     = null;
	private static int  selectedRowPos   = -1;
	private static int  selectedColPos   = -1;
	private static int  selectedPileType = -1;
	private static int  selectedPilePos  = -1;

	/**
	 * Registers an observer.
	 * 
	 * @param observer - the observer to be registered.
	 * @throws IllegalArgumentException if observer is {@code null}.
	 * @author Frank
	 */
	public static void registerObserver(UI observer) {
		if(observer == null) {
			throw new IllegalArgumentException("Observer can not be null.");
		}
		
		Controller.observer = observer;
	}

	/**
	 * Instantiate a game of gameType. This method will generate an initial
	 * game board and notify the registered observer after instantiation.
	 * 
	 * @param gameType - the type of game to instantiate.
	 * @throws IllegalArgumentException if {@code gameType} does not correspond to any existing game type.
	 * @author Edwin
	 * @author Frank (minor changes)
	 */
	public static void instantiateGame(int gameType) {
		resetSelectedCard();
		ArrayList<ArrayList<String>> initialGameBoard;
		ArrayList<ArrayList<MouseListener>> initialMouseListeners;
		
		if (gameType == BakersDozenGame.GAME_ID) {
			game = new BakersDozenGame();
			initialGameBoard = GameBoardFactory.createBakersDozenGameBoard(game);
			initialMouseListeners = GameBoardFactory.createBakersDozenTileListeners(game);
			observer.updateAllTileImages(initialGameBoard);
			observer.setAllMouseListeners(initialMouseListeners);
		} else if(gameType == FreecellGame.GAME_ID) {
			game = new FreecellGame();
			initialGameBoard = GameBoardFactory.createFreecellGameBoard(game);
			initialMouseListeners = GameBoardFactory.createFreecellTileListeners(game);
			observer.updateAllTileImages(initialGameBoard);
			observer.setAllMouseListeners(initialMouseListeners);
		} else if(gameType == AcesUpGame.GAME_ID) {
			game = new AcesUpGame();
			initialGameBoard = GameBoardFactory.createAcesUpGameBoard(game);
			initialMouseListeners = GameBoardFactory.createAcesUpTileListeners(game);
			observer.updateAllTileImages(initialGameBoard);
			observer.setAllMouseListeners(initialMouseListeners);
		} else {
			throw new IllegalArgumentException("Game id does not correspond to any game.");
		}
	}

	/**
	 * Selects a card. If a card has already been selected, it invokes pile action.
	 * If the same card is selected twice, it deselects the card.
	 * If the selected pile is Stock pile. This method will invoke pile action and deselect
	 * any cards.
	 * 
	 * @param rowPos   - the row position of the card.
	 * @param colPos   - the col position of the card.
	 * @param pileType - the type of the pile in which the selected card is.
	 * @param pilePos  - the position of the pile in which the selected card is.
	 * @author Mehmet
	 * @author Frank (minor changes)
	 */
	public static void select(int rowPos, int colPos, int pileType, int pilePos) {
		if(game instanceof AcesUpGame && pileType == Piles.STOCK) {
			if(selectedCard != null) {
				observer.displayErrorMessage();
			} else {
				invokePileAction();
				resetSelectedCard();
			}
		} else {
			__select(rowPos, colPos, pileType, pilePos);
		}
	}
	
	/**
	 * Selects a card. This helper method does the actual selection stuff.
	 */
	private static void __select(int rowPos, int colPos, int pileType, int pilePos) {
		if (selectedCard == null) {
			Card topCard = game.getTopCard(pileType, pilePos);
			if (topCard != null && pileType != 1) {
				observer.setSelectedBorderAt(rowPos, colPos);
				selectedCard = topCard;
				selectedRowPos = rowPos;
				selectedColPos = colPos;
				selectedPileType = pileType;
				selectedPilePos = pilePos;
			}
		} else if (selectedRowPos == rowPos && selectedColPos == colPos) {
			observer.setUnselectedBorderAt(rowPos, colPos);
			resetSelectedCard();
		} else {
			moveCard(selectedCard, rowPos, colPos, pileType, pilePos);
			observer.setUnselectedBorderAt(selectedRowPos, selectedColPos);
			resetSelectedCard();
		}
	}
	
	/**
	 * Resets selected card.
	 * @author Frank
	 */
	private static void resetSelectedCard() {
		selectedCard     = null;
		selectedRowPos   = -1;
		selectedColPos   = -1;
		selectedPileType = -1;
		selectedPilePos  = -1;
	}

	/**
	 * Moves a card from its initial position to its destination. If this operation
	 * is illegal, this will notify observer to create a message dialog.
	 * 
	 * @param card         - the card to be moved.
	 * @param destRow      - the row position of the destination.
	 * @param destCol      - the col position of the destination.
	 * @param destPileType - the pile type of the destination
	 * @param destPilePos  - the pile position of the destination
	 * @author Mehmet
	 * @author Frank (minor changes)
	 */
	private static void moveCard(Card card, int destRow, int destCol, int destPileType, int destPilePos) {
		if(!(game instanceof AcesUpGame)) {
			if (game.isAddingLegal(card, destPileType, destPilePos) &&
					game.isRemovingLegal(selectedPileType, selectedPilePos)) {
				game.addCard(card, destPileType, destPilePos);
				game.removeCard(selectedPileType, selectedPilePos);
				observer.updateTileImageAt(card.toString(), destRow, destCol);
				
				Card initialPileTopCard = game.getTopCard(selectedPileType, selectedPilePos);
				String imageName;
				if(initialPileTopCard == null) {
					imageName = "gold";
				} else {
					imageName = initialPileTopCard.toString();
				}
				
				observer.updateTileImageAt(imageName, selectedRowPos, selectedColPos);
			} else {
				observer.displayErrorMessage();
			}
		} else {
			handleAcesUpMoveCard(card, destRow, destCol, destPileType, destPilePos);
		}
	}
	
	/**
	 * Moves a card from its initial position to its destination. If this operation
	 * is illegal, this will notify observer to create a message dialog.
	 * This method is used to handle Ace's Up card moving operation.
	 * @param card         - the card to be moved.
	 * @param destRow      - the row position of the destination.
	 * @param destCol      - the col position of the destination.
	 * @param destPileType - the pile type of the destination
	 * @param destPilePos  - the pile position of the destination
	 */
	private static void handleAcesUpMoveCard(Card card, int destRow, int destCol, int destPileType, int destPilePos) {
		if(game.isAddingLegal(card, destPileType, destPilePos) &&
				game.isRemovingLegal(selectedPileType, selectedPilePos, destPileType)) {
			game.removeCard(selectedPileType, selectedPilePos, destPileType);
			game.addCard(card, destPileType, destPilePos);
			observer.updateTileImageAt(card.toString(), destRow, destCol);
			
			Card initialPileTopCard = game.getTopCard(selectedPileType, selectedPilePos);
			String imageName;
			if(initialPileTopCard == null) {
				imageName = "gold";
			} else {
				imageName = initialPileTopCard.toString();
			}
			observer.updateTileImageAt(imageName, selectedRowPos, selectedColPos);
		} else {
			observer.displayErrorMessage();
		}
	}
	
	/**
	 * When the stock pile is selected. 4 cards should be removed from the stock, and
	 * they should be distributed equally to tableau piles.
	 */
	private static void invokePileAction() {
		game.invokeDefaultSpecialAction();
		
		Card stockTopCard = game.getTopCard(Piles.STOCK, 0);
		if(stockTopCard != null) {
			observer.updateTileImageAt(stockTopCard.toString(), 0, 0);
		} else {
			observer.updateTileImageAt("gold", 0, 0);
		}
		
		for(int i = 0; i < 4; i++) {
			Card newTableauTopCard = game.getTopCard(Piles.TABLEAU, i);
			observer.updateTileImageAt(newTableauTopCard.toString(), 0, i + 3);
		}
	}
}
