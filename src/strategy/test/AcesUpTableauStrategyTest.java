package strategy.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import model.Piles;
import strategy.AcesUpTableauStrategy;

public class AcesUpTableauStrategyTest {
	private static final AcesUpTableauStrategy STRATEGY = new AcesUpTableauStrategy();
	
	@Test
	public void testGetInitialSetUp() {
		List<Stack<Card>> setup = STRATEGY.getInitialSetUp(new Deck());
		assertEquals("An Ace's Up game should have 4 Tableau piles", 4, setup.size());
		Set<Stack<Card>> pileSet = new HashSet<Stack<Card>>();
		for(Stack<Card> pile : setup) {
			assertEquals("Each pile should have 1 card", 1, pile.size());
			pileSet.add(pile);
		}
		assertEquals("Any two piles must not be the same", 4, pileSet.size());
	}
	
	@Test
	public void testIsAddingLegal() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertTrue("Any card can be added to any empty Tableau pile", STRATEGY.isAddingLegal(card, null));
				assertFalse("Adding a card to a non-empty Tableau pile is illegal", 
						STRATEGY.isAddingLegal(card, new Card(Suit.CLUB, Rank.ACE)));
				assertFalse("Adding a card to a non-empty Tableau pile is illegal",
						STRATEGY.isAddingLegal(card, new Card(Suit.DIAMOND, Rank.KING)));
			}
		}
	}
	
	@Test
	public void testIsAddingNullLegal() {
		try {
			STRATEGY.isAddingLegal(null, null);
			fail("isAddingLegal() must not accept a card whose value is null");
		} catch(IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testIsRemovingToHomecellLegalWithNoEmptyPiles() {
		Card [] otherTops = { new Card(Suit.CLUB, Rank.FIVE),
				              new Card(Suit.HEART, Rank.KING),
				              new Card(Suit.DIAMOND, Rank.SEVEN) };
		assertTrue("When the destination is Homecell, removing 4club should be legal "
				+ "since 5club is a top card of another pile", 
				STRATEGY.isRemovingLegal(new Card(Suit.CLUB, Rank.FOUR), otherTops, Piles.HOMECELL));
		assertTrue("When the destination is Homecell, removing 7heart should be legal "
				+ "since 13heart is a top card of another pile",
				STRATEGY.isRemovingLegal(new Card(Suit.HEART, Rank.SEVEN), otherTops, Piles.HOMECELL));
		assertTrue("When the destination is Homecell, removing 3diamond shoule be legal "
				+ "since 7diamond is a top card of another pile",
				STRATEGY.isRemovingLegal(new Card(Suit.DIAMOND, Rank.THREE), otherTops, Piles.HOMECELL));
		assertFalse("When the destination is Homecell, removing 4spade should be illegal "
				+ "since there is no spades in other top cards",
				STRATEGY.isRemovingLegal(new Card(Suit.SPADE, Rank.FOUR), otherTops, Piles.HOMECELL));
		assertFalse("When the destination is Homecell, removing 1club should be illegal "
				+ "since aces are the highest valued cards",
				STRATEGY.isRemovingLegal(new Card(Suit.CLUB, Rank.ACE), otherTops, Piles.HOMECELL));
		assertFalse("When the destination is Homecell, removing 9diamond should be illegal "
				+ "since no top cards of the same suit has a higher value",
				STRATEGY.isRemovingLegal(new Card(Suit.DIAMOND, Rank.NINE), otherTops, Piles.HOMECELL));
	}
	
	@Test
	public void testIsRemovingToHomecellLegalWithEmptyPiles() {
		Card [] otherTops = { new Card(Suit.SPADE, Rank.SEVEN),
				              new Card(Suit.SPADE, Rank.JACK),
				              null };
		assertTrue("When the destination is Homecell, removing 10spade should be legal "
				+ "since 11spade is a top card of another pile", 
				STRATEGY.isRemovingLegal(new Card(Suit.SPADE, Rank.TEN), otherTops, Piles.HOMECELL));
		assertFalse("When the destination is Homecell, removing 3heart should be illegal "
				+ "since there is no cards of hearts",
				STRATEGY.isRemovingLegal(new Card(Suit.HEART, Rank.THREE), otherTops, Piles.HOMECELL));
		assertFalse("When the destination is Homecell, removing 12heart should be illegal "
				+ "since there is no cards valued higher than this card",
				STRATEGY.isRemovingLegal(new Card(Suit.SPADE, Rank.QUEEN), otherTops, Piles.HOMECELL));
	}
	
	@Test
	public void testIsRemovingToTableauLegalWithNoEmptyPiles() {
		Card [] otherTops = { new Card(Suit.HEART, Rank.ACE),
			   	              new Card(Suit.DIAMOND, Rank.THREE),
			   	              new Card(Suit.CLUB, Rank.SEVEN) };
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertFalse("When the destination is Tableau, removing any card should be illegal "
						+ "since there is no empty piles", STRATEGY.isRemovingLegal(card, otherTops, Piles.TABLEAU));
			}
		}
	}
	
	@Test
	public void testIsRemovingToTableauLegalWithEmptyPiles() {
		Card [] otherTops = { null,
				              new Card(Suit.HEART, Rank.TWO),
				              new Card(Suit.SPADE, Rank.THREE) };
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertTrue("When the destination is Tableau, removing any card should be legal "
						+ "since there is one empty pile", STRATEGY.isRemovingLegal(card, otherTops, Piles.TABLEAU));
			}
		}
	}
	
	@Test
	public void testIsRemovingEmptyLegal() {
		Card [] otherTops = { null,
				              new Card(Suit.DIAMOND, Rank.EIGHT),
				              new Card(Suit.HEART, Rank.FIVE) };
		assertFalse("It is impossible to remove a card from an empty pile",
				STRATEGY.isRemovingLegal(null, otherTops, Piles.HOMECELL));
		assertFalse("It is impossible to remove a card from an empty pile",
				STRATEGY.isRemovingLegal(null, otherTops, Piles.TABLEAU));
	}
	
	@Test
	public void testIsRemovingLegalWithBadInputs() {
		Card [] otherTops = { null,
				              null,
				              new Card(Suit.CLUB, Rank.ACE),
				              new Card(Suit.DIAMOND, Rank.TEN) };
		try {
			STRATEGY.isRemovingLegal(new Card(Suit.DIAMOND ,Rank.FIVE), otherTops, Piles.HOMECELL);
			fail("isRemovingLegal(...) only accepts an array of length 3");
		} catch(IllegalArgumentException iae) {
			// expected
		}
		
		otherTops = new Card [1];
		otherTops[0] = null;
		try {
			STRATEGY.isRemovingLegal(null, otherTops, Piles.TABLEAU);
			fail("null cannot be added, this should induce an IllegalArgumentException");
		} catch(IllegalArgumentException iae) {
			// expected
		}
	}
}
