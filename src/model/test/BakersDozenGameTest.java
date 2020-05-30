package model.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.BakersDozenGame;
import model.Game;
import model.Piles;
import strategy.BakersDozenTableauStrategy;
import strategy.HomecellStrategy;
import strategy.PileStrategy;

@SuppressWarnings("unchecked")
public class BakersDozenGameTest {
	BakersDozenGame game;
	
	@Before
	public void setUpTest() {
		assertEquals("baker's dozen's game id is 0x00000000", 0x00000000, BakersDozenGame.GAME_ID);
		game = new BakersDozenGame();
	}
	
	@Test
	public void testConstructor() {
		try {
			Field pilesField = Game.class.getDeclaredField("_piles");
			pilesField.setAccessible(true);
			Field pileStrategy = Piles.class.getDeclaredField("_pileStrategy");
			pileStrategy.setAccessible(true);
			ArrayList<Piles> piles = (ArrayList<Piles>) pilesField.get(game);
			assertEquals("Baker's Dozen only uses 2 types of piles", 2, piles.size());
			PileStrategy strategy = (PileStrategy) pileStrategy.get(piles.get(0));
			assertTrue("The first collection of piles should be Tableau piles",
					strategy instanceof BakersDozenTableauStrategy);
			strategy = (PileStrategy) pileStrategy.get(piles.get(1));
			assertTrue("The second collection of piles should be Homecell piles",
					strategy instanceof HomecellStrategy);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception.");
		}
	}
	
	@Test
	public void testTableauPileInitialSetUp() {
		try {
			Field pilesField = Game.class.getDeclaredField("_piles");
			pilesField.setAccessible(true);
			ArrayList<Piles> piles = (ArrayList<Piles>) pilesField.get(game);
			assertNotNull("Baker's Dozen's Tableau piles should be initialized with getInitialSetUp in BakersDozenTableauStrategy",
					piles.get(0));
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception.");
		}
		// getInitialSetUp is tested in BakersDozenStrategyTest
	}
	
	@Test
	public void testHomecellPileInitialSetUp() {
		try {
			Field pilesField = Game.class.getDeclaredField("_piles");
			pilesField.setAccessible(true);
			ArrayList<Piles> piles = (ArrayList<Piles>) pilesField.get(game);
			assertNotNull("Baker's Dozen's Homecell piles should be initialized with getInitialSetUp in HomecellStrategy",
					piles.get(1));
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}
		// getInitialSetUp is tested in HomecellStrategyTest
	}
	
	@Test
	public void testAddCardToTableauPiles() {
		// movement legality tested in BakersDozenTableauStrategyTest
		testAddCard(13, 0, new BakersDozenTableauStrategy());
	}
	
	@Test
	public void testAddCardToHomecellPiles() {
		// movement legality tested in HomecellStrategyTest
		testAddCard(4, 1, new HomecellStrategy());
	}
	
	@Test
	public void testRemoveCardFromTableauPiles() {
		// movement legality tested in BakersDozenTableauStrategyTest
		testRemoveCard(13, 0, new BakersDozenTableauStrategy());
	}
	
	@Test
	public void testRemoveCardFromHomecellPiles() {
		// movement legality tested in HomecellStrategyTest
		PileStrategy strategy = new HomecellStrategy();
		testAddCard(4, 1, strategy); // add cards for removing
		testRemoveCard(4, 1, strategy);
	}
	
	private void testAddCard(int howManyPiles, int whichPile, PileStrategy strategy) {
		Suit [] suits = Suit.values();
		Rank [] ranks = Rank.values();
		Random rand = new Random();
		
		try {
			for(int i = 0; i < 42; i++) {
				Card card = new Card(suits[rand.nextInt(4)], ranks[rand.nextInt(13)]);
				int pos = rand.nextInt(howManyPiles);
				int originalSize = game.size(whichPile, pos);
				if(strategy.isAddingLegal(card, game.getTopCard(whichPile, pos))) {
					assertTrue("If adding is legal, addCard should return true", game.addCard(card, whichPile, pos));
					assertEquals("If adding is legal, addCard should modify the correct pile and increase its size by 1",
							originalSize + 1, game.size(whichPile, pos));
					// It's a delegate method. Other details are tested in PilesTest.
				} else {
					assertFalse("If adding is illegal, addCard should return false", game.addCard(card, whichPile, pos));
					assertEquals("If adding is illegal, addCard should not modify any pile",
							originalSize, game.size(whichPile, pos));
					// It's a delegate method. Other details are tested in PilesTest.
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}
	}
	
	private void testRemoveCard(int howManyPiles, int whichPile, PileStrategy strategy) {
		Random rand = new Random();
		
		for(int i = 0; i < 42; i++) {
			int pos = rand.nextInt(howManyPiles);
			int originalSize = game.size(whichPile, pos);
			if(strategy.isRemovingLegal(game.getTopCard(whichPile, pos))) {
				assertTrue("If removing is legal, removeCard should return true", game.removeCard(whichPile, pos));
				assertEquals("If removing is legal, removeCard should modify the correct pile and decrease its size by 1",
						originalSize - 1, game.size(whichPile, pos));
				// It's a delegate method. Other details are tested in PilesTest.
			} else {
				assertFalse("If removing is illegal, removeCard should return false", game.removeCard(whichPile, pos));
				assertEquals("If removing is illegal, removeCard should not modify any pile",
						originalSize, game.size(whichPile, pos));
				// It's a delegate method. Other details are tested in PilesTest.
			}
		}
	}
}
