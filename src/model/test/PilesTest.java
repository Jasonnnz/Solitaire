package model.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import model.Piles;
import strategy.AcesUpTableauStrategy;
import strategy.BakersDozenTableauStrategy;
import strategy.FreecellFreecellStrategy;
import strategy.FreecellTableauStrategy;
import strategy.HomecellStrategy;
import strategy.PileStrategy;

@SuppressWarnings("unchecked")
public class PilesTest {
	@Test
	public void testConstructor() {
		try {
			// getInitialSetUp is tested in /*StrategyTest/
			Field pileStrategyField = Piles.class.getDeclaredField("_pileStrategy");
			Field pilesField = Piles.class.getDeclaredField("_piles");
			pileStrategyField.setAccessible(true);
			pilesField.setAccessible(true);
			Piles piles = new Piles(new BakersDozenTableauStrategy(), new Deck());
			PileStrategy strategy = (PileStrategy) pileStrategyField.get(piles);
			ArrayList<Stack<Card>> pilesValue = (ArrayList<Stack<Card>>) pilesField.get(piles);
			assertTrue("Creating a Piles instance with BakersDozenTableauStrategy,"
					+ "_pileStrategy should be set to an instance of BakersDozenTableauStrategy",
					strategy instanceof BakersDozenTableauStrategy);
			assertNotNull("_piles should be initialized with getInitialSetUp in BakersDozenTableauStrategy",
					pilesValue);
			piles = new Piles(new HomecellStrategy(), new Deck());
			strategy = (PileStrategy) pileStrategyField.get(piles);
			pilesValue = (ArrayList<Stack<Card>>) pilesField.get(piles);
			assertTrue("Creating a Piles instance with HomecellStrategy,"
					+ "_pileStrategy should be set to an instance of HomecellStrategy",
					strategy instanceof HomecellStrategy);
			assertNotNull("_piles should be intitialized with getInitialSetUp in HomecellStrategy",
					pilesValue);
			piles = new Piles(new FreecellFreecellStrategy(), new Deck());
			strategy = (PileStrategy) pileStrategyField.get(piles);
			pilesValue = (ArrayList<Stack<Card>>) pilesField.get(piles);
			assertTrue("Creating a Piles instance with FreecellFreecellStrategy,"
					+ "_pileStrategy should be set to an instance of FreecellFreecellStrategy",
					strategy instanceof FreecellFreecellStrategy);
			assertNotNull("_piles should be intialized with getInitialSetUp in FreecellFreecellStrategy",
					pilesValue);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}
	}
	
	@Test
	public void testAddCard() {
		// movement legality is tested in /*StrategyTest/
		// this test focuses on whether this method modifies the piles correctly
		PileStrategy strategy = new BakersDozenTableauStrategy();
		Piles piles = new Piles(strategy, new Deck());
		testAddCard(13, strategy, piles);
		strategy = new FreecellFreecellStrategy();
		piles = new Piles(strategy, new Deck());
		testAddCard(4, strategy, piles);
	}
	
	@Test
	public void testRemoveCard() {
		// movement legality is tested in /*StrategyTest/
		// this test focuses on whether this method modifies the piles correctly
		PileStrategy strategy = new FreecellTableauStrategy();
		Piles piles = new Piles(strategy, new Deck());
		testRemoveCard(8, strategy, piles);
		strategy = new BakersDozenTableauStrategy();
		piles = new Piles(strategy, new Deck());
		testRemoveCard(13, strategy, piles);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRemoveCardType2() {
		PileStrategy strategy = new AcesUpTableauStrategy();
		Piles piles = new Piles(strategy, new Deck());
		Card [] cards = { new Card(Suit.DIAMOND, Rank.FIVE), new Card(Suit.DIAMOND, Rank.EIGHT),
				          new Card(Suit.HEART, Rank.FOUR),   new Card(Suit.SPADE, Rank.KING) };
		piles.setPiles(cards);
		assertTrue("expected true to be returned when removing is legal", piles.removeCard(0, Piles.HOMECELL));
		assertNull("after removing the only card in the first pile, the pile should be empty", piles.getTopCard(0));
		for(int i = 1; i < 4; i++) {
			assertEquals("after removal other piles should not be modified", cards[i], piles.getTopCard(i));
		}
		cards[1] = null;
		piles.setPiles(cards);
		assertTrue("expected true to be returned when removing is legal", piles.removeCard(3, Piles.TABLEAU));
		assertNull("after removing the only card in the last pile, the pile should be empty", piles.getTopCard(3));
		for(int i = 0; i < 3; i++) {
			assertEquals("after removal other piles should not be modified", cards[i], piles.getTopCard(i));
		}
		piles.setPiles(cards);
		assertFalse("expected false to be returned when removing is illegal", piles.removeCard(0, Piles.HOMECELL));
		for(int i = 0; i < 4; i++) {
			assertEquals("when no removal piles should not be modified", cards[i], piles.getTopCard(i));
		}
	}
	
	@Test
	public void testGetTopCard() {
		try {
			Field pilesField = Piles.class.getDeclaredField("_piles");
			pilesField.setAccessible(true);
			Piles piles = new Piles(new BakersDozenTableauStrategy(), new Deck());
			ArrayList<Stack<Card>> value = new ArrayList<Stack<Card>>();
			for(int i = 0; i < 3; i++) {
				value.add(new Stack<Card>());
			}
			value.get(1).push(new Card(Suit.DIAMOND, Rank.FIVE));
			value.get(2).push(new Card(Suit.HEART, Rank.JACK));
			value.get(2).push(new Card(Suit.SPADE, Rank.KING));
			pilesField.set(piles, value);
			assertEquals("getTopCard did not return the correct value", null, piles.getTopCard(0));
			assertEquals("getTopCard should not modify pile size", 0, piles.size(0));
			assertEquals("getTopCard did not return the correct value", new Card(Suit.DIAMOND, Rank.FIVE), piles.getTopCard(1));
			assertEquals("getTopCard should not modify pile size", 1, piles.size(1));
			assertEquals("getTopCard did not return the correct value", new Card(Suit.SPADE, Rank.KING), piles.getTopCard(2));
			assertEquals("getTopCard should not modify piles size", 2, piles.size(2));
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception");
		}
	}
	
	private void testAddCard(int howManyPiles, PileStrategy strategy, Piles piles) {
		Suit [] suits = Suit.values();
		Rank [] ranks = Rank.values();
		Random rand = new Random();
		
		for(int i = 0; i < 42; i++) {
			Card card = new Card(suits[rand.nextInt(4)], ranks[rand.nextInt(13)]);
			int pos = rand.nextInt(howManyPiles);
			int originalSize = piles.size(pos);
			Card originalTop = piles.getTopCard(pos);
			if(strategy.isAddingLegal(card, originalTop)) {
				assertTrue("If adding is legal, addCard should return true", piles.addCard(card, pos));
				assertEquals("After adding a card, the size of the pile should increase by 1",
						originalSize + 1, piles.size(pos));
				assertEquals("After adding a card, the top card of the pile should be the added card",
						card, piles.getTopCard(pos));
			} else {
				assertFalse("If adding is illegal, addCard should return false", piles.addCard(card, pos));
				assertEquals("If adding is illegal, pile size should not change", originalSize, piles.size(pos));
				assertEquals("If adding is illegal, top card should not change", originalTop, piles.getTopCard(pos));
			}
		}
	}
	
	// don't use this method after testAddCard in the same test case
	private void testRemoveCard(int howManyPiles, PileStrategy strategy, Piles piles) {
		Random rand = new Random();
		
		for(int i = 0; i < 42; i++) {
			int pos = rand.nextInt(howManyPiles);
			int originalSize = piles.size(pos);
			Card originalTop = piles.getTopCard(pos);
			if(strategy.isRemovingLegal(originalTop)) {
				assertTrue("If removing is legal, removeCard should return true", piles.removeCard(pos));
				assertEquals("After removing a card, the size of the pile should decrease by 1",
						originalSize - 1, piles.size(pos));
				// it works because deck has 52 UNIQUE cards
				assertNotEquals("After removing a card, the top card should not be the original top card",
						originalTop, piles.getTopCard(pos));
			} else {
				assertFalse("If removing is illegal, removeCard should return false", piles.removeCard(pos));
				assertEquals("If removing is illegal, pile size should not change", originalSize, piles.size(pos));
				assertEquals("If removing is illegal, top card should not change", originalTop, piles.getTopCard(pos));
			}
		}
	}
}
