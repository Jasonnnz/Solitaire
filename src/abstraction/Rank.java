package abstraction;

/**
 * Numeric enumeration of rank in a solitaire game.
 */
public enum Rank {
	/**
	 * Numeric representations of ranks.
	 */
	ACE(1), TWO(2), THREE(3),
	FOUR(4), FIVE(5), SIX(6),
	SEVEN(7), EIGHT(8), NINE(9),
	TEN(10), JACK(11), QUEEN(12),
	KING(13);
	
	/**
	 * _rank - value of rank.
	 */
	private final int _rank;
	
	private Rank(int rank) {
		_rank = rank;
	}
	
	/** 
	 * @return An {@code int} representaion of {@code Rank}.
	 */
	public int rank() {
		return _rank;
	}
}
