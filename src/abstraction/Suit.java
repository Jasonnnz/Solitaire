package abstraction;

/**
 * {@code String} enumeration of suit in a solitaire game.
 * It it includes <i>suit</i> and the <i>color</i> of that suit.
 */
public enum Suit {
	/**
	 * String representations of a suits and their colors.
	 */
	CLUB("club", "black"), DIAMOND("diamond", "red"), HEART("heart", "red"), SPADE("spade", "black");
	
	/**
	 * _suit - value of suit.
	 * _color - value of color.
	 */
	private final String _suit;
	private final String _color;
	
	private Suit(String suit, String color) {
		_suit = suit;
		_color = color;
	}
	
	/**
	 * @return A {@code String} representaion of this instance of {@link Suit}.
	 */
	public String suit() {
		return _suit;
	}
	
	/**
	 * @return A {@code String} representaion of the <i>color</i> of this instance of {@link Suit}.
	 */
	public String color() {
		return _color;
	}
}
