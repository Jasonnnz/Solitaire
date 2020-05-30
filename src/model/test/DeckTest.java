package model.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import abstraction.Card;
import abstraction.Rank;
import abstraction.Suit;
import model.Deck;

@SuppressWarnings("unchecked")
public class DeckTest {
	Deck deck;
	Field deckField;
	
	@Before
	public void setUpTest() throws Exception {
		deck = new Deck();
		deckField = getDeckField();
		deckField.setAccessible(true);
	}
	
	@Test
	public void testConstructor() {
		try {
			ArrayList<Card> cardsInDeck = (ArrayList<Card>) deckField.get(deck);
			assertNotNull("field must be initialized when an instance of Deck is created", cardsInDeck);
			assertEquals("A newly instantiated deck must have 52 cards", 52, cardsInDeck.size());
			
			for(Suit suit : Suit.values()) {
				for(Rank rank : Rank.values()) {
					Card card = new Card(suit, rank);
					assertTrue("Every card in the deck must be unique but I got 2 or more " + card.toString(),
							cardsInDeck.contains(card));
				}
			}
			
			/* 
			 * Randomness test too hard to implement. But since the chance for two randomly generated sequences
			 * being the same is extremely low(1/52! ~ 6.45e-26), we can use this fact to test the randomness of a newly
			 * instantiated deck.
			 */
			Deck testRandomness = new Deck();
			ArrayList<Card> cardsInTestRandomness = (ArrayList<Card>) deckField.get(testRandomness);
			assertNotEquals("Cards must be shuffled", cardsInTestRandomness, cardsInDeck);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testGetCardsWithArgumentMuchLessThanDeckSize() {
		try {
			ArrayList<Card> cardsInDeck = (ArrayList<Card>) deckField.get(deck);
			
			ArrayList<Card> expectedRetVal = getExpectedRetVal(cardsInDeck, 2);
			ArrayList<Card> expectedRemainders = getExpectedRemainders(cardsInDeck, 2);
			ArrayList<Card> actual = deck.getCards(2);
			assertEquals("Requesting 2 cards from a new deck", 2, actual.size());
			assertEquals("Requesting 2 cards from a new deck", expectedRetVal, actual);
			assertEquals("Giving out 2 cards from a new deck", 50, cardsInDeck.size());
			assertEquals("Giving out 2 cards from a new deck", expectedRemainders, cardsInDeck);
			
			expectedRetVal = getExpectedRetVal(cardsInDeck, 3);
			expectedRemainders = getExpectedRemainders(cardsInDeck, 3);
			actual = deck.getCards(3);
			assertEquals("Requesting 3 cards from a deck that has 50 cards remaining", 3, actual.size());
			assertEquals("Requesting 3 cards from a deck that has 50 cards remaining", expectedRetVal, actual);
			assertEquals("Giving out 3 cards from a deck that has 50 cards remaining", 47, cardsInDeck.size());
			assertEquals("Giving out 3 cards from a deck that has 50 cards remaining", expectedRemainders, cardsInDeck);
			
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testGetCardsWithArgumentAboutHalfOfDeckSize() {
		try {
			ArrayList<Card> cardsInDeck = (ArrayList<Card>) deckField.get(deck);
			
			ArrayList<Card> expectedRetVal = getExpectedRetVal(cardsInDeck, 27);
			ArrayList<Card> expectedRemainders = getExpectedRemainders(cardsInDeck, 27);
			ArrayList<Card> actual = deck.getCards(27);
			assertEquals("Requesting 27 cards from a new deck", 27, actual.size());
			assertEquals("Requesting 27 cards from a new deck", expectedRetVal, actual);
			assertEquals("Giving out 27 cards from a new deck", 25, cardsInDeck.size());
			assertEquals("Giving out 27 cards from a new deck", expectedRemainders, cardsInDeck);
			
			expectedRetVal = getExpectedRetVal(cardsInDeck, 25);
			expectedRemainders = getExpectedRemainders(cardsInDeck, 25);
			actual = deck.getCards(25);
			assertEquals("Requesting 25 cards from a deck that has 25 cards remaining", 25, actual.size());
			assertEquals("Requesting 25 cards from a deck that has 25 cards remaining", expectedRetVal, actual);
			assertEquals("Giving out 25 cards from a deck that has 25 cards remaining", 0, cardsInDeck.size());
			assertEquals("Giving out 25 cards from a deck that has 25 cards remaining", expectedRemainders, cardsInDeck);
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testGetCardsWithArgumentAlmostEqualDeckSize() {
		try {
			ArrayList<Card> cardsInDeck = (ArrayList<Card>) deckField.get(deck);
			
			ArrayList<Card> expectedRetVal = getExpectedRetVal(cardsInDeck, 50);
			ArrayList<Card> expectedRemainders = getExpectedRemainders(cardsInDeck, 50);
			ArrayList<Card> actual = deck.getCards(50);
			assertEquals("Requesting 50 cards from a new deck", 50, actual.size());
			assertEquals("Requesting 50 cards from a new deck", expectedRetVal, actual);
			assertEquals("Giving out 50 cards from a new deck", 2, cardsInDeck.size());
			assertEquals("Giving out 50 cards from a new deck", expectedRemainders, cardsInDeck);
			
			try {
				deck.getCards(49);
				fail("Requesting 49 cards from a deck that has 2 cards remaining. IndexOutOfBoundsException expected");
			} catch(IndexOutOfBoundsException e) {
				// expected
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("Unexpected exception. Contact Frank ASAP");
		}
	}
	
	@Test
	public void testGetCardsWithIllegalArguments() {
		try {
			deck.getCards(0);
			fail("Requesting 0 card from the deck. IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// expected
		}
		
		try {
			deck.getCards(-1);
			fail("Requesting -1 card from the deck. IllegalArgumentException expected");
		} catch(IllegalArgumentException e) {
			// expected
		}
	}
	
	private Field getDeckField() throws Exception {
		return Deck.class.getDeclaredField("_deck");
	}
	
	private ArrayList<Card> getExpectedRetVal(ArrayList<Card> cardsInDeck, int n) {
		return new ArrayList<Card>(cardsInDeck.subList(0, n));
	}
	
	private ArrayList<Card> getExpectedRemainders(ArrayList<Card> cardsInDeck, int n) {
		return new ArrayList<Card>(cardsInDeck.subList(n, cardsInDeck.size()));
	}
}
