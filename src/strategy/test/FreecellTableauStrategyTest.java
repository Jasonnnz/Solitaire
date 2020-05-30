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
import strategy.FreecellTableauStrategy;

public class FreecellTableauStrategyTest {
	FreecellTableauStrategy strategy;

	@Before
	public void setUpTest() {
		strategy = new FreecellTableauStrategy();
	}

	@Test
	public void testGetInitialSetUp() {
		ArrayList<Stack<Card>> actual = strategy.getInitialSetUp(new Deck());
		assertEquals("Freecell has 8 Tableau piles", 8, actual.size());
		ArrayList<Stack<Card>> previous = new ArrayList<Stack<Card>>();
		int sixCards = 0;
		int sevenCards = 0;
		for(Stack<Card> pile : actual) {
			if(pile.size() == 6) {
				sixCards += 1;
			} else if(pile.size() == 7) {
				sevenCards += 1;
			} else {
				fail("A Tableau pile can only have either 6 or 7 cards");
			}

			for(Stack<Card> test : previous) {
				assertNotSame("Any 2 piles must not be the same object", test, pile);
			}
			previous.add(pile);
		}
		assertEquals("Half of the piles should be dealt 6 cards", 4, sixCards);
		assertEquals("Half of the piles should be dealt 7 cards", 4, sevenCards);
	}

	@Test
	public void testIsAddingLegal() {
		assertTrue("Moving five of spades onto six of hearts is legal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.FIVE), new Card(Suit.HEART, Rank.SIX)));
		assertTrue("Moving ace of diamonds on to two of clubs is legal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.ACE), new Card(Suit.CLUB, Rank.TWO)));
		assertTrue("Moving queen of hearts onto king of spades is legal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.QUEEN), new Card(Suit.SPADE, Rank.KING)));
		assertFalse("Moving seven of spades onto eight of spades is illegal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.SEVEN), new Card(Suit.SPADE, Rank.EIGHT)));
		assertFalse("Moving jack of hearts onto queen of diamonds is illegal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.JACK), new Card(Suit.DIAMOND, Rank.QUEEN)));
		assertFalse("Moving two of hearts onto four of spades is illegal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.TWO), new Card(Suit.SPADE, Rank.FOUR)));
		assertFalse("Moving three of diamonds onto three of diamonds is illegal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.THREE), new Card(Suit.DIAMOND, Rank.THREE)));
		assertFalse("Moving seven of clubs onto three of hearts is illegal",
				strategy.isAddingLegal(new Card(Suit.CLUB, Rank.SEVEN), new Card(Suit.HEART, Rank.THREE)));
	}

	@Test
	public void testIsAddingLegalWithNull() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				try {
					strategy.isAddingLegal(null, card);
					fail("Adding null to a pile should produce IllegalArgumentException");
				} catch(IllegalArgumentException e) {
					// expected
				}

				assertTrue("Any card can be added to an empty Tableau pile", strategy.isAddingLegal(card, null));
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
		// since we are using stack, this should always return true unless the pile is empty
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				assertTrue("Only the card which is currently at the top of the Tableau pile can be removed",
						strategy.isRemovingLegal(new Card(suit, rank)));
			}
		}
		assertFalse("It is impossible to remove a card from an empty pile",
				strategy.isRemovingLegal(null));
	}
}
