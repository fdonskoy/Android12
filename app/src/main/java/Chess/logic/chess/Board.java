/**
 * @author Timothy Elbert
 */
package Chess.logic.chess;

import java.io.Serializable;

/**
 * displays and calls to create all of the piece objects on the board console output
*/
public class Board implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4335059395494216995L;
	/**double array storing the positions(squares) of the game board*/
	Position[][] board;
	/**whose turn it is in current state*/
	Color turn;
	/**number of turns since start of the game*/
	public int turnNum;
	/**stores the kings of both teams to check for checks, mates, and stalemates
	 * */
	King blackKing, whiteKing;
	
	/**creates the board and pieces set up in the classic chess starting position
	 * @throws Exception if invalid position */
	public Board() throws Exception{
		turn = Color.White;
		turnNum = 1;
		board = new Position[8][8];		
		
		Position current = null;
		
		for(int rank = 0; rank < 8; rank++){
			current = new Position(Position.indexToRank(rank), Position.indexToFile(0));
			board[rank][0] = current;
			
			if(rank != 0){
				current.setSouth(board[rank - 1][0]);
				current.getSouth().setNorth(current);
				current.setSouthEast(board[rank - 1][1]);
				current.getSouthEast().setNorthWest(current);
			}
			
			for(int file = 1; file < 8; file++){
				Position next = new Position(Position.indexToRank(rank), Position.indexToFile(file));
				current.setEast(next);
				next.setWest(current);
				
				if(rank != 0){
					next.setSouth(board[rank - 1][file]);
					next.getSouth().setNorth(next);
					next.setSouthEast(next.getSouth().getEast());
					if(next.getSouthEast() != null)
						next.getSouthEast().setNorthWest(next);
					next.setSouthWest(next.getSouth().getWest());
					if(next.getSouthWest() != null)
						next.getSouthWest().setNorthEast(next);
				}
				
				current = next;	
				board[rank][file] = current;
			}
		}
		
		whiteKing = new King(board[0][4], Color.White, this);
		board[0][0].setPiece(new Rook(board[0][0], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][0].getPiece());
		board[0][1].setPiece(new Knight(board[0][1], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][1].getPiece());
		board[0][2].setPiece(new Bishop(board[0][2], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][2].getPiece());
		board[0][3].setPiece(new Queen(board[0][3], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][3].getPiece());
		board[0][4].setPiece(whiteKing);

		board[0][5].setPiece(new Bishop(board[0][5], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][5].getPiece());
		board[0][6].setPiece(new Knight(board[0][6], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][6].getPiece());
		board[0][7].setPiece(new Rook(board[0][7], Color.White, whiteKing, this));
		whiteKing.team.add(board[0][7].getPiece());
		
		
		blackKing = new King(board[7][4], Color.Black, this);
		board[7][0].setPiece(new Rook(board[7][0], Color.Black, blackKing, this));
		blackKing.team.add(board[7][0].getPiece());
		board[7][1].setPiece(new Knight(board[7][1], Color.Black, blackKing, this));
		blackKing.team.add(board[7][1].getPiece());
		board[7][2].setPiece(new Bishop(board[7][2], Color.Black, blackKing, this));
		blackKing.team.add(board[7][2].getPiece());
		board[7][3].setPiece(new Queen(board[7][3], Color.Black, blackKing, this));
		blackKing.team.add(board[7][3].getPiece());
		board[7][4].setPiece(blackKing);
		
		board[7][5].setPiece(new Bishop(board[7][5], Color.Black, blackKing, this));
		blackKing.team.add(board[7][5].getPiece());
		board[7][6].setPiece(new Knight(board[7][6], Color.Black, blackKing, this));
		blackKing.team.add(board[7][6].getPiece());
		board[7][7].setPiece(new Rook(board[7][7], Color.Black, blackKing, this));
		blackKing.team.add(board[7][7].getPiece());
		
		for(int file = 0; file < 8; file++){
			board[1][file].setPiece(new Pawn(board[1][file], Color.White, whiteKing, this));
			whiteKing.team.add(board[1][file].getPiece());
			board[6][file].setPiece(new Pawn(board[6][file], Color.Black, blackKing, this));
			blackKing.team.add(board[6][file].getPiece());
		}
	}
	
	/**
	 * @param start - position of the piece that is moving
	 * @param finish - position of where the piece is moving to
	 * move the contents of one position on the board to another
	 * @exception Exception - thrown if invalid positions on the board given*/
	public void move(Position start, Position finish) throws Exception{
		Piece piece = start.getPiece();
		
		if(finish.getPiece() != null)
			if(finish.getPiece().getColor() == Color.White) whiteKing.team.remove(finish.getPiece());
			else blackKing.team.add(finish.getPiece());
		
		finish.setPiece(piece);
		piece.getPosition().setPiece(null);
		piece.setPosition(this.board[Position.rankToIndex(finish.getRank())][Position.fileToIndex(finish.getFile())]);
		
		turnNum++;
		turn = (turn == Color.White)? Color.Black : Color.White;
	}
	
	/**
	 * @param start original position from which the move was made
	 * @param finish where the piece ended
	 * @param replace if finish is null, it is the piece that moved, otherwise it is the piece that was taken
	 * @throws Exception if invalid position
	 */
	public void undoMove(Position start, Position finish, Piece replace) throws Exception{
		if(finish == null){
			start.setPiece(replace);
		} else {
			Piece piece = finish.getPiece();
			
			this.board[Position.rankToIndex(start.getRank())][Position.fileToIndex(start.getFile())].setPiece(piece);
			piece.getPosition().setPiece(replace);
			piece.setPosition(this.board[Position.rankToIndex(start.getRank())][Position.fileToIndex(start.getFile())]);
		}
		
		if(replace != null)
			if(replace.getColor() == Color.White) whiteKing.team.add(replace);
			else blackKing.team.add(replace);
		
		turnNum--;
		turn = (turn == Color.White)? Color.Black : Color.White;
	}
	
	/**Safely removes a piece from the board in "God Mode" without breaking team logic. Used to test game logic in setting up test positions. Not used to play the game
	 * @param position - position of piece to be removed
	 * @return returns the piece that was removed*/
	public Piece removePieceAt(Position position){
		if(position.getPiece() == null) return null;
		if(position.getPiece().getColor() == Color.White) whiteKing.team.remove(position.getPiece());
		else blackKing.team.remove(position.getPiece());
		
		Piece retval = position.getPiece();
		position.setPiece(null);
		
		return retval;
	}
	
	public String toString(){
		String retval = "";
		
		for(int rank = 7; rank >= 0; rank --){
			for(int file = 0; file < 8; file++){
				if(board[rank][file].getPiece() != null){
					retval += board[rank][file].getPiece().toString();
				} else if(board[rank][file].getColor().equals(Color.Black)){
					retval += "##";
				} else {
					retval += "  ";
				}
				
				retval += " ";
			}
			
			retval += (rank + 1) + "\n";
		}
		
		retval += " a  b  c  d  e  f  g  h\n";
		
		return retval;
	}

	public String pieceAtString(String position){
		try{
			int rank = Position.rankToIndex(position.charAt(1) - 48);
			int file = Position.fileToIndex(position.charAt(0));

			Piece piece = board[rank][file].getPiece();

			return (piece == null) ? "" : piece.toString();
		}catch(Exception e){
			return null;
		}
	}
}
