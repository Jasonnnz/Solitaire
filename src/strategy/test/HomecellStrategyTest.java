package strategy.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import strategy.HomecellStrategy;

public class HomecellStrategyTest {
	HomecellStrategy strategy;
	
	@Before
	public void setUpTest() {
		strategy = new HomecellStrategy();
	}
	
	@Test
	public void testGetInitialSetUpWithDeck() {
		testGetInitialSetUp(new Deck());
	}
	
	@Test
	public void testGetinitialSetUpWithNull() {
		try {
			testGetInitialSetUp(null);
		} catch(NullPointerException e) {
			fail("Deck is not required so program should not crash even no deck is provided");
		}
	}
	
	@Test
	public void testIsAddingLegal() {
		assertTrue("Moving three of spades onto two of spades is legal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.THREE), new Card(Suit.SPADE, Rank.TWO)));
		assertTrue("Moving seven of hearts onto six of hearts is legal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.SEVEN), new Card(Suit.HEART, Rank.SIX)));
		assertTrue("Moving jack of diamonds onto ten of diamonds is legal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.JACK), new Card(Suit.DIAMOND, Rank.TEN)));
		assertFalse("Moving two of spades onto ace of clubs is illegal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.TWO), new Card(Suit.CLUB, Rank.ACE)));
		assertFalse("Moving four of diamonds onto three of clubs is illegal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.FOUR), new Card(Suit.CLUB, Rank.THREE)));
		assertFalse("Moving queen of clubs onto king of clubs is illegal",
				strategy.isAddingLegal(new Card(Suit.CLUB, Rank.QUEEN), new Card(Suit.CLUB, Rank.KING)));
		assertFalse("Moving six of spades onto four of spades is illegal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.SIX), new Card(Suit.SPADE, Rank.FOUR)));
		
		for(Suit suit : Suit.values()) {
			assertTrue("Any aces can be added to an empty Homecell",
					strategy.isAddingLegal(new Card(suit, Rank.ACE), null));
			assertFalse("Only aces can be added to an empty Homecell",
					strategy.isAddingLegal(new Card(suit, Rank.TWO), null));
			assertFalse("Only aces can be added to an empty Homecell",
					strategy.isAddingLegal(new Card(suit, Rank.THREE), null));
			assertFalse("Only aces can be added to an empty Homecell",
					strategy.isAddingLegal(new Card(suit, Rank.SEVEN), null));
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
				assertFalse("Cards cannot be removed from a Homecell pile",
						strategy.isRemovingLegal(new Card(suit, rank)));
				assertFalse("It is impossible to remove a card from an empty pile",
						strategy.isRemovingLegal(null));
			}
		}
	}
	
	private void testGetInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> actual = strategy.getInitialSetUp(deck);
		assertEquals("There should be 4 Homecell piles", 4, actual.size());
		ArrayList<Stack<Card>> previous = new ArrayList<Stack<Card>>();
		for(Stack<Card> pile : actual) {
			assertEquals("Each pile should be empty", 0, pile.size());
			for(Stack<Card> test : previous) {
				assertNotSame("Any two piles must not be the same object", test, pile);
			}
			previous.add(pile);
		}
	}
}
