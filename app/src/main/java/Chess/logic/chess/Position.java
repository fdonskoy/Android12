/**
 * @author Timothy Elbert
 */
package Chess.logic.chess;

import java.io.Serializable;
import java.util.LinkedList;

/**e
 * Responsible for the position on the board, including accessing all adjacent tiles to the current piece's position
 */
public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -651944598278406425L;
	/**chess board rank*/
	private int rank;
	/**chess board file*/
	private char file;
	/**piece at this position*/
	private Piece piece;
	/**chessboard square color used for printing out the board*/
	Color color;
	
	/**relative positions to this position*/
	private Position north, northEast, east, southEast, south, southWest, west, northWest;

	/**
	 * @param rank is the human readable rank between 1 and 8 
	 * @param file is the human readable file between 'a' and 'h'
	 * 
	 * @exception Exception - thrown if rank or file are out of range
	 */
	public Position(int rank, char file) throws Exception{
		if(file < 97 || file > 104)
			throw new Exception("Invalid file");
		if(rank < 1 || rank > 8)
			throw new Exception("Invalid rank");
		
		this.color = ((rankToIndex(rank) + fileToIndex(file)) % 2  == 0) ? Color.Black : Color.White; 
		
		this.rank = rank;
		this.file = file;
	}
	
	/**@return returns the color of this square
	 * */
	public Color getColor(){
		return color;
	}
	
	
	/**Do not use this to remove pieces from the board. This will break team logic
	 * @param piece - the piece to be set in this position.
	 * */
	public void setPiece(Piece piece){
		this.piece = piece;
	}
	
	/**@return piece in current position. Returns null if no such piece*/
	public Piece getPiece(){
		return piece;
	}
	
	/**@return this position's human readable rank*/
	public int getRank(){
		return rank;
	}
	
	/**@return this position's human readable rank*/
	public char getFile(){
		return file;
	}
	
	/** @param square - the position that is of the same file but 1 greater rank than this position*/
	public void setNorth(Position square){
		north = square;
	}
	
	/** @param square - the position that is of 1 file and rank greater than this position*/
	public void setNorthEast(Position square){
		northEast = square;
	}
	
	/** @param square - the position that is of same rank but 1 file greater than this position*/
	public void setEast(Position square){
		east = square;
	}
	
	/** @param square - the position that is of 1 file greater and 1 rank less than this position*/
	public void setSouthEast(Position square){
		southEast = square;
	}
	
	/** @param square - the position that is of 1 rank less than this position*/
	public void setSouth(Position square){
		south = square;
	}
	
	/** @param square - the position that is of 1 file and 1 rank less than this position*/
	public void setSouthWest(Position square){
		southWest = square;
	}
	
	/** @param square - the position that is of 1 file less than this position*/
	public void setWest(Position square){
		west = square;
	}
	
	/** @param square - the position that is of 1 file less and 1 rank greater than this position*/
	public void setNorthWest(Position square){
		northWest = square;
	}
	
	/**@return the position 12 o'clock from this one*/
	public Position getNorth(){
		return north;
	}
	
	/**@return the position 1:30 o'clock from this one*/
	public Position getNorthEast(){
		return northEast;
	}
	
	/**@return the position 3 o'clock from this one*/
	public Position getEast(){
		return east;
	}
	
	/**@return the position 4:30 o'clock from this one*/
	public Position getSouthEast(){
		return southEast;
	}
	
	/**@return the position 6 o'clock from this one*/
	public Position getSouth(){
		return south;
	}
	
	/**@return the position 7:30 o'clock from this one*/
	public Position getSouthWest(){
		return southWest;
	}
	
	/**@return the position 9 o'clock from this one*/
	public Position getWest(){
		return west;
	}
	
	/**@return the position 10:30 o'clock from this one*/
	public Position getNorthWest(){
		return northWest;
	}
	
	/**Assumes friendly team is of the color of current piece at this position.
	 * @return true if attacked by nonFriendly color; false if not attacked or no piece in position
	*/
	public LinkedList<Piece> getAttackers(){
		return (piece != null)? getAttackers(piece.getColor()) : new LinkedList<Piece>();
	}
	
	/**
	    * @param homeTeamColor : friendly team color
	    * @return true if attacked by nonFriendly color; false if not attacked or no piece in position
	*/
	public LinkedList<Piece> getAttackers(Color homeTeamColor){		
		LinkedList<Piece> retval = new LinkedList<Piece>();
		
		boolean adjacentPosition;
		
		/*checking file and rank*/
		adjacentPosition = true;
		for(Position cur = getEast(); cur != null; cur = cur.getEast()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Rook.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| (adjacentPosition && cur.getPiece().getClass() == King.class))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		
		adjacentPosition = true;
		for(Position cur = getNorth(); cur != null; cur = cur.getNorth()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Rook.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| (adjacentPosition && cur.getPiece().getClass() == King.class))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		adjacentPosition = true;
		for(Position cur = getSouth(); cur != null; cur = cur.getSouth()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Rook.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| (adjacentPosition && cur.getPiece().getClass() == King.class))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		adjacentPosition = true;
		for(Position cur = getWest(); cur != null; cur = cur.getWest()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Rook.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| (adjacentPosition && cur.getPiece().getClass() == King.class))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		/*checking diagonals*/
		adjacentPosition = true;
		for(Position cur = getNorthEast(); cur != null; cur = cur.getNorthEast()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Bishop.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| (adjacentPosition && 
							(cur.getPiece().getClass() == King.class
						|| (	homeTeamColor == Color.White 
							&& 	cur.getPiece().getClass() == Pawn.class))))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		adjacentPosition = true;
		for(Position cur = getNorthWest(); cur != null; cur = cur.getNorthWest()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Bishop.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| cur.getPiece().getClass() == King.class
					|| (adjacentPosition && 
							(cur.getPiece().getClass() == King.class
						|| (	homeTeamColor == Color.White 
							&& 	cur.getPiece().getClass() == Pawn.class))))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		adjacentPosition = true;
		for(Position cur = getSouthEast(); cur != null; cur = cur.getSouthEast()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Bishop.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| cur.getPiece().getClass() == King.class
					|| (adjacentPosition && 
						(	cur.getPiece().getClass() == King.class
						|| 	(	homeTeamColor == Color.Black 
							&& 	cur.getPiece().getClass() == Pawn.class))))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		adjacentPosition = true;
		for(Position cur = getSouthWest(); cur != null; cur = cur.getSouthWest()){
			if(cur.getPiece() != null && cur.getPiece().getColor() != homeTeamColor && 
					(  cur.getPiece().getClass() == Bishop.class 
					|| cur.getPiece().getClass() == Queen.class 
					|| cur.getPiece().getClass() == King.class
					|| (adjacentPosition && 
							(cur.getPiece().getClass() == King.class
						|| (	homeTeamColor == Color.Black 
							&& 	cur.getPiece().getClass() == Pawn.class))))){
				retval.add(cur.getPiece());
				break;
			} else if(cur.getPiece() != null){
				break;
			}
			
			adjacentPosition = false;
		}
		
		/*check for knights*/
		Piece checkedPiece;
		/*Check East Knights*/
		if(getFile() + 1 <= 'h'){
			if(getRank() + 2 <= 8){
				checkedPiece = getNorthEast().getNorth().getPiece();
				if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
					retval.add(checkedPiece);
				}
			}
			
			if(getRank() - 2 >= 1){
				checkedPiece = getSouthEast().getSouth().getPiece();
				if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
					retval.add(checkedPiece);
				}
			}
			
			/*Check way East Knights*/
			if(getFile() + 2 <= 'h'){
				if(getRank() + 1 <= 8){
					checkedPiece = getEast().getNorthEast().getPiece();
					if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
						retval.add(checkedPiece);
					}
				}
				
				if(getRank() - 1 >= 1){
					checkedPiece = getEast().getSouthEast().getPiece();
					if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
						retval.add(checkedPiece);
					}
				}
			}
		}
		
		/*Check West Knights*/
		if(getFile() - 1 >= 'a'){
			if(getRank() + 2 <= 8){
				checkedPiece = getNorthWest().getNorth().getPiece();
				if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
					retval.add(checkedPiece);
				}
			}
			
			if(getRank() - 2 >= 1){
				checkedPiece = getSouthWest().getSouth().getPiece();
				if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
					retval.add(checkedPiece);
				}
			}
			
			/*Check way West Knights*/
			if(getFile() - 2 >= 'a'){
				if(getRank() + 1 <= 8){
					checkedPiece = getWest().getNorthWest().getPiece();
					if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
						retval.add(checkedPiece);
					}
				}
				
				if(getRank() - 1 >= 1){
					checkedPiece = getWest().getSouthWest().getPiece();
					if(checkedPiece != null && checkedPiece.getColor() != homeTeamColor && checkedPiece.getClass() == Knight.class){
						retval.add(checkedPiece);
					}
				}
			}
		}
		
		return retval;
	}
	
	/**direct paths only, no knights
	 * @param target is the position to which a path from current position is requested
	 * @return a linked list of positions between this position and target, including target*/
	public LinkedList<Position> getPathTo(Position target){
		LinkedList<Position> retval = new LinkedList<Position>();
			
		try{
			if(Math.abs(Position.fileToIndex(this.getFile()) - Position.fileToIndex(target.getFile())) != Math.abs(Position.rankToIndex(this.getRank()) - Position.rankToIndex(target.getRank()))){				
				if(			(this.getFile() != target.getFile() && this.getRank() != target.getRank()) 
						|| 	(this.getFile() == target.getFile() && this.getRank() == target.getRank())){
					return retval;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getStackTrace());
		}
		
		Position cur = this;
		while(!cur.equals(target)){
			if(cur.getRank() < target.getRank()) cur = cur.getNorth();
			else if(cur.getRank() > target.getRank()) cur = cur.getSouth();
			
			if(cur.getFile() < target.getFile()) cur = cur.getEast();
			else if(cur.getFile() > target.getFile()) cur = cur.getWest();
			
			retval.add(cur);
		}
		
		return retval;
	}
	
	/**
	    * converts human readable file to array index ex. 'a' to 0
	    * @param file - human readable file
	    * @return array index representing this file
	    * @exception Exception if input is out of 8x8 chessboard bounds
	*/
	public static int fileToIndex(char file) throws Exception{
		if(file < 97 || file > 104)
			throw new Exception("Invalid file");
		return file - 97;
	}
	
	/**
	    * converts human readable file to array index ex. 0 to 'a'
	    * @param index - array index representing file
	    * @return array index representing this file
	    * @exception Exception if input is out of 8x8 chessboard bounds
	*/
	public static char indexToFile(int index) throws Exception{
		if(index < 0 || index > 7)
			throw new Exception("Invalid file index");
		return (char) (index + 97);
	}
	
	/**
	    * converts human readable file to array index ex. 1 to 0 or 5 to 4, etc
	    * @param rank - human readable rank number
	    * @return array index representing this file
	    * @exception Exception if input is out of 8x8 chessboard bounds
	*/
	public static int rankToIndex(int rank) throws Exception{
		if(rank < 1 || rank > 8)
			throw new Exception("Invalid rank");
		return rank - 1;
	}
	
	/**
	    * converts human readable file to array index ex. 0 to 1 or 4 to 5, etc
	    * @param index - array index representing file
	    * @return human readable rank
	    * @exception Exception if input is out of 8x8 chessboard bounds
	*/
	public static int indexToRank(int index) throws Exception{
		if(index < 0 || index > 7)
			throw new Exception("Invalid rank index");
		return index + 1;
	}
	
	public String toString(){
		return file + "," + rank;
	}
}
