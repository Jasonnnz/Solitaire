package abstraction;

/**
 * Instances of this class represent playcards.
 */
public class Card {
	/**
	 * _suit - representation of suit of a {@code Card} instance.
	 * _rank - representation of rank of a {@code Card} instance.
	 */
	private Suit _suit;
	private Rank _rank;

	/**
	 * Create an instance of {@link Card}.
	 * @param suit - the {@link Suit} of the {@link Card} that is being created.
	 * @param rank - the rank of the card that is being created.
	 */
	public Card(Suit suit, Rank rank) {
		_suit = suit;
		_rank = rank;
	}

	/**
	 * @return A {@code String} representaion of the {@link Suit} of this instance of {@link Card}.
	 */
	public String getSuit() {
		return _suit.suit();
	}

	/**
	 * @return A {@code String} representaion of <i>color</i> of the {@link Suit} of this instance of {@link Card}.
	 */
	public String getColor() {
		return _suit.color();
	}

	/**
	 * @return A {@code int} representaion of the {@link Rank} of this instance of {@link Card}.
	 */
	public int getRank() {
		return _rank.rank();
	}
	
	/**
	 * Compare this object with another object and determine whether they are the same.
	 * @param obj - the reference object with which to compare.
	 * @return True if this object is the same as argument obj; false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		String suit = _suit.suit();
		int rank = _rank.rank();
		Card other = (Card)obj;
		return (other.getRank() == rank) && (suit.equals(other.getSuit()));
	}
	
	/**
	 * @return A {@code String} representation of the object
	 */
	@Override
	public String toString() {
		return _rank.rank() + _suit.suit();
	}
}
