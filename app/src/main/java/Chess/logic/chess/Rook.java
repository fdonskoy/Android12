/**
 * @author Filip Donskoy and Timothy Elbert
 */
package Chess.logic.chess;


import java.io.Serializable;

/**
 * Rook piece on chess board identified as bR or wR for black Rook and white Rook, respectively
 */
public class Rook extends Piece implements Serializable{
	private static final long serialVersionUID = 344424574567456781L;

	/**keeps track if the piece has moved this game for castling purposes*/
	public boolean hasMoved;
	
	/**
	 * @author Timothy Elbert
	 * @param position to initialize object on board
	 * @param color for white or black
	 * @param king for this piece's team king
	 * @throws Exception if invalid position
	 */
	public Rook(Position position, Color color, King king, Board board) throws Exception {
		super(position, color, king, board);
		hasMoved = false;
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author Timothy Elbert
	 * @param finish the new position the piece is to attempt to move to
	 * @return true if move successful, false otherwise
	 */
	@Override
	public TypeOfMove move(Position finish) {
		if (finish == null) {
			return TypeOfMove.INVALID;
		}
		if(			(this.getPosition().getFile() != finish.getFile() && this.getPosition().getRank() != finish.getRank()) 
				|| 	(this.getPosition().getFile() == finish.getFile() && this.getPosition().getRank() == finish.getRank())){
			return TypeOfMove.INVALID;
		}
	
		//Picking a direction. This should work for any piece, actually, as long as you don't mind moving in Manhattan distance instead of diagonals. Or just add more if statements
		Position cur = this.getPosition();
		do{
			if(cur.getFile() < finish.getFile()){
				cur = cur.getEast();
			} else if(getPosition().getFile() > finish.getFile()){
				cur = cur.getWest();
			} else if(getPosition().getRank() < finish.getRank()){
				cur = cur.getNorth();
			} else if(getPosition().getRank() > finish.getRank()){
				cur = cur.getSouth();
			} else{
				return TypeOfMove.INVALID;
			}
			
			//does not apply for Knights
			//if passing through a piece or landing on own piece
			if(		cur.getPiece() != null 
				 && (cur.getPiece().getColor() == this.getColor() || !cur.equals(finish))){
				return TypeOfMove.INVALID;
			}
		}while(!cur.equals(finish));
		
		//Perform the move. If this leaves you king in check, undo the move and return false
		Piece taken = cur.getPiece();
		Position start = this.getPosition();
		try {
			board.move(start, finish);
			if(!king.checkedBy().isEmpty()){
				board.undoMove(start, finish, taken);
				return TypeOfMove.INVALID;
			}
			if (testMove) {
				board.undoMove(start, finish, taken);
				return TypeOfMove.VALID;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		hasMoved = true;
		return TypeOfMove.VALID;
	}

	/**
	 * @author Timothy Elbert
	 * return string wR for white Rook and bR for black Rook
	 */
	public String toString(){
		return ((this.getColor() == Color.White) ? "w" : "b") + "R";
	}

	/**
	 * @author Filip Donskoy
	 * * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	*/
	@Override
	public boolean canMove() {
		// TODO Auto-generated method stub
		Position north = this.getPosition().getNorth();
		Position south = this.getPosition().getSouth();
		Position east = this.getPosition().getEast();
		Position west = this.getPosition().getWest();


		if (!overrideTestMove) {
			testMove = true;
		}
		
		boolean possibleMove = move(north) != TypeOfMove.INVALID || move(south) != TypeOfMove.INVALID || move(east) != TypeOfMove.INVALID || move(west) != TypeOfMove.INVALID;
		testMove = false;
		return possibleMove;
	}

	@Override
	public Position getValidMove() {
		// TODO Auto-generated method stub

		Position north = this.getPosition().getNorth();
		Position south = this.getPosition().getSouth();
		Position east = this.getPosition().getEast();
		Position west = this.getPosition().getWest();

		if (!overrideTestMove) {
			testMove = true;
		}

		Position retval = null;

		if(move(north) != TypeOfMove.INVALID){
			retval = north;
		}
		else if(move(west) != TypeOfMove.INVALID){
			retval = west;
		}
		else if(move(south) != TypeOfMove.INVALID){
			retval = south;
		}
		else if(move(east) != TypeOfMove.INVALID){
			retval = east;
		}

		testMove = false;
		return retval;
	}

}
