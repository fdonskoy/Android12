package Chess.logic.chess;

import android.support.v7.app.AppCompatActivity;
import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CurrentGame implements Serializable{
	private static final long serialVersionUID = -3739580358789280590L;
	public ArrayList<String> listMoves = new ArrayList<String>();
	
	static String toFrom[] = new String[3];
	public Board currentBoard;
	
	boolean requestedDraw = false;
	boolean drawDeclared = false;
	public boolean finished=  false;
	int currentTurnNum = 0;
	String dateSaved;
		
	public CurrentGame() throws Exception {
		currentBoard = new Board();
		System.out.println(currentBoard);
	}
	/**
	 * @param move: the chess board
	 * Takes user input, parses, and calls the target piece's move function
	 * Does this continuously until checkmate, draw, stalemate, or resign
	 * @throws Exception if invalid file or rank
	 */
	public TypeOfMove makeMove(String move) throws Exception{
		TypeOfMove retVal = null;

		//turn = true for white, false for black
		if (currentBoard.whiteKing.checkmate() || currentBoard.blackKing.checkmate()) {
			gameOver();
		}
		
		String from = null;
		String to = null;
		Piece piece = null;
		String input = move;
		
			try {
				
				if (currentBoard.whiteKing.inCheck() || currentBoard.blackKing.inCheck()) {
					System.out.println("Check");
				}
				if (currentBoard.turn == Color.White) {
					System.out.print("White's move: ");
				}
				else {
					System.out.print("Black's move: ");
				}
				toFrom = null;
				toFrom = checkInput(input);
				if (toFrom == null) {
					gameOver();
				}
				
				
				if (toFrom.length == 1 && checkDraw(toFrom[0], requestedDraw)) {
					drawDeclared = true;
					gameOver();
				}
					
				from = toFrom[0];
				if (toFrom.length > 1) {
					to = toFrom[1];
				}
				
				//checks from and to are equal
				if (from.equals(to)) {
					System.out.println("Illegal move, try again\n");
					return TypeOfMove.INVALID;
				}
				if (from == null || to == null) {
					System.out.println("Illegal move, try again\n");
					return TypeOfMove.INVALID;
				}

				
				
				int letter = Position.fileToIndex(from.charAt(0));
				int num = (Character.getNumericValue(from.charAt(1)));
				
				piece = currentBoard.board[Position.rankToIndex(num)][letter].getPiece();
				if (piece == null) {
					System.out.println("Illegal move, try again\n");
					return TypeOfMove.INVALID;
				}
				//if piece is null, go back to start of while loop
				
				num = Character.getNumericValue(to.charAt(1));
				if (currentBoard.turn == Color.White) {
					if (piece.getColor() != Color.White) {
						System.out.println("Illegal move, try again\n");
						return TypeOfMove.INVALID;
					}
				}
				else {
					if (piece.getColor() != Color.Black) {
						System.out.println("Illegal move, try again\n");
						return TypeOfMove.INVALID;
					}
				}
				
				Position pos = new Position(num, to.charAt(0));

                TypeOfMove moveAttempt = piece.move(currentBoard.board[Position.rankToIndex(pos.getRank())][Position.fileToIndex(pos.getFile())]);
				retVal = moveAttempt;
				if (moveAttempt == TypeOfMove.INVALID) {
					System.out.println("Illegal move, try again\n");
					return TypeOfMove.INVALID;
				}
				System.out.println("\n" + currentBoard);
				
				
				//check for draw
				if (toFrom.length == 3 && requestDraw(toFrom[2])) {
					requestedDraw = true;
				}
				else {
					requestedDraw = false;
				}
				if (!finished) {
					listMoves.add(from + " " + to);
				}
				
			}
			catch (Exception e) {
				System.out.println("Illegal move, try again2\n");
				return TypeOfMove.INVALID;
			}


			if(drawDeclared ){
                return TypeOfMove.DRAW;
            }

            if(finished && currentBoard.turn == Color.White){
                return TypeOfMove.BLACK_WIN;
            }

            if(finished && currentBoard.turn == Color.Black){
                return TypeOfMove.WHITE_WIN;
            }

			return retVal;
	}
	
	private void gameOver() {
		if (drawDeclared) {
			System.out.println("Draw");
		}
		
		else if (currentBoard.turn == Color.White) {
			System.out.println("Black wins");
		}
		else if (currentBoard.turn == Color.Black) {
			System.out.println("White wins");
		}
		finished = true;
		System.out.println("Finished");
		return;
	}
	
	/**
	 * @param line: the nextLine of user input from scanner
	 * Checks for resign
	 * @return a string array of tokens from user input
	 */
	private static String[] checkInput(String line) {
		String[] data = line.split(" ");
		String from = data[0];
		
		if (from.equals("resign") && data.length == 1) {
			return null;
		}
		return data;
	}
	/**
	 * @param data: the third token from user input, if any
	 * Checks for "draw?"
	 * @return true if third token = "draw?", else false
	 */
	private static boolean requestDraw(String data) {
		if (data.equals("draw?")) {
			return true;
		}
		return false;
	}
	/**
	 * @param data is the nextLine of user input from scanner
	 * @param request : Did the previous player ask for draw?
	 * Checks for resign
	 * @return true if data = "draw" and request for draw is true
	 */
	private static boolean checkDraw(String data, boolean request) {
		if (data.equals("draw") && request) {
			return true;
		}
		return false;
	}
	/**
	 * Used by pawn to check for user input regarding promotion
	 * @return the token or null if user did not specify
	 */
	public static String getInput(){
		if (toFrom.length >= 3) {
			return toFrom[2];
		}
		return null;
	}
	
	public void writeGame(String title) throws IOException {

		File f = new File("src/main/java/Chess/logic/chess/savedGames" + File.separator + title + ".ser");
		if(!f.exists())
		    f.createNewFile();
		ObjectOutputStream oos = new ObjectOutputStream(
									new FileOutputStream("src/main/java/Chess/logic/chess/savedGames" + File.separator + title + ".ser"));
		oos.writeObject(this);
		oos.close(); 
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateSaved = dateFormat.format(date);
		System.out.println("Date created " + dateSaved);
	} 
	
	public CurrentGame readGame (String title) throws IOException, Exception, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("src/main/java/Chess/logic/chess/savedGames" + File.separator + title + ".ser"));
		CurrentGame user = (CurrentGame)ois.readObject();
		ois.close();
		return user;
	} 
	
	public void nextMove () throws Exception {
		if (!this.finished) {
			System.out.println("Game not finished");
			return;
		}
		if (currentTurnNum+1 > listMoves.size()) {
			System.out.println("Reached end of game");
			return;
		}
		if (currentTurnNum == 0) {
			currentBoard = new Board();
		}
		System.out.println(currentTurnNum);
		makeMove(listMoves.get(currentTurnNum));
		currentTurnNum++;
		
	}

	public void resign() {
		listMoves.add("resign");
		gameOver();
	}

	public void draw() {
		listMoves.add("draw");
		drawDeclared = true;
		gameOver();
	}

	public boolean makeAImove() {
		Position start = null;
		if (currentBoard.turn == Color.White) {
			for(Piece piece : currentBoard.whiteKing.team){
				start = piece.getPosition();
				if (piece.makeMove()) {
					System.out.println(currentBoard);
					piece.overrideTestMove = false;
					listMoves.add("" + start.getFile() + start.getRank() + " " + piece.getPosition().getFile() + piece.getPosition().getRank());
					return true;
				}
			}
		}
		else {
			for(Piece piece : currentBoard.blackKing.team){
				start = piece.getPosition();
				if (piece.makeMove()) {
					System.out.println(currentBoard);
					piece.overrideTestMove = false;
					listMoves.add("" + start.getFile() + start.getRank() + " " + piece.getPosition().getFile() + piece.getPosition().getRank());
					return true;
				}

			}
		}
		return false;
	}


	public Board getCurrentBoard(){
		return currentBoard;
	}
}
