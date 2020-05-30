package strategy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;
import strategy.AcesUpHomecellStrategy;

public class AcesUpHomecellStrategyTest {
	private static final AcesUpHomecellStrategy STRATEGY = new AcesUpHomecellStrategy();
	@Test
	public void testGetInitialSetUp() {
		ArrayList<Stack<Card>> setup = STRATEGY.getInitialSetUp(new Deck());
		assertEquals("Ace's up only needs one homecell pile", 1, setup.size());
		assertEquals("homecell pile should be empty when initialized", 0, setup.get(0).size());
	}
	
	@Test
	public void testIsAddingLegal() {
		for(Suit suit : Suit.values()) {
			for(Rank rank : Rank.values()) {
				Card card = new Card(suit, rank);
				assertTrue("It is always legal to add a card to Homecell",
						STRATEGY.isAddingLegal(card, null));
				assertTrue("It is always legal to add a card to Homecell",
						STRATEGY.isAddingLegal(card, new Card(Suit.CLUB, Rank.ACE)));
				assertTrue("It is always legal to add a card to Homecell",
						STRATEGY.isAddingLegal(card, new Card(Suit.DIAMOND, Rank.SEVEN)));
			}
		}
	}
}
