package ui;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import abstraction.Rank;
import abstraction.Suit;
import controller.Controller;
import model.AcesUpGame;
import model.BakersDozenGame;
import model.FreecellGame;

/**
 * UI is an observer. It receives update notifications from observables which it subscribes to.
 * <s>We adopted a very lazy error handling strategy.</s>
 * @author Feng Mao Tsai (Frank)
 * @author Edwin Chiu    (Edwin)
 * @author Mehmet Ozel   (Mehmet)
 */
public class UI implements Runnable {
	/**
	 * Invariants
	 * 
	 * * Once _tileImages is initialized, it guarantees to hold the same elements forever.
	 * * Once _tilesInDisplay is initialized, it guarantees to hold the same instances of JLabel forever.
	 *   However, modifying the state, e.g. chaning their ImageIcons, of the JLabels is allowed.
	 * * Each tile can only have either no {@code MouseListener} or one {@code MouseListener}.
	 */
	/**
	 * Constants
	 * 
	 * public:
	 * ROW                     - the number of tiles that the GUI can display in a column.
	 * COL                     - the number of tiles that the GUI can display in a row.
	 * SELECTED_BORDER_COLOR   - the color of the tile border when a tile is selected.
	 * UNSELECTED_BORDER_COLOR - the color of the tile border when a tile is unselected.
	 * 
	 * private:
	 * IMAGE_PATH - the permanent path to the asset directory.
	 * NOTOPUS    - the image that will be used when a user makes an illegal move.
	 * GUNTOPUS   - the image that will be used when a user drags the NOTOPUS
	 * GOODBYE    - the image that will be used when a user tries to quit the program.
	 * HINT       - the image that will be used when a user find the easter egg.
	 */
	public static final int ROW = 4;
	public static final int COL = 8;
	public static final Color SELECTED_BORDER_COLOR   = Color.RED;
	public static final Color UNSELECTED_BORDER_COLOR = new Color(0.2f, 0.73f, 0.37f);
	
	private static final String IMAGE_PATH = "assets/[].gif";
	private static final ImageIcon NOTOPUS  = new ImageIcon(getImagePath("nope00"));
	private static final ImageIcon GUNTOPUS = new ImageIcon(getImagePath("nope01"));
	private static final ImageIcon GOODBYE  = new ImageIcon(getImagePath("goodbye"));
	private static final ImageIcon HINT     = new ImageIcon(getImagePath("hint"));
	private static final ImageIcon EVIL     = new ImageIcon(getImagePath("evil"));

	/**
	 * Fields
	 * 
	 * _tileImages     - images that will be used to represent tiles.
	 * _tilesInDisplay - tiles which are currently displayed on screen.
	 * _frame          - a window which the user will be using.
	 */
	private HashMap<String, ImageIcon> _tileImages;
	private ArrayList<ArrayList<JLabel>> _tilesInDisplay;
	private JFrame _frame;
	
	/**
	 * Creates and initializes a UI instance and then subscribe the observables automatically.
	 * 
	 * @author Frank
	 */
	public UI() {
		/**
		 * All images must be loaded to memory when an instance of UI is constructed.
		 * The mapping rule is as follow: (filename) maps to (imageIcon of that file)
		 */
		_tileImages = new HashMap<String, ImageIcon>();
		_tilesInDisplay = new ArrayList<ArrayList<JLabel>>();
		Controller.registerObserver(this);
		loadImages();
		initializeTilesInDisplay();
	}

	/**
	 * Updates all images of the displaying tiles with {@code ImageIcon}s corresponding to
	 * the supplied {@code String}s. This method assumes that no card is selected.
	 * If the supplied {@code String} does not correspond to
	 * any {@code ImageIcon}s pre-loaded into the memory or if it is impossible to render the
	 * argument, an {@code IllegalArgumentException} is thrown.
	 * 
	 * @param newTiles - the names of the images which are used to render the new gameboard.
	 * @throws IllegalArgumentException if the dimension of the argument is not 4 by 8 or if
	 * the argument is {@code null}.
	 * @author Frank
	 */
	public void updateAllTileImages(ArrayList<ArrayList<String>> newTiles) {
		if (!hasLegalDimension(newTiles)) {
			throw new IllegalArgumentException("Unable to render the supplied argument.");
		}
		
		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				setBorderAt(null, i, j); // reset border of each tile
				String imageName = newTiles.get(i).get(j);
				updateTileImageAt(imageName, i, j);
				if(!imageName.equals("green")) {
					setUnselectedBorderAt(i, j);
				}
			}
		}
	}

	/**
	 * Updates the image of a tile at a specified location with the {@code ImageIcon}
	 * corresponding to the supplied {@code String}. If the supplied location is out of
	 * the bounds or if the supplied {@code String} does not map to any pre-loaded image,
	 * an {@code IllegalArgumentException} is thrown.
	 * 
	 * @param newTile - the name of the new tile's image.
	 * @param row     - the row position of the tile.
	 * @param col     - the col position of the tile.
	 * @throws IllegalArgumentException if row < 0 || row >= 4 || col < 0 || col >= 4 || newTile == null
	 * or newTile does not map to any pre-loaded image.
	 * @author Edwin
	 * @author Frank (minor changes)
	 */
	public void updateTileImageAt(String newTile, int row, int col) {
		if(!inBound(row, col)) {
			throw new IllegalArgumentException("The specified location is not in the bounds.");
		}
		
		if(newTile == null) {
			throw new IllegalArgumentException("The name of the new image can not be null.");
		}
		
		ImageIcon ii = _tileImages.get(newTile);
		if(ii == null) {
			throw new IllegalArgumentException("The supplied name does not map to any image.");
		}
		
		JLabel l = _tilesInDisplay.get(row).get(col);
		l.setIcon(ii);
	}

	/**
	 * Sets the {@code MouseListener} of each tile to the corresponding {@code MouseListener}.
	 * If {@code null} is an element of the argument, this method will remove the {@code MouseListener}
	 * of that corresponding tile. If the argument does not have a legal dimension,
	 * an {@code IllegalArgumentException} is thrown.
	 * 
	 * @param newListeners - the new {@code MouseListener}s that will be set to the
	 * corresponding tiles.
	 * @throws IllegalArgumentException if the argument does not have a legal dimension.
	 * @author Mehmet
	 * @author Frank (minor changes)
	 */
	public void setAllMouseListeners(ArrayList<ArrayList<MouseListener>> newListeners) {
		if (!hasLegalDimension(newListeners)) {
			throw new IllegalArgumentException("The dimension of the argument must be 4 by 8.");
		}
		for(int i = 0; i < ROW; i++) {
			for(int j = 0; j < COL; j++) {
				ArrayList<MouseListener> list = newListeners.get(i);
				MouseListener listener = list.get(j);
				setMouseListenerAt(listener, i, j);
			}
		}
		
		setMouseListenerAt(new MouseListener() {
			private long _enterTime;
			
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
				_enterTime = System.currentTimeMillis();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				long exitTime = System.currentTimeMillis();
				if(exitTime - _enterTime >= 7000) {
					_tilesInDisplay.get(3).get(0).setIcon(EVIL);
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {	
			}
		}, 3, 0);
	}

	/**
	 * Sets the {@code MouseListener} of a tile at a specified location to the new {@code MouseListener}.
	 * If the specified location is out of bounds, an {@code IllegalArgumentException} is thrown.
	 * 
	 * @param listener - the new {@code MouseListener} of the tile.
	 * @param row      - row position of the tile.
	 * @param col      - col position of the tile.
	 * @throws IllegalArgumentException if row < 0 || row >= 4 or col < 0 || col >= 8
	 * @author Edwin
	 * @author Frank (minor changes)
	 */
	public void setMouseListenerAt(MouseListener listener, int row, int col) {
		/**
		 * Each tile can only have either 0 or 1 MouseListener.
		 */
		if (!inBound(row, col)) {
			throw new IllegalArgumentException("The specified location is out of bounds.");
		}
		
		MouseListener[] l = _tilesInDisplay.get(row).get(col).getMouseListeners();
		// it is okay because there is at most one MouseListener in each component.
		if(l.length != 0) {
			_tilesInDisplay.get(row).get(col).removeMouseListener(l[0]);
		}
		_tilesInDisplay.get(row).get(col).addMouseListener(listener);
	}
	
	/**
	 * Sets the border of a specified tile to selected.
	 * 
	 * @param row - row position of the tile.
	 * @param col - col position of the tile.
	 * @throws IllegalArgumentException if row < 0 || row >= 4 or col < 0 || col >= 8
	 * @author Frank
	 */
	public void setSelectedBorderAt(int row, int col) {
		Border border = BorderFactory.createLineBorder(SELECTED_BORDER_COLOR);
		setBorderAt(border, row, col);
	}
	
	/**
	 * Sets the border of a specified tile to unselected.
	 * 
	 * @param row - row position of the tile.
	 * @param col - col position of the tile.
	 * @throws IllegalArgumentException if row < 0 || row >= 4 or col < 0 || col >= 8
	 * @author Frank
	 */
	public void setUnselectedBorderAt(int row, int col) {
		Border border = BorderFactory.createLineBorder(UNSELECTED_BORDER_COLOR);
		setBorderAt(border, row, col);
	}

	/**
	 * Sets the {@code Border} of the specified tile to the supplied one.
	 * 
	 * @param border - the new {@code Border} of this tile.
	 * @param row    - row position of the tile.
	 * @param col    - col position of the tile.
	 * @throws IllegalArgumentException if row < 0 || row >= 4 or col < 0 || col >= 8
	 * @author Edwin
	 * @author Frank (minor changes)
	 */
	public void setBorderAt(Border border, int row, int col) {
		if(!inBound(row, col)){
			throw new IllegalArgumentException("The specified location is out of bounds.");
		}
		_tilesInDisplay.get(row).get(col).setBorder(border);
	}

	/**
	 * Displays an error message
	 * @author Frank
	 */
	public void displayErrorMessage() {
		JLabel message = new JLabel(NOTOPUS);
		message.setToolTipText("Don't drag me!");
		message.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				message.setToolTipText("I TOLD YOU NOT TO DRAG ME!");
				message.setIcon(GUNTOPUS);
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {}
		});
		JOptionPane.showMessageDialog(_frame, message, "NOPE", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Checks whether or not the specified location is in bounds.
	 * 
	 * @param row - row position to be checked.
	 * @param col - col position to be checked.
	 * @return {@code true} if the specified location is in bounds, {@code false} otherwise.
	 * @author Edwin
	 * @author Frank (minor changes)
	 */
	private boolean inBound(int row, int col) {
		return row >= 0 && row < ROW && col >= 0 && col < COL;
	}

	/**
	 * Checks whether or not the argument has legal dimension. The legal dimension is 4 x 8.
	 * 
	 * @param arr - the argument to be checked.
	 * @return {@code true} if argument has legal dimension, {@code false} otherwise.
	 * @author Frank
	 */
	private <E> boolean hasLegalDimension(ArrayList<ArrayList<E>> arr) {
		if (arr == null) {
			return false;
		} else if (arr.size() != ROW) {
			return false;
		}

		for (int i = 0; i < ROW; i++) {
			if (arr.get(i) == null) {
				return false;
			} else if (arr.get(i).size() != COL) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @see java.lang.Runnable#run()
	 * @author Frank
	 */
	@Override
	public void run() {
		/**
		 * This should run asynchronously on AWT EDT so main() should use
		 * invokeLater(doRun) instead of invokeAndWait(doRun)
		 * 
		 * @see https://docs.oracle.com/javase/7/docs/api/javax/swing/SwingUtilities.html#invokeLater(java.lang.Runnable)
		 */
		_frame = new JFrame("Gimme 100 or I cry");
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().setLayout(new GridLayout(ROW, COL));
		JMenuBar menubar = new JMenuBar();
		JMenu newGameMenu = new JMenu("New Game");
		JMenuItem bakersDozen = new JMenuItem("Baker's Dozen");
		bakersDozen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.instantiateGame(BakersDozenGame.GAME_ID);
			}
		});
		JMenuItem freecell = new JMenuItem("Freecell");
		freecell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.instantiateGame(FreecellGame.GAME_ID);
			}
		});
		JMenuItem acesUp = new JMenuItem("Ace's Up");
		acesUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller.instantiateGame(AcesUpGame.GAME_ID);
			}
		});
		JMenuItem easterEgg = new JMenuItem("???");
		easterEgg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI("https://www.cse.buffalo.edu/~mhertz/"));
						JPanel message = new JPanel();
						message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
						message.add(new JLabel("Professor Hertz's website has an obvious easter egg."
								+ " Why not find that easter egg first?"));
						message.add(new JLabel(HINT));
						JOptionPane.showMessageDialog(_frame, message, "???", JOptionPane.PLAIN_MESSAGE);
					} catch (Exception ex) {
						// do nothing
					}
				}
			}
		});
		newGameMenu.add(bakersDozen);
		newGameMenu.add(freecell);
		newGameMenu.add(acesUp);
		newGameMenu.add(easterEgg);
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JPanel message = new JPanel();
				message.setLayout(new BoxLayout(message, BoxLayout.Y_AXIS));
				message.add(new JLabel(GOODBYE));
				message.add(new JLabel("Leaving your friend?"));
				int option = JOptionPane.showConfirmDialog(_frame, message, "Are you sure... ?", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				
				if(option == JOptionPane.YES_OPTION) {
					_frame.dispose();
					System.exit(0);
				}
			}
		});
		menubar.add(newGameMenu);
		menubar.add(quit);
		for (ArrayList<JLabel> row : _tilesInDisplay) {
			for (JLabel col : row) {
				_frame.add(col);
			}
		}
		_frame.setJMenuBar(menubar);
		_frame.pack();
		_frame.setResizable(false);
		_frame.setVisible(true);
	}
	
	/**
	 * Returns the path to the specified image in the assets folder.
	 * This method assumes that the image exists.
	 * @param imageName - the name, excluding the filename extension, of the image.
	 * @return a {@code String} representation of the path to the specified image.
	 */
	private static String getImagePath(String imageName) {
		return IMAGE_PATH.replace("[]", imageName);
	}

	/**
	 * Loads all images to memory from asset folder.
	 * @author Frank
	 */
	private void loadImages() {
		for (Suit suit : Suit.values()) {
			for (Rank rank : Rank.values()) {
				String imageName = rank.rank() + suit.suit();
				loadSingleImage(imageName);
			}
		}

		loadSingleImage("green");
		loadSingleImage("gold");
	}

	/**
	 * Loads a single image to the memory.
	 * @param imageName
	 * @author Frank
	 */
	private void loadSingleImage(String imageName) {
		String path = IMAGE_PATH.replace("[]", imageName);
		_tileImages.put(imageName, new ImageIcon(path));
	}

	/**
	 * Initializes _tilesInDisplay and generate a welcome screen.
	 * @author Frank
	 */
	private void initializeTilesInDisplay() {
		for (int i = 0; i < ROW; i++) {
			ArrayList<JLabel> row = new ArrayList<JLabel>();
			for (int j = 0; j < COL; j++) {
				JLabel tile = new JLabel(_tileImages.get("green"));
				row.add(tile);
			}
			_tilesInDisplay.add(row);
		}
	}
}
