/**
 * @author Filip Donskoy and Timothy Elbert
 */
package Chess.logic.chess;

import java.util.LinkedList;

/**
 * King piece on chess board identified as bK or wK for black King and white King, respectively
 */
public class King extends Piece{
	/**keeps track if the piece has moved this game for castling purposes*/
	private boolean hasMoved;
	/**lists all the pieces on the king's team currently active on the field*/
	public LinkedList<Piece> team;
	
	/**
	 * @author Timothy Elbert
	 * @param position to initialize object on board
	 * @param color for white or black
	 * Constructor for King, calls super constructor of parent class Piece
	 * @throws Exception if invalid position
	 */
	public King(Position position, Color color, Board board) throws Exception {
		super(position, color, null, board);
		// TODO Auto-generated constructor stub
		team = new LinkedList<Piece>();
		hasMoved = false;
	}
	
	/**
	 * @author Filip Donskoy
	 * Assumes the king is not currently in check
	 * @return true if this piece can legally move
	 */
	@Override
	public boolean move(Position finish){
		// TODO Auto-generated method stub
		try {
			//castle allows the king to move only 2 squares in a file direction
			if (Math.abs(Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(finish.getFile())) == 2 &&
					Math.abs(Position.rankToIndex(this.getPosition().getRank()) - Position.rankToIndex(finish.getRank())) == 0) {
				if (!this.inCheck()) {
					return castle(finish);
				}
				else {
					return false;
				}
			}

			//Move request must be at most 1 position away
			boolean filediff = false;
			boolean rankdiff = false;
			if (Math.abs(Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(finish.getFile())) == 1){
				filediff = true;
			}
			if (Math.abs(Position.rankToIndex(this.getPosition().getRank()) - Position.rankToIndex(finish.getRank())) == 1){
				rankdiff = true;
			}
			if (!((!filediff && rankdiff) || (filediff && !rankdiff) || (filediff && rankdiff))) {
				return false;
			}
			
			Position cur = this.getPosition();
			if(cur.getFile() < finish.getFile() && cur.getRank() < finish.getRank()){
				cur = cur.getNorthEast();
			} else if(cur.getFile() > finish.getFile() && cur.getRank() < finish.getRank()){
				cur = cur.getNorthWest();
			} else if(cur.getFile() < finish.getFile() && cur.getRank() > finish.getRank()){
				cur = cur.getSouthEast();
			} else if(cur.getFile() > finish.getFile() && cur.getRank() > finish.getRank()){
				cur = cur.getSouthWest();
			} else if(cur.getFile() < finish.getFile()){
				cur = cur.getEast();
			} else if(getPosition().getFile() > finish.getFile()){
				cur = cur.getWest();
			} else if(getPosition().getRank() < finish.getRank()){
				cur = cur.getNorth();
			} else if(getPosition().getRank() > finish.getRank()){
				cur = cur.getSouth();
			} else{
				return false;
			}
			//does not apply for Knights
			//if passing through a piece or landing on own piece
			if(		cur.getPiece() != null 
				 && (cur.getPiece().getColor() == this.getColor() || !cur.equals(finish))){
				return false;
			}
			
			//Perform the move. If this leaves you king in check, undo the move and return false
			Piece taken = cur.getPiece();
			Position start = this.getPosition();
			board.move(start, finish);
			if(!this.checkedBy().isEmpty()){
				board.undoMove(start, finish, taken);
				return false;
			}
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hasMoved = true;
		return true;
	}
	
	/**
	 * @author Filip Donskoy
	 * @param move for destination position
	 * Checks for castle by seeing if the rook and king have moved and if the king moves through checked tiles
	 * @throws Exception if invalid position
	 * @return true if the move is valid and successful
	 */
	private boolean castle(Position move) throws Exception {
		Rook rook = null;
		String direction = null;
		//initializes the correct rook depending on the position move relative to the king
		if (!this.hasMoved && Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(move.getFile()) < 0 &&
				move.getEast().getPiece() != null) {	
			if (move.getEast().getPiece().getClass() == Rook.class) {
				rook = (Rook) move.getEast().getPiece();
				direction = "east";
			}
			else return false;
		}
		else if (!this.hasMoved && Position.fileToIndex(this.getPosition().getFile()) - Position.fileToIndex(move.getFile()) > 0 &&
				move.getWest().getWest().getPiece() != null){
			
			if (move.getWest().getWest().getPiece().getClass() == Rook.class) {
				rook = (Rook) move.getWest().getWest().getPiece();
				direction = "west";
			}
			else return false;
		}
		else {
			return false;
		}
		
		
		if (rook.hasMoved) {
			return false;
		}
		
		Position cur = this.getPosition();
		do{
			if(cur.getFile() < rook.getPosition().getFile()){
				cur = cur.getEast();
			} else if(getPosition().getFile() > rook.getPosition().getFile()){
				cur = cur.getWest();
			} else{
				return false;
			}
			
			if(cur.getPiece() != null){
				return false;
			}
		}while(!cur.equals(move));
		

		return moveCastle(move, rook, direction);
	}
	/**
	 * @author Filip Donskoy
	 * @param move to initialize object on board
	 * @param rook for the rook being used in castle identified from previous method
	 * @param direction to identify the direction for the king to travel
	 * Is only called if the castle request is a valid castle
	 * @return false is king passes through a tile that puts the king in check, true if valid
	 */
	private boolean moveCastle(Position move, Rook rook, String direction) {
		
		//Perform the move. If this leaves you king in check, undo the move and return false
		Position start = this.getPosition();
		Position current = start;
		try {
			if (direction.equals("west")) {
				current = start.getWest();
				while (current != rook.getPosition()) {
					if (current.getPiece() != null) {
						return false;
					}
					current = current.getWest();
				}
				board.move(start, start.getWest());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(start, start.getWest(),  null); //start.getWest().getPiece()
					return false;
				}
				start = this.getPosition();
				board.move(start, start.getWest());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(start, start.getWest(),  null);
					start = this.getPosition().getEast();
					board.undoMove(start, start.getWest(),  null);
					return false;
				}
				start = this.getPosition();
				board.move(rook.getPosition(), start.getEast());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(rook.getPosition(), start.getEast(),  null);//start.getEast().getPiece()
					start = this.getPosition().getEast();
					board.undoMove(start, start.getWest(),  null);
					start = this.getPosition().getEast();
					board.undoMove(start, start.getWest(),  null);
					return false;
				}	
			}
			else {
				current = start.getEast();
				while (current != rook.getPosition()) {
					if (current.getPiece() != null) {
						return false;
					}
					current = current.getEast();
				}
				board.move(start, start.getEast());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(start, start.getEast(), null); //start.getEast().getPiece()
					return false;
				}	
				start = this.getPosition();
				board.move(start, start.getEast());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(start, start.getEast(),  null);
					start = this.getPosition().getWest();
					board.undoMove(start, start.getEast(),  null);
					return false;
				}	
				start = this.getPosition();
				board.move(rook.getPosition(), start.getWest());
				if(!this.checkedBy().isEmpty()){
					board.undoMove(rook.getPosition(), start.getWest(),  null);
					start = this.getPosition().getWest();
					board.undoMove(start, start.getEast(),  null);
					start = this.getPosition().getWest();
					board.undoMove(start, start.getEast(),  null);
					return false;
				}	
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rook.hasMoved = true;
		return true;
		
	}

	/**
	 * @author Timothy Elbert
	 * @return true if can make a legal move and false otherwise
	 */
	//checks if king can move from target positions
	public boolean canMove() {
		// TODO Auto-generated method stub
		boolean north = !(this.getPosition().getNorth() == null 		|| (this.getPosition().getNorth().getPiece() != null 		&& this.getPosition().getNorth().getPiece().getColor() == this.getColor()		)|| !this.getPosition().getNorth().getAttackers(this.getColor()).isEmpty());
		boolean northEast = !(this.getPosition().getNorthEast() == null || (this.getPosition().getNorthEast().getPiece() != null 	&& this.getPosition().getNorthEast().getPiece().getColor() == this.getColor()	)|| !this.getPosition().getNorthEast().getAttackers(this.getColor()).isEmpty()); 
		boolean east = !(this.getPosition().getNorthEast() == null 		|| (this.getPosition().getEast().getPiece() != null			&& this.getPosition().getEast().getPiece().getColor() == this.getColor()		)|| !this.getPosition().getEast().getAttackers(this.getColor()).isEmpty());
		boolean southEast = !(this.getPosition().getSouthEast() == null || (this.getPosition().getSouthEast().getPiece() != null 	&& this.getPosition().getSouthEast().getPiece().getColor() == this.getColor()	)|| !this.getPosition().getSouthEast().getAttackers(this.getColor()).isEmpty());
		boolean south = !(this.getPosition().getSouth() == null 		|| (this.getPosition().getSouth().getPiece() != null		&& this.getPosition().getSouth().getPiece().getColor() == this.getColor()		)|| !this.getPosition().getSouth().getAttackers(this.getColor()).isEmpty());
		boolean southWest = !(this.getPosition().getSouthWest() == null || (this.getPosition().getSouthWest().getPiece() != null 	&& this.getPosition().getSouthWest().getPiece().getColor() == this.getColor()	)|| !this.getPosition().getSouthWest().getAttackers(this.getColor()).isEmpty());
		boolean west = !(this.getPosition().getWest() == null 			|| (this.getPosition().getWest().getPiece() != null			&& this.getPosition().getWest().getPiece().getColor() == this.getColor()		)|| !this.getPosition().getWest().getAttackers(this.getColor()).isEmpty()); 
		boolean northWest = !(this.getPosition().getNorthWest() == null || (this.getPosition().getNorthWest().getPiece() != null 	&& this.getPosition().getNorthWest().getPiece().getColor() == this.getColor()	)|| !this.getPosition().getNorthWest().getAttackers(this.getColor()).isEmpty());
				
		return 	north || northEast || east || southEast || south || southWest || west || northWest;
	}

	/**
	 * @author Timothy Elbert
	 * @return linked list of pieces attacking this king
	*/
	public LinkedList<Piece> checkedBy() {
		return getPosition().getAttackers();
	}

	/**
	 * @author Timothy Elbert
	 * @return true if this king is in check
	 */
	public boolean inCheck() {
		return !getPosition().getAttackers().isEmpty();
	}

	/**
	 * @author Timothy Elbert
	 * @return true if king is in check mate. ie: nowhere to go, in check, and no piece can intercept the attacker in any way
	 */
	public boolean checkmate(){
		LinkedList<Piece> attackers = this.checkedBy();
		boolean attackerCanBeBlockedOrTaken = false;
		boolean inCheck = inCheck();
		boolean canMove = canMove();
		
		//check if attacker can be blocked or taken
		//only possible if there is just one attacker
		if(attackers.size() == 1){
			attackerCanBeBlockedOrTaken = attackerCanBeBlocked(attackers.removeFirst());
		}
		
		//there are attackers and can't move and attackers can't be intercepted or blocked
		if (inCheck() && !canMove && !attackerCanBeBlockedOrTaken) {
			System.out.println("Checkmate");
			return true;
		}
		return false;
	}
	
	/**
	 * @author Timothy Elbert
	 * @return string wK for white King and bK for black King
	 */
	public String toString(){
		return ((this.getColor() == Color.White) ? "w" : "b") + "K";
	}

	/**
	 * @author Timothy Elbert
	 * helper method for checkmate that determines if an attacker can be legally intercepted by another piece
	 * @param attacker - the attacker to be checked for possible interceptors
	 * @return true if attacker can be intercepted
	 */
	private boolean attackerCanBeBlocked(Piece attacker){
		boolean blockerFound = false;
		
		if(attacker.getClass() == Knight.class){
			for(Piece piece : attacker.getPosition().getAttackers()){
				Position finish = attacker.getPosition();
				Piece taken = finish.getPiece();
				Position start = piece.getPosition();
				
				blockerFound = piece.move(finish);
				try {
					board.undoMove(start, finish, taken);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(blockerFound){
					return true;
				}
			}
		}
		else{
			for(Piece piece : team){
				for(Position position : this.getPosition().getPathTo(attacker.getPosition())){
					Position finish = position;
					Piece taken = finish.getPiece();
					Position start = piece.getPosition();
					
					blockerFound = piece.move(finish);
										
					if(blockerFound){
						try {
							board.undoMove(start, finish, taken);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
}
