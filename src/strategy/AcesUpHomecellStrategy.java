package strategy;

import java.util.ArrayList;
import java.util.Stack;

import abstraction.Card;
import model.Deck;

/**
 * Instances of this class contain algorithms necessary for a Homecell pile in an Ace's Up game.
 * Even though all methods of this class do not depend on their instances, for design reasons they
 * are not made static.
 */
/*
 * Inheritance makes sense because AcesUpHomecellStrategy IS-A HomecellStrategy
 */
public class AcesUpHomecellStrategy extends HomecellStrategy {
	/**
	 * Ace's up has one Homecell pile. It hold 0 cards initially
	 * @param deck - this parameter is ignored.
	 */
	@Override
	public ArrayList<Stack<Card>> getInitialSetUp(Deck deck) {
		ArrayList<Stack<Card>> a = new ArrayList<Stack<Card>>();
		Stack<Card> h = new Stack<Card>();
		a.add(h);
		return a;
	}
	
	/**
	 * Cards can always be added to a Homecell pile.
	 */
	@Override
	public boolean isAddingLegal(Card card, Card top) {
		return true;
	}
}
