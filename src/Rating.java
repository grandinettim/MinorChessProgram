//chess.com tutorial
public class Rating {
	private int pawnBoard[][] = {//attribute to chessprogramming wikispace/simplified evaluation+functions
		{  0,  0,  0,  0,  0,  0,  0,  0},
		{ 50, 50, 50, 50, 50, 50, 50, 50},
		{ 10, 10, 20, 30, 30, 20, 10, 10},
		{  5,  5, 10, 25, 25, 10,  5,  5},
		{  0,  0,  0, 20, 20,  0,  0,  0},
		{  5, -5,-10,  0,  0,-10, -5,  5},
		{  5, 10, 10,-20,-20, 10, 10,  5},
		{  0,  0,  0,  0,  0,  0,  0,  0},
	};
	private int rookBoard[][] = {
		{  0,  0,  0,  0,  0,  0,  0,  0},
		{  5, 10, 10, 10, 10, 10, 10,  5},
		{ -5,  0,  0,  0,  0,  0,  0, -5},
		{ -5,  0,  0,  0,  0,  0,  0, -5},
		{ -5,  0,  0,  0,  0,  0,  0, -5},
		{ -5,  0,  0,  0,  0,  0,  0, -5},
		{ -5,  0,  0,  0,  0,  0,  0, -5},
		{  0,  0,  0,  5,  5,  0,  0,  0},
	};
	private int knightBoard[][] = {
		{-50,-40,-30,-30,-30,-30,-40,-50},
		{-40,-20,  0,  0,  0,  0,-20,-40},
		{-30,  0, 10, 15, 15, 10,  0,-30},
		{-30,  5, 15, 20, 20, 15,  5,-30},
		{-30,  0, 15, 20, 20, 15,  0,-30},
		{-30,  5, 10, 15, 15, 10,  5,-30},
		{-40,-20,  0,  5,  5,  0,-20,-40},
		{-50,-40,-30,-30,-30,-30,-40,-50},
	};
	private int bishopBoard[][] = {
		{-20,-10,-10,-10,-10,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5, 10, 10,  5,  0,-10},
		{-10,  5,  5, 10, 10,  5,  5,-10},
		{-10,  0, 10, 10, 10, 10,  0,-10},
		{-10, 10, 10, 10, 10, 10, 10,-10},
		{-10,  5,  0,  0,  0,  0,  5,-10},
		{-20,-10,-10,-10,-10,-10,-10,-20},
	};
	private int queenBoard[][] = {
		{-20,-10,-10, -5, -5,-10,-10,-20},
		{-10,  0,  0,  0,  0,  0,  0,-10},
		{-10,  0,  5,  5,  5,  5,  0,-10},
		{ -5,  0,  5,  5,  5,  5,  0, -5},
		{  0,  0,  5,  5,  5,  5,  0, -5},
		{-10,  5,  5,  5,  5,  5,  0,-10},
		{-10,  0,  5,  0,  0,  0,  0,-10},
		{-20,-10,-10, -5, -5,-10,-10,-20},
	};
	private int kingMidBoard[][] = {
		{-50,-40,-40,-50,-50,-40,-40,-50},
		{-30,-40,-40,-50,-50,-40,-40,-30},
		{-30,-40, 40,-50,-50,-40,-40,-30},
		{-30,-40, 40,-50,-50,-40,-40,-30},
		{-20,-30,-30,-40,-40,-30,-30,-20},
		{-10,-20,-20,-20,-20,-20,-20,-10},
		{ 20, 20,  0,  0,  0,  0, 20, 20},
		{ 20, 30, 10,  0,  0, 10, 30, 20},
	};
	private int kingEndBoard[][] = {
		{-50,-40,-30,-20,-20,-30,-40,-50},
		{-30,-10,-10,  0,  0,-10,-20,-30},
		{-30,-10, 20, 30, 30, 20,-10,-30},
		{-30,-10, 30, 40, 40, 30,-10,-30},
		{-30,-10, 30, 40, 40, 30,-10,-30},
		{-30,-10, 20, 30, 30, 20,-10,-30},
		{-30,-30,  0,  0,  0,  0,-30,-30},
		{-50,-30,-30,-30,-30,-30,-30,-50},
	};

	private AlphaBetaChess game;
	
	public Rating(AlphaBetaChess game){
		this.game = game;
	}

	public int rating(int list, int depth) {
		int counter = 0, material = rateMaterial();
		counter += rateAttack();
		counter += material;
		counter += rateMoveablitly(list,depth,material);
		counter += ratePositional(material);
		game.flipboard();
		material = rateMaterial();
		counter -= rateAttack();
		counter -= material;
		counter -= rateMoveablitly(list,depth,material);
		counter -= ratePositional(material);
		game.flipboard();
		return -(counter+depth*50);
	}

	public int rateAttack() {
		int counter = 0;
		int tempPositionC=game.getKingPositionC();
		for (int i = 0; i < 64; i++) {
			switch (game.getChessBoard()[i / 8][i % 8]) {
			case "P":
				game.setKingPositionC(i);
				if(!game.kingSafe()){
					counter-=64;
				}
				break;
			case "R":
				game.setKingPositionC(i);
				if(!game.kingSafe()){
					counter-=500;
				}
				break;
			case "N":
				game.setKingPositionC(i);
				if(!game.kingSafe()){
					counter-=300;
				}
				break;
			case "B":
				game.setKingPositionC(i);
				if(!game.kingSafe()){
					counter-=300;
				}
				break;
			case "Q":
				game.setKingPositionC(i);
				if(!game.kingSafe()){
					counter-=900;
				}
				break;

			}
		}
		game.setKingPositionC(tempPositionC);
		if(!game.kingSafe()){
			counter-=200;
		}
		return counter/2;

	}

	public int rateMaterial() {
		int counter = 0,bishopCounter = 0;
		for (int i = 0; i < 64; i++) {
			switch (game.getChessBoard()[i / 8][i % 8]) {
			case "P":
				counter += 100;
				break;
			case "R":
				counter += 500;
				break;
			case "N":
				counter += 300;
				break;
			case "B":
				bishopCounter += 1;
				break;
			case "Q":
				counter += 900;
				break;

			}
		}
		if(bishopCounter>=2){
			counter+=300*bishopCounter;
		}else{
			if(bishopCounter == 1){
				counter+=250;
			}
		}
		return counter;

	}

	public int rateMoveablitly(int listLength,int depth,int material) {
		int counter = 0;
		counter+=listLength;//five points per possible move
		if(listLength==0){//Checkmate or stalemate
			if(!game.kingSafe()){//checkate
				counter+=(-200000*depth);
			}else{//stalemate
				counter+=(-150000*depth);
			}
		}
		return counter;

	}

	public int ratePositional(int material) {
		int counter = 0;
		for (int i = 0; i < 64; i++) {
			switch (game.getChessBoard()[i / 8][i % 8]) {
			case "P":
				counter += pawnBoard[i/8][i%8];
				break;
			case "R":
				counter += rookBoard[i/8][i%8];
				break;
			case "N":
				counter += knightBoard[i/8][i%8];
				break;
			case "B":
				counter += bishopBoard[i/8][i%8];
				break;
			case "Q":
				counter += queenBoard[i/8][i%8];
				break;
			case "K":
				if(material>=1750){
					counter+=kingMidBoard[i/8][i%8];
					counter+=game.possibleK(game.getKingPositionC()).length()*10;
				}else{
					counter+=kingEndBoard[i/8][i%8];
					counter+=game.possibleK(game.getKingPositionC()).length()*30;

				}
				break;
			}
		}
		
		return counter;
	}
}
