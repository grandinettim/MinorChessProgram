import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChessGame {
	public static void main(String[] args){
		AlphaBetaChess game = new AlphaBetaChess();
		
		while(!"K".equals(game.getChessBoard()[game.getKingPositionC()/8][game.getKingPositionC()%8])) 
			game.setKingPositionC(game.getKingPositionC()+1);
		
		while(!"k".equals(game.getChessBoard()[game.getKingPositionL()/8][game.getKingPositionL()%8])) 
			game.setKingPositionL(game.getKingPositionL()+1);
		
		JFrame f = new JFrame("Chess Tutorial");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UserInterface ui = new UserInterface(game);
		f.add(ui, BorderLayout.CENTER);
		f.setSize(518, 540);
		f.setResizable(false);
		f.setLocation(500, 100);
		f.setVisible(true);
		System.out.println(game.possibleMoves());
		Object[] option = {"Computer", "Human"};
		game.setHumanAsWhite(JOptionPane.showOptionDialog(null, "Who should play as White", "ABC Options", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[1]));
	
		if (game.getHumanAsWhite() == 0){
			long startTime=System.currentTimeMillis();
			game.makeMove(game.alphaBeta("", 0));
			long endTime = System.currentTimeMillis();
			System.out.println("That took "+(endTime-startTime)+" milliseconds");
			game.flipboard();
			f.repaint();
		}
		
		//makeMove("7655 ");
		//undoMove("7655 ");
		for(int i = 0;i<8;i++){
			System.out.println(game.getChessBoard()[i/8][i%8]);
			//System.out.println(Arrays.toString(game.getChessBoard[i]));
		}
	}
}
