package handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import controller.Controller;

/**
 * A {@code MouseEventHandler} that keeps track of of the position and pile type of each tile.
 * @author Feng Mao Tsai (Frank)
 */
public class MouseEventHandler implements MouseListener {
	/**
	 * Fields
	 * 
	 * Any tile that is not a pile should not register this handler.
	 * _rowPos   - the row position of tile which registers this handler.
	 * _colPos   - the col position of tile which registers this handler.
	 * _pileType - the type of the pile of the tile which registers this handler.
	 * _pilePos  - the tile's position in this pile.
	 */
	private int _rowPos;
	private int _colPos;
	private int _pileType;
	private int _pilePos;
	
	public MouseEventHandler(int rowPos, int colPos, int pileType, int pilePos) {
		_rowPos = rowPos;
		_colPos = colPos;
		_pileType = pileType;
		_pilePos = pilePos;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Controller.select(_rowPos, _colPos, _pileType, _pilePos);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
