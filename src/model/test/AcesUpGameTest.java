package model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import model.AcesUpGame;
import model.Piles;
import strategy.AcesUpHomecellStrategy;
import strategy.AcesUpStockStrategy;
import strategy.AcesUpTableauStrategy;

@SuppressWarnings("deprecation")
public class AcesUpGameTest {
	AcesUpGame game;
	
	@Before
	public void setup() {
		game = new AcesUpGame();
	}
	
	@Test
	public void testConstructor() {
		ArrayList<Piles> piles = game.getPiles();
		assertEquals("There should be 3 types of piles", 3, piles.size());
		// initial setup is tested in strategy.test
		assertTrue("The first collection of piles should be tableau",
				(piles.get(0).getPileStrategy() instanceof AcesUpTableauStrategy));
		assertTrue("The second collection of piles should be homecell",
				(piles.get(1).getPileStrategy() instanceof AcesUpHomecellStrategy));
		assertTrue("The third collection of piles should be stock",
				(piles.get(2).getPileStrategy() instanceof AcesUpStockStrategy));
	}
	
	@Test
	public void testForceAddCard() {
		Stack<Card> stock = game.getPiles().get(Piles.STOCK).getPiles().get(0);
		ArrayList<Stack<Card>> tableau = game.getPiles().get(Piles.TABLEAU).getPiles();
		Card stockTopCard = stock.peek();
		game.forceAddCard(stockTopCard, Piles.TABLEAU, 0);
		assertEquals("force adding a card to the first tableau, card should be added", stockTopCard, tableau.get(0).peek());
		try {
			game.forceAddCard(null, Piles.TABLEAU, 2);
			assertNotNull("if stock is empty, forceAddCard should do nothing", tableau.get(2).peek());
		} catch(Exception e) {
			fail("if stock is empty, forceAddCard should do nothing");
		}
	}
	
	@Test
	public void testInvokeDefaultSpecialAction() {
		ArrayList<Stack<Card>> tableauPiles = game.getPiles().get(Piles.TABLEAU).getPiles();
		Stack<Card> stockPile = game.getPiles().get(Piles.STOCK).getPiles().get(0);
		for(int i = 0; i < 12; i++) {
			game.invokeDefaultSpecialAction();
			assertEquals("After " + (i + 1) + "calls to invokeDefaultSpecialAction(), " +
					"stock pile shoule have " + (48 - (4 * (i + 1))) + "cards", (48 - (4 * (i + 1))), stockPile.size());
			// since stack is used. The new top card must be the 5th card.
			
			for(int j = 0; j < 4; j++) {
				assertEquals("After " + (i + 1) + "calls to invokeDefaultSpecialAction(), " +
						"tableau pile shoule have " + (i + 2) + "cards", (i + 2), tableauPiles.get(j).size());
			}
		}
	}
}
