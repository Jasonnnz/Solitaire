package strategy.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import model.Piles;
import strategy.AcesUpStockStrategy;

public class AcesUpStockStrategyTest {
	private static final AcesUpStockStrategy STRATEGY = new AcesUpStockStrategy();
	
	@Test
	public void testGetInitialSetUp() {
		Deck deck = new Deck();
		ArrayList<Card> toTableau = deck.getCards(4);
		ArrayList<Stack<Card>> setup = STRATEGY.getInitialSetUp(deck);
		Stack<Card> pile = setup.get(0);
		assertEquals("Ace's Up only needs one Stock pile", 1, setup.size());
		assertEquals("A Stock pile shoule hold 48 cards when initialized", 48, pile.size());
		for(Card card : toTableau) {
			if(pile.search(card) != -1) {
				fail("stock pile contains the entire deck "
						+ "except those cards dealt to the tableau piles");
			}
		}
	}
	
	@Test
	public void testGetInitialSetUpWithBadInputs() {
		try {
			STRATEGY.getInitialSetUp(new Deck());
			Deck deck = new Deck();
			deck.getCards(7);
			STRATEGY.getInitialSetUp(deck);
			fail("getInitialSetUp() only accepts a deck with 48 cards, IllegalArgumentException expected");
		} catch(IllegalArgumentException iae) {
			// expected
		}
	}
	
	@Test
	public void testIsAddingLegal() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertFalse("isAddingLegal() should always return false", STRATEGY.isAddingLegal(card, null));
				assertFalse("isAddingLegal() should always return false",
						STRATEGY.isAddingLegal(card, new Card(Suit.CLUB, Rank.ACE)));
			}
		}
	}
	
	@Test
	public void testIsRemovingLegal() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertTrue("It is always okay to remove a card from a Stock pile if the dest is a Tableau pile",
						STRATEGY.isRemovingLegal(card, new Card [0], Piles.TABLEAU));
				assertFalse("It is always illegal to remove a card from a Stock pile if the dest is not a Tableau pile",
						STRATEGY.isRemovingLegal(card, new Card [0], Piles.HOMECELL));
			}
		}
	}
	
	@Test
	public void testIsRemovingLegalWhenPileIsEmpty() {
		assertFalse("It is impossible to remove a card from an empty pile",
				STRATEGY.isRemovingLegal(null, new Card [0], Piles.TABLEAU));
	}
}