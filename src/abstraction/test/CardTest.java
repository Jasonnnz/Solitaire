package abstraction.test;

import static org.junit.Assert.*;

import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;

public class CardTest {
	@Test
	public void testEqualsWithDifferentInstances() {
		assertTrue("Comparing three of clubs with three of clubs; the result should be true but I got false",
				new Card(Suit.CLUB, Rank.THREE).equals(new Card(Suit.CLUB, Rank.THREE)));
		assertTrue("Comparing five of diamonds with five of diamonds; the result should be true but I got false",
				new Card(Suit.DIAMOND, Rank.FIVE).equals(new Card(Suit.DIAMOND, Rank.FIVE)));
		assertTrue("Comparing seven of hearts with seven of hearts; the result should be true but I got false",
				new Card(Suit.HEART, Rank.SEVEN).equals(new Card(Suit.HEART, Rank.SEVEN)));
		assertTrue("Comparing king of spades with king of spades; the result should be true but I got false",
				new Card(Suit.SPADE, Rank.KING).equals(new Card(Suit.SPADE, Rank.KING)));
		assertFalse("Comparing two of spades with three of spades; the result should be false but I got true",
				new Card(Suit.SPADE, Rank.TWO).equals(new Card(Suit.SPADE, Rank.THREE)));
		assertFalse("Comparing queen of diamonds with queen of heart; the result should be false but I got true",
				new Card(Suit.DIAMOND, Rank.QUEEN).equals(new Card(Suit.HEART, Rank.QUEEN)));
		assertFalse("Comparing jack of spades with ace with hearts; the result should be false but I got true",
				new Card(Suit.SPADE, Rank.JACK).equals(new Card(Suit.HEART, Rank.ACE)));
	}
	
	@Test
	public void testEqualsWithSameInstances() {
		Card tenOfClubs = new Card(Suit.CLUB, Rank.TEN);
		Card nineOfHearts = new Card(Suit.HEART, Rank.NINE);
		Card fourOfDiamonds = new Card(Suit.DIAMOND, Rank.FOUR);
		Card eightOfSpades = new Card(Suit.SPADE, Rank.EIGHT);
		
		assertTrue("Comparing ten of clubs with itself; the result should be true but I got false",
				tenOfClubs.equals(tenOfClubs));
		assertTrue("Comparing nine of hearts with itself; the result should be true but I got false",
				nineOfHearts.equals(nineOfHearts));
		assertTrue("Comparing four of diamonds with itself; the result should be true but I got false",
				fourOfDiamonds.equals(fourOfDiamonds));
		assertTrue("Comparing eight of spades with itself; the result should be true but I got false",
				eightOfSpades.equals(eightOfSpades));
	}
	
	@Test
	public void testEqualsWithNull() {
		assertFalse("Comparing ace of hearts with null; the result should be false but I got true",
				new Card(Suit.HEART, Rank.ACE).equals(null));
		assertFalse("Comparing two of clubs with null; the result should be false but I got true",
				new Card(Suit.CLUB, Rank.TWO).equals(null));
		assertFalse("Comparing six of spades with null; the result should be false but I got true",
				new Card(Suit.SPADE, Rank.SIX).equals(null));
		assertFalse("Comparing seven of diamonds with null; the result should be false but I got true",
				new Card(Suit.DIAMOND, Rank.SEVEN).equals(null));
	}
	
	@Test
	public void testToString() {
		assertEquals(new Card(Suit.CLUB, Rank.ACE).toString(), "1club");
		assertEquals(new Card(Suit.DIAMOND, Rank.KING).toString(), "13diamond");
		assertEquals(new Card(Suit.HEART, Rank.JACK).toString(), "11heart");
	}
}
