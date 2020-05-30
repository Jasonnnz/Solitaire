package strategy.test;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import strategy.BakersDozenTableauStrategy;

@SuppressWarnings("unchecked")
public class BakersDozenTableauStrategyTest {
	BakersDozenTableauStrategy strategy;
	
	@Before
	public void setUpTest() {
		strategy = new BakersDozenTableauStrategy();
	}
	
	@Test
	public void testMoveKingsDown() {
		try {
			
			Card card0 = new Card(Suit.CLUB, Rank.KING);
			Card card1 = new Card(Suit.HEART, Rank.KING);
			Card card2 = new Card(Suit.CLUB, Rank.ACE);
			Card card3 = new Card(Suit.DIAMOND, Rank.FIVE);
			Card card4 = new Card(Suit.SPADE, Rank.SEVEN);
			ArrayList<Card> pile = new ArrayList<Card>(Arrays.asList(card0, card1, card2, card3, card4));
			testMoveKingsDown(pile);
			pile = new ArrayList<Card>(Arrays.asList(card0, card2, card3, card4, card1));
			testMoveKingsDown(pile);
			pile = new ArrayList<Card>(Arrays.asList(card2, card0, card3, card1, card4));
			testMoveKingsDown(pile);
			pile = new ArrayList<Card>(Arrays.asList(card4, card3, card0, card1, card2));
			testMoveKingsDown(pile);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testGetInitialSetUp() {
		try {
			Method method = BakersDozenTableauStrategy.class.getDeclaredMethod("moveKingsDown", ArrayList.class);
			method.setAccessible(true);
			ArrayList<Stack<Card>> actual = strategy.getInitialSetUp(new Deck());
			ArrayList<Card> allCards = new ArrayList<Card>();
			assertEquals("Baker's Dozen has 13 Tableau piles", 13, actual.size());
			for(Stack<Card> pile : actual) {
				assertEquals("Each Tableau pile in Baker's Dozen should have 4 cards", 4, pile.size());
				allCards.addAll(pile);
				
				ArrayList<Card> arg = new ArrayList<Card>();
				arg.addAll(pile);
				Stack<Card> specialHandlingExpected = (Stack<Card>) method.invoke(strategy, arg);
				assertEquals("Kings should be moved to the bottom", specialHandlingExpected, pile);
			}
			
			// 13 * 4 = 52
			for(Suit suit : Suit.values()) {
				for(Rank rank : Rank.values()) {
					assertTrue("Every card must appear once in all piles", allCards.contains(new Card(suit, rank)));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testIsAddingLegal() {
		assertTrue("Moving three of spades onto four of spades is legal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.THREE), new Card(Suit.SPADE, Rank.FOUR)));
		assertTrue("Moving seven of hearts onto eight of clubs is legal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.SEVEN), new Card(Suit.CLUB, Rank.EIGHT)));
		assertTrue("Moving jack of diamonds onto queen of hearts is legal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.JACK), new Card(Suit.HEART, Rank.QUEEN)));
		assertFalse("Moving king of hearts onto queen of hearts is illegal",
				strategy.isAddingLegal(new Card(Suit.HEART, Rank.KING), new Card(Suit.HEART, Rank.QUEEN)));
		assertFalse("Moving five of spades onto five of hearts is illegal",
				strategy.isAddingLegal(new Card(Suit.SPADE, Rank.FIVE), new Card(Suit.HEART, Rank.FIVE)));
		assertFalse("Moving ace of diamonds onto three of clubs is illegal",
				strategy.isAddingLegal(new Card(Suit.DIAMOND, Rank.ACE), new Card(Suit.CLUB, Rank.THREE)));
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
				
				assertFalse("Cards cannot be added to an empty Tableau pile", strategy.isAddingLegal(card, null));
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
	
	private void testMoveKingsDown(ArrayList<Card> pile) throws Exception {
		ArrayList<Card> cards = new ArrayList<Card>();
		cards.addAll(pile);
		Method method = BakersDozenTableauStrategy.class.getDeclaredMethod("moveKingsDown", ArrayList.class);
		method.setAccessible(true);
		Stack<Card> handled = (Stack<Card>) method.invoke(strategy, pile);
		ArrayList<Card> actual = new ArrayList<Card>();
		actual.addAll(handled);
		
		assertEquals("Testing with 5 cards. No card should be added or removed after testing", 5, handled.size());
		for(Card card : cards) {
			assertTrue("No card should be modified after calling moveKingsDown", actual.contains(card));
		}
		// Stack's last element is actually the first element in the internal array
		assertEquals("Second last element should be a king", 13, actual.get(1).getRank());
		assertEquals("Last element should be a king", 13, actual.get(0).getRank());
	}
}
