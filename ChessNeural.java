package reinforcechess;

import java.util.ArrayList;
import java.util.Arrays;

public class Main {

	ArrayList<int[]> states = new ArrayList<>();
	ArrayList<Integer> appearances = new ArrayList<>();
	ArrayList<Integer> wins = new ArrayList<>();

	static String[] colNames = "abcdefgh".split("");
	static String[][] chessBoard = {

			//A   B   C   D   E   F   G   H
			{"r","n","b","q","k","b","n","r"},  //8
			{"p","p","p","p","p","p","p","p"},  //7
			{" "," "," "," "," "," "," "," "},  //6
			{" "," "," "," "," "," "," "," "},  //5
			{" "," "," "," "," "," "," "," "},  //4
			{" "," "," "," "," "," "," "," "},  //3
			{"P","P","P","P","P","P","P","P"},  //2
			{"R","N","B","Q","K","B","N","R"}};	//1


	//First 8 are for white pawns, first 8 are for black pawns
	static boolean[] pawnDoubleMove = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
	static int[] timePawnMoved = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	static int totalMoves = 0;
	static int whiteKingMoved = 0;
	static int blackKingMoved = 0;
	static int kwRookMoved = 0;
	static int qwRookMoved = 0;
	static int kbRookMoved = 0;
	static int qbRookMoved = 0;

	public static void main(String[] args) {
		
		/* Fool's Mate
		makeMove("f2f3");
		makeMove("e7e5");
		makeMove("g2g4");
		makeMove("d8h4");
		*/
		
		/*Scholar's Mate
		makeMove("e2e4");
		makeMove("e7e5");
		makeMove("f1c4");
		makeMove("b8a6");
		makeMove("d1f3");
		makeMove("h7h5");
		makeMove("f3f7");
		*/
		
		printBoard(chessBoard);
		System.out.println(Arrays.toString(legalBMoves()));
		//showAllPossibleWhiteMoves();
		gameStatus();
	}

	public static void showAllPossibleWhiteMoves() {
		String[] legalWMoves = legalWMoves();
		if (legalWMoves[0].equals("")) System.out.println("No possible moves.");
		else {
			for (int i = 0;i<legalWMoves().length;i++) {
				makeMove(legalWMoves[i]);
				System.out.println(legalWMoves[i]);
				moveStatusBoardPrint();
				undoMove(legalWMoves[i]);
			}
		}
	}
	
	public static void showAllPossibleBlackMoves() {
		String[] legalBMoves = legalBMoves();
		if (legalBMoves[0].equals("")) System.out.println("No possible moves.");
		else {
			for (int i = 0;i<legalWMoves().length;i++) {
				makeMove(legalBMoves[i]);
				System.out.println(legalBMoves[i]);
				moveStatusBoardPrint();
				undoMove(legalBMoves[i]);
			}
		}
	}

	public static int gameStatus() {
		//1 is a white win, 0 is a tie, -1 is a black win, and 5 is ongoing.
		if(legalWMoves()[0].equals("")) { 
			if (!whiteKingSafe()) { System.out.println("Black wins."); return -1; }
			else { System.out.println("Tie." ); return 0;} 
		}
		if(legalBMoves()[0].equals("")) { 
			if (!blackKingSafe()) { System.out.println("White wins."); return 1; }
			else { System.out.println("Tie." ); return 0;} 
		}
		System.out.println("Play on!");
		return 5;
	}
	
	public static void moveStatusBoardPrint() {
		System.out.println("Move #" + ((totalMoves-1)/2+1));
		String nextToMove = (totalMoves%2 == 0) ? "White" : "Black";
		System.out.println("Next to move: " + nextToMove + "\n");
		printBoard(chessBoard);
	}

	public static String compReadableMove(String a) {
		String move = a;
		String[] files = "abcdefgh".split("");
		if (a.length()>3) {
			if (!a.substring(4).equals("P")) {
				for (int i = 0;i<files.length;i++) {
					move = move.replace(files[i], Integer.toString(i+1));
				}
			}
			else {
				for (int i = 0;i<files.length;i++) {
					move = move.substring(0,1).replace(files[i], Integer.toString(i+1))+move.substring(1,2).replace(files[i], Integer.toString(i+1))+move.substring(2);
				}
			}
		}
		return move.toString();
	}

	public static void undoMove(String a) {
		a = compReadableMove(a);
		if (a.equals("O-O")) {
			if (totalMoves%2!=0) {
				chessBoard[7][4] = "K";
				chessBoard[7][7] = "R";
				chessBoard[7][6] = " ";
				chessBoard[7][5] = " ";
				whiteKingMoved--;
				kwRookMoved--;
			}
			if (totalMoves%2!=1) {
				chessBoard[0][4] = "k";
				chessBoard[0][7] = "r";
				chessBoard[0][6] = " ";
				chessBoard[0][5] = " ";
				blackKingMoved--;
				kbRookMoved--;
			}
		}
		if (a.length()>3) {
			if (a.substring(4).equals("=")) {
				chessBoard[1][Character.getNumericValue(a.charAt(0))-1] = "P";
				chessBoard[0][Character.getNumericValue(a.charAt(1))-1] = (a.substring(2, 3).equals("/")) ? " " : a.substring(2, 3);
			}
			else if (a.substring(4).equals("x")) {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = " ";
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = temp;
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(2))-1] = (totalMoves%2==0)? "P":"p";

			}
			else if (a.equals("O-O-O")) {
				if (totalMoves%2!=0) {
					chessBoard[7][4] = "K";
					chessBoard[7][0] = "R";
					chessBoard[7][1] = " ";
					chessBoard[7][2] = " ";
					chessBoard[7][3] = " ";
					whiteKingMoved--;
					qwRookMoved--;
				}
				if (totalMoves%2!=1) {
					chessBoard[0][4] = " ";
					chessBoard[0][0] = " ";
					chessBoard[0][1] = " ";
					chessBoard[0][2] = "k";
					chessBoard[0][3] = "r";
					blackKingMoved--;
					qbRookMoved--;
				}
			}
			else {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = (totalMoves%2 == 0) ? a.substring(4) : a.substring(4).toLowerCase();
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = temp;
				if (chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1].equals("")) chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1]=" ";
				//CHECKING EN PASSANT
				if (temp.equals("P") && a.substring(1, 2).equals("2")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))-1] = false; timePawnMoved[Integer.parseInt(a.substring(0,1))-1] = 0;}
				if (temp.equals("p") && a.substring(1, 2).equals("7")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))+7] = false; timePawnMoved[Integer.parseInt(a.substring(0,1))+7] = 0;}
				//CHECKING CASTLE LEGALITY
				if (temp.equals("K")) whiteKingMoved--;
				if (temp.equals("k")) blackKingMoved--;
				if (temp.equals("R") && a.substring(0,2).equals("a1")) qwRookMoved--;
				if (temp.equals("R") && a.substring(0,2).equals("h1")) kwRookMoved--;
				if (temp.equals("r") && a.substring(0,2).equals("a8")) qbRookMoved--;
				if (temp.equals("r") && a.substring(0,2).equals("h8")) kbRookMoved--;

			}
		}
		totalMoves--;
	}

	public static void makeMove(String a) {
		a = compReadableMove(a);
		if (a.length()>3) {
			if (a.substring(4).equals("=")) {
				chessBoard[1][Character.getNumericValue(a.charAt(0))-1] = " ";
				chessBoard[0][Character.getNumericValue(a.charAt(1))-1] = a.substring(3,4);
			}
			else if (a.substring(4).equals("x")) {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = temp;
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = " ";
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(2))-1] = " ";
			}
			else if (a.equals("O-O-O")) {
				if (totalMoves%2==0 && whiteKingMoved==0 && qwRookMoved==0 && chessBoard[7][3].equals(" ") && chessBoard[7][2].equals(" ") && chessBoard[7][1].equals(" ")) {
					chessBoard[7][4] = " ";
					chessBoard[7][0] = " ";
					chessBoard[7][2] = "K";
					chessBoard[7][3] = "R";
					whiteKingMoved++;
					qwRookMoved++;
				}
				if (totalMoves%2==1 && blackKingMoved==0 && qbRookMoved==0 && chessBoard[0][3].equals(" ") && chessBoard[0][2].equals(" ") && chessBoard[0][1].equals(" ")) {
					chessBoard[0][4] = " ";
					chessBoard[0][0] = " ";
					chessBoard[0][2] = "k";
					chessBoard[0][3] = "r";
					blackKingMoved++;
					qbRookMoved++;
				}
			}
			else {
				String temp = chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1];
				chessBoard[8-(Character.getNumericValue(a.charAt(3)))][Character.getNumericValue(a.charAt(2))-1] = temp;
				chessBoard[8-(Character.getNumericValue(a.charAt(1)))][Character.getNumericValue(a.charAt(0))-1] = " ";
				//CHECKING EN PASSANT LEGALITY
				if (temp.equals("P") && a.substring(1, 2).equals("2") && a.substring(3, 4).equals("4")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))-1] = true; timePawnMoved[Integer.parseInt(a.substring(0,1))-1] = totalMoves+1;}
				if (temp.equals("p") && a.substring(1, 2).equals("7") && a.substring(3, 4).equals("5")) {pawnDoubleMove[Integer.parseInt(a.substring(0,1))+7] = true; timePawnMoved[Integer.parseInt(a.substring(0,1))+7] = totalMoves+1;}
				if (temp.equals("K")) whiteKingMoved++;
				if (temp.equals("k")) blackKingMoved++;
				if (temp.equals("R") && a.substring(0,2).equals("a1")) qwRookMoved++;
				if (temp.equals("R") && a.substring(0,2).equals("h1")) kwRookMoved++;
				if (temp.equals("r") && a.substring(0,2).equals("a8")) qbRookMoved++;
				if (temp.equals("r") && a.substring(0,2).equals("h8")) kbRookMoved++;
			}
		}
		else {
			if (a.equals("O-O")) {
				if (totalMoves%2==0 && whiteKingMoved==0 && kwRookMoved==0 && chessBoard[7][6].equals(" ") && chessBoard[7][5].equals(" ")) {
					chessBoard[7][4] = " ";
					chessBoard[7][7] = " ";
					chessBoard[7][6] = "K";
					chessBoard[7][5] = "R";
					whiteKingMoved++;
					kwRookMoved++;
				}
				if (totalMoves%2==1 && blackKingMoved==0 && kbRookMoved==0 && chessBoard[0][6].equals(" ") && chessBoard[0][5].equals(" ")) {
					chessBoard[0][4] = " ";
					chessBoard[0][7] = " ";
					chessBoard[0][6] = "K";
					chessBoard[0][5] = "R";
					blackKingMoved ++;
					kbRookMoved++;
				}
			}
		}
		totalMoves++;
	}

	public static String[][] stateVisual(int[] state) {
		String[][] a = {
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "},
				{" "," "," "," "," "," "," "," "}};

		for (int i = 0;i<(64*6);i++) {
			if (state[i]==1) {
				if (i%6==0) a[(i/6)/8][(i/6)%8] = "P";
				if (i%6==1) a[(i/6)/8][(i/6)%8] = "N";
				if (i%6==2) a[(i/6)/8][(i/6)%8] = "B";
				if (i%6==3) a[(i/6)/8][(i/6)%8] = "R";
				if (i%6==4) a[(i/6)/8][(i/6)%8] = "Q";
				if (i%6==5) a[(i/6)/8][(i/6)%8] = "K";
			} 
			else if (state[i]==-1) {
				if (i%6==0) a[(i/6)/8][(i/6)%8] = "p";
				if (i%6==1) a[(i/6)/8][(i/6)%8] = "n";
				if (i%6==2) a[(i/6)/8][(i/6)%8] = "b";
				if (i%6==3) a[(i/6)/8][(i/6)%8] = "r";
				if (i%6==4) a[(i/6)/8][(i/6)%8] = "q";
				if (i%6==5) a[(i/6)/8][(i/6)%8] = "k";
			} 
		}
		return a;
	}

	public static void printBoard(String[][] a) {
		System.out.println(" A  B  C  D  E  F  G  H");
		System.out.println("------------------------");
		for (int i = 0;i<8;i++) {
			System.out.println(Arrays.toString(a[i]) + " |" + Integer.valueOf(8-(i)) + "| ");
		}
		System.out.println("------------------------");
	}

	public static int[] convertToState(String[][] chessBoard) {
		int[] a = new int[64*6];
		for (int i = 0;i<64;i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": a[i*6] = 1;
			break;
			case "N": a[i*6+1] = 1;
			break;
			case "B": a[i*6+2] = 1;
			break;
			case "R": a[i*6+3] = 1;
			break;
			case "Q": a[i*6+4] = 1;
			break;
			case "K": a[i*6+5] = 1;
			break;
			case "p": a[i*6] = -1;
			break;
			case "n": a[i*6+1] = -1;
			break;
			case "b": a[i*6+2] = -1;
			break;
			case "r": a[i*6+3] = -1;
			break;
			case "q": a[i*6+4] = -1;
			break;
			case "k": a[i*6+5] = -1;
			break;
			}
		}
		return a;
	} 

	public static int findWhiteKing() {
		for (int i = 0;i<64;i++) {
			if (chessBoard[i/8][i%8].equals("K")) return i;
		}
		return 0;
	}

	public static String[] legalWMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "P": moves+=legalWP(i);
			break;
			case "R": moves+=legalWR(i);
			break;
			case "N": moves+=legalWN(i);
			break;
			case "B": moves+=legalWB(i);
			break;
			case "Q": moves+=legalWQ(i);
			break;
			case "K": moves+=legalWK(i);
			break;
			}
		}
		return moves.split(" ");
	}

	public static String legalWP(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			try {//capture
				if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
					oldPiece=chessBoard[r-1][c+j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r-1][c+j]="P";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR+1)+oldPiece+" ";
					}
					chessBoard[r][c]="P";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r-1][c+j]=oldPiece;
				}
			} catch (Exception e) {}
			try {//en passant
				if (chessBoard[r][c+j].substring(0,1)=="p" && r==3) {
					chessBoard[r][c]=" ";
					chessBoard[r][c+j]=" ";
					chessBoard[r-1][c+j]="P";
					if (whiteKingSafe() && pawnDoubleMove[8+c+j] && ((totalMoves-timePawnMoved[8+c+j])==0)) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR+1)+"x ";
					}
					chessBoard[r][c]="P";
					chessBoard[r][c+j]="p";
					chessBoard[r-1][c+j]=" ";
				}
			} catch (Exception e) {}
			try {//promotion && capture
				if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
					String[] temp={"Q","R","B","N"};
					for (int k=0; k<4; k++) {
						oldPiece=chessBoard[r-1][c+j];
						chessBoard[r][c]=" ";
						chessBoard[r-1][c+j]=temp[k];
						if (whiteKingSafe()) {
							//column1,column2,captured-piece,new-piece,=
							moves=moves+colNames[c]+colNames[(c+j)]+oldPiece+temp[k]+"="+" ";
						}
						chessBoard[r][c]="P";
						chessBoard[r-1][c+j]=oldPiece;
					}
				}
			} catch (Exception e) {}
		}
		try {//move one up
			if (" ".equals(chessBoard[r-1][c]) && i>=16) {
				oldPiece=chessBoard[r-1][c];
				chessBoard[r][c]=" ";
				chessBoard[r-1][c]="P";
				if (whiteKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR+1)+oldPiece;
				}
				chessBoard[r][c]="P";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r-1][c]=oldPiece;
			}
		} catch (Exception e) {}
		try {//promotion && no capture
			if (" ".equals(chessBoard[r-1][c]) && i<16) {
				String[] temp={"Q","R","B","N"};
				for (int k=0; k<4; k++) {
					oldPiece=chessBoard[r-1][c];
					chessBoard[r][c]=" ";
					chessBoard[r-1][c]=temp[k];
					if (whiteKingSafe()) {
						//column1,column2,captured-piece,new-piece,P
						moves=moves+colNames[c]+colNames[c]+"/"+temp[k]+"="+" ";
					}
					chessBoard[r][c]="P";
					chessBoard[r-1][c]=oldPiece;
				}
			}
		} catch (Exception e) {}
		try {//move two up
			if (" ".equals(chessBoard[r-1][c]) && " ".equals(chessBoard[r-2][c]) && i>=48) {
				oldPiece=chessBoard[r-2][c];
				chessBoard[r][c]=" ";
				chessBoard[r-2][c]="P";
				if (whiteKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR+2)+oldPiece;
				}
				chessBoard[r][c]="P";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r-2][c]=oldPiece;
			}
		} catch (Exception e) {}
		return moves;
	}

	public static String legalWR(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			try {
				while(" ".equals(chessBoard[r][c+temp*j]))
				{
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
					temp++;
				}
				if (Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))) {
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[r+temp*j][c]))
				{
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
					temp++;
				}
				if (Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="R";
					if (whiteKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="R";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
		}
		return moves;
	}
	public static String legalWN(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					if (Character.isLowerCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
						oldPiece=chessBoard[r+j][c+k*2];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k*2)]+(displayR-j)+oldPiece+" ";
						}
						chessBoard[r][c]="N";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j][c+k*2]=oldPiece;
					}
				} catch (Exception e) {}
				try {
					if (Character.isLowerCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
						oldPiece=chessBoard[r+j*2][c+k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k)]+(displayR-j*2)+oldPiece+" ";
						}
						chessBoard[r][c]="N";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j*2][c+k]=oldPiece;
					}
				} catch (Exception e) {}
			}
		}
		return moves;
	}

	public static String legalWB(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
					{
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="B";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
						temp++;
					}
					if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="B";
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="B";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		return moves;
	}
	public static String legalWQ(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j++) {
			for (int k=-1; k<=1; k++) {
				if (j!=0 || k!=0) {
					try {
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if (whiteKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="Q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
							temp++;
						}
						if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="Q";
							if (whiteKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="Q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
						}
					} catch (Exception e) {}
					temp=1;
				}
			}
		}
		return moves;
	}
	public static String legalWK(int i) {
		int whiteKingPos = findWhiteKing();
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		if (whiteKingSafe() && whiteKingMoved==0 && qwRookMoved==0 && chessBoard[7][3].equals(" ") && chessBoard[7][2].equals(" ") && chessBoard[7][1].equals(" ")) {
			chessBoard[7][4] = " ";
			chessBoard[7][0] = " ";
			chessBoard[7][2] = "K";
			chessBoard[7][3] = "R";
			if (whiteKingSafe()) moves = moves+"O-O-O ";
			chessBoard[7][4] = "K";
			chessBoard[7][0] = "R";
			chessBoard[7][2] = " ";
			chessBoard[7][3] = " ";
		}
		if (whiteKingSafe() && whiteKingMoved==0 && kwRookMoved==0 && chessBoard[7][6].equals(" ") && chessBoard[7][5].equals(" ")) {
			chessBoard[7][4] = " ";
			chessBoard[7][0] = " ";
			chessBoard[7][6] = "K";
			chessBoard[7][5] = "R";
			if (whiteKingSafe()) moves = moves+"O-O ";
			chessBoard[7][4] = "K";
			chessBoard[7][0] = "R";
			chessBoard[7][6] = " ";
			chessBoard[7][5] = " ";
		}
		for (int j=0; j<9; j++) {
			if (j!=4) {
				try {
					if (Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
						oldPiece=chessBoard[r-1+j/3][c-1+j%3];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r-1+j/3][c-1+j%3]="K";
						int kingTemp=whiteKingPos;
						whiteKingPos=i+(j/3)*8+j%3-9;
						if (whiteKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c-1+j%3)]+(displayR+1-j/3)+oldPiece+" ";
						}
						chessBoard[r][c]="K";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
						whiteKingPos=kingTemp;
					}
				} catch (Exception e) {}
			}
		}
		//need to add casting later
		return moves;
	}
	public static boolean whiteKingSafe() {
		int whiteKingPos = findWhiteKing();
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {temp++;}
					if ("b".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j]) ||
							"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i]) ||
						"q".equals(chessBoard[whiteKingPos/8][whiteKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {temp++;}
				if ("r".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8]) ||
						"q".equals(chessBoard[whiteKingPos/8+temp*i][whiteKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("n".equals(chessBoard[whiteKingPos/8+i*2][whiteKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (whiteKingPos>=16) {
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8-1])) {
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("p".equals(chessBoard[whiteKingPos/8-1][whiteKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("k".equals(chessBoard[whiteKingPos/8+i][whiteKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}
	
	public static int findBlackKing() {
		for (int i = 0;i<64;i++) {
			if (chessBoard[i/8][i%8].equals("k")) return i;
		}
		return 0;
	}

	public static String[] legalBMoves() {
		String moves="";
		for (int i=0; i<64; i++) {
			switch (chessBoard[i/8][i%8]) {
			case "p": moves+=legalBP(i);
			break;
			case "r": moves+=legalBR(i);
			break;
			case "n": moves+=legalBN(i);
			break;
			case "b": moves+=legalBB(i);
			break;
			case "q": moves+=legalBQ(i);
			break;
			case "k": moves+=legalBK(i);
			break;
			}
		}
		return moves.split(" ");
	}
	
	
	public static String legalBP(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			try {//capture
				if (Character.isUpperCase(chessBoard[r+1][c+j].charAt(0)) && i<=64) {
					oldPiece=chessBoard[r+1][c+j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+1][c+j]="p";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR-1)+oldPiece+" ";
					}
					chessBoard[r][c]="p";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+1][c+j]=oldPiece;
				}
			} catch (Exception e) {}
			try {//en passant
				if (chessBoard[r][c+j].substring(0,1)=="P" && r==4) {
					chessBoard[r][c]=" ";
					chessBoard[r][c+j]=" ";
					chessBoard[r+1][c+j]="p";
					if (blackKingSafe() && pawnDoubleMove[c+j] && ((totalMoves-timePawnMoved[c+j])==0)) {
						moves=moves+colNames[c]+displayR+colNames[(c+j)]+(displayR-1)+"x ";
					}
					chessBoard[r][c]="p";
					chessBoard[r][c+j]="P";
					chessBoard[r+1][c+j]=" ";
				}
			} catch (Exception e) {}
			try {//promotion && capture
				if (Character.isUpperCase(chessBoard[r+1][c+j].charAt(0)) && i>64) {
					String[] temp={"q","r","b","n"};
					for (int k=0; k<4; k++) {
						oldPiece=chessBoard[r+1][c+j];
						chessBoard[r][c]=" ";
						chessBoard[r+1][c+j]=temp[k];
						if (blackKingSafe()) {
							//column1,column2,captured-piece,new-piece,=
							moves=moves+colNames[c]+colNames[(c+j)]+oldPiece+temp[k]+"="+" ";
						}
						chessBoard[r][c]="p";
						chessBoard[r+1][c+j]=oldPiece;
					}
				}
			} catch (Exception e) {}
		}
		try {//move one up
			if (" ".equals(chessBoard[r+1][c]) && i<=64) {
				oldPiece=chessBoard[r+1][c];
				chessBoard[r][c]=" ";
				chessBoard[r+1][c]="p";
				if (blackKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR-1)+oldPiece;
				}
				chessBoard[r][c]="p";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r+1][c]=oldPiece;
			}
		} catch (Exception e) {}
		try {//promotion && no capture
			if (" ".equals(chessBoard[r-1][c]) && i>64) {
				String[] temp={"q","r","b","n"};
				for (int k=0; k<4; k++) {
					oldPiece=chessBoard[r-1][c];
					chessBoard[r][c]=" ";
					chessBoard[r+1][c]=temp[k];
					if (blackKingSafe()) {
						//column1,column2,captured-piece,new-piece,P
						moves=moves+colNames[c]+colNames[c]+"/"+temp[k]+"="+" ";
					}
					chessBoard[r][c]="p";
					chessBoard[r+1][c]=oldPiece;
				}
			}
		} catch (Exception e) {}
		try {//move two up
			if (" ".equals(chessBoard[r+1][c]) && " ".equals(chessBoard[r+2][c]) && i<=48) {
				oldPiece=chessBoard[r+2][c];
				chessBoard[r][c]=" ";
				chessBoard[r+2][c]="p";
				if (blackKingSafe()) {
					moves=moves+colNames[c]+displayR+colNames[c]+(displayR-2)+oldPiece;
				}
				chessBoard[r][c]="p";
				if(oldPiece.equals("")) oldPiece = " ";
				chessBoard[r+2][c]=oldPiece;
			}
		} catch (Exception e) {}
		return moves;
	}

	
	public static String legalBR(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			try {
				while(" ".equals(chessBoard[r][c+temp*j]))
				{
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
					temp++;
				}
				if (Character.isUpperCase(chessBoard[r][c+temp*j].charAt(0))) {
					oldPiece=chessBoard[r][c+temp*j];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r][c+temp*j]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[(c+temp*j)]+displayR+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r][c+temp*j]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[r+temp*j][c]))
				{
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
					temp++;
				}
				if (Character.isUpperCase(chessBoard[r+temp*j][c].charAt(0))) {
					oldPiece=chessBoard[r+temp*j][c];
					if(oldPiece.equals(" ")) oldPiece = "";
					chessBoard[r][c]=" ";
					chessBoard[r+temp*j][c]="r";
					if (blackKingSafe()) {
						moves=moves+colNames[c]+displayR+colNames[c]+(displayR-temp*j)+oldPiece+" ";
					}
					chessBoard[r][c]="r";
					if(oldPiece.equals("")) oldPiece = " ";
					chessBoard[r+temp*j][c]=oldPiece;
				}
			} catch (Exception e) {}
			temp=1;
		}
		return moves;
	}
	
	
	public static String legalBN(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					if (Character.isUpperCase(chessBoard[r+j][c+k*2].charAt(0)) || " ".equals(chessBoard[r+j][c+k*2])) {
						oldPiece=chessBoard[r+j][c+k*2];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k*2)]+(displayR-j)+oldPiece+" ";
						}
						chessBoard[r][c]="n";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j][c+k*2]=oldPiece;
					}
				} catch (Exception e) {}
				try {
					if (Character.isUpperCase(chessBoard[r+j*2][c+k].charAt(0)) || " ".equals(chessBoard[r+j*2][c+k])) {
						oldPiece=chessBoard[r+j*2][c+k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+k)]+(displayR-j*2)+oldPiece+" ";
						}
						chessBoard[r][c]="n";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+j*2][c+k]=oldPiece;
					}
				} catch (Exception e) {}
			}
		}
		return moves;
	}

	
	public static String legalBB(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j+=2) {
			for (int k=-1; k<=1; k+=2) {
				try {
					while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
					{
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="b";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="b";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
						temp++;
					}
					if (Character.isUpperCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
						oldPiece=chessBoard[r+temp*j][c+temp*k];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r+temp*j][c+temp*k]="b";
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
						}
						chessBoard[r][c]="b";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r+temp*j][c+temp*k]=oldPiece;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		return moves;
	}
	
	
	public static String legalBQ(int i) {
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		int temp=1;
		for (int j=-1; j<=1; j++) {
			for (int k=-1; k<=1; k++) {
				if (j!=0 || k!=0) {
					try {
						while(" ".equals(chessBoard[r+temp*j][c+temp*k]))
						{
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="q";
							if (blackKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
							temp++;
						}
						if (Character.isUpperCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
							oldPiece=chessBoard[r+temp*j][c+temp*k];
							if (oldPiece.equals(" ")) oldPiece="";
							chessBoard[r][c]=" ";
							chessBoard[r+temp*j][c+temp*k]="q";
							if (blackKingSafe()) {
								moves=moves+colNames[c]+displayR+colNames[(c+temp*k)]+(displayR-temp*j)+oldPiece+" ";
							}
							chessBoard[r][c]="q";
							if (oldPiece.equals("")) oldPiece=" ";
							chessBoard[r+temp*j][c+temp*k]=oldPiece;
						}
					} catch (Exception e) {}
					temp=1;
				}
			}
		}
		return moves;
	}
	public static String legalBK(int i) {
		int blackKingPos = findBlackKing();
		String moves="", oldPiece;
		int r=i/8, c=i%8;
		int displayR = 8-r;
		if (blackKingSafe() && blackKingMoved==0 && qbRookMoved==0 && chessBoard[0][3].equals(" ") && chessBoard[0][2].equals(" ") && chessBoard[0][1].equals(" ")) {
			chessBoard[7][4] = " ";
			chessBoard[7][0] = " ";
			chessBoard[7][2] = "k";
			chessBoard[7][3] = "r";
			if (blackKingSafe()) moves = moves+"O-O-O ";
			chessBoard[7][4] = "k";
			chessBoard[7][0] = "r";
			chessBoard[7][2] = " ";
			chessBoard[7][3] = " ";
		}
		if (blackKingSafe() && blackKingMoved==0 && kbRookMoved==0 && chessBoard[0][6].equals(" ") && chessBoard[0][5].equals(" ")) {
			chessBoard[0][4] = " ";
			chessBoard[0][0] = " ";
			chessBoard[0][6] = "k";
			chessBoard[0][5] = "r";
			if (blackKingSafe()) moves = moves+"O-O ";
			chessBoard[0][4] = "k";
			chessBoard[0][0] = "r";
			chessBoard[0][6] = " ";
			chessBoard[0][5] = " ";
		}
		for (int j=0; j<9; j++) {
			if (j!=4) {
				try {
					if (Character.isUpperCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
						oldPiece=chessBoard[r-1+j/3][c-1+j%3];
						if (oldPiece.equals(" ")) oldPiece="";
						chessBoard[r][c]=" ";
						chessBoard[r-1+j/3][c-1+j%3]="k";
						int kingTemp=blackKingPos;
						blackKingPos=i+(j/3)*8+j%3-9;
						if (blackKingSafe()) {
							moves=moves+colNames[c]+displayR+colNames[(c-1+j%3)]+(displayR+1-j/3)+oldPiece+" ";
						}
						chessBoard[r][c]="k";
						if (oldPiece.equals("")) oldPiece=" ";
						chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
						blackKingPos=kingTemp;
					}
				} catch (Exception e) {}
			}
		}
		//need to add casting later
		return moves;
	}
	public static boolean blackKingSafe() {
		int blackKingPos = findBlackKing();
		//bishop/queen
		int temp=1;
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {temp++;}
					if ("B".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j]) ||
							"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8+temp*j])) {
						return false;
					}
				} catch (Exception e) {}
				temp=1;
			}
		}
		//rook/queen
		for (int i=-1; i<=1; i+=2) {
			try {
				while(" ".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i]) ||
						"Q".equals(chessBoard[blackKingPos/8][blackKingPos%8+temp*i])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
			try {
				while(" ".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {temp++;}
				if ("R".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8]) ||
						"Q".equals(chessBoard[blackKingPos/8+temp*i][blackKingPos%8])) {
					return false;
				}
			} catch (Exception e) {}
			temp=1;
		}
		//knight
		for (int i=-1; i<=1; i+=2) {
			for (int j=-1; j<=1; j+=2) {
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j*2])) {
						return false;
					}
				} catch (Exception e) {}
				try {
					if ("N".equals(chessBoard[blackKingPos/8+i*2][blackKingPos%8+j])) {
						return false;
					}
				} catch (Exception e) {}
			}
		}
		//pawn
		if (blackKingPos<=64) {
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8-1])) { 
					return false;
				}
			} catch (Exception e) {}
			try {
				if ("P".equals(chessBoard[blackKingPos/8+1][blackKingPos%8+1])) {
					return false;
				}
			} catch (Exception e) {}
			//king
			for (int i=-1; i<=1; i++) {
				for (int j=-1; j<=1; j++) {
					if (i!=0 || j!=0) {
						try {
							if ("K".equals(chessBoard[blackKingPos/8+i][blackKingPos%8+j])) {
								return false;
							}
						} catch (Exception e) {}
					}
				}
			}
		}
		return true;
	}
}