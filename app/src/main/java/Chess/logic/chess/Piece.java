/**
 * @author Timothy Elbert
 */
package Chess.logic.chess;

import java.io.Serializable;

/**
 * The backbone of every type of piece object
 */
public abstract class Piece implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3644637708483215838L;
	/**utility  field for {@link #canMove()} method to test if moves are legal without actually making them*/
	private Position position;
	/**piece color*/
	private Color color;
	/**king of the team this piece is on*/
	King king; 
	/**utility  field for  canMove() to test if moves are legal without actually making them
	 * @author Filip Donskoy*/
	boolean testMove = false;
	protected boolean overrideTestMove = false;
	protected Board board;
	
	/**
	 * @param position to initialize object on board
	 * @param color for white or black
	 * @param king for this piece's team king
	 * @throws Exception if invalid position
	 */
	public Piece(Position position, Color color, King king, Board board) throws Exception{
		this.position = position;
		this.color = color;
		this.king = king;
		this.board = board;
	}
	
	/**
	 * @return current position of this piece
	 */
	public Position getPosition(){
		return position;
	}
	
	/**
	 * @param position: change the current position of the piece from the piece's perspective
	 */
	public void setPosition(Position position){
		this.position = position;
	}
	
	/**
	 * @return the color of the team this piece is on.
	 */
	public Color getColor(){
		return color;
	}
	
	/**
	 * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	*/
	public abstract boolean canMove();
	public abstract Position getValidMove();
	/**@param finish the new position the piece is to attempt to move to
	 * @return true if move successful, false otherwise
	*/
	public abstract TypeOfMove move(Position finish);

	public boolean makeMove() {
		overrideTestMove = true;
		return canMove();
	}
}
