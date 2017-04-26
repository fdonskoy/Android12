/**
 * @author Filip Donskoy and Timothy Elbert
 */
package Chess.logic.chess;

/**
 * Bishop piece on chess board identified as bB or wB for black Bishop and white Bishop, respectively
 */
public class Bishop extends Piece{
	/**
	 * @author Timothy Elbert
	 * @param position to initialize object on board
	 * @param color for white or black
	 * @param king for this piece's team king
	 * @throws Exception if invalid position
	 */
	public Bishop(Position position, Color color, King king, Board board) throws Exception {
		super(position, color, king, board);
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
		try {
			if(Math.abs(Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(finish.getFile())) != Math.abs(Position.rankToIndex(this.getPosition().getRank()) - Position.rankToIndex(finish.getRank()))){
				return TypeOfMove.INVALID;
			}
			
			//Picking a direction. This should work for any piece, actually, as long as you don't mind moving in Manhattan distance instead of diagonals. Or just add more if statements
			Position cur = this.getPosition();
			do{
				if(cur.getFile() < finish.getFile() && cur.getRank() < finish.getRank()){
					cur = cur.getNorthEast();
				} else if(cur.getFile() > finish.getFile() && cur.getRank() < finish.getRank()){
					cur = cur.getNorthWest();
				} else if(cur.getFile() < finish.getFile() && cur.getRank() > finish.getRank()){
					cur = cur.getSouthEast();
				} else if(cur.getFile() > finish.getFile() && cur.getRank() > finish.getRank()){
					cur = cur.getSouthWest();
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
			return TypeOfMove.INVALID;
		}
		
		return TypeOfMove.VALID;
	}
	
	/**
	 * @author Timothy Elbert
	 * @return string wB for white Bishop and bB for black Bishop
	 */
	public String toString(){
		return ((this.getColor() == Color.White) ? "w" : "b") + "B";
	}
	
	/**
	 * @author Filip Donskoy
	 * * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	*/
	@Override
	public boolean canMove() {
		// TODO Auto-generated method stub
		
		Position northEast = this.getPosition().getNorthEast();
		Position northWest = this.getPosition().getNorthWest();
		Position southEast = this.getPosition().getSouthEast();
		Position southWest = this.getPosition().getSouthWest();

		testMove = true;
		boolean possibleMove = move(northEast) == TypeOfMove.VALID || move(northWest) == TypeOfMove.VALID || move(southEast)  == TypeOfMove.VALID || move(southWest) == TypeOfMove.VALID;
		testMove = false;
		return possibleMove;
	}


}
