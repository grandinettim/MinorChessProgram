//Mitchell Kendrioski Chess tutorial chess.com
//10/4/2014

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.*;

public class AlphaBetaChess {

	private int kingPositionC;
	private int kingPositionL; // 0 - 63
	private int humanAsWhite = -1; // 1 = human ad white, 0 = human as black
	private int globalDepth = 4;

	private String chessBoard[][] = {
			{ "r", "n", "b", "q", "k", "b", "n", "r" },
			{ "p", "p", "p", "p", "p", "p", "p", "p" },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ " ", " ", " ", " ", " ", " ", " ", " " },
			{ "P", "P", "P", "P", "P", "P", "P", "P" },
			{ "R", "N", "B", "Q", "K", "B", "N", "R" } };

	/*
	 * public static void main(String[] args) { while
	 * (!"K".equals(chessBoard[kingPositionC / 8][kingPositionC % 8]))
	 * kingPositionC++; while (!"k".equals(chessBoard[kingPositionL /
	 * 8][kingPositionL % 8])) kingPositionL++;
	 * 
	 * JFrame f = new JFrame("Chess Tutorial");
	 * f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); UserInterface ui = new
	 * UserInterface(); f.add(ui); f.setSize(500, 500); f.setVisible(true);
	 * System.out.println(possibleMoves()); Object[] option = { "Computer",
	 * "Human" }; humanAsWhite = JOptionPane.showOptionDialog(null,
	 * "Who should play as White", "ABC Options", JOptionPane.YES_NO_OPTION,
	 * JOptionPane.QUESTION_MESSAGE, null, option, option[1]); if (humanAsWhite
	 * == 0) { long startTime = System.currentTimeMillis();
	 * makeMove(alphaBeta(globalDepth, 1000000, -1000000, "", 0)); long endTime
	 * = System.currentTimeMillis(); System.out.println("That took " + (endTime
	 * - startTime) + " milliseconds"); flipboard(); f.repaint(); }
	 * 
	 * // makeMove("7655 "); // undoMove("7655 "); for (int i = 0; i < 8; i++) {
	 * System.out.println(Arrays.toString(chessBoard[i])); } }
	 */

	public AlphaBetaChess() {

	}

	public String alphaBeta(String move, int player) {
		return alphaBeta(this.globalDepth, 1000000, -1000000, move, player);
	}

	private String alphaBeta(int depth, int beta, int alpha, String move,
			int player) {
		// return in form of 1234b###########
		
		Rating rating = new Rating(this);
		
		String list = possibleMoves();
		if (depth == 0 || list.length() == 0) {
			return move
					+ (rating.rating(list.length(), depth) * (player * 2 - 1));
		}
		// list = sortMoves(list);
		// sort later
		player = 1 - player;// either 1 or 0
		for (int i = 0; i < list.length(); i += 5) {
			// System.out.println(list.substring(i,i+5));
			makeMove(list.substring(i, i + 5));
			flipboard();
			String returnString = alphaBeta(depth - 1, beta, alpha,
					list.substring(i, i + 5), player);
			int value = Integer.valueOf(returnString.substring(5));
			flipboard();
			undoMove(list.substring(i, i + 5));
			if (player == 0) {
				if (value <= beta) {
					beta = value;
					if (depth == getGlobalDepth()) {
						move = returnString.substring(0, 5);
					}
				}
			} else {
				if (value > alpha) {
					alpha = value;
					if (depth == getGlobalDepth()) {
						move = returnString.substring(0, 5);
					}
				}
			}
			if (alpha >= beta) {
				if (player == 0) {
					return move + beta;
				} else {
					return move + alpha;
				}
			}
		}
		if (player == 0) {
			return move + beta;
		} else {
			return move + alpha;
		}
	}

	public void flipboard() {
		String temp;
		for (int i = 0; i < 32; i++) {
			int r = i / 8, c = i % 8;
			if (Character.isUpperCase(getChessBoard()[r][c].charAt(0))) {
				temp = getChessBoard()[r][c].toLowerCase();
			} else {
				temp = getChessBoard()[r][c].toUpperCase();
			}
			if (Character.isUpperCase(getChessBoard()[7 - r][7 - c].charAt(0))) {
				getChessBoard()[r][c] = getChessBoard()[7 - r][7 - c]
						.toLowerCase();
			} else {
				getChessBoard()[r][c] = getChessBoard()[7 - r][7 - c]
						.toUpperCase();
			}
			getChessBoard()[7 - r][7 - c] = temp;
		}
		int kingTemp = getKingPositionC();
		setKingPositionC(63 - getKingPositionL());
		setKingPositionL(63 - kingTemp);
		// for(int i = 0;i<8;i++){
		// System.out.println(Arrays.toString(chessBoard[i]));
		// }

	}

	public void makeMove(String move) {
		if (move.charAt(4) != 'P') {
			// x1,y1,x2,y2,captured piece
			getChessBoard()[Character.getNumericValue(move.charAt(2))][Character
					.getNumericValue(move.charAt(3))] = getChessBoard()[Character
					.getNumericValue(move.charAt(0))][Character
					.getNumericValue(move.charAt(1))];
			getChessBoard()[Character.getNumericValue(move.charAt(0))][Character
					.getNumericValue(move.charAt(1))] = " ";
			if ("K".equals(getChessBoard()[Character.getNumericValue(move
					.charAt(2))][Character.getNumericValue(move.charAt(3))])) {
				setKingPositionC(8 * Character.getNumericValue(move.charAt(2))
						+ Character.getNumericValue(move.charAt(3)));
			}
		} else {
			// if pawn promotion
			// column 1,column2,captured piece,new piece,P
			getChessBoard()[1][Character.getNumericValue(move.charAt(0))] = " ";
			getChessBoard()[0][Character.getNumericValue(move.charAt(1))] = String
					.valueOf(move.charAt(3));
		}
	}

	public void undoMove(String move) {
		if (move.charAt(4) != 'P') {
			// x1,y1,x2,y2,captured piece
			getChessBoard()[Character.getNumericValue(move.charAt(0))][Character
					.getNumericValue(move.charAt(1))] = getChessBoard()[Character
					.getNumericValue(move.charAt(2))][Character
					.getNumericValue(move.charAt(3))];
			getChessBoard()[Character.getNumericValue(move.charAt(2))][Character
					.getNumericValue(move.charAt(3))] = String.valueOf(move
					.charAt(4));
			if ("K".equals(getChessBoard()[Character.getNumericValue(move
					.charAt(0))][Character.getNumericValue(move.charAt(1))])) {
				setKingPositionC(8 * Character.getNumericValue(move.charAt(0))
						+ Character.getNumericValue(move.charAt(1)));
			}
		} else {
			// if pawn promotion
			// column 1,column2,captured piece,new piece,P
			getChessBoard()[1][Character.getNumericValue(move.charAt(0))] = " ";
			getChessBoard()[0][Character.getNumericValue(move.charAt(1))] = String
					.valueOf(move.charAt(2));
		}
	}

	public String possibleMoves() {
		String list = "";
		for (int i = 0; i < 64; i++) {
			switch (getChessBoard()[i / 8][i % 8]) {
			case "P":
				list += possibleP(i);
				break;
			case "R":
				list += possibleR(i);
				break;
			case "N":
				list += possibleN(i);
				break;
			case "B":
				list += possibleB(i);
				break;
			case "Q":
				list += possibleQ(i);
				break;
			case "K":
				list += possibleK(i);
				break;// castling is dealt with from perspective of the king

			}

		}
		return list;// x1,y1,x2,y2,captured piece
	}

	public String possibleP(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		for (int j = -1; j <= 1; j += 2) {
			try {// capture
				if (Character.isLowerCase(getChessBoard()[r - 1][c + j]
						.charAt(0)) && i >= 16) {
					oldPiece = getChessBoard()[r - 1][c + j];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r - 1][c + j] = "P";
					if (kingSafe()) {
						list = list + r + c + (r - 1) + (c + j) + oldPiece;
					}
					getChessBoard()[r][c] = "P";
					getChessBoard()[r - 1][c + j] = oldPiece;
				}
			} catch (Exception e) {
			}
			try {// capture&promotion
				if (Character.isLowerCase(getChessBoard()[r - 1][c + j]
						.charAt(0)) && i < 16) {
					String[] temp = { "Q", "R", "B", "N" };
					for (int k = 0; k < 4; k++) {
						oldPiece = getChessBoard()[r - 1][c + j];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r - 1][c + j] = temp[k];
						if (kingSafe()) {
							// column 1,column2,captured piece,new piece,P
							list = list + c + (c + j) + oldPiece + temp[k]
									+ "P";
						}
						getChessBoard()[r][c] = "P";
						getChessBoard()[r - 1][c + j] = oldPiece;
					}

				}
			} catch (Exception e) {
			}
		}
		try {// move 1 up
			if (" ".equals(getChessBoard()[r - 1][c]) && i >= 16) {
				oldPiece = getChessBoard()[r - 1][c];
				getChessBoard()[r][c] = " ";
				getChessBoard()[r - 1][c] = "P";
				if (kingSafe()) {
					list = list + r + c + (r - 1) + c + oldPiece;
				}
				getChessBoard()[r][c] = "P";
				getChessBoard()[r - 1][c] = oldPiece;
			}
		} catch (Exception e) {
		}
		try {// promotion no capture
			if (" ".equals(getChessBoard()[r - 1][c]) && i < 16) {
				String[] temp = { "Q", "R", "B", "N" };
				for (int k = 0; k < 4; k++) {
					oldPiece = getChessBoard()[r - 1][c];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r - 1][c] = temp[k];
					if (kingSafe()) {
						// column 1,column2,captured piece,new piece,P
						list = list + c + c + oldPiece + temp[k] + "P";
					}
					getChessBoard()[r][c] = "P";
					getChessBoard()[r - 1][c] = oldPiece;
				}
			}
		} catch (Exception e) {
		}
		try {// move 2 up
			if (" ".equals(getChessBoard()[r - 1][c])
					&& " ".equals(getChessBoard()[r - 2][c]) && i >= 48) {
				oldPiece = getChessBoard()[r - 2][c];
				getChessBoard()[r][c] = " ";
				getChessBoard()[r - 2][c] = "P";
				if (kingSafe()) {
					list = list + r + c + (r - 2) + c + oldPiece;
				}
				getChessBoard()[r][c] = "P";
				getChessBoard()[r - 2][c] = oldPiece;
			}
		} catch (Exception e) {
		}
		return list;
	}

	public String possibleR(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		int temp = 1;
		for (int j = -1; j <= 1; j += 2) {
			try {
				while (" ".equals(getChessBoard()[r][c + temp * j])) {
					oldPiece = getChessBoard()[r][c + temp * j];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r][c + temp * j] = "R";
					if (kingSafe()) {
						list = list + r + c + r + (c + temp * j) + oldPiece;
					}
					getChessBoard()[r][c] = "R";
					getChessBoard()[r][c + temp * j] = oldPiece;
					temp++;
				}
				if (Character.isLowerCase(getChessBoard()[r][c + temp * j]
						.charAt(0))) {
					oldPiece = getChessBoard()[r][c + temp * j];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r][c + temp * j] = "R";
					if (kingSafe()) {
						list = list + r + c + r + (c + temp * j) + oldPiece;
					}
					getChessBoard()[r][c] = "R";
					getChessBoard()[r][c + temp * j] = oldPiece;

				}
			} catch (Exception e) {
			}
			temp = 1;
			try {
				while (" ".equals(getChessBoard()[r + temp * j][c])) {
					oldPiece = getChessBoard()[r + temp * j][c];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r + temp * j][c] = "R";
					if (kingSafe()) {
						list = list + r + c + (r + temp * j) + c + oldPiece;
					}
					getChessBoard()[r][c] = "R";
					getChessBoard()[r + temp * j][c] = oldPiece;
					temp++;
				}
				if (Character.isLowerCase(getChessBoard()[r + temp * j][c]
						.charAt(0))) {
					oldPiece = getChessBoard()[r + temp * j][c];
					getChessBoard()[r][c] = " ";
					getChessBoard()[r + temp * j][c] = "R";
					if (kingSafe()) {
						list = list + r + c + (r + temp * j) + c + oldPiece;
					}
					getChessBoard()[r][c] = "R";
					getChessBoard()[r + temp * j][c] = oldPiece;

				}
			} catch (Exception e) {
			}
			temp = 1;
		}
		return list;
	}

	public String possibleN(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					if (Character.isLowerCase(getChessBoard()[r + j][c + k * 2]
							.charAt(0))
							|| " ".equals(getChessBoard()[r + j][c + k * 2])) {
						oldPiece = getChessBoard()[r + j][c + k * 2];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r + j][c + k * 2] = "N";
						if (kingSafe()) {
							list = list + r + c + (r + j) + (c + k * 2)
									+ oldPiece;
						}
						getChessBoard()[r][c] = "N";
						getChessBoard()[r + j][c + k * 2] = oldPiece;
					}
				} catch (Exception e) {
				}
				try {
					if (Character.isLowerCase(getChessBoard()[r + j * 2][c + k]
							.charAt(0))
							|| " ".equals(getChessBoard()[r + j * 2][c + k])) {
						oldPiece = getChessBoard()[r + j * 2][c + k];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r + j * 2][c + k] = "N";
						if (kingSafe()) {
							list = list + r + c + (r + j * 2) + (c + k)
									+ oldPiece;
						}
						getChessBoard()[r][c] = "N";
						getChessBoard()[r + j * 2][c + k] = oldPiece;
					}
				} catch (Exception e) {
				}
			}
		}
		return list;

	}

	public String possibleB(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		int temp = 1;
		for (int j = -1; j <= 1; j += 2) {
			for (int k = -1; k <= 1; k += 2) {
				try {
					while (" ".equals(getChessBoard()[r + temp * j][c + temp
							* k])) {
						oldPiece = getChessBoard()[r + temp * j][c + temp * k];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r + temp * j][c + temp * k] = "B";
						if (kingSafe()) {
							list = list + r + c + (r + temp * j)
									+ (c + temp * k) + oldPiece;
						}
						getChessBoard()[r][c] = "B";
						getChessBoard()[r + temp * j][c + temp * k] = oldPiece;
						temp++;
					}
					if (Character.isLowerCase(getChessBoard()[r + temp * j][c
							+ temp * k].charAt(0))) {
						oldPiece = getChessBoard()[r + temp * j][c + temp * k];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r + temp * j][c + temp * k] = "B";
						if (kingSafe()) {
							list = list + r + c + (r + temp * j)
									+ (c + temp * k) + oldPiece;
						}
						getChessBoard()[r][c] = "B";
						getChessBoard()[r + temp * j][c + temp * k] = oldPiece;

					}
				} catch (Exception e) {
				}
				temp = 1;
			}
		}
		return list;

	}

	public String possibleQ(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		int temp = 1;
		for (int j = -1; j <= 1; j++) {
			for (int k = -1; k <= 1; k++) {
				if (j != 0 || k != 0) {
					try {
						while (" ".equals(getChessBoard()[r + temp * j][c
								+ temp * k])) {
							oldPiece = getChessBoard()[r + temp * j][c + temp
									* k];
							getChessBoard()[r][c] = " ";
							getChessBoard()[r + temp * j][c + temp * k] = "Q";
							if (kingSafe()) {
								list = list + r + c + (r + temp * j)
										+ (c + temp * k) + oldPiece;
							}
							getChessBoard()[r][c] = "Q";
							getChessBoard()[r + temp * j][c + temp * k] = oldPiece;
							temp++;
						}
						if (Character
								.isLowerCase(getChessBoard()[r + temp * j][c
										+ temp * k].charAt(0))) {
							oldPiece = getChessBoard()[r + temp * j][c + temp
									* k];
							getChessBoard()[r][c] = " ";
							getChessBoard()[r + temp * j][c + temp * k] = "Q";
							if (kingSafe()) {
								list = list + r + c + (r + temp * j)
										+ (c + temp * k) + oldPiece;
							}
							getChessBoard()[r][c] = "Q";
							getChessBoard()[r + temp * j][c + temp * k] = oldPiece;

						}
					} catch (Exception e) {
					}
					temp = 1;
				}
			}
		}
		return list;
	}

	public String possibleK(int i) {
		String list = "", oldPiece;
		int r = i / 8, c = i % 8;
		for (int j = 0; j < 9; j++) {
			if (j != 4) {
				try {
					if (Character.isLowerCase(getChessBoard()[r - 1 + j / 3][c
							- 1 + j % 3].charAt(0))
							|| " ".equals(getChessBoard()[r - 1 + j / 3][c - 1
									+ j % 3])) {
						oldPiece = getChessBoard()[r - 1 + j / 3][c - 1 + j % 3];
						getChessBoard()[r][c] = " ";
						getChessBoard()[r - 1 + j / 3][c - 1 + j % 3] = "K";
						int kingTemp = getKingPositionC();
						setKingPositionC(i + (j / 3) * 8 + j % 3 - 9);
						if (kingSafe()) {
							list = list + r + c + (r - 1 + j / 3)
									+ (c - 1 + j % 3) + oldPiece;
						}
						getChessBoard()[r][c] = "K";
						getChessBoard()[r - 1 + j / 3][c - 1 + j % 3] = oldPiece;
						setKingPositionC(kingTemp);
					}
				} catch (Exception e) {
				}
			}
		}
		// need to add castling
		return list;
	}

	/*public String sortMoves(String list) {
		int[] score = new int[list.length() / 5];
		for (int i = 0; i < list.length(); i += 5) {
			makeMove(list.substring(i, i + 5));
			score[i / 5] = -Rating.rating(-1, 0);
			undoMove(list.substring(i, i + 5));
		}
		String newListA = "", newListB = list;
		for (int i = 0; i < Math.min(6, list.length() / 5); i++) {// first few
																	// moves
																	// only
			int max = -1000000, maxLocation = 0;
			for (int j = 0; j < list.length() / 5; j++) {
				if (score[j] > max) {
					max = score[j];
					maxLocation = j;
				}
				score[maxLocation] = -1000000;
				newListA += list
						.substring(maxLocation * 5, maxLocation * 5 + 5);
				newListB = newListB.replaceAll(
						list.substring(maxLocation * 5, maxLocation * 5 + 5),
						"");
			}
		}
		return newListA + newListB;
	}*/

	public boolean kingSafe() {
		// bishop/queen
		int temp = 1;
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					while (" ".equals(getChessBoard()[getKingPositionC() / 8
							+ temp * i][getKingPositionC() % 8 + temp * j])) {
						temp++;
					}
					if ("b".equals(getChessBoard()[getKingPositionC() / 8
							+ temp * i][getKingPositionC() % 8 + temp * j])
							|| "q".equals(getChessBoard()[getKingPositionC()
									/ 8 + temp * i][getKingPositionC() % 8
									+ temp * j])) {
						return false;
					}
				} catch (Exception e) {
				}
				temp = 1;
			}
		}
		// rook/queen
		for (int i = -1; i <= 1; i += 2) {
			try {
				while (" "
						.equals(getChessBoard()[getKingPositionC() / 8][getKingPositionC()
								% 8 + temp * i])) {
					temp++;
				}
				if ("r".equals(getChessBoard()[getKingPositionC() / 8][getKingPositionC()
						% 8 + temp * i])
						|| "q ".equals(getChessBoard()[getKingPositionC() / 8][getKingPositionC()
								% 8 + temp * i])) {
					return false;
				}

			} catch (Exception e) {
			}
			temp = 1;
			try {
				while (" ".equals(getChessBoard()[getKingPositionC() / 8 + temp
						* i][getKingPositionC() % 8])) {
					temp++;
				}
				if ("r".equals(getChessBoard()[getKingPositionC() / 8 + temp
						* i][getKingPositionC() % 8])
						|| "q".equals(getChessBoard()[getKingPositionC() / 8
								+ temp * i][getKingPositionC() % 8])) {
					return false;
				}

			} catch (Exception e) {
			}
			temp = 1;
		}
		// knight
		for (int i = -1; i <= 1; i += 2) {
			for (int j = -1; j <= 1; j += 2) {
				try {
					if ("n".equals(getChessBoard()[getKingPositionC() / 8 + i][getKingPositionC()
							% 8 + j * 2])) {
						return false;
					}
				} catch (Exception e) {
				}
				try {
					if ("n".equals(getChessBoard()[getKingPositionC() / 8 + i
							* 2][getKingPositionC() % 8 + j])) {
						return false;
					}
				} catch (Exception e) {
				}
			}
		}
		// pawn
		if (getKingPositionC() >= 16) {
			try {
				if ("p".equals(getChessBoard()[getKingPositionC() / 8 - 1][getKingPositionC() % 8 - 1])) {
					return false;
				}
			} catch (Exception e) {
			}
			try {
				if ("p".equals(getChessBoard()[getKingPositionC() / 8 - 1][getKingPositionC() % 8 + 1])) {
					return false;
				}
			} catch (Exception e) {
			}
			// king
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					if (i != 0 || j != 0) {
						try {
							if ("k".equals(getChessBoard()[getKingPositionC()
									/ 8 + i][getKingPositionC() % 8 + j])) {
								return false;
							}
						} catch (Exception e) {
						}

					}
				}
			}
		}
		return true;
	}

	public int getKingPositionC() {
		return kingPositionC;
	}

	public void setKingPositionC(int kingPositionC) {
		this.kingPositionC = kingPositionC;
	}

	public int getKingPositionL() {
		return kingPositionL;
	}

	public void setKingPositionL(int kingPositionL) {
		this.kingPositionL = kingPositionL;
	}

	public int getHumanAsWhite() {
		return humanAsWhite;
	}

	public void setHumanAsWhite(int humanAsWhite) {
		this.humanAsWhite = humanAsWhite;
	}

	public String[][] getChessBoard() {
		return chessBoard;
	}

	public void setChessBoard(String chessBoard[][]) {
		this.chessBoard = chessBoard;
	}

	public int getGlobalDepth() {
		return globalDepth;
	}

	public void setGlobalDepth(int globalDepth) {
		this.globalDepth = globalDepth;
	}

}
