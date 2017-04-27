/**
 * @author Filip Donskoy and Timothy Elbert
 */
package Chess.logic.chess;

/**
 * Pawn piece on chess board identified as bP or wP for black Pawn and white Pawn, respectively
 */
public class Pawn extends Piece {
	/**
	 * @author Timothy Elbert
	 * used when pawn is requested to move up two tiles and in en passant for move history
	 */
	int lastTurnMoved = 0;
	boolean up2 = false;
	
	/**
	 * @author Timothy Elbert
	 * @param position to initialize object on board
	 * @param color for white or black
	 * @param king for this piece's team king
	 * Constructor for Pawn, calls super constructor of parent class Piece
	 * @throws Exception if couldn't create object
	 */
	public Pawn(Position position, Color color, King king, Board board) throws Exception {
		super(position, color, king, board);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @author Filip Donskoy
	 * @param finish the new position the piece is to attempt to move to
	 * @return true if move successful, false otherwise
	 */
	@Override
	//if pawn is in initial row, can move two squares, else only one square
	//also check for en passant and promotion in move pawn
	public TypeOfMove move(Position finish) {
		if (finish == null) {
			return  TypeOfMove.INVALID;
		}
		if (this.getPosition().getRank() - finish.getRank() > 2) {
			return TypeOfMove.INVALID;
		}
		else if (this.getPosition().getRank() - finish.getRank() == 2 && this.lastTurnMoved != 0 ) {
			return TypeOfMove.INVALID;
		}
		// TODO Auto-generated method stub
		Position current = this.getPosition();
		Piece taken = finish.getPiece();
		Position start = this.getPosition();
		//System.out.println("This is taken: " + taken);
		
		if (this.getColor().equals(Color.White)){
			int diff = finish.getRank()- current.getRank();
			//if same file, can only finish 1 up or 2 up, check obstruction
			//if not same file, must have obstruction to eat on the side AND must not put king in check
			if (current.getFile() == finish.getFile()) {
				if (diff <=2 && current.getRank() < finish.getRank()) {
					if (this.lastTurnMoved == 0) {
						if (diff == 1 && current.getNorth().getPiece() == null) {
							//check for queen promotion

							return movePawn(start, finish, taken);
						}
						if (diff == 2 && current.getNorth().getPiece() == null && current.getNorth().getNorth().getPiece() == null) {
							return movePawn(start, finish, taken);
						}
					}
					else if (diff == 1 && current.getNorth().getPiece() == null) {
						return movePawn(start, finish, taken);
					}
				}
			}
			else {
				//Pawn eats
				int fileDiff = 0;
				try {
					fileDiff = Math.abs(Position.fileToIndex(current.getFile()) - Position.fileToIndex(finish.getFile()) );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("Diff " + diff + " fileDiff " + fileDiff + " obstruction " + taken);
				if (diff == 1 && fileDiff == 1 && taken != null && taken.getColor() == Color.Black) {
					return movePawn(start, finish, taken);
				}
				else if (diff == 1 && fileDiff == 1 && taken == null) {
					//EN PASSANT
					return passant(finish);
				}
				
			}
			
		}
		//black moves
		else{
			int diff = current.getRank() - finish.getRank();
			//if same file, can only finish 1 up or 2 up, check obstruction
			//if not same file, must have obstruction to eat on the side AND must not put king in check
			if (current.getFile() == finish.getFile()) {
				if (diff <=2 && current.getRank() > finish.getRank()) {
					if (this.lastTurnMoved == 0) {
						if (diff == 1 && current.getSouth().getPiece() == null) {
							return movePawn(start, finish, taken);
						}
						if (diff == 2 && current.getSouth().getPiece() == null && current.getSouth().getSouth().getPiece() == null) {
							return movePawn(start, finish, taken);
						}
					}
					else if (diff == 1 && current.getSouth().getPiece() == null) {
						return movePawn(start, finish, taken);
					}
				}
			}
			else {
				//Pawn eats
				int fileDiff = 0;
				try {
					fileDiff = Math.abs(Position.fileToIndex(current.getFile()) - Position.fileToIndex(finish.getFile()) );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("Diff " + diff + " fileDiff " + fileDiff + " obstruction " + finish.getPiece());
				if (diff == 1 && fileDiff == 1 && finish.getPiece() != null && taken.getColor() == Color.White) {
					return movePawn(start, finish, taken);
				}
				else if (diff == 1 && fileDiff == 1 && taken == null) {
					//EN PASSANT
					return passant(finish);
				}
				
			}
		}
		
		//Perform the move. If this leaves you king in check, undo the move and return false
		return TypeOfMove.INVALID;
	}
	/**
	 * @author Filip Donskoy
	 * @param start for start position of pawn
	 * @param finish for destination position
	 * @param taken for piece, if any on the finish position tile
	 * moves the pawn, providing it doesn't leave the king in check
	 * @return true if the move was successful
	 */
	private TypeOfMove movePawn(Position start, Position finish, Piece taken) {
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

		if (Math.abs(start.getRank() - finish.getRank()) == 2) {
			this.up2 = true;
		}
		else {
			this.up2 = false;
		}
		this.lastTurnMoved = board.turnNum;

		return promotion();
	}
	/**
	 * @author Filip Donskoy
	 * Replaces the pawn with the requested piece
	 * Places a Queen by default
	 */
	private TypeOfMove promotion(){
		if ( (this.getPosition().getRank() == 8 && this.getColor() == Color.White) || (this.getPosition().getRank() == 1 && this.getColor() == Color.Black)) {
			try {
				this.getPosition().setPiece(null);
				if (CurrentGame.getInput() == null || CurrentGame.getInput().equals("Q")) {
					this.getPosition().setPiece(new Queen(this.getPosition(), this.getColor(), king, this.board));
				}else if (CurrentGame.getInput().equals("N")) {
					this.getPosition().setPiece(new Knight(this.getPosition(), this.getColor(), king, this.board));
				}
				else if (CurrentGame.getInput().equals("R")) {
					this.getPosition().setPiece(new Rook(this.getPosition(), this.getColor(), king, this.board));
				}
				else if (CurrentGame.getInput().equals("B")) {
					this.getPosition().setPiece(new Bishop(this.getPosition(), this.getColor(), king, this.board));
				}
				
				return TypeOfMove.PROMOTION;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return TypeOfMove.INVALID;
			}
		}
		return TypeOfMove.VALID;
	}
	/**
	 * @author Filip Donskoy
	 * @param target for destination position
	 * Uses lastTurnMoved to see if the opposing pawn had moved 2 tiles in the previous move
	 * @return true if en passant is a valid move for this pawn
	 */
	private TypeOfMove passant(Position target) {
		if (this.getColor() == Color.White) {
			if (target.getSouth().getPiece() != null && target.getSouth().getPiece().getClass() == Pawn.class) {
				Pawn p = (Pawn) target.getSouth().getPiece();
				if (p.getColor() != this.getColor() && p.lastTurnMoved == board.turnNum  && p.up2) {
					if (movePawn(this.getPosition(), target, p) != TypeOfMove.INVALID) {
						p.getPosition().setPiece(null);
						return TypeOfMove.EN_PASSANT;
						//return TypeOfMove.VALID;
					}
				}
			}
		}
		else {
			if (target.getNorth().getPiece() != null && target.getNorth().getPiece().getClass() == Pawn.class) {
				Pawn p = (Pawn) target.getNorth().getPiece();
				if (p.getColor() != this.getColor() && p.lastTurnMoved == board.turnNum && p.up2) {
					if (movePawn(this.getPosition(), target, p) != TypeOfMove.INVALID) {
						p.getPosition().setPiece(null);
						return TypeOfMove.EN_PASSANT;
						//return TypeOfMove.VALID;
					}
				}
			}
		}
		return TypeOfMove.INVALID;
	}
	/**
	 * @author Timothy Elbert
	 * return string wP for white Pawn and bP for black Pawn
	 */
	public String toString(){
		return ((this.getColor() == Color.White) ? "w" : "b") + "P";
	}

	/**
	 * @author Filip Donskoy
	 * * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	*/
	@Override
	public boolean canMove() {
		//test for 1 tile up, 2 tiles up (white and black) && getfile is at start, eat, en passant
		// TODO Auto-generated method stub
		
		Position northNorth = this.getPosition();
		Position southSouth = this.getPosition();
		Position north = this.getPosition().getNorth();
		if (north != null) {
			northNorth = this.getPosition().getNorth().getNorth();
		}
		Position south = this.getPosition().getSouth();
		if (south != null) {
			southSouth = this.getPosition().getSouth().getSouth();
		}
		
		
		Position northEast = this.getPosition().getNorthEast();
		Position northWest = this.getPosition().getNorthWest();
		Position southEast = this.getPosition().getSouthEast();
		Position southWest = this.getPosition().getSouthWest();

		if (!overrideTestMove) {
			testMove = true;
		}
		
		boolean possibleMove = move(north) != TypeOfMove.INVALID
							|| move(northNorth) != TypeOfMove.INVALID
							|| move(south) != TypeOfMove.INVALID
							|| move(southSouth) != TypeOfMove.INVALID
							|| move(northEast) != TypeOfMove.INVALID
							|| move(northWest) != TypeOfMove.INVALID
							|| move(southEast) != TypeOfMove.INVALID
							|| move(southWest) != TypeOfMove.INVALID;

		testMove = false;
		return possibleMove;
	}

	
}
