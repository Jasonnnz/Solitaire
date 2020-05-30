package strategy.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import strategy.FreecellFreecellStrategy;

public class FreecellFreecellStrategyTest {
	FreecellFreecellStrategy strategy;
	
	@Before
	public void setUpTest() {
		strategy = new FreecellFreecellStrategy();
	}
	
	@Test
	public void testGetInitialSetUpWithDeck() {
		testGetInitialSetUp(new Deck());
	}
	
	@Test
	public void testGetInitialSetUpWithNull() {
		try {
			testGetInitialSetUp(null);
		} catch(NullPointerException e) {
			fail("Deck is not required so program should not crash even no deck is provided");
		}
	}
	
	@Test
	public void testIsAddingLegal() {
		Random rand = new Random();
		Suit [] suits = Suit.values();
		Rank [] ranks = Rank.values();
		
		for(Suit suit : suits) {
			for(Rank rank : ranks) {
				int suitsIndex = rand.nextInt(4);
				int ranksIndex = rand.nextInt(13);
				Card card = new Card(suit, rank);
				assertTrue("Any card can be added to an empty pile",
						strategy.isAddingLegal(card, null));
				assertFalse("Cards cannot be added to a pile that already has a card",
						strategy.isAddingLegal(card, new Card(suits[suitsIndex], ranks[ranksIndex])));
			}
		}
	}
	
	@Test
	public void testIsAddingLegalWithNull() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				try {
					strategy.isAddingLegal(null, new Card(suit, rank));
					fail("Adding null to a pile should produce IllegalArgumentException");
				} catch(IllegalArgumentException e) {
					// expected
				}
			}
		}
		
		try {
			strategy.isAddingLegal(null, null);
			fail("Adding null to a pile should produce IllegalArgumentException");
		} catch(IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testIsRemovingLegal() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertTrue("Cards can always be removed", strategy.isRemovingLegal(card));
				assertFalse("It is impossible to remove a card from an empty pile",
						strategy.isRemovingLegal(null));
			}
		}
	}
	
	private void testGetInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> actual = strategy.getInitialSetUp(deck);
		assertEquals("Freecell has 4 Freecell piles", 4, actual.size());
		ArrayList<Stack<Card>> previous = new ArrayList<Stack<Card>>();
		for(Stack<Card> pile : actual) {
			assertEquals("Each Freecell pile should be empty", 0, pile.size());
			for(Stack<Card> test : previous) {
				assertNotSame("Any 2 piles must not be the same object", test, pile);
			}
			previous.add(pile);
		}
	}
}
