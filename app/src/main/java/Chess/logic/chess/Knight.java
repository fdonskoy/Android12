/**
 * @author Filip Donskoy and Timothy Elbert
 */
package Chess.logic.chess;

/**
 * Knight piece on chess board identified as bN or wN for black Knight and white Knight, respectively
 */
public class Knight extends Piece {
	/**
	 * @author Timothy Elbert
	 * @param position to initialize object on board
	 * @param color for white or black
	 * @param king for this piece's team king
	 * @throws Exception if invalid position
	 */
	public Knight(Position position, Color color, King king, Board board) throws Exception {
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
			if(!(	Math.abs(Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(finish.getFile())) ==  2 
				 && Math.abs(Position.rankToIndex(this.getPosition().getRank()) - Position.rankToIndex(finish.getRank())) == 1)
			  &&!(	Math.abs(Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(finish.getFile())) ==  1 
				 && Math.abs(Position.rankToIndex(this.getPosition().getRank()) - Position.rankToIndex(finish.getRank())) == 2)){				
				return TypeOfMove.INVALID;
			}
			
			if(finish.getPiece() != null && finish.getPiece().getColor() == this.getColor()){
				return TypeOfMove.INVALID;
			}
			
			//Perform the move. If this leaves you king in check, undo the move and return false
			Piece taken = finish.getPiece();
			Position start = this.getPosition();
	
			board.move(start, finish);
			if(king.inCheck()){
				board.undoMove(start, finish, taken);
				return TypeOfMove.INVALID;
			}
			if (testMove) {
				board.undoMove(start, finish, taken);
				return TypeOfMove.INVALID;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return TypeOfMove.INVALID;
		}
		
		return TypeOfMove.VALID;
	}

	/**
	 * @author Filip Donskoy
	 * * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	*/
	@Override
	public boolean canMove() {
		Position pos = this.getPosition();
		Position northEast = setNorthEast(pos);
		Position northEast2 = setNorthEast2(pos);
		Position southEast = setSouthEast(pos);
		Position southEast2 = setSouthEast2(pos);
		
		Position northWest = setNorthWest(pos);
		Position northWest2 = setNorthWest2(pos);
		Position southWest = setSouthWest(pos);
		Position southWest2 = setSouthWest2(pos);
		
		testMove = true;
		
		boolean possibleMove = move(northEast) == TypeOfMove.VALID
							|| move(northEast2) == TypeOfMove.VALID
							|| move(southEast) == TypeOfMove.VALID
							|| move(southEast2) == TypeOfMove.VALID
							|| move(northWest) == TypeOfMove.VALID
							|| move(northWest2) == TypeOfMove.VALID
							|| move(southWest) == TypeOfMove.VALID
							|| move(southWest2) == TypeOfMove.VALID;
		testMove = false;
		return possibleMove;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 north 1 east
	 * @return the requested position if it exists, else null
	 */
	private Position setNorthEast(Position pos) {
		if (pos.getNorth() != null) {
			if (pos.getNorth().getNorth() != null) {
				if (pos.getNorth().getNorth().getEast() != null) {
					return pos.getNorth().getNorth().getEast();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 east 1 north
	 * @return the requested position if it exists, else null
	 */
	private Position setNorthEast2(Position pos) {
		if (pos.getEast() != null) {
			if (pos.getEast().getEast() != null) {
				if (pos.getEast().getEast().getNorth() != null) {
					return pos.getEast().getEast().getNorth();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 south 1 east
	 * @return the requested position if it exists, else null
	 */
	private Position setSouthEast(Position pos) {
		if (pos.getSouth() != null) {
			if (pos.getSouth().getSouth() != null) {
				if (pos.getSouth().getSouth().getEast() != null) {
					return pos.getSouth().getSouth().getEast();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 east 1 south
	 * @return the requested position if it exists, else null
	 */
	private Position setSouthEast2(Position pos) {
		if (pos.getEast() != null) {
			if (pos.getEast().getEast() != null) {
				if (pos.getEast().getEast().getSouth() != null) {
					return pos.getEast().getEast().getSouth();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 north 1 west
	 * @return the requested position if it exists, else null
	 */
	private Position setNorthWest(Position pos) {
		if (pos.getNorth() != null) {
			if (pos.getNorth().getNorth() != null) {
				if (pos.getNorth().getNorth().getWest() != null) {
					return pos.getNorth().getNorth().getWest();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 west 1 north
	 * @return the requested position if it exists, else null
	 */
	private Position setNorthWest2(Position pos) {
		if (pos.getWest() != null) {
			if (pos.getWest().getWest() != null) {
				if (pos.getWest().getWest().getNorth() != null) {
					return pos.getWest().getWest().getNorth();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 south 1 west
	 * @return the requested position if it exists, else null
	 */
	private Position setSouthWest(Position pos) {
		if (pos.getSouth() != null) {
			if (pos.getSouth().getSouth() != null) {
				if (pos.getSouth().getSouth().getWest() != null) {
					return pos.getSouth().getSouth().getWest();
				}
			}
		}
		return null;
	}
	/**
	 * @author Filip Donskoy
	 * @param pos for destination position
	 * sets the appropriate possible position to avoid null pointers if outside of the board
	 * 2 west 1 south
	 * @return the requested position if it exists, else null
	 */
	private Position setSouthWest2(Position pos) {
		if (pos.getWest() != null) {
			if (pos.getWest().getWest() != null) {
				if (pos.getWest().getWest().getSouth() != null) {
					return pos.getWest().getWest().getSouth();
				}
			}
		}
		return null;
	}
	
	/**
	 * @author Timothy Elbert
	 * return string wN for white Knight and bN for black Knight
	 */
	public String toString(){
		return ((this.getColor() == Color.White) ? "w" : "b") + "N";
	}

	
}
