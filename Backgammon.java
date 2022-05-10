//316321272
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Backgammon {
	// attributes
	private int[] board; // Positive numbers - white pieces, negative - black pieces.
	private int[] eaten; // 2 cells - 1st cell for whites eaten pieces, 2nd for blacks.
	private int[] cubesUsages; // How many more usages do we have for each cube
	private boolean whitesTurn; // Is it whites turn?
	private Random rd = new Random(); // Random generator.
	private Scanner sc = new Scanner(System.in); // For getting the users position and cube to use.
	// magic numbers
	static final int NO_STONES = 0;
	static final int NOT_EATEN = 0;
	static final int START_BOARD = 0;
	static final int WHITE_STONE = 1;
	static final int BLACK_STONE = -1;
	static final int RANGE_CUBE = 6;
	static final int END_BOARD = 23;
	static final int IN_1_QUARTER = 5;
	static final int IN_4_QUARTER = 18;
	// empty constructor
	Backgammon() {
		this.initBoard();
	}

	// constructor to initiate the board size
	Backgammon(int boardSize) {
		initBoard(boardSize);
	}

	@SuppressWarnings("unused")
	public void initBoard() { // useful when playing more than 1 game.
		initBoard(24);
	}

	// initiate the attribute of the board
	public void initBoard(int boardSize) {
		if (boardSize == 24)
			this.board = new int[] { 2, 0, 0, 0, 0, -5, 0, -3, 0, 0, 0, 5, -5, 0, 0, 0, 3, 0, 5, 0, 0, 0, 0, -2 };
		else
			this.board = new int[boardSize];

		this.eaten = new int[2];
		this.cubesUsages = new int[2];
		// select random who start the game
		this.whitesTurn = rd.nextBoolean();

	}

	// print the status of the board
	public String toString() {
		// make the 3 and 4 quarter to reveres array
		int[] thirdQuarter = new int[6];
		for (int i = 0; i < 6; i++) {
			thirdQuarter[i] = this.board[17 - (i)];
		}
 
		int[] fourthQuarter = new int[6];
		for (int i = 0; i < 6; i++) {
			fourthQuarter[i] = this.board[END_BOARD - (i)];
		}
		// print the quarters according to 1-2 line down 4-3 to get proper
		return Arrays.toString(Arrays.copyOfRange(board, 0, 6)) + Arrays.toString(Arrays.copyOfRange(board, 6, 12))
				+ "\n" + Arrays.toString(Arrays.copyOfRange(fourthQuarter, 0, 6))
				+ Arrays.toString(Arrays.copyOfRange(thirdQuarter, 0, 6)) + "\n" + "Whites eaten - " + eaten[0]
				+ ", blacks eaten - " + eaten[1];
	}

	// getters
	public int[] getBoard() {
		return this.board;
	}

	public boolean getWhitesTurn() {
		return this.whitesTurn;
	}

	public void setBoard(int[] board) {
		// Use with caution, should be used only for debugging purposes!
		this.board = board;
	}

	// check if white start
	public boolean whiteStarts() {
		// You can give random boolean here, equivalent to randomly rolling 2 cubes and
		// deciding from them
		return this.whitesTurn;

	}

	// initiate the cubes with random numbers between 1-6
	int[] cubes = new int[2];
	public int[] roll2Cubes() {
				
		cubes[0] = rd.nextInt(RANGE_CUBE) + 1;
		cubes[1] = rd.nextInt(RANGE_CUBE) + 1;
		return cubes;
	}

	// check if the board is without positive or negative numbers
	public boolean gameOver() {

		int whiteColect = 0;
		int blackColect = 0;
		//loop to count all stone of player
		for (int i = 0; i < this.board.length; i++) {
			if (this.board[i] >= WHITE_STONE)
				whiteColect = +this.board[i];
			if (this.board[i] <= BLACK_STONE)
				blackColect = +this.board[i];
		}
		//if player is not eaten and doesn't have stone in board the game is over
		if ((this.eaten[0] == NOT_EATEN && whiteColect == NO_STONES) 
				|| (this.eaten[1] == NOT_EATEN && blackColect == NO_STONES))
				return true;

		else
			return false;
	}

	// move operator	
	public boolean move(int position, int moveSize) {

		int direction = this.whitesTurn ? 1 : -1;
		// check if move legal
		if (legalMove(position, moveSize) == false)
			return false;
		
		// cases if all stone of player in last quarter
		if (farthestStoneInLastQuadrant(farthestStone()) == true) {
			if (this.whitesTurn && position + moveSize > END_BOARD) {
				this.board[position]--;
				return true;
			}
			if (!this.whitesTurn && position - moveSize < START_BOARD) {
				this.board[position]++;
				return true;
			}

		}

		if ((this.whitesTurn) && (position + moveSize > END_BOARD))
			return false;
		if ((!this.whitesTurn) && (position - moveSize < START_BOARD))
			return false;
		// white turn
		if (direction == 1) {
			if (eaten[0] == NOT_EATEN) {
				if ((this.board[position] >= 0) && this.board[position + moveSize] >= 0) {
					this.board[position + moveSize]++;
					this.board[position]--;
					return true;
				} else if (this.board[position + moveSize] == BLACK_STONE) {
					this.board[position + moveSize] = WHITE_STONE;
					this.board[position]--;
					eaten[1]++;
					return true;
				} else
					return false;
			}
			// white eaten cases
			else if ((eaten[0] != NOT_EATEN) && (position == START_BOARD-1)) {
				if (this.board[moveSize - 1] >= 0) {
					this.board[moveSize - 1]++;
					eaten[0]--;
					return true;
				} else if (this.board[moveSize - 1] == BLACK_STONE) {
					this.board[moveSize - 1] = WHITE_STONE;
					eaten[1]++;
					eaten[0]--;
					return true;
				} else
					return false;
			}
		}
		// black turn
		if (direction == -1) {
			if (eaten[1] == NOT_EATEN) {
				if (this.board[position] <= 0 && this.board[position - moveSize] <= 0) {
					this.board[position - moveSize]--;
					this.board[position]++;
					return true;
				} else if (this.board[position - moveSize] == WHITE_STONE) {
					this.board[position - moveSize] = BLACK_STONE;
					this.board[position]++;
					eaten[0]++;
					return true;
				} else
					return false;
			}
			// black eaten cases
			else if ((eaten[1] != NOT_EATEN) && (position == END_BOARD +1)) {
				if (this.board[END_BOARD +1 - moveSize] <= 0) {
					this.board[END_BOARD +1 - moveSize]--;
					eaten[1]--;
					return true;
				} else if (this.board[END_BOARD +1 - moveSize] == WHITE_STONE) {
					this.board[24 - moveSize] = BLACK_STONE;
					eaten[1]--;
					eaten[0]++;
					return true;
				} else
					return false;
			}
		}

		return false;
	}

	// testing the move if is a legal
	public boolean legalMove(int startPosition, int moveSize) {
		//check if  startPosition in range of array
		if (startPosition <= END_BOARD && startPosition >= START_BOARD) {
			// check if index of board == 0
			if (this.board[startPosition] == NO_STONES)
				return false;
			// case if take players out of board
			if (farthestStoneInLastQuadrant(farthestStone()) == true) {
				if ((this.whitesTurn) && (startPosition + moveSize > END_BOARD)) {
					return true;
				}
				if ((!this.whitesTurn) && (startPosition - moveSize < START_BOARD)) {
					return true;
				}
			}
			// check move for white
			if (this.whitesTurn && eaten[0] == NOT_EATEN) {
				if (this.board[startPosition] < WHITE_STONE)
					return false;
				else if (startPosition + moveSize >END_BOARD)
					return false;
				else if (this.board[startPosition + moveSize] < BLACK_STONE)
					return false;
				else
					return true;
			}
			// check move for black
			if (!this.whitesTurn && eaten[1]==NOT_EATEN) {
				if (this.board[startPosition] > BLACK_STONE)
					return false;
				else if (startPosition - moveSize < START_BOARD )
					return false;
				else if (this.board[startPosition - moveSize] > WHITE_STONE)
					return false;
				else
					return true;
			}
		}
		// cases to enter eaten players
		if (  (this.whitesTurn)&& eaten[0] != NOT_EATEN && startPosition == START_BOARD -1 
				&&(this.board[-1 + cubes[0]] >= BLACK_STONE || this.board[-1 + cubes[1]] >= BLACK_STONE))
			return true;
		
		if ( (!this.whitesTurn)&& eaten[1] != NOT_EATEN && startPosition == END_BOARD +1 
				&& (this.board[24- cubes[0]] >= WHITE_STONE || this.board[24- cubes[1]] >= WHITE_STONE)          )
			return true;
				
		else return false;
	}

	// check if the player can move or need to change the turn
	public boolean haveLegalMoves(int[] cubes) { 
		// Any legal moves for the player
		// loop to check all the cases in the board
		for (int i = 0; i < this.board.length ; i++) {
			if (( legalMove(i, cubes[0] ) )|| ( legalMove(i, cubes[1] )   ))
				return true;
		}
		// cases to enter eaten stone
		if (this.whitesTurn && eaten[0] != NOT_EATEN ) {
			if (  (this.board[START_BOARD-1 + cubes[0]] >= BLACK_STONE 
					|| this.board[START_BOARD-1 + cubes[1]] >= BLACK_STONE))
					return true;
			else return false;
			}
		if (!this.whitesTurn && eaten[1] != NOT_EATEN ) {
			if (  (this.board[END_BOARD +1 - cubes[0]] <= WHITE_STONE 
					|| this.board[END_BOARD +1- cubes[1]] <= WHITE_STONE))
					return true;
			else return false; 
			}
		return false;
	}

	// check where the far stone
	public int farthestStone() {
		//check if have eaten stone
		if (this.whitesTurn && eaten[0] != NOT_EATEN  )
			return START_BOARD -1;
		if (!this.whitesTurn && eaten[1] != NOT_EATEN  )
			return END_BOARD +1;
		int i = 0;
		// check for white
		if (this.whitesTurn)
			for ( i = 0; i < this.board.length; i++) {
				if (this.board[i] >= WHITE_STONE)
					return i;
			}
		// check for black
		if (!this.whitesTurn)
			for ( i = 0; i < this.board.length ; i++) {
				if (this.board[this.board.length-1 -i] <= BLACK_STONE)
					return this.board.length-1 - i;
			}
		return i;
	}
	// check if the far stone is in the proper quarter
	public boolean farthestStoneInLastQuadrant(int farthestStone) {

		// check if all white stone in 4 quarter
		if (this.whitesTurn)
			if (farthestStone() >= IN_4_QUARTER)
				return true;
		// check if all black stone in 1 quarter
		if (!this.whitesTurn)
			if (farthestStone() <= IN_1_QUARTER )
				return true;

			return false;
	}

	// check if the position out of board
	public boolean outOfBoard(int position) {

		if ((position > END_BOARD) || (position < START_BOARD))
			return true;

		return false;
	}

	// change the turn to next player
	public void nextTurn() {
		if (this.whitesTurn == false)
			this.whitesTurn = true;

		else this.whitesTurn = false;
	}

	public void runGame() {
		while (!this.gameOver()) {
			int[] cubes = this.roll2Cubes();
			this.cubesUsages[1] = this.cubesUsages[0] = (cubes[0] == cubes[1]) ? 2 : 1;

			// Move the board using legal moves rolled by the cubes:
			while (this.cubesUsages[0] > 0 || this.cubesUsages[1] > 0) {
				if (!this.haveLegalMoves(cubes))
					break;

				System.out.print(this.whitesTurn ? "Whites turn (⇄)" : "Blacks turn (⇆)");
				System.out.println(", Rolled " + cubes[0] + " " + cubes[1]);
				System.out.print("Insert position number: ");
				int choosenPosition = this.sc.nextInt();
				System.out.print("Insert cube number (0 or 1): ");
				int cubeToUse = this.sc.nextInt();
				if (cubeToUse < 0 || cubeToUse >= cubes.length) {
					System.out.println("Please select a cube from the range of 0 to " + (cubes.length - 1));
					continue;
				}
				if (this.cubesUsages[cubeToUse] <= 0) {
					System.out.println("Can\'t use this cube again!");
					continue;
				}
				int choosenMove = cubes[cubeToUse];
				boolean moved = this.move(choosenPosition, choosenMove);
				if (moved) {
					this.cubesUsages[cubeToUse] -= 1;
					System.out.println(this);
				} else
					System.out.println("Illegal move!");
			}
			this.nextTurn();
		}
		System.out.println(this.whitesTurn ? "Black won!" : "White won!");
	}

	public static void main(String[] args) {
		Backgammon bg = new Backgammon();
		System.out.println(bg);
		bg.runGame();
	}
}
